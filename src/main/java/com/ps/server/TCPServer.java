package com.ps.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static com.ps.Server.*;

public class TCPServer implements Runnable {
    private final ServerSocket serverSocket;
    private long startTime;
    private long totalData;
    private long lostData;
    private double totalTime;
    private double transmissionSpeed;
    private double errorPercentage;
    private int singleDataSize = 60000;
    InputStream in;
    OutputStream out;

    public TCPServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        resetData();
        System.out.println("TCP Server started");
    }

    @Override
    public void run() {
        try {
            while (!exit) {
                Socket clientSocket = serverSocket.accept();
                this.in = new DataInputStream(clientSocket.getInputStream());
                this.out = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("TCP: New client connected" + clientSocket.getInetAddress());
                resetData();
                byte[] buffer = new byte[singleDataSize];
                int bytesRead;
                while (!clientSocket.isClosed() && !exit) {
                    try {
                        bytesRead = in.read(buffer);
                        if (bytesRead <= 0) {
                            clientSocket.close();
                            break;
                        }
                        totalData += bytesRead;
                        singleDataSize = bytesRead;
                    } catch (SocketException e) {
                        System.out.println("Client closed");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                totalData -= 7;
                long endTime = System.currentTimeMillis();
                totalTime = (endTime - startTime) / 1000.0;
                transmissionSpeed = totalData / totalTime;
                errorPercentage = (double) lostData / totalData * 100;
                Statistics statistics = Statistics.getInstance();
                statistics.setTCPStatistics(singleDataSize, totalData, totalTime, transmissionSpeed, lostData, errorPercentage);
                System.out.println(statistics.getTCP());
                singleDataSize = 60000;
                if (exit) {
                    clientSocket.close();
                    serverSocket.close();
                    tcpSemaphore.release();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetData() {
        startTime = System.currentTimeMillis();
        totalData = 0;
        lostData = 0;
        totalTime = 0;
        transmissionSpeed = 0;
        errorPercentage = 0;
    }
}

