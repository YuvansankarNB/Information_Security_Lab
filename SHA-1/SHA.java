import java.util.Scanner;

public class SHA{

    private static int leftRotate(int value, int bits) {
        return (value << bits) | (value >>> (32 - bits));
    }
    public static int[] sha1Step(int a, int b, int c, int d, int e, int messageWord) {
        int f = b ^ c ^ d;         
        int K = 0x5A827999;     
        int temp = leftRotate(a, 5) + f + e + K + messageWord;
        e = d;
        d = c;
        c = leftRotate(b, 30);
        b = a;
        a = temp;
        return new int[]{a, b, c, d, e};
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        java.util.function.Function<String, Integer> parseHex = 
            s -> Integer.parseUnsignedInt(s.replace("0x", "").replace("0X", ""), 16);
        System.out.print("Enter a (hex): ");
        int a = parseHex.apply(scanner.next());
        System.out.print("Enter b (hex): ");
        int b = parseHex.apply(scanner.next());
        System.out.print("Enter c (hex): ");
        int c = parseHex.apply(scanner.next());
        System.out.print("Enter d (hex): ");
        int d = parseHex.apply(scanner.next());
        System.out.print("Enter e (hex): ");
        int e = parseHex.apply(scanner.next());

        System.out.print("Enter 4-character message: ");
        String message = scanner.next();
        byte[] bytes = message.getBytes();
        int messageWord = ((bytes[0] & 0xFF) << 24)|((bytes[1] & 0xFF) << 16)|
        ((bytes[2] & 0xFF) << 8)|(bytes[3] & 0xFF);
        System.out.printf("Message word W[0]: 0x%08X\n", messageWord);
        int[] updated = sha1Step(a, b, c, d, e, messageWord);
        System.out.println("\nUpdated values after one SHA-1 step:");
        System.out.printf("a = 0x%08X\n", updated[0]);
        System.out.printf("b = 0x%08X\n", updated[1]);
        System.out.printf("c = 0x%08X\n", updated[2]);
        System.out.printf("d = 0x%08X\n", updated[3]);
        System.out.printf("e = 0x%08X\n", updated[4]);
        scanner.close();
    }
}