import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class DSSServer {
    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(9876)) {
            byte[] receiveData = new byte[1024];
            System.out.println("DSA UDP Server is running...");

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received message: " + message);
            SecureRandom random = new SecureRandom();
            BigInteger q, p, k, g, h, x, y;
            do {
                q = new BigInteger(160, 64, random);
            } while (!q.isProbablePrime(64));

            do {
                k = new BigInteger(864, random);
                p = q.multiply(k).add(BigInteger.ONE);
            } while (!p.isProbablePrime(64));

            BigInteger exp = p.subtract(BigInteger.ONE).divide(q);
            do {
                h = new BigInteger(p.bitLength() - 1, random);
                g = h.modPow(exp, p);
            } while (g.compareTo(BigInteger.ONE) <= 0);

            do {
                x = new BigInteger(q.bitLength(), random);
            } while (x.compareTo(BigInteger.ZERO) <= 0 || x.compareTo(q) >= 0);

            y = g.modPow(x, p);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(message.getBytes());
            BigInteger H = new BigInteger(1, hash);
            BigInteger r, s;
            do {
                do {
                    k = new BigInteger(q.bitLength(), random);
                } while (k.compareTo(BigInteger.ZERO) <= 0 || k.compareTo(q) >= 0);

                r = g.modPow(k, p).mod(q);
                BigInteger kInv = k.modInverse(q);
                s = kInv.multiply(H.add(x.multiply(r))).mod(q);
            } while (r.equals(BigInteger.ZERO) || s.equals(BigInteger.ZERO));
            String response = r + "," + s + "," + p + "," + q + "," + g + "," + y;
            byte[] sendData = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                sendData,
                sendData.length,
                receivePacket.getAddress(),
                receivePacket.getPort()
            );
            serverSocket.send(sendPacket);
            System.out.println("Signature and public key sent to client.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
