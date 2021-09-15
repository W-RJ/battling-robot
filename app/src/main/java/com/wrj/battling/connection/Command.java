package com.wrj.battling.connection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Command {
    public byte type;
    public byte cmd;
    public byte[] params;

    public Command() {
    }

    public Command(byte type, byte cmd, byte[] params) {
        this.type = type;
        this.cmd = cmd;
        this.params = params;
    }

    public byte[] encode() {
        byte dataLen = (byte) (params.length + 3);
        byte check = (byte) (dataLen + type + cmd + params.length);
        for (byte param : params) {
            check += param;
        }
        check = (byte) ~check;

        byte[] result = new byte[params.length + 9];
        result[0] = (byte) 0xFE;
        result[1] = (byte) 0xEF;
        result[2] = (byte) 0x00;
        result[3] = (byte) 0x00;
        result[4] = dataLen;
        result[5] = type;
        result[6] = cmd;
        result[7] = (byte) params.length;

        System.arraycopy(params, 0, result, 8, params.length);
        result[params.length + 8] = check;

        return result;
    }

    @Nullable
    public static Command decode(@NonNull byte[] bytes) {
        ArrayList<Command> array = new ArrayList<>();
        Command result = new Command();
        array.add(new Command());
        if (bytes[0] != (byte) 0xFE || bytes[1] != (byte) 0xEF || bytes[4] != bytes[7] + 3) {
            return null;
        }
        byte check = 0;
        int end = bytes[4] + 6;
        for (int i = 2; i < end - 1; i++) {
            check += bytes[i];
        }
        if (~check != bytes[end - 1]) {
            return null;
        }
        result.type = bytes[5];
        result.cmd = bytes[6];
        result.params = new byte[bytes[7]];
        System.arraycopy(bytes, 8, result.params, 0, bytes[7]);
        return result;
    }

    public byte length() {
        return (byte) (params.length + 9);
    }
}
