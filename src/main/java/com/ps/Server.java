package com.ps;

import com.ps.server.MulticastServer;
import com.ps.server.TCPServer;
import com.ps.server.UDPServer;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Server {
	public static int PORT = 7777;
	public static Semaphore udpSemaphore = new Semaphore(0);
	public static Semaphore tcpSemaphore = new Semaphore(0);
	public static boolean exit = false;

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter port number: ");
		PORT = scanner.nextInt();
		startServer();
		while (!exit) {
			String command = scanner.nextLine();
			if (command.equals("exit")) {
				exit = true;
			}
		}
		try {
			tcpSemaphore.acquire();
			udpSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Server stopped");
		System.exit(0);
	}

	private static void startServer() throws IOException {
		new Thread(new TCPServer()).start();
		new Thread(new UDPServer()).start();
		new Thread(new MulticastServer()).start();
	}
}
