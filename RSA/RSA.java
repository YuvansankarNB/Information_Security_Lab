import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class RSA {
    private BigInteger p, q, n, phi, e, d;
    private int bitLen = 1024;
    private SecureRandom random = new SecureRandom();

    public RSA() {
        p = BigInteger.probablePrime(bitLen, random);
        q = BigInteger.probablePrime(bitLen, random);
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        n = p.multiply(q);
        System.out.println("n: " + n);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = new BigInteger("65537");
        while (phi.gcd(e).intValue() > 1) {
            e = e.add(BigInteger.TWO);
        }
        d = e.modInverse(phi);
    }

    public BigInteger encrypt(BigInteger msg) {
        return msg.modPow(e, n);
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d, n);
    }

    public static void main(String[] args) {
        RSA rsa = new RSA();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- RSA Menu ---");
            System.out.println("1. Encrypt a message");
            System.out.println("2. Decrypt a message");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter a message to encrypt: ");
                    String msg = sc.nextLine();
                    BigInteger msgnum = new BigInteger(msg.getBytes());
                    BigInteger ciphertext = rsa.encrypt(msgnum);
                    System.out.println("Encrypted (BigInteger): " + ciphertext);
                    break;

                case 2:
                    System.out.print("Enter ciphertext (BigInteger): ");
                    String cipherInput = sc.nextLine();
                    try {
                        BigInteger cipherNum = new BigInteger(cipherInput);
                        BigInteger decryptedNum = rsa.decrypt(cipherNum);
                        String decryptedMsg = new String(decryptedNum.toByteArray());
                        System.out.println("Decrypted message: " + decryptedMsg);
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid ciphertext format!");
                    }
                    break;

                case 3:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
