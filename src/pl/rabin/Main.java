package pl.rabin;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.lang.Math;

public class Main {

    public static BigInteger p = BigInteger.valueOf(379);
    public static BigInteger q = BigInteger.valueOf(383);

    public static void main(String[] args) {

        String message = "rafal to pies";
        byte[] b = message.getBytes();
        String newStr = new String(b);
        System.out.println(newStr);


        BigInteger[] encryptedMessage = encryptMessage(message.getBytes(), p, q);

        System.out.println(getEncryptedMessage(encryptedMessage));

        BigInteger[] some = convertStringToArray(getEncryptedMessage(encryptedMessage));
        System.out.println(decryptMessage(encryptedMessage));


        //String decMess = decryptMessage(new String(encMess1));
        //BigInteger[] decMess = decryptMessage(new BigInteger(encMess1));

        //for (BigInteger mess : decMess) {
        //    System.out.println(new String(mess.toByteArray()));
       // }

        //int message = 106;
       /* for (int message = 2; message < Main.p * Main.q; message++) {

            BigInteger message_BI = BigInteger.valueOf(message);
            //int encryptedMessage = encryptMessage(message, Main.p, Main.q);
            BigInteger encryptedMessage = encryptMessage(BigInteger.valueOf(message), BigInteger.valueOf(Main.p), BigInteger.valueOf(Main.q));


            BigInteger[] decryptedMessages = decryptMessage(encryptedMessage);

            for (int i = 0; i < decryptedMessages.length; i++) {
                if (decryptedMessages[i].equals(message_BI)) {
                    System.out.print("M: " + message);
                    System.out.print(" C: " + encryptedMessage);
                    System.out.print(" TRUE " + i);
                    System.out.println();

                    break;
                }

                if (i == decryptedMessages.length - 1) {
//                    System.out.print("M: " + message);
//                    System.out.print(" C: " + encryptedMessage);
//                    System.out.print(" FALSE ");
                    System.out.println();
                }
            }

        }


        List<Integer> asdf = Main.getRabinPrimes(14);

        for (int i : asdf) {
            System.out.println(i);
        }*/

        /*String message = "a";
        System.out.println("Message: " + message);
        String encryptedMessage = encryptMessage(message, 379, 383);
        System.out.println("Encrypted message: " + encryptedMessage);

        String[] decryptedMessages = decryptMessage(encryptedMessage);

        int i = 0;
        for (String decryptedMessage : decryptedMessages) {
            System.out.println("Message " + i + ": " + decryptedMessage);
            i++;
        }
        System.out.println();
*/

    }


    // generate list of primes in parameters bounds
    static List<Integer> getPrimes(int from, int to) {
        List<Integer> primeNumbers = new ArrayList<>();

        for (int i = from; i <= to; i++) {
            boolean isPrime = true;

            for (int j = i - 1; j > 1; j--) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime)
                primeNumbers.add(i);
        }

