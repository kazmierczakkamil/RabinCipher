package pl.rabin;

import java.util.ArrayList;
import java.util.List;

public class Rabin {

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

}
