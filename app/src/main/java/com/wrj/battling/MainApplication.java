package com.wrj.battling;

import android.app.Application;

import com.wrj.battling.connection.Connection;

public class MainApplication extends Application {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