        return primeNumbers;
    }

    static List<Integer> getPrimes(int amount) {
        List<Integer> primeNumbers = new ArrayList<>();

        int number = 3;

        while (primeNumbers.size() < amount) {
            boolean isPrime = true;

            for (int j = number - 1; j > 1; j--) {
                if (number % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime)
                primeNumbers.add(number);

            number++;
        }

        return primeNumbers;
    }

    static List<Integer> getRabinPrimes(int amount) {
        List<Integer> primeRabinNumbers = new ArrayList<>();

        int number = 3;

        while (primeRabinNumbers.size() < amount) {
            boolean isPrime = true;

            for (int j = number - 1; j > 1; j--) {
                if (number % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime && number % 4 == 3)
                primeRabinNumbers.add(number);

            number++;
        }

        return primeRabinNumbers;
    }

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

    ////////////////////////////////////Inverse modulo/////////////////////////////
    static int[] gcd(int p, int q) {
        if (q == 0)
            return new int[]{p, 1, 0};
        int[] vals = gcd(q, p % q);
        int d = vals[0];
        int a = vals[2];
        int b = vals[1] - (p / q) * vals[2];
        return new int[]{d, a, b};
    }

    static int inverseModulo(int number, int modulo) {
        int[] vals = gcd(number, modulo);
        int d = vals[0];
        int a = vals[1];
        int b = vals[2];
        if (d > 1) {
            System.out.println("Inverse does not exist.");
            return 0;
        }
        if (a > 0)
            return a;
        return modulo + a;
    }
    ///////////////////////////////////////////////////////////////


    static int powerModulo(int value, int power, int modulo) {
        int e = 1;

        for (int i = 0; i < power; i++)
            e = ((e * value) % modulo);

        if (e < 0)
            e += modulo;


        return e;
    }

//    static String encryptMessage(String message, int p, int q) {
//        char[] charMessage = message.toCharArray();
//        int n = p * q; // public key
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < message.length(); i++) {
//            int C;
//            int M = charMessage[i];
//            C = Main.powerModulo(M, 2, n);
//            sb.append((char) C);
//        }
//
//        return sb.toString();
//    }

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

    static int encryptMessage(int M, int p, int q) {
        int n = p * q; // public key
        int C = Main.powerModulo(M, 2, n);

        return C;
    }

    static String getEncryptedMessage(BigInteger[] message) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < message.length; i++) {
            sb.append(message[i].toString());
            sb.append('_');
        }
        return sb.toString();
    }

    static BigInteger[] encryptMessage(byte[] M, BigInteger p, BigInteger q) {
        BigInteger n = p.multiply(q);

        BigInteger[] C = new BigInteger[M.length];

        for (int i = 0; i < M.length; i++) {
            BigInteger temp = new BigInteger(new byte[] { M[i], M[i] }); // podwojona wiadomosc - suma kontrolna

            if (temp.compareTo(n) >= 0)
                throw new IllegalArgumentException("Block cannot be more than n");

            byte[] tempArray = temp.modPow(BigInteger.valueOf(2), n).toByteArray();

            C[i] = new BigInteger(tempArray);
        }

        return C;
    }

    // return m1, m2, m3, m4
    static int[] calculateMFactors(int C, int p, int q) {
        int[] mFactorsArray = new int[4];

        mFactorsArray[0] = powerModulo(C, (p + 1) / 4, p);
        mFactorsArray[1] = powerModulo(p - (int)Math.pow(C, (p+1)/4), 1, p); //C ^ ((p + 1) / 4)
        mFactorsArray[2] = powerModulo(C, (q + 1) / 4, q);
        mFactorsArray[3] = powerModulo(q - (int)Math.pow(C, (q+1)/4), 1, q); //C ^ ((q + 1) / 4)

        return mFactorsArray;
    }

    static BigInteger[] calculateMFactors(BigInteger C, int p, int q) {
        BigInteger[] mFactorsArray = new BigInteger[4];

        mFactorsArray[0] = C.modPow(BigInteger.valueOf((p+1) / 4), BigInteger.valueOf(p));
        mFactorsArray[1] = ( (BigInteger.valueOf(p)).subtract(C.pow((p+1) / 4)) ).mod(BigInteger.valueOf(p));
        mFactorsArray[2] = C.modPow(BigInteger.valueOf((q+1) / 4), BigInteger.valueOf(q));
        mFactorsArray[3] = ( (BigInteger.valueOf(q)).subtract(C.pow((q+1) / 4)) ).mod(BigInteger.valueOf(q));

        return mFactorsArray;
    }

//    static int[] calculateABFactors(int p, int q) {
//        int[] abFactors = new int[2];
//
//        abFactors[0] = q * (inverseModulo(q, p)); // a-factor
//        abFactors[1] = p * (inverseModulo(p, q)); // b-factor
//
//        return abFactors;
//    }

    static BigInteger[] calculateABFactors(int p, int q) {
        BigInteger p_BI = BigInteger.valueOf(p);
        BigInteger q_BI = BigInteger.valueOf(q);
        BigInteger[] abFactors = new BigInteger[2];

        abFactors[0] = q_BI.multiply( (q_BI.modInverse(p_BI)) );
        abFactors[1] = p_BI.multiply( (p_BI.modInverse(q_BI)) );

        return abFactors;
    }

