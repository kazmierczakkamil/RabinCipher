package pl.rabin;

import java.util.Arrays;
import java.util.Formatter;

public class BigInt implements Comparable<BigInt> {
    final static int BASE = 1000000000;
    final static int BASE_DECIMAL_DIGITS = 9;

    private int[] digits;

    public BigInt(int... digits) {
        for (int digit : digits) {
            if (digit < 0 || BASE <= digit) {
                throw new IllegalArgumentException("digit " + digit + " out of range.");
            }
        }
        this.digits = digits.clone();

    }

    @Override
    public String toString() {
        return "BigInt" + Arrays.toString(digits);
    }

    public static BigInt valueOf(String decimalDigits) {
        int decLen = decimalDigits.length();
        int bigLen = (decLen-1) / BASE_DECIMAL_DIGITS + 1;

        int firstSome = decLen - (bigLen-1) * BASE_DECIMAL_DIGITS;

        int[] digits = new int[bigLen];

        for (int i = 0; i < bigLen; i++) {
            String block = decimalDigits.substring(Math.max(firstSome + (i-1) * BASE_DECIMAL_DIGITS, 0),
                    firstSome + i * BASE_DECIMAL_DIGITS);

            digits[i] = Integer.parseInt(block);
        }

        return new BigInt(digits);
    }

    public String toDecimalString() {
        Formatter f = new Formatter();
        f.format("%d", digits[0]);
        for(int i = 1 ; i < digits.length; i++) {
            f.format("%09d", digits[i]);
        }
        return f.toString();
    }

    /**
     * calculates the sum of this and that.
     */
    public BigInt plus(BigInt that) {
        int[] result = new int[Math.max(this.digits.length,
                that.digits.length)+ 1];

        addDigits(result, result.length-1, this.digits);
        addDigits(result, result.length-1, that.digits);

        // cut of leading zero, if any
        if(result[0] == 0) {
            result = Arrays.copyOfRange(result, 1, result.length);
        }
        return new BigInt(result);
    }

    /**
     * adds one digit from the addend to the corresponding digit
     * of the result.
     * If there is carry, it is recursively added to the next digit
     * of the result.
     */
    private void addDigit(int[] result, int resultIndex, int addendDigit) {
        int sum = result[resultIndex] + addendDigit;
        result[resultIndex] = sum % BASE;
        int carry = sum / BASE;
        if(carry > 0) {
            addDigit(result, resultIndex - 1, carry);
        }
    }

    /**
     * adds all the digits from the addend array to the result array.
     */
    private void addDigits(int[] result, int resultIndex, int... addend) {
        int addendIndex = addend.length - 1;
        while(addendIndex >= 0) {
            addDigit(result, resultIndex,
                    addend[addendIndex]);
            addendIndex--;
            resultIndex--;
        }
    }

    /**
     * returns the product {@code this × that}.
     */
    public BigInt times(BigInt that) {
        int[] result = new int[this.digits.length + that.digits.length];
        multiplyDigits(result, result.length-1,
                this.digits, that.digits);

        // cut off leading zero, if any
        if(result[0] == 0) {
            result = Arrays.copyOfRange(result, 1, result.length);
        }
        return new BigInt(result);
    }

    /**
     * multiplies two digits and adds the product to the result array
     * at the right digit-position.
     */
    private void multiplyDigit(int[] result, int resultIndex,
                               int firstFactor, int secondFactor) {
        long prod = (long)firstFactor * (long)secondFactor;
        int prodDigit = (int)(prod % BASE);
        int carry = (int)(prod / BASE);
        addDigits(result, resultIndex, carry, prodDigit);
    }

    private void multiplyDigits(int[] result, int resultIndex,
                                int[] leftFactor, int[] rightFactor) {
        for(int i = 0; i < leftFactor.length; i++) {
            for(int j = 0; j < rightFactor.length; j++) {

                multiplyDigit(result, resultIndex - (i + j),
                        leftFactor[leftFactor.length-i-1],
                        rightFactor[rightFactor.length-j-1]);
            }
        }
    }


    @Override
    public int compareTo(BigInt that) {
        if(this.digits.length < that.digits.length) {
            return -1;
        }
        if (that.digits.length < this.digits.length) {
            return 1;
        }

        for(int i = 0; i < this.digits.length; i++) {
            if(this.digits[i] < that.digits[i]) {
                return -1;
            }
            if(that.digits[i] < this.digits[i]) {
                return 1;
            }
        }

        return 0;
    }

    /**
     * calculates a hashCode for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for(int digit : digits) {
            hash = hash * 13 + digit;
        }
        return hash;
    }

    /**
     * compares this object with another object for equality.
     * A DecimalBigInt is equal to another object only if this other
     * object is also a DecimalBigInt and both represent the same
     * natural number.
     */
    public boolean equals(Object o) {
        return o instanceof BigInt && this.compareTo((BigInt) o) == 0;
    }

    /**
     * calculates the factorial of an int number.
     * This uses a simple iterative loop.
     */
    public static BigInt factorial(int n) {
        BigInt fac = new BigInt(1);
        for(int i = 2; i <= n; i++) {
            fac = fac.times(new BigInt(i));
        }
        return fac;
    }

