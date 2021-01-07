//7. Write a program for error detecting code using CRC-CCITT (16-bits).
/*
Overview:
The accurate implementations (long-hand and programmed) of the 16-bit CRC-CCITT specification, is as follows:
• Width = 16 bits
• Truncated polynomial = 0x1021
• Initial value = 0xFFFF
• Input data is NOT reflected
• Output CRC is NOT reflected
• No XOR is performed on the output CRC

*/    
import java.util.Scanner;

class CRC {

    static String xor(String dividend, String divisor) {

        String result = new String();
        for (int i = 0; i < 17; i++) {
            if (dividend.charAt(i) == divisor.charAt(i)) {
            	result=result+'0';
            } else {
            	result=result+'1';
            }
        }
        return result.toString();
    }

    static String divide(String dividend, String divisor) {
        int divisorLength = 17;
        int dividendLength =dividend.length();
        String temp;
        while (dividendLength >= divisorLength) {
            if (dividend.charAt(0) == '1')
                temp = xor(divisor, dividend.substring(0, divisorLength));
            else
                temp = dividend.substring(0, divisorLength);
            dividend = temp.substring(1) + dividend.substring(divisorLength);
            dividendLength -= 1;
        }   
        return dividend;
    }
/*------------------CRCGENERATION ------------------------ */
    static String codeword_eval(String dataword, String poly) {
        int DwLength = dataword.length();
    	//int polyLength = 17;
    	//System.out.println(polyLength);
    //	System.out.println(DwLength);
        String dividend=dataword+"0000000000000000";
    //	System.out.println(DwLength + 17 - 1);
        dividend=dividend.substring(0,(DwLength + 17 - 1));
        System.out.println("dividend is --"+dividend); 
	        String remainder = divide(dividend, poly);
        return dataword + remainder;
    }
/*-------------------ERRORDETECTION --------------------- */
    static boolean checkCodeWord(String codeword, String poly) {
        String temp = divide(codeword, poly);
        int len = temp.length();
        for (int i = 0; i < len; i++) {
            if (temp.charAt(i) == '1') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
     //   System.out.println("Enter Data Word String");
        String poly = "10001000000100001";
        System.out.println("Enter dataword");
        String dataword = scanner.next();
        String result = codeword_eval(dataword, poly);
        System.out.println("CodeWord: " + result);    
        for(int i=0;i<2;i++) {
        System.out.println("Enter Code Word");
        String codeWord = scanner.next();
        if (checkCodeWord(codeWord, poly)) {
        	System.out.println("Code Word is Valid");
        } else {
            System.out.println("Code Word is Invalid") ;
            }
        }
        scanner.close();
    }
}
