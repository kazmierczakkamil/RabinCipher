package pl.rabin;

import javax.swing.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class Main {

    public static BigInteger p = BigInteger.valueOf(379);
    public static BigInteger q = BigInteger.valueOf(383);


    /*public static void main(String[] args) {

        String message = "No niby dziala";
        byte[] b = message.getBytes();
        String newStr = new String(b);
        System.out.println(newStr);


        BigInteger[] encryptedMessage = encryptMessage(message.getBytes(), p, q);

        System.out.println(getEncryptedMessage(encryptedMessage));

        BigInteger[] some = convertStringToArray(getEncryptedMessage(encryptedMessage));
        System.out.println(decryptMessage(encryptedMessage));


    }*/



    // roll one number
    static int rollCorrectPrime(List<Integer> primes) {
        Random random = new SecureRandom();
        return primes.get(random.nextInt(primes.size() - 1));
    }

    // convert string to byte array
    static byte[] convertText(String text) {
        byte[] byteArray = new byte[text.length()];
        char[] textChars = text.toCharArray();

        for (int i = 0; i < text.length(); i++) {
            byteArray[i] = (byte) textChars[i];
        }

        return byteArray;
    }

    static String convertByteArray(byte[] array) {
        StringBuilder sb = new StringBuilder();

        for (byte _char : array) {
            char c = (char) _char;
            sb.append(c);
        }

        return sb.toString();
    }

    static BigInteger[] encryptMessage(String message, BigInteger p, BigInteger q) {
        //char[] charMessage = message.toCharArray();
        BigInteger n = p.multiply(q); // public key

        BigInteger[] encryptedMessage = new BigInteger[message.length()];
        for (int i = 0; i < message.length(); i++) {
            BigInteger M = BigInteger.valueOf(message.codePointAt(i));
            BigInteger C = M.modPow(BigInteger.valueOf(2), n);
            encryptedMessage[i] = C;
        }
        return encryptedMessage;
    }

    public static String getEncryptedMessage(BigInteger[] message) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < message.length; i++) {
            sb.append(message[i].toString());
            sb.append('_');
        }
        return sb.toString();
    }

    public static BigInteger[] encryptMessage(byte[] M, BigInteger p, BigInteger q) { // kazdy bajt jako jeden znak
        BigInteger n = p.multiply(q);

        BigInteger[] C = new BigInteger[M.length];

        for (int i = 0; i < M.length; i++) {
            BigInteger temp = new BigInteger(new byte[] { M[i], M[i] }); // podwojona wiadomosc - dodana suma kontrolna

            if (temp.compareTo(n) >= 0)
                throw new IllegalArgumentException("Block cannot be more than n");

            byte[] tempArray = temp.modPow(BigInteger.valueOf(2), n).toByteArray();
            C[i] = new BigInteger(tempArray);
        }

        return C;
    }

    static BigInteger[] calculateMFactors(BigInteger C, int p, int q) {
        BigInteger[] mFactorsArray = new BigInteger[4];

        mFactorsArray[0] = C.modPow(BigInteger.valueOf((p+1) / 4), BigInteger.valueOf(p));
        mFactorsArray[1] = ( (BigInteger.valueOf(p)).subtract(C.pow((p+1) / 4)) ).mod(BigInteger.valueOf(p));
        mFactorsArray[2] = C.modPow(BigInteger.valueOf((q+1) / 4), BigInteger.valueOf(q));
        mFactorsArray[3] = ( (BigInteger.valueOf(q)).subtract(C.pow((q+1) / 4)) ).mod(BigInteger.valueOf(q));

        return mFactorsArray;
    }


    static BigInteger[] calculateABFactors(int p, int q) {
        BigInteger p_BI = BigInteger.valueOf(p);
        BigInteger q_BI = BigInteger.valueOf(q);
        BigInteger[] abFactors = new BigInteger[2];

        abFactors[0] = q_BI.multiply( (q_BI.modInverse(p_BI)) );
        abFactors[1] = p_BI.multiply( (p_BI.modInverse(q_BI)) );

        return abFactors;
    }


    static BigInteger[] decryptMessage(BigInteger encryptedMessage) {

        ///// FOR TESTS
        BigInteger publicKey = p.multiply(q);
        /////

        BigInteger[] messages = new BigInteger[4];
        BigInteger[] abFactors = calculateABFactors(p.intValue(), q.intValue()); // p and q as tested values

        // M1 message
        BigInteger[] mFactors = calculateMFactors(encryptedMessage, p.intValue(), q.intValue());
        BigInteger decryptedInt = (((abFactors[0].multiply(mFactors[0]))
                .add((abFactors[1].multiply(mFactors[2]))))
                .mod(publicKey));
        messages[0] = decryptedInt;

        // M2 message
        decryptedInt = (((abFactors[0].multiply(mFactors[0]))
                .add((abFactors[1].multiply(mFactors[3]))))
                .mod(publicKey));
        messages[1] = decryptedInt;

        // M3 message
        decryptedInt = (((abFactors[0].multiply(mFactors[1]))
                .add((abFactors[1].multiply(mFactors[2]))))
                .mod(publicKey));
        messages[2] = decryptedInt;


        // M4 message
        decryptedInt = (((abFactors[0].multiply(mFactors[1]))
                .add((abFactors[1].multiply(mFactors[3]))))
                .mod(publicKey));
        messages[3] = decryptedInt;

        return messages;
    }


    public static BigInteger[] convertStringToArray(String encryptedMessage) {
        String[] tabValues = encryptedMessage.split("\\_");
        BigInteger[] array = new BigInteger[tabValues.length];

        for (int i = 0; i < array.length; i++)
            array[i] = new BigInteger(tabValues[i]);

        return array;
    }

    public static String decryptMessage(BigInteger[] encryptedMessage, BigInteger p, BigInteger q) {

        StringBuilder sb = new StringBuilder();

        ///// FOR TESTS
        BigInteger publicKey = p.multiply(q);
        /////

        BigInteger[] abFactors = calculateABFactors(p.intValue(), q.intValue()); // p and q as tested values

        for (int i = 0; i < encryptedMessage.length; i++) {
            BigInteger[] mFactors = calculateMFactors(encryptedMessage[i], p.intValue(), q.intValue());

            // M1 message
            BigInteger decryptedChar = (((abFactors[0].multiply(mFactors[0]))
                    .add((abFactors[1].multiply(mFactors[2]))))
                    .mod(publicKey));

            byte[] tempBytes = decryptedChar.toByteArray();
            if (tempBytes.length == 2 && (tempBytes[0] == tempBytes[1])) {
                sb.append((char) tempBytes[0]);
                continue;
            }

            // M2 message
            BigInteger decryptedChar1 = (((abFactors[0].multiply(mFactors[0]))
                    .add((abFactors[1].multiply(mFactors[3]))))
                    .mod(publicKey));

            tempBytes = decryptedChar1.toByteArray();
            if (tempBytes.length == 2 && (tempBytes[0] == tempBytes[1])) {
                sb.append((char) tempBytes[0]);
                continue;
            }

            // M3 message
            BigInteger decryptedChar2 = (((abFactors[0].multiply(mFactors[1]))
                    .add((abFactors[1].multiply(mFactors[2]))))
                    .mod(publicKey));

            tempBytes = decryptedChar2.toByteArray();
            if (tempBytes.length == 2 && (tempBytes[0] == tempBytes[1])) {
                sb.append((char) tempBytes[0]);
                continue;
            }

            // M4 message
            BigInteger decryptedChar3 = (((abFactors[0].multiply(mFactors[1]))
                    .add((abFactors[1].multiply(mFactors[3]))))
                    .mod(publicKey));

            tempBytes = decryptedChar3.toByteArray();
            if (tempBytes.length == 2 && (tempBytes[0] == tempBytes[1])) {
                sb.append((char) tempBytes[0]);
            }
        }

        return sb.toString();
    }

}