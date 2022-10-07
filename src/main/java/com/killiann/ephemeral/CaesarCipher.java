package com.killiann.ephemeral;

public class CaesarCipher
    {
        // Encrypts text using a shift od s
        public static StringBuffer encrypt(String text, int s) {
            StringBuffer result = new StringBuffer();

            for (int i = 0; i < text.length(); i++) {
                char ch = (char) (((int)text.charAt(i) +
                        s - 65) % 26 + 65);
                result.append(ch);
            }

            return result;
        }


        public static StringBuffer decrypt(StringBuffer text, int s)
        {
            StringBuffer result= new StringBuffer();

            for (int i = 0; i < text.length(); i++) {
                char ch = (char) (((int)text.charAt(i) -
                        s - 65) % 26 + 65);
                result.append(ch);
            }

            return result;
        }

        // Driver code
        public static void main(String[] args)
        {
            String text = "5448576911832339";
            int s = 4;
            System.out.println("Text  : " + text);
            System.out.println("Shift : " + s);
            StringBuffer encrypt = encrypt(text, s);
            System.out.println("Cipher: " + encrypt);
            System.out.println("Original: " + decrypt(encrypt, s));
        }
}
