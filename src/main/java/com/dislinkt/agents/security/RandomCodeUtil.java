package com.dislinkt.agents.security;

import java.security.SecureRandom;
import java.util.Random;

public class RandomCodeUtil {

    static char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    static char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static char[] NUMBERS = "0123456789".toCharArray();
    static char[] ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    static Random rand = new SecureRandom();

    public static String getCode(int length) {
        assert length >= 4;
        char[] code = new char[length];

        code[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        code[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        code[2] = NUMBERS[rand.nextInt(NUMBERS.length)];

        //populate rest of the password with random chars
        for (int i = 3; i < length; i++) {
            code[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        //shuffle it up
        for (int i = 0; i < code.length; i++) {
            int randomPosition = rand.nextInt(code.length);
            char temp = code[i];
            code[i] = code[randomPosition];
            code[randomPosition] = temp;
        }

        return new String(code);
    }

}
