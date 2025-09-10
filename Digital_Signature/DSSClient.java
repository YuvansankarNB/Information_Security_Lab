import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.Scanner;

public class DSSClient {
    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 9876;

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter message to sign: ");
            String message = scanner.nextLine();

            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);

            byte[] receiveData = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("\nReceived from server:\n" + response);
            String[] parts = response.split(",");
            BigInteger r = new BigInteger(parts[0]);
            BigInteger s = new BigInteger(parts[1]);
            BigInteger p = new BigInteger(parts[2]);
            BigInteger q = new BigInteger(parts[3]);
            BigInteger g = new BigInteger(parts[4]);
            BigInteger y = new BigInteger(parts[5]);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(message.getBytes());
            BigInteger H = new BigInteger(1, hash);

            BigInteger w = s.modInverse(q);
            BigInteger u1 = H.multiply(w).mod(q);
            BigInteger u2 = r.multiply(w).mod(q);
            BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);

            System.out.println("\nVerification result:");
            if (v.equals(r)) {
                System.out.println("Signature is VALID");
            } else {
                System.out.println("Signature is INVALID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