    public static BigInt valueOf(String text, int radix) {
        BigInt bigRadix = new BigInt(radix);
        BigInt value = new BigInt(); // 0
        for(char digit : text.toCharArray()) {
            BigInt bigDigit = new BigInt(Character.digit(digit, radix));
            value = value.times(bigRadix).plus(bigDigit);
        }
        return value;
    }

    /**
     * Divides this number by a small number.
     * @param divisor an integer with {@code 0 < divisor < BASE}.
     * @return the integer part of the quotient, ignoring the remainder.
     * @throws IllegalArgumentException if the divisor is <= 0 or >= BASE.
     */
    public BigInt divideBy(int divisor)
    {
        if(divisor <= 0 || BASE <= divisor) {
            throw new IllegalArgumentException("divisor " + divisor +
                    " out of range!");
        }

        int[] result = new int[digits.length];
        divideDigits(result, 0,
                digits, 0,
                divisor);
        return new BigInt(result);
    }

    /**
     * does one step in the short division algorithm, i.e. divides
     *  a two-digit number by a one-digit one.
     *
     * @param result the array to put the quotient digit in.
     * @param resultIndex the index in the result array where
     *             the quotient digit should be put.
     * @param divident the last digit of the divident.
     * @param lastRemainder the first digit of the divident (being the
     *           remainder of the operation one digit to the left).
     *           This must be < divisor.
     * @param divisor the divisor.
     * @returns the remainder of the division operation.
     */
    private int divideDigit(int[] result, int resultIndex,
                            int divident, int lastRemainder,
                            int divisor) {
        assert divisor < BASE;
        assert lastRemainder < divisor;

        long ent = divident + (long)BASE * lastRemainder;

        long quot = ent / divisor;
        long rem = ent % divisor;

        assert quot < BASE;
        assert rem < divisor;

        result[resultIndex] = (int)quot;
        return (int)rem;
    }

    /**
     * The short division algorithm, like described in
     * <a href="http://en.wikipedia.org/wiki/Short_division">Wikipedia's
     *   article <em>Short division</em></a>.
     * @param result an array where we should put the quotient digits in.
     * @param resultIndex the index in the array where the highest order digit
     *     should be put, the next digits will follow.
     * @param divident the array with the divident's digits. (These will only
     *          be read, not written to.)
     * @param dividentIndex the index in the divident array where we should
     *         start dividing. We will continue until the end of the array.
     * @param divisor the divisor. This must be a number smaller than
     *        {@link #BASE}.
     * @return the remainder, which will be a number smaller than
     *     {@code divisor}.
     */
    private int divideDigits(int[] result, int resultIndex,
                             int[] divident, int dividentIndex,
                             int divisor) {
        int remainder = 0;
        for(; dividentIndex < divident.length; dividentIndex++, resultIndex++) {
            remainder = divideDigit(result, resultIndex,
                    divident[dividentIndex],
                    remainder, divisor);
        }
        return remainder;
    }

    /**
     * Divides this number by a small number, returning the remainder.
     * @param divisor an integer with {@code 0 < divisor < BASE}.
     * @return the remainder from the division {@code this / divisor}.
     * @throws IllegalArgumentException if the divisor is <= 0 or >= BASE.
     */
    public int modulo(int divisor) {
        if(divisor <= 0 || BASE <= divisor) {
            throw new IllegalArgumentException("divisor " + divisor +
                    " out of range!");
        }
        int[] result = new int[digits.length];
        return divideDigits(result, 0,
                digits, 0,
                divisor);
    }

    /**
     * converts this number to an arbitrary radix.
     * @param radix the target radix, {@code 1 < radix < BASE}.
     * @return the digits of this number in the base-radix system,
     *     in big-endian order.
     */
    public int[] convertTo(int radix) {
        if (radix <= 1 || BASE <= radix) {
            throw new IllegalArgumentException("radix " + radix +
                    " out of range!");
        }

        // zero has no digits.
        if (digits.length == 0)
            return new int[0];

        // raw estimation how many output digits we will need.
        // This is just enough in cases like BASE-1, and up to
        // 30 digits (for base 2) too much for something like (1,0,0).
        int len = (int) (Math.log(BASE) / Math.log(radix) * digits.length) + 1;
        int[] rDigits = new int[len];
        int rIndex = len - 1;
        int[] current = digits;
        int quotLen = digits.length;

        while (quotLen > 0) {
            int[] quot = new int[quotLen];
            int rem = divideDigits(quot, 0, current, current.length - quotLen, radix);
            rDigits[rIndex] = rem;
            rIndex--;
            current = quot;

            if (current[0] == 0) {
                // omit leading zeros in next round.
                quotLen--;
            }
        }

        //cut of leading zeros in rDigits:
        while (rIndex < 0 || rDigits[rIndex] == 0) {
            rIndex++;
        }
        return Arrays.copyOfRange(rDigits, rIndex, rDigits.length);
    }


    public String toString(int radix) {
        if(radix < Character.MIN_RADIX || Character.MAX_RADIX < radix) {
            throw new IllegalArgumentException("radix out of range: " + radix);
        }
        if(digits.length == 0)
            return "0";
        int[] rdigits = convertTo(radix);
        StringBuilder b = new StringBuilder(rdigits.length);
        for(int dig : rdigits) {
            b.append(Character.forDigit(dig, radix));
        }
        return b.toString();
    }
}

