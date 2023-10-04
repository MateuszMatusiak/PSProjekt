package com.ps.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import static com.ps.Client.*;
import static java.lang.Thread.sleep;

public class TCPClient implements Runnable {
	private final byte[] data;
	private Socket socket;
	private DataOutputStream out;

	public TCPClient() {
		data = new byte[bufferSize];
		Arrays.fill(data, (byte) 1);
	}

	@Override
	public void run() {
		while (!exit) {
			try {
				tcpSemaphore.acquire();
				if (exit) break;
				socket = new Socket(HOST, PORT);
				socket.setTcpNoDelay(!useNagle);
				out = new DataOutputStream(socket.getOutputStream());
				byte[] buffer = ("SIZE:" + bufferSize).getBytes();
				out.write(buffer, 0, buffer.length);
				while (!stop) {
					out.write(data, 0, data.length);
					sleep(10L);
				}
				socket.close();
			} catch (SocketException e) {
				System.out.println("TCP Server stopped");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
