package ru.geekbrains.chat.server;

import com.sun.org.apache.xerces.internal.impl.dv.xs.BaseDVFactory;

import java.sql.*;

public class SqlAuthManager implements AuthManager {

    private Connection connection;
    private PreparedStatement getNameByLoginAndPassword;
    private PreparedStatement changeNickName;


    @Override
    public void start() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mainDB.db");
            getNameByLoginAndPassword = connection.prepareStatement("SELECT username FROM users WHERE login = ? and password = ?");
            changeNickName = connection.prepareStatement("UPDATE users SET username = ? WHERE username = ?");
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }



    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        String username  ="";
        try {
            getNameByLoginAndPassword.setString(1, login);
            getNameByLoginAndPassword.setString(2, password);
            ResultSet rs = getNameByLoginAndPassword.executeQuery();
            while (rs.next()){
                username = rs.getString(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return username;
    }

    @Override
    public void changeNick(String oldNick, String newNick) {
        try {
            changeNickName = connection.prepareStatement("UPDATE users SET username = ? WHERE username = ?");
            changeNickName.setString(1, newNick);
            changeNickName.setString(2, oldNick);
            changeNickName.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }



    @Override
    public void stop() {
        try {
            if (connection != null){
                connection.close();;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if (getNameByLoginAndPassword != null){
                getNameByLoginAndPassword.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if (changeNickName != null){
                changeNickName.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
