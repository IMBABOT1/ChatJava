package ru.geekbrains.chat.server;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickname;

    private String newNick;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String name){
        this.nickname = name;
    }

    private File file;
    private PrintWriter writer;


    public ClientHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                while (true) { // цикл аутентификации
                    String msg = in.readUTF();
                    System.out.print("Сообщение от клиента: " + msg + "\n");
                    if (msg.startsWith("/auth ")) { // /auth login1 pass1
                        String[] tokens = msg.split(" ", 3);
                        String nickFromAuthManager = server.getAuthManager().getNicknameByLoginAndPassword(tokens[1], tokens[2]);
                        if (nickFromAuthManager != null) {
                            if (server.isNickBusy(nickFromAuthManager)) {
                                sendMsg("Данный пользователь уже в чате");
                                continue;
                            }
                            nickname = nickFromAuthManager;
                            sendMsg("/authok " + nickname);
                            server.subscribe(this);
                            file = new File(nickname + ".txt");
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            break;
                        } else {
                            sendMsg("Указан неверный логин/пароль");
                        }
                    }
                }
                while (true) { // цикл общения с сервером (обмен текстовыми сообщениями и командами)
                    String msg = in.readUTF();
                    System.out.print("Сообщение от клиента: " + msg + "\n");
                    if (msg.startsWith("/")) {
                        if (msg.startsWith("/w ")) {
                            String[] tokens = msg.split(" ", 3); // /w user2 hello, user2
                            server.sendPrivateMsg(this, tokens[1], tokens[2]);
                            continue;
                        }
                        if (msg.equals("/end")) {
                            sendMsg("/end_confirm");
                            break;
                        }

                        if (msg.startsWith("/change_nick ")){
                            String[] tokens = msg.split(" ");
                            String newNick = tokens[1];
                            server.getAuthManager().changeNick(nickname, newNick);
                            nickname = newNick;
                        }
                    } else {
                        server.broadcastMsg(nickname + ": " + msg, true);
                        StringBuilder sb = new StringBuilder();
                        sb.append(msg + "\n");
                        Files.write(Paths.get(file.getPath()), sb.toString().getBytes(), StandardOpenOption.APPEND);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                close();

            }
        }).start();
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        server.unsubscribe(this);
        nickname = null;
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
