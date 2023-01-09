package com.easyhi.manage.util;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {

    public static String getRandomSpecialChars(int count) {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
