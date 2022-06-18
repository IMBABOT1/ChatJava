package ru.geekbrains.chat.server;

public interface AuthManager {
    String getNicknameByLoginAndPassword(String login, String password);

    void changeNick(String oldNick, String newNick);

    void start();

    void stop();

}
