package com.ps;

import com.ps.client.MulticastClient;
import com.ps.client.TCPClient;
import com.ps.client.UDPClient;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Client {
	public static Semaphore udpSemaphore = new Semaphore(0);
	public static Semaphore tcpSemaphore = new Semaphore(0);
	public static String HOST = "127.0.0.1";
	public static int PORT = 7777;
	public static int bufferSize = 60000;
	public static boolean stop = false;
	public static boolean exit = false;
	public static boolean useNagle = false;

	public static void main(String[] args) {
		new Thread(new MulticastClient()).start();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter ip:");
		HOST = scanner.nextLine();
		System.out.println("Enter port:");
		PORT = scanner.nextInt();
		System.out.println("Use Nagle algorithm? (y/n)");
		String useNagleString = scanner.next();
		if (useNagleString.equalsIgnoreCase("y")) {
			useNagle = true;
		}
		System.out.println("Enter buffer size:");
		do {
			bufferSize = scanner.nextInt();
			if (bufferSize < 1) {
				System.out.println("Buffer size must be greater than 0");
			}
			if (bufferSize > 60000) {
				System.out.println("Buffer size must be less than 60000");
			}
		} while (bufferSize < 1 || bufferSize > 60000);
		System.out.println("Enter command:");
		try {
			startClient();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		while (true) {
			String command = scanner.nextLine();
			if (command.equalsIgnoreCase("start")) {
				stop = false;
				tcpSemaphore.release();
				udpSemaphore.release();
			} else if (command.equals("stop")) {
				stop = true;
			} else if (command.equals("exit")) {
				stop = true;
				exit = true;
				udpSemaphore.release();
				tcpSemaphore.release();
				break;
			}
		}
		System.exit(0);
	}

	private static void startClient() throws IOException {
		new Thread(new TCPClient()).start();
		new Thread(new UDPClient()).start();
	}

}
