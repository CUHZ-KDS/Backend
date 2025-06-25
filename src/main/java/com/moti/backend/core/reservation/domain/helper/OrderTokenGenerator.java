package com.moti.backend.core.reservation.domain.helper;

import java.security.SecureRandom;

public class OrderTokenGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 12;
    private static final SecureRandom random = new SecureRandom();

    public static String generateToken() {
        StringBuilder sb = new StringBuilder("ORD_");
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}


