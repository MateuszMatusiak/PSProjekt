package com.ps.server;

import java.util.Arrays;

public class Statistics {
	private long tcpSingleDataSize, udpSingleDataSize;
	private long tcpTotalData, udpTotalData;
	private double tcpTotalTime, udpTotalTime;
	private double tcpTransmissionSpeed, udpTransmissionSpeed;
	private long tcpLostData, udpLostData;
	private double tcpErrorPercentage, udpErrorPercentage;
	private static volatile Statistics instance = null;

	public static Statistics getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (Statistics.class) {
			if (instance == null) {
				instance = new Statistics();
			}
			return instance;
		}
	}

	private Statistics() {
		this.tcpSingleDataSize = 0;
		this.udpSingleDataSize = 0;
		this.tcpTotalData = 0;
		this.udpTotalData = 0;
		this.tcpTotalTime = 0;
		this.udpTotalTime = 0;
		this.tcpTransmissionSpeed = 0;
		this.udpTransmissionSpeed = 0;
		this.tcpLostData = 0;
		this.udpLostData = 0;
		this.tcpErrorPercentage = 0;
		this.udpErrorPercentage = 0;
	}

	public void setTCPStatistics(long singleDataSize, long totalData, double totalTime, double transmissionSpeed, long lostData, double errorPercentage) {
		this.tcpSingleDataSize = singleDataSize;
		this.tcpTotalData = totalData;
		this.tcpTotalTime = totalTime;
		this.tcpTransmissionSpeed = transmissionSpeed;
		this.tcpLostData = lostData;
		this.tcpErrorPercentage = errorPercentage;
	}

	public void setUDPStatistics(long singleDataSize, long totalData, double totalTime, double transmissionSpeed, long lostData, double errorPercentage) {
		this.udpSingleDataSize = singleDataSize;
		this.udpTotalData = totalData;
		this.udpTotalTime = totalTime;
		this.udpTransmissionSpeed = transmissionSpeed;
		this.udpLostData = lostData;
		this.udpErrorPercentage = errorPercentage;
	}

	@Override
	public String toString() {
		String[] vars = {
				"TCP = " + tcpSingleDataSize + " bytes",
				"TCP = " + tcpTotalData + " kB",
				"TCP = " + tcpTotalTime + " s",
				"TCP = " + tcpTransmissionSpeed + " kB/s",
				"TCP = " + tcpLostData + " bytes",
				"TCP = " + tcpErrorPercentage + " %",
		};

		int maxLength = Arrays.stream(vars).mapToInt(String::length).max().getAsInt();

		return "Single data size: " + " ".repeat(33 - ("Single data size: ").length()) + "TCP = " + tcpSingleDataSize + " bytes" + " ".repeat(maxLength - (String.valueOf(tcpSingleDataSize).length() + 6)) + "UDP = " + udpSingleDataSize + " bytes\n" +
				"Total size of transferred data: " + " ".repeat(33 - ("Total size of transferred data: ").length()) + "TCP = " + tcpTotalData + " bytes" + " ".repeat(maxLength - (String.valueOf(tcpTotalData).length() + 6)) + "UDP = " + udpTotalData + " bytes\n" +
				"Total transmission time: " + " ".repeat(33 - ("Total transmission time: ").length()) + "TCP = " + tcpTotalTime + " s" + " ".repeat(maxLength - (String.valueOf(tcpTotalTime).length() + 2)) + "UDP = " + udpTotalTime + " s\n" +
				"Transmission speed: " + " ".repeat(33 - ("Transmission speed: ").length()) + "TCP = " + tcpTransmissionSpeed + " bytes/s" + " ".repeat(maxLength - (String.valueOf(tcpTransmissionSpeed).length() + 8)) + "UDP = " + udpTransmissionSpeed + " bytes/s\n" +
				"Lost data: " + " ".repeat(33 - ("Lost data: ").length()) + "TCP = " + tcpLostData + " bytes" + " ".repeat(maxLength - (String.valueOf(tcpLostData).length() + 6)) + "UDP = " + udpLostData + " bytes\n" +
				"Transmission error: " + " ".repeat(33 - ("Transmission error: ").length()) + "TCP = " + tcpErrorPercentage + " %" + " ".repeat(maxLength - (String.valueOf(tcpErrorPercentage).length() + 2)) + "UDP = " + udpErrorPercentage + " %\n";
	}

	public String getTCP(){
		return "Single data size: " + " ".repeat(33 - ("Single data size: ").length()) + "TCP = " + tcpSingleDataSize + " bytes\n" +
				"Total size of transferred data: " + " ".repeat(33 - ("Total size of transferred data: ").length()) + "TCP = " + tcpTotalData + " bytes\n" +
				"Total transmission time: " + " ".repeat(33 - ("Total transmission time: ").length()) + "TCP = " + tcpTotalTime + " s\n" +
				"Transmission speed: " + " ".repeat(33 - ("Transmission speed: ").length()) + "TCP = " + tcpTransmissionSpeed + " bytes/s\n" +
				"Lost data: " + " ".repeat(33 - ("Lost data: ").length()) + "TCP = " + tcpLostData + " bytes\n" +
				"Transmission error: " + " ".repeat(33 - ("Transmission error: ").length()) + "TCP = " + tcpErrorPercentage + " %\n";
	}

	public String getUDP(){
		return "Single data size: " + " ".repeat(33 - ("Single data size: ").length()) + "UDP = " + udpSingleDataSize + " bytes\n" +
				"Total size of transferred data: " + " ".repeat(33 - ("Total size of transferred data: ").length()) + "UDP = " + udpTotalData + " bytes\n" +
				"Total transmission time: " + " ".repeat(33 - ("Total transmission time: ").length()) + "UDP = " + udpTotalTime + " s\n" +
				"Transmission speed: " + " ".repeat(33 - ("Transmission speed: ").length()) + "UDP = " + udpTransmissionSpeed + " bytes/s\n" +
				"Lost data: " + " ".repeat(33 - ("Lost data: ").length()) + "UDP = " + udpLostData + " bytes\n" +
				"Transmission error: " + " ".repeat(33 - ("Transmission error: ").length()) + "UDP = " + udpErrorPercentage + " %\n";
	}
}
