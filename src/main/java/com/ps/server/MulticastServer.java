package com.ps.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static com.ps.Server.PORT;
import static com.ps.Server.exit;
import static java.lang.Thread.sleep;

public class MulticastServer implements Runnable {
	final int bufferSize = 1024;

	@Override
	public void run() {

		try {
			InetAddress group = InetAddress.getByName("230.0.0.0");

			byte[] receivingBuffer = new byte[bufferSize];
			byte[] sendingBuffer = ("OFFER:" + PORT).getBytes();

			DatagramPacket responsePacket = new DatagramPacket(sendingBuffer, sendingBuffer.length, group, 65000);
			DatagramPacket receivedPacket = new DatagramPacket(receivingBuffer, receivingBuffer.length);

			MulticastSocket socket = new MulticastSocket(65000);
			socket.joinGroup(group);

			while (!exit) {
				socket.receive(receivedPacket);
				String received = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
				if (received.startsWith("DISCOVER")) {
					socket.send(responsePacket);
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