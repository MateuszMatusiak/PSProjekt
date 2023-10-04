package com.ps.client;

import java.io.IOException;
import java.net.*;

import static com.ps.Client.*;
import static java.lang.Thread.sleep;


public class UDPClient implements Runnable {
	private final DatagramSocket clientSocket;
	private final InetAddress serverAddress;

	public UDPClient() throws SocketException, UnknownHostException {
		this.serverAddress = InetAddress.getByName(HOST);
		this.clientSocket = new DatagramSocket();
	}

	@Override
	public void run() {
		while (true) {
			try {
				udpSemaphore.acquire();
				if (exit) break;
				byte[] buffer = ("SIZE:" + bufferSize).getBytes();
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);
				clientSocket.send(packet);
				while (!stop) {
					buffer = new byte[bufferSize];
					packet = new DatagramPacket(buffer, bufferSize, serverAddress, PORT);
					clientSocket.send(packet);
					sleep(10L);
				}
				buffer = ("FINE").getBytes();
				packet = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);
				clientSocket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
