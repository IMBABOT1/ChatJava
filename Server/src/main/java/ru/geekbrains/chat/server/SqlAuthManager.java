package ru.geekbrains.chat.server;

import java.sql.*;

public class SqlAuthManager implements AuthManager {

    private Connection connection;
    private PreparedStatement statement;


    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        String username  ="";
        try {
            statement = connection.prepareStatement("SELECT username FROM users WHERE login = ? and password = ?");
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                username = rs.getString(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return username;
    }

    @Override
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mainDB.db");
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }




    public  void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
