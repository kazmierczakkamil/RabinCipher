package pl.rabin;

public class Rabin {

    private int[] privateKeys;
    private int publicKey;

    Rabin() {
        privateKeys = new int[2];
        privateKeys[0] = Main.rollCorrectPrime(Main.getRabinPrimes(40));
        privateKeys[1] = Main.rollCorrectPrime(Main.getRabinPrimes(40));
        publicKey = privateKeys[0] * privateKeys[1];
    }

    // chyba kazdy bit w ten sposob
    // todo: dlugosc wiadomosci mniejsza niz n
    // todo: wtedy c = m^2 mod n

    public void printInfo() {
        System.out.println("p = " + privateKeys[0]);
        System.out.println("q = " + privateKeys[1]);
        System.out.println("n = " + publicKey);
    }

}
