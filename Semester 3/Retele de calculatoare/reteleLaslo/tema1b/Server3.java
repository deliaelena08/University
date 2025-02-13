import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Server3 {
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(1234);
            while (true) {
                Socket c = s.accept();
                System.out.println("S-a conectat un client.");

                DataInputStream in = new DataInputStream(c.getInputStream());
                DataOutputStream out = new DataOutputStream(c.getOutputStream());

		byte[] lengthBytes = new byte[4];
                in.read(lengthBytes);
                int length = ByteBuffer.wrap(lengthBytes).getInt();
                length = Integer.reverseBytes(length);
		//System.out.println("Numarul de caractere este "+ length);

		byte[] stringBytes = new byte[length];
                in.read(stringBytes);
		String receivedString = new String(stringBytes);
		System.out.println("Sirul primit este:\n " + receivedString);

		int spaceCount = 0;
                for (int i = 0; i < receivedString.length(); i++) {
                    if (receivedString.charAt(i) == ' ') {
                        spaceCount++;
                    }
                }
                System.out.println("Numărul de spații: " + spaceCount);

                out.writeInt(spaceCount);
		c.close();
            }

        } catch (IOException e) {
            System.out.println("Eroare la crearea socketului server: " + e.getMessage());
        }
    }
}

