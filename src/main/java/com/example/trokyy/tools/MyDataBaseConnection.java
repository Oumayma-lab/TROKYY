package com.example.trokyy.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MyDataBaseConnection {

    private static MyDataBaseConnection instance ;
    private final String URL="jdbc:mysql://127.0.0.1:3306/javatroky";
    private final String USERNAME="root";
    private final String PASSWORD ="";
    private static Connection connection ;

    public MyDataBaseConnection(){

        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            System.out.println("Connected ...");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("____not connected____ ");

        }



    }

    public static MyDataBaseConnection getInstance(){
        if (instance == null)
            instance = new MyDataBaseConnection();

        return instance;
    }
    public static Connection getConnection(){
        return connection;
    }

}