package com.wrj.battling.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Connection {

    private InetAddress address;
    private static final int PORT = 9999;

    private byte[] authCmd;

    public byte[] generateWithAuth(byte type, byte cmd, byte[] params) {
        if (authCmd == null) {
            throw new NullPointerException();
        }

        byte[] result = new byte[authCmd.length + params.length + 9];

        System.arraycopy(authCmd, 0, result, 0, authCmd.length);
        System.arraycopy(new Command(type, cmd, params).encode(), 0, result, authCmd.length, params.length + 9);

        return result;
    }

    public boolean send(byte[] cmd) {
        DatagramPacket dataGramPacket = new DatagramPacket(cmd, cmd.length, address, PORT);
        try {
            DatagramSocket socket = new DatagramSocket();  //创建套接字
            socket.send(dataGramPacket);  //通过套接字发送数据
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] sendAndRecv(byte[] cmd) {
        DatagramPacket dataGramPacket = new DatagramPacket(cmd, cmd.length, address, PORT);
        try {
            DatagramSocket socket = new DatagramSocket();  //创建套接字
            socket.send(dataGramPacket);  //通过套接字发送数据
            //接收服务器反馈数据
            byte[] buffer = new byte[268];
            DatagramPacket backPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(backPacket);  //接收返回数据
            socket.close();
            byte[] result = new byte[backPacket.getLength()];
            System.arraycopy(buffer, 0, result, 0, backPacket.getLength());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean auth(String ip, String password) {
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        authCmd = new Command((byte) 0x0A, (byte) 0x71, password.getBytes()).encode();
//        byte[] result = sendAndRecv(authCmd);
//        if (result == null) {
//            authCmd = null;
//            return false;
//        }
//        Command resultCmd = Command.decode(result);
//        if (resultCmd == null || resultCmd.params[0] == 0) {
//            authCmd = null;
//            return false;
//        }
        return true;

        // TODO
    }
}
