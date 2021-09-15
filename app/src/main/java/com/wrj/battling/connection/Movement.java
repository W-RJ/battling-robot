package com.wrj.battling.connection;

public class Movement {
    private Connection connection;

    public Movement(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void move(short angle, short speed, short turning, short duration) {
        connection.send(connection.generateWithAuth((byte) 0x08, (byte) 0x06,
                new byte[] {
                        (byte) (angle & 0xFF), (byte) (angle >>> 8),
                        (byte) (speed & 0xFF), (byte) (speed >>> 8),
                        (byte) (turning & 0xFF), (byte) (turning >>> 8),
                        (byte) (duration & 0xFF), (byte) (duration >>> 8),
                }));
    }

    public void action(String name) {
        connection.send(connection.generateWithAuth((byte) 0x07, (byte) 0x54, name.getBytes()));
    }

    public void battle() {
        connection.send(connection.generateWithAuth((byte) 0x09, (byte) 0x01, new byte[]{2}));
    }

    public void normal() {
        connection.send(connection.generateWithAuth((byte) 0x09, (byte) 0x01, new byte[]{1}));
    }
}
