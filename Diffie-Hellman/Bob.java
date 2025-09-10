import java.math.BigInteger;
import java.net.*;
import java.util.*;

public class Bob{
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");
        int serverPort = 9876;
        String sample="Yuvan";
        byte[] samplebuffer=sample.getBytes();
        DatagramPacket send = new DatagramPacket(samplebuffer, samplebuffer.length, serverAddress, serverPort);
        socket.send(send);
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);
        String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
        String[] parts = received.split(",");
        int p = Integer.parseInt(parts[0]);
        int g = Integer.parseInt(parts[1]);
        BigInteger A = new BigInteger(parts[2]);
        System.out.println("Received p = " + p + ", g = " + g + ", A = " + A);
        Random rand = new Random();
        BigInteger P = BigInteger.valueOf(p);
        BigInteger G = BigInteger.valueOf(g);
        BigInteger b = new BigInteger(P.bitLength() - 1, rand).mod(P.subtract(BigInteger.TWO)).add(BigInteger.ONE);
        BigInteger B = G.modPow(b, P);
        String message = B.toString();
        byte[] sendBuffer = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
        socket.send(sendPacket);
        BigInteger sharedSecret = A.modPow(b, P);
        System.out.println("Shared Secret (Client): " + sharedSecret);
        socket.close();
    }
}