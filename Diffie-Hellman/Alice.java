import java.math.BigInteger;
import java.net.*;
import java.util.*;

public class Alice{
    static int findPrimitiveRoot(int p) {
        int phi = p - 1;
        Set<Integer> factors = new HashSet<>();
        int n = phi;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                factors.add(i);
                while (n % i == 0){
                    n /= i;
                }
            }
        }
        if (n > 1) factors.add(n);
        for (int g = 2; g < p; g++) {
            boolean flag = true;
            for (int factor : factors) {
                BigInteger G = BigInteger.valueOf(g);
                BigInteger P = BigInteger.valueOf(p);
                BigInteger exp = BigInteger.valueOf(phi / factor);
                if (G.modPow(exp, P).equals(BigInteger.ONE)) {
                    flag = false;
                    break;
                }
            }
            if (flag) return g;
        }
        return -1;
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a prime number p: ");
        int p = sc.nextInt();
        int g = findPrimitiveRoot(p);
        if (g == -1) {
            System.out.println("No primitive root found. Exiting.");
            return;
        }
        System.out.println("Primitive root g: " + g);
        Random rand = new Random();
        BigInteger P = BigInteger.valueOf(p);
        BigInteger G = BigInteger.valueOf(g);
        BigInteger a = new BigInteger(P.bitLength() - 1, rand).mod(P.subtract(BigInteger.TWO)).add(BigInteger.ONE);
        BigInteger A = G.modPow(a, P); 
        DatagramSocket socket = new DatagramSocket(9876);
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();
        String message = p + "," + g + "," + A.toString();
        byte[] sendBuffer = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
        socket.send(sendPacket);
        receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);
        String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
        BigInteger B = new BigInteger(received);
        System.out.println("Received key from B: " + B);
        BigInteger sharedSecret = B.modPow(a, P);
        System.out.println("Shared Secret key is " + sharedSecret);
        socket.close();
        sc.close();
    }
}