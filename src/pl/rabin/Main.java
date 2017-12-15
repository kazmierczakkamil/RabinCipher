package pl.rabin;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.lang.Math;

public class Main {

    public static int p = 11;
    public static int q = 19;

    public static void main(String[] args) {

        // WORKING CORRECTLY
        byte[] byteA = new byte[1];
        byte[] byteB = new byte[1];

        byteA[0] = 123;
        byteB[0] = 4;

        BigInt a = new BigInt(byteA);
        BigInt b = new BigInt(byteB);

        BigInt c = a.add(b);

        System.out.println(c);






        //int message = 106;
        for (int message = 2; message < Main.p * Main.q; message++) {


            int encryptedMessage = encryptMessage(message, Main.p, Main.q);


            int[] decryptedMessages = decryptMessage(encryptedMessage);

            for (int i = 0; i < decryptedMessages.length; i++) {
                if (decryptedMessages[i] == message) {
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


        /*List<Integer> asdf = Main.getRabinPrimes(14);

        for (int i : asdf) {
            System.out.println(i);
        }

        String message = "a";
        System.out.println("Message: " + message);
        String encryptedMessage = encryptMessage(message, 379, 383);
        System.out.println("Encrypted message: " + encryptedMessage);

        String[] decryptedMessages = decryptMessage(encryptedMessage);

        int i = 0;
        for (String decryptedMessage : decryptedMessages) {
            System.out.println("Message " + i + ": " + decryptedMessage);
            i++;
        }
        System.out.println();*/


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

    static String encryptMessage(String message, int p, int q) {
        char[] charMessage = message.toCharArray();
        int n = p * q; // public key

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            int C;
            int M = charMessage[i];
            C = Main.powerModulo(M, 2, n);
            sb.append((char) C);
        }

        return sb.toString();
    }

    static int encryptMessage(int M, int p, int q) {
        int n = p * q; // public key
        int C = Main.powerModulo(M, 2, n);

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

    static int[] calculateABFactors(int p, int q) {
        int[] abFactors = new int[2];

        abFactors[0] = q * (inverseModulo(q, p)); // a-factor
        abFactors[1] = p * (inverseModulo(p, q)); // b-factor

        return abFactors;
    }

    static String[] decryptMessage(String encryptedMessage) {

        ///// FOR TESTS
        int p = Main.p;
        int q = Main.q;
        int publicKey = p * q;
        /////

        StringBuilder sb = new StringBuilder();
        String[] messages = new String[4];
        int[] encryptedMessageChars = encryptedMessage.chars().toArray();
        int[] abFactors = calculateABFactors(p, q); // p and q as tested values

        // M1 message
        for (int _char : encryptedMessageChars) {
            int[] mFactors = calculateMFactors(_char, p, q);
            int decryptedChar = (abFactors[0] * mFactors[0] + abFactors[1] * mFactors[2]) % publicKey;
            sb.append(decryptedChar);
        }
        messages[0] = sb.toString();

        sb = new StringBuilder();
        // M2 message
        for (int _char : encryptedMessageChars) {
            int[] mFactors = calculateMFactors(_char, p, q);
            int decryptedChar = (abFactors[0] * mFactors[0] + abFactors[1] * mFactors[3]) % publicKey;
            sb.append(decryptedChar);
        }
        messages[1] = sb.toString();

        sb = new StringBuilder();
        // M3 message
        for (int _char : encryptedMessageChars) {
            int[] mFactors = calculateMFactors(_char, p, q);
            int decryptedChar = (abFactors[0] * mFactors[1] + abFactors[1] * mFactors[2]) % publicKey;
            sb.append(decryptedChar);
        }
        messages[2] = sb.toString();

        sb = new StringBuilder();
        // M4 message
        for (int _char : encryptedMessageChars) {
            int[] mFactors = calculateMFactors(_char, p, q);
            int decryptedChar = (abFactors[0] * mFactors[1] + abFactors[1] * mFactors[3]) % publicKey;
            sb.append(decryptedChar);
        }
        messages[3] = sb.toString();

        return messages;
    }

    static int[] decryptMessage(int encryptedMessage) {

        ///// FOR TESTS
        int p = Main.p;
        int q = Main.q;
        int publicKey = p * q;
        /////

        int[] messages = new int[4];
        int[] abFactors = calculateABFactors(p, q); // p and q as tested values

        // M1 message
        int[] mFactors = calculateMFactors(encryptedMessage, p, q);
        int decryptedInt = (abFactors[0] * mFactors[0] + abFactors[1] * mFactors[2]) % publicKey;
        messages[0] = decryptedInt;

        // M2 message
        mFactors = calculateMFactors(encryptedMessage, p, q);
        decryptedInt = (abFactors[0] * mFactors[0] + abFactors[1] * mFactors[3]) % publicKey;
        messages[1] = decryptedInt;

        // M3 message
        mFactors = calculateMFactors(encryptedMessage, p, q);
        decryptedInt = (abFactors[0] * mFactors[1] + abFactors[1] * mFactors[2]) % publicKey;
        messages[2] = decryptedInt;


        // M4 message
        mFactors = calculateMFactors(encryptedMessage, p, q);
        decryptedInt = (abFactors[0] * mFactors[1] + abFactors[1] * mFactors[3]) % publicKey;
        messages[3] = decryptedInt;

        return messages;
    }


}




