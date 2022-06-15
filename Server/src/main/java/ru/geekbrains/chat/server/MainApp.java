package ru.geekbrains.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MainApp {
    // Домашнее задание:
    // 1. Разобраться с кодом. Задать вопросы что непонятно;
    // 2. Попробуйте реализовать личные сообщения
    // если я с user1 пишу '/w user2 hello',
    // то сообщение hello должен получить только user2, или user1 должен получить
    // ответ что user2 не в сети
    // 3. Попробуйте сделать в чате оповещения о заходящих и выходящий пользователях
    // Если будет сложно с кодом: возможно выполнение только задания 1. Или можно выбрать
    // п.2 или п.3

    public static void main(String[] args) {
        new Server(8189);
    }
}
