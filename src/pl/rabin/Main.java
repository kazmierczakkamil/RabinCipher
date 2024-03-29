package pl.rabin;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class Main {

    public static BigInteger p = BigInteger.valueOf(379);
    public static BigInteger q = BigInteger.valueOf(383);


    /*public static void main(String[] args) {

        String message = "";
        byte[] b = message.getBytes(StandardCharsets.US_ASCII);
        String newStr = new String(b);
        System.out.println(newStr);
        byte bajt = 1;
        byte bajt1 = -10;
        byte bajty = (byte) (bajt1 + bajt);
        System.out.println(bajty);


        BigInteger[] encryptedMessage = encryptMessage(message.getBytes(), p, q);
        byte[] someByteArray = encryptedMessage[0].toByteArray(); // -59 encrypted on 3 bytes (1, -11, -73)
        byte[] someByteArray2 = encryptedMessage[1].toByteArray();// -126 encrypted on 2 bytes (53, 38)

        String someString = decryptMessage(encryptedMessage, p, q);
        System.out.println(getEncryptedMessage(encryptedMessage));

        BigInteger[] some = convertStringToArray(getEncryptedMessage(encryptedMessage));
        //System.out.println(decryptMessage(encryptedMessage));


    } */


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
            if (i != message.length - 1)
                sb.append('_');
        }
        return sb.toString();
    }

    public static BigInteger[] encryptMessage(byte[] M, BigInteger p, BigInteger q) { // kazdy bajt jako jeden znak
        BigInteger n = p.multiply(q);

        BigInteger[] C = new BigInteger[M.length];

        for (int i = 0; i < M.length; i++) {
            BigInteger temp = new BigInteger(new byte[] { M[i], M[i] }); // podwojona wiadomosc - dodana suma kontrolna
            ////
            byte[] forTests = temp.toByteArray();
            ////
            if (temp.compareTo(n) >= 0)
                throw new IllegalArgumentException("Block cannot be more than n");

            byte[] tempArray = temp.modPow(BigInteger.valueOf(2), n).toByteArray();
            C[i] = new BigInteger(tempArray);
        }

        return C;
    }



    static BigInteger[] calculateMFactors(BigInteger C, int p, int q) {
        BigInteger[] mFactorsArray = new BigInteger[4];

        mFactorsArray[0] = C.modPow(BigInteger.valueOf((p + 1) / 4), BigInteger.valueOf(p));
        mFactorsArray[1] = ((BigInteger.valueOf(p)).subtract(C.pow((p + 1) / 4))).mod(BigInteger.valueOf(p));
        mFactorsArray[2] = C.modPow(BigInteger.valueOf((q + 1) / 4), BigInteger.valueOf(q));
        mFactorsArray[3] = ((BigInteger.valueOf(q)).subtract(C.pow((q + 1) / 4))).mod(BigInteger.valueOf(q));
        return mFactorsArray;
    }


    static BigInteger[] calculateABFactors(int p, int q) {
        BigInteger p_BI = BigInteger.valueOf(p);
        BigInteger q_BI = BigInteger.valueOf(q);
        BigInteger[] abFactors = new BigInteger[2];

        abFactors[0] = q_BI.multiply((q_BI.modInverse(p_BI)));
        abFactors[1] = p_BI.multiply((p_BI.modInverse(q_BI)));

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

        byte[] tempArray = new byte[2];
        boolean firstByteLoaded = false;
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
            } else if (tempBytes.length == 2 && (tempBytes[1]-tempBytes[0] == 1)) {
                tempBytes[1] = (byte)(tempBytes[1] * (-1));
                int index = firstByteLoaded ? 1 : 0;
                tempArray[index] = tempBytes[1];
                if (index == 1)
                    sb.append(new String(tempArray));
                firstByteLoaded = !firstByteLoaded;
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
            } else if (tempBytes.length == 2 && (tempBytes[1]-tempBytes[0] == 1)) {
                tempBytes[1] = (byte)(tempBytes[1] * (-1));
                int index = firstByteLoaded ? 1 : 0;
                tempArray[index] = tempBytes[1];
                if (index == 1)
                    sb.append(new String(tempArray));
                firstByteLoaded = !firstByteLoaded;
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
            } else if (tempBytes.length == 2 && (tempBytes[1]-tempBytes[0] == 1)) {
                tempBytes[1] = (byte)(tempBytes[1] * (-1));
                int index = firstByteLoaded ? 1 : 0;
                tempArray[index] = tempBytes[1];
                if (index == 1)
                    sb.append(new String(tempArray));
                firstByteLoaded = !firstByteLoaded;
                continue;
            }

            // M4 message
            BigInteger decryptedChar3 = (((abFactors[0].multiply(mFactors[1]))
                    .add((abFactors[1].multiply(mFactors[3]))))
                    .mod(publicKey));

            tempBytes = decryptedChar3.toByteArray();
            if (tempBytes.length == 2 && (tempBytes[0] == tempBytes[1])) {
                sb.append((char) tempBytes[0]);
            } else if (tempBytes.length == 2 && (tempBytes[1]-tempBytes[0] == 1)) {
                tempBytes[1] = (byte)(tempBytes[1] * (-1));
                int index = firstByteLoaded ? 1 : 0;
                tempArray[index] = tempBytes[1];
                if (index == 1)
                    sb.append(new String(tempArray));
                firstByteLoaded = !firstByteLoaded;
                continue;
            }
        }

        return sb.toString();
    }



    public static void encryptFile(Path path, String newFilename, BigInteger p, BigInteger q) {
        byte[] file = convertFileToByteArray(path);
        byte[] encryptedFileInBytes;

        BigInteger[] encryptedMessage = encryptMessage(file, p, q);
        String messageString = getEncryptedMessage(encryptedMessage);
        encryptedFileInBytes = messageString.getBytes(StandardCharsets.UTF_8);

        try {
            Files.write(new File(newFilename).toPath(), encryptedFileInBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decryptFile(Path path, String newFilename, BigInteger p, BigInteger q) {
        byte[] fileToDecrypt;
        byte[] decryptedFile;

        try {
            fileToDecrypt = Files.readAllBytes(path);
            String encryptedString = new String(fileToDecrypt, StandardCharsets.UTF_8);
            BigInteger[] encryptedMessageArray = convertStringToArray(encryptedString);
            String plaintext = decryptMessage(encryptedMessageArray, p, q);

            decryptedFile = plaintext.getBytes(StandardCharsets.UTF_8);

            Files.write(new File(newFilename).toPath(), decryptedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getFileSize(String filename) {
        int size = 0;

        try {
            size = Files.readAllBytes(new File(filename).toPath()).length;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return size;
    }

    public static void saveByteArrayToFile(byte[] bytes, String filename) {
        try {
            Files.write(new File(filename).toPath(), bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] convertFileToByteArray(Path path) {
        byte[] fileInBytesArray = null;

        try {
            fileInBytesArray = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileInBytesArray;
    }

}