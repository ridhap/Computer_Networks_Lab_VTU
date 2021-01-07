//11. Write a program for simple RSA algorithm to encrypt and decrypt the data.
/*
Key generation:
1) Select random prime numbers p and q, and check that p != q
2) Compute modulus n = pq
3) Compute phi = (p 1)(q 1)
4) Select public exponent e, 1 < e < phi such that gcd(e,phi) = 1
5) Compute private exponent d = e^ 1 mod phi
6) Public key is {n, e}, private key is d

Encryption: c= m e mod n , decryption: m = c d mod n
*/

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;
public class RSA {
	
	static BigInteger p, q, n, phi, e, d;
	static SecureRandom secureRandom;
	static int bitLength = 64;
	
	static String encrypt(String msg) {
		return new BigInteger(msg).modPow(e, n).toString();
	}
	
	static String decrypt(String cipher) {
		return new BigInteger(cipher).modPow(d, n).toString();
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		secureRandom = new SecureRandom();
		p = BigInteger.probablePrime(bitLength, secureRandom);
		q = BigInteger.probablePrime(bitLength, secureRandom);
		n = p.multiply(q);
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		e = BigInteger.probablePrime(bitLength / 2, secureRandom);
		
		while (e.gcd(phi).compareTo(BigInteger.ONE) != 0 &&e.compareTo(phi) < 0)
		{
			e = e.add(BigInteger.ONE);
		}
		
		d = e.modInverse(phi);
		System.out.println("P =: " + p);
		System.out.println("Q =: " + q);
		System.out.println("N =: " + n);
		System.out.println("PHI =: " + phi);
		System.out.println("\nEnter your Message");
		String msg = sc.next();
		String encryptedMessage = encrypt(msg);
		System.out.println("Encrypted Message: " + encryptedMessage);
		String decryptedMessage = decrypt(encryptedMessage);
		System.out.println("Decrypted Message: " + decryptedMessage);
		sc.close();
	}
}
/*

OUTPUT:--
P =: 16781739680627642401
Q =: 15495285510235664579
N =: 260037847709776396618399617312194214179
PHI =: 260037847709776396586122592121330907200

Enter your Message
5432101
Encrypted Message: 91889348894766405910953729310624075803
Decrypted Message: 5432101
*/
