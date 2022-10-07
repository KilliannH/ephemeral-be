package com.killiann.ephemeral.helpers;

public class CaesarCipher
    {
        // Encrypts text using a shift od s
        public static String encrypt(String text, int s) {
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < text.length(); i++) {
                char ch = (char) (((int)text.charAt(i) + s - 65) % 26 + 65);
                result.append(ch);
            }

            return String.valueOf(result);
        }


        public static String decrypt(String txt, int s)
        {
            StringBuilder text = new StringBuilder();
            text.append(txt);
            StringBuilder result= new StringBuilder();

            for (int i = 0; i < text.length(); i++) {
                char ch = (char) (((int)text.charAt(i) - s - 65) % 26 + 65);
                result.append(ch);
            }

            return String.valueOf(result);
        }
}