//    static String[] decryptMessage(String encryptedMessage) {
//
//        ///// FOR TESTS
//        int p = Main.p;
//        int q = Main.q;
//        int publicKey = p * q;
//        /////
//
//        StringBuilder sb = new StringBuilder();
//        String[] messages = new String[4];
//        int[] encryptedMessageChars = encryptedMessage.chars().toArray();
//        int[] abFactors = calculateABFactors(p, q); // p and q as tested values
//
//        // M1 message
//        for (int _char : encryptedMessageChars) {
//            int[] mFactors = calculateMFactors(_char, p, q);
//            int decryptedChar = (abFactors[0] * mFactors[0] + abFactors[1] * mFactors[2]) % publicKey;
//            sb.append(decryptedChar);
//        }
//        messages[0] = sb.toString();
//
//        sb = new StringBuilder();
//        // M2 message
//        for (int _char : encryptedMessageChars) {
//            int[] mFactors = calculateMFactors(_char, p, q);
//            int decryptedChar = (abFactors[0] * mFactors[0] + abFactors[1] * mFactors[3]) % publicKey;
//            sb.append(decryptedChar);
//        }
//        messages[1] = sb.toString();
//
//        sb = new StringBuilder();
//        // M3 message
//        for (int _char : encryptedMessageChars) {
//            int[] mFactors = calculateMFactors(_char, p, q);
//            int decryptedChar = (abFactors[0] * mFactors[1] + abFactors[1] * mFactors[2]) % publicKey;
//            sb.append(decryptedChar);
//        }
//        messages[2] = sb.toString();
//
//        sb = new StringBuilder();
//        // M4 message
//        for (int _char : encryptedMessageChars) {
//            int[] mFactors = calculateMFactors(_char, p, q);
//            int decryptedChar = (abFactors[0] * mFactors[1] + abFactors[1] * mFactors[3]) % publicKey;
//            sb.append(decryptedChar);
//        }
//        messages[3] = sb.toString();
//
//        return messages;
//    }

    /*static String[] decryptMessage(String encryptedMessage) {

        ///// FOR TESTS
        int p = Main.p;
        int q = Main.q;
        int publicKey = p * q;
        /////

        StringBuilder sb = new StringBuilder();
        String[] messages = new String[4];
        int[] encryptedMessageChars = encryptedMessage.chars().toArray();
        BigInteger[] abFactors = calculateABFactors(p, q); // p and q as tested values

        // M1 message
        for (int _char : encryptedMessageChars) {
            BigInteger[] mFactors = calculateMFactors(BigInteger.valueOf(_char), p, q);
            int decryptedChar =
                    (((abFactors[0].multiply(mFactors[0]))
                    .add((abFactors[1].multiply(mFactors[2]))))
                    .mod(BigInteger.valueOf(publicKey)))
                    .intValue();
            sb.append(decryptedChar);
        }
        messages[0] = sb.toString();

        sb = new StringBuilder();
        // M2 message
        for (int _char : encryptedMessageChars) {
            BigInteger[] mFactors = calculateMFactors(BigInteger.valueOf(_char), p, q);
            int decryptedChar =
                    (((abFactors[0].multiply(mFactors[0]))
                            .add((abFactors[1].multiply(mFactors[3]))))
                            .mod(BigInteger.valueOf(publicKey)))
                            .intValue();
            sb.append(decryptedChar);
        }
        messages[1] = sb.toString();

        sb = new StringBuilder();
        // M3 message
        for (int _char : encryptedMessageChars) {
            BigInteger[] mFactors = calculateMFactors(BigInteger.valueOf(_char), p, q);
            int decryptedChar =
                    (((abFactors[0].multiply(mFactors[1]))
                            .add((abFactors[1].multiply(mFactors[2]))))
                            .mod(BigInteger.valueOf(publicKey)))
                            .intValue();
            sb.append(decryptedChar);
        }
        messages[2] = sb.toString();

        sb = new StringBuilder();
        // M4 message
        for (int _char : encryptedMessageChars) {
            BigInteger[] mFactors = calculateMFactors(BigInteger.valueOf(_char), p, q);
            int decryptedChar =
                    (((abFactors[0].multiply(mFactors[1]))
                            .add((abFactors[1].multiply(mFactors[3]))))
                            .mod(BigInteger.valueOf(publicKey)))
                            .intValue();
            sb.append(decryptedChar);
        }
        messages[3] = sb.toString();

        return messages;
    }*/

