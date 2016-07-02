/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ibangenerator;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author ntv
 */
class BBNCalculator {
    static final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static final String alphaNum = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static String generateRandomNumber(int count) {
        int n = ThreadLocalRandom.current().ints(0, (int) Math.pow(10.0, (double) count)).findFirst().getAsInt();
        String randomNum = Integer.toString(n);
        if (randomNum.length() < count) {
            randomNum = String.format("%0" + count + "d", n);
        }
        return randomNum;
    }

    static String generateRandomString(int count) {
        char c;
        StringBuilder sb = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < count; i++) {
            c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    static String generateRandomAlphaNumeric(int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(alphaNum.charAt(secureRnd.nextInt(alphaNum.length())));
        }
        return sb.toString();
    }
    
    /**
     * This method used to generate random number/String/alpha numeric in general case.
     * Use other generate methods in case of some special logic applies.
     * @param type the random type 
     * @param count number of letter/digits/alphanumeric
     * @return the randomly generated value
     */
    static String generateUniversaltype(char type, int count) {
         String output = "";
        switch (type) {
            case 'n':
                output = generateRandomNumber(count);
                break;
            case 'a':
                output = generateRandomString(count);
                break;
            case 'c':
                output = generateRandomAlphaNumeric(count);
                break;
        }
        return output;
    }

    /* Not Supported at the moment because AT,DE,NL does not contain this pattern*/
    static String generateBICBankCode(char type, int count) {
        return "";
    }

    static String generateNationalBankCode(char type, int count) {
       return generateUniversaltype(type, count);
    }

    static String generateAccountNumber(char type, int count) {
        return generateUniversaltype(type, count);
    }

    /* Not Supported at the moment because AT,DE,NL does not contain this pattern*/
    static String generateNationalIdentificationNumber(char type, int count) {
        return "";
    }

    /* Not Supported at the moment because AT,DE,NL does not contain this pattern*/
    static String generateCurrency(char type, int count) {
        return "";
    }

    /* Not Supported at the moment because AT,DE,NL does not contain this pattern*/
    static String generateBranchNumber(char type, int count) {
        return "";
    }

    /* Not Supported at the moment because AT,DE,NL does not contain this pattern*/
    static String generateBranchCode(char type, int count) {
        return "";
    }

    /* Not Supported at the moment because AT,DE,NL does not contain this pattern*/
    static String generateAccountType(char type, int count) {
        return "";
    }

    /* Not Supported at the moment because AT,DE,NL does not contain this pattern*/
    static String generateNationalCheckDigit(char type, int count) {
        return "";
    }

    /* Not Supported at the moment because AT,DE,NL does not contain this pattern*/
    static String generateZeros(char type, int count) {
        return "";
    }
}
