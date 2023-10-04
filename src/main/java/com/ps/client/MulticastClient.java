package com.ps.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashSet;
import java.util.Set;

import static com.ps.Server.exit;
import static java.lang.Thread.sleep;

public class MulticastClient implements Runnable {
	final int bufferSize = 1024;
	Set<String> servers = new HashSet<>();

	@Override
	public void run() {
		try {
			InetAddress group = InetAddress.getByName("230.0.0.0");

			byte[] receiveBuffer = new byte[bufferSize];
			byte[] sendingBuffer = "DISCOVER".getBytes();

			MulticastSocket socket = new MulticastSocket(65000);
			socket.joinGroup(group);

			DatagramPacket requestPacket = new DatagramPacket(sendingBuffer, sendingBuffer.length, group, 65000);
			DatagramPacket receivedPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

			while (!exit) {
				socket.send(requestPacket);
				socket.receive(receivedPacket);
				String received = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
				if (received.startsWith("OFFER")) {
					if (!servers.contains(received)) {
						servers.add(received);
						System.out.println("Found server: " + receivedPacket.getAddress().toString().substring(receivedPacket.getAddress().toString().lastIndexOf("/") + 1) + ":" + received.split(":")[1]);
					}
				}
				sleep(500);
			}
			socket.leaveGroup(group);
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}
}
