package com.killiann.ephemeral;

import java.nio.charset.StandardCharsets;

public class CaesarCipher
    {
        // Encrypts text using a shift od s
        public static String encrypt(String text, int s) {
            StringBuffer result = new StringBuffer();

            for (int i = 0; i < text.length(); i++) {
                char ch = (char) (((int)text.charAt(i) +
                        s - 65) % 26 + 65);
                result.append(ch);
            }

            return result.toString();
        }


        public static String decrypt(String txt, int s)
        {
            StringBuffer text = new StringBuffer();
            text.append(txt);
            StringBuffer result= new StringBuffer();

            for (int i = 0; i < text.length(); i++) {
                char ch = (char) (((int)text.charAt(i) -
                        s - 65) % 26 + 65);
                result.append(ch);
            }

            return result.toString();
        }
}
