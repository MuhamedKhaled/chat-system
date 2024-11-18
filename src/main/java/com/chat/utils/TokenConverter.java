package com.chat.utils;

import org.mapstruct.Named;

import java.nio.ByteBuffer;
import java.util.UUID;

public class TokenConverter {

    @Named("bytesToUuidString")
    public static String toString(byte[] token) {
        if (token == null) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(token);
        UUID uuid = new UUID(buffer.getLong(), buffer.getLong());
        return uuid.toString();
    }

    public static byte[] toBytes(String token) {
        UUID uuid = UUID.fromString(token);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
