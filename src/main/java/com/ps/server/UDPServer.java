package com.ps.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static com.ps.Server.*;

public class UDPServer implements Runnable {
    long startTime, endTime, totalData = 0, lostData = 0;
    double totalTime, transmissionSpeed, errorPercentage = 0;
    int singleDataSize = 60000;
    private final DatagramSocket serverSocket;
    Statistics statistics = Statistics.getInstance();

    public UDPServer() throws SocketException {
        System.out.println("UDP Server started");
        serverSocket = new DatagramSocket(PORT);
        resetData();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[singleDataSize];
        while (!exit) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, singleDataSize);
                serverSocket.receive(packet);
                String[] msg = (new String(packet.getData(), packet.getOffset(), packet.getLength())).split(":");
                if (msg[0].equalsIgnoreCase("SIZE")) {
                    singleDataSize = Integer.parseInt(msg[1].trim());
                    resetData();
                }
                totalData += packet.getLength();
                endTime = System.currentTimeMillis();
                totalTime = (endTime - startTime) / 1000.0;
                transmissionSpeed = totalData / totalTime;
                errorPercentage = (double) (lostData / totalData) * 100;
                statistics.setUDPStatistics(singleDataSize, totalData, totalTime, transmissionSpeed, lostData, errorPercentage);
                if (msg[0].equalsIgnoreCase("FINE")) {
                    statistics.setUDPStatistics(singleDataSize, totalData - 11, totalTime, transmissionSpeed, lostData, errorPercentage);
                    System.out.println(statistics.getUDP());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverSocket.close();
        udpSemaphore.release();
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