//    static int[] decryptMessage(int encryptedMessage) {
//
//        ///// FOR TESTS
//        int p = Main.p;
//        int q = Main.q;
//        int publicKey = p * q;
//        /////
//
//        int[] messages = new int[4];
//        int[] abFactors = calculateABFactors(p, q); // p and q as tested values
//
//        // M1 message
//        int[] mFactors = calculateMFactors(encryptedMessage, p, q);
//        int decryptedInt = (abFactors[0] * mFactors[0] + abFactors[1] * mFactors[2]) % publicKey;
//        messages[0] = decryptedInt;
//
//        // M2 message
//        mFactors = calculateMFactors(encryptedMessage, p, q);
//        decryptedInt = (abFactors[0] * mFactors[0] + abFactors[1] * mFactors[3]) % publicKey;
//        messages[1] = decryptedInt;
//
//        // M3 message
//        mFactors = calculateMFactors(encryptedMessage, p, q);
//        decryptedInt = (abFactors[0] * mFactors[1] + abFactors[1] * mFactors[2]) % publicKey;
//        messages[2] = decryptedInt;
//
//
//        // M4 message
//        mFactors = calculateMFactors(encryptedMessage, p, q);
//        decryptedInt = (abFactors[0] * mFactors[1] + abFactors[1] * mFactors[3]) % publicKey;
//        messages[3] = decryptedInt;
//
//        return messages;
//    }

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

//    static String[] decryptMessage(String encryptedMessage) {
//
//        StringBuilder sb1 = new StringBuilder();
//        StringBuilder sb2 = new StringBuilder();
//        StringBuilder sb3 = new StringBuilder();
//        StringBuilder sb4 = new StringBuilder();
//
//        ///// FOR TESTS
//        BigInteger publicKey = p.multiply(q);
//        /////
//
//        String[] messages = new String[4];
//        BigInteger[] abFactors = calculateABFactors(p.intValue(), q.intValue()); // p and q as tested values
//
//        for (int i = 0; i < encryptedMessage.length(); i++) {
//            // M1 message
//            BigInteger[] mFactors = calculateMFactors(BigInteger.valueOf(encryptedMessage.codePointAt(i)), p.intValue(), q.intValue());
//            char decryptedChar = (char)(((abFactors[0].multiply(mFactors[0]))
//                    .add((abFactors[1].multiply(mFactors[2]))))
//                    .mod(publicKey)).intValue();
//            sb1.append(decryptedChar);
//
//            // M2 message
//            char decryptedChar1 = (char)(((abFactors[0].multiply(mFactors[0]))
//                    .add((abFactors[1].multiply(mFactors[3]))))
//                    .mod(publicKey)).intValue();
//            sb2.append((char)decryptedChar1);
//
//            // M3 message
//            char decryptedChar2 = (char)(((abFactors[0].multiply(mFactors[1]))
//                    .add((abFactors[1].multiply(mFactors[2]))))
//                    .mod(publicKey)).intValue();
//            sb3.append((char)decryptedChar2);
//
//
//            // M4 message
//            char decryptedChar3 = (char)(((abFactors[0].multiply(mFactors[1]))
//                    .add((abFactors[1].multiply(mFactors[3]))))
//                    .mod(publicKey)).intValue();
//            sb4.append((char)decryptedChar3);
//        }
//
//        messages[0] = sb1.toString();
//        messages[1] = sb2.toString();
//        messages[2] = sb3.toString();
//        messages[3] = sb4.toString();
//
//        return messages;
//    }

    static BigInteger[] convertStringToArray(String encryptedMessage) {
        String[] tabValues = encryptedMessage.split("\\_");
        BigInteger[] array = new BigInteger[tabValues.length];

        for (int i = 0; i < array.length; i++)
            array[i] = new BigInteger(tabValues[i]);

        return array;
    }

    static String decryptMessage(BigInteger[] encryptedMessage) {

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




