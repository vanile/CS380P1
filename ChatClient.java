import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient extends Thread {
	private Socket socket;
	private static final String EXIT = "exit";

	public ChatClient(Socket socket) {
		this.socket = socket;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String input = "";

		try (Socket socket = new Socket("18.221.102.182", 38001)) {
			OutputStream os = socket.getOutputStream();
			PrintStream out = new PrintStream(os, true, "UTF-8");

			System.out.println("Connected to server");
			System.out.println("Client> Enter username");

			input = sc.nextLine();
			out.print(input);

			Thread thread = new Thread(new ChatClient(socket));
			thread.start();

			while (sc.hasNext()) {
				input = sc.nextLine();

				if (input.equals(EXIT)) {
					System.out.println("Server> You have disconnected.");
					System.exit(0);
				} else {
					out.println(input);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String input = "";

		try {
			while (true) {
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);

				input = br.readLine();
				System.out.println("Server> " + input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}