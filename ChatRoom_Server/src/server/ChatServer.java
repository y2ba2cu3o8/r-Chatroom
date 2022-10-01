package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	ServerSocket server;

	public void setupServer(int port) {
		try {
			server = new ServerSocket(port);
			System.out.println("��������ɹ�");
			while (true) {
				Socket client = server.accept();
				System.out.println("Incoming" + client.getRemoteSocketAddress());
				ServerThread st = new ServerThread(client);
				st.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeServer() {
		try {
			server.close();
			System.out.println("�������رճɹ�");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * �������Է�����
	 */

	public static void main(String[] args) {
		System.out.println("����������");
		ChatServer cs = new ChatServer();
		cs.setupServer(9090);
	}

}
