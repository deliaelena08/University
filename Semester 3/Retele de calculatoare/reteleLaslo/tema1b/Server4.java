import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Server4 {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Serverul este pornit. Aștept conectări...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("S-a conectat un client.");

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // Citim lungimea șirului
                byte[] lengthBytes = new byte[4];
		in.read(lengthBytes);
                int length=ByteBuffer.wrap(lengthBytes).getInt();
		length=Integer.reverseBytes(length);
		System.out.println("Lungime primita a sirului este de "+length);
                // Convertim byte array în string

		byte[] stringBytes = new byte[length];
		in.read(stringBytes);
                String inputString = new String(stringBytes);
                System.out.println("Sirul primit: " + inputString);

                // Inversăm șirul
                String reversedString = new StringBuilder(inputString).reverse().toString();
                System.out.println("Sirul inversat: " + reversedString);

		out.writeInt(reversedString.length());
		out.write(reversedString.getBytes());
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Eroare la server: " + e.getMessage());
        }
    }
}
