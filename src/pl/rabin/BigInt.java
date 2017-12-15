package pl.rabin;

import java.util.Arrays;

public class BigInt {

    private byte[] digits;
    private boolean isNegative;

    public BigInt(byte[] digits) {
        this.digits = digits;
    }

    public BigInt(byte[] digits, boolean isNegative) {
        this.digits = digits;
        this.isNegative = isNegative;
    }

    public static final BigInt ZERO = new BigInt(new byte[]{0});
    public static final BigInt ONE = new BigInt(new byte[]{1});

    public BigInt add(BigInt other) {
        byte[] digits1, digits2;
        if (this.digits.length >= other.digits.length) {
            digits1 = this.digits;
            digits2 = other.digits;
        } else {
            digits1 = other.digits;
            digits2 = this.digits;
        }
        int remainder = 0;
        int s1 = digits1.length;
        int s2 = digits2.length;
        byte[] resultDigits = new byte[s1+1];
        for (int i = 0; i < s1; i++) {
            int digit1 = digits1[i];
            int digit2 = i >= s2 ? 0 : digits2[i];
            int r = digit1 + digit2 + remainder;
            remainder = r > 9 ? 1 : 0;
            resultDigits[i] = (byte) (r % 10);
        }
        resultDigits[s1] = (byte) remainder;




        return new BigInt(resultDigits);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BigInt))
            return false;

        byte[] digits1 = this.digits;
        byte[] digits2 = ((BigInt) object).getDigits();
        if (digits1.length != digits2.length) return false;
        for (int i = 0; i < digits1.length; i++) {
            if (digits1[i] != digits2[i]) return false;
        }
        return true;
    }

    public int compareTo(BigInt object) {
        byte[] digits1 = this.digits;
        byte[] digits2 = object.getDigits();
        int nzLength1 = digits1.length;
        int nzLength2 = digits2.length;
        for (int i = nzLength1 - 1; i >= 0; i--) {
            if (digits1[i] != 0)
                break;
            nzLength1--;
        }
        for (int i = nzLength2 - 1; i >= 0; i--) {
            if (digits2[i] != 0)
                break;
            nzLength2--;
        }
        if (nzLength1 > nzLength2)
            return 1;
        else if (nzLength1 < nzLength2)
            return -1;
        else {
            for (int i = nzLength1 - 1; i >= 0; i--) {
                if (digits1[i] == digits2[i])
                    continue;
                else if (digits1[1] > digits2[i])
                    return 1;
                else
                    return -1;
            }
        }
        return 0;
    }

    public BigInt subtract(BigInt other) {
        boolean isNegative = this.compareTo(other) < 0;
        byte[] digits1, digits2;
        if (!isNegative) {
            digits1 = this.digits;
            digits2 = other.digits;
        } else {
            digits1 = other.digits;
            digits2 = this.digits;
        }
        int remainder = 0;
        int s1 = digits1.length;
        int s2 = digits2.length;
        byte[] resultDigits = new byte[s1+1];
        for (int i = 0; i < s1; i++) {
            int digit1 = digits1[i];
            int digit2 = i >= s2 ? 0 : digits2[i];
            int r = 10 + digit1 - digit2 - remainder;
            remainder = r > 9 ? 1 : 0;
            resultDigits[i] = (byte) (r % 10);
        }
        resultDigits[s1] = (byte)remainder;
        return new BigInt(resultDigits, isNegative);
    }

    public byte[] getDigits() {
        return digits;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = digits.length-1; i >= 0; i--) {
            sb.append(String.valueOf(digits[i]));
        }

        String value = sb.toString();

//        for (int i = 0; i < value.length(); i++) {
//            if (value.charAt(i) == '0') {
//                value = value.substring(1);
//            } else {
//                break;
//            }
//
//        }

        return value;
    }
}
