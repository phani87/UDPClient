package com.udp.project;

import java.util.*;
import java.nio.ByteBuffer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Logger;
import java.lang.*;

public class Client {

	public byte intCheck;
	Client client;
	public String temp = "";
	public DatagramSocket clientSocket;
	int i;

	// byte[][] x = new byte[20][2];
	// ByteBuffer x = ByteBuffer.allocate(100);

	// byte[] bytes = ByteBuffer.allocate(8).putString()

	public String createFileName() throws IOException {
		client = new Client();
		String protocol = "ENTS/1.0 Request\r\n";
		String fileName;
		System.out
				.println("Please Enter the filename to be requested from Server");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		Integer option = Integer.parseInt(reader.readLine());
		if (option == 1) {
			fileName = "directors_message.txt\r\n";
		} else if (option == 2) {
			fileName = "program_overview.txt\r\n";
		} else {
			fileName = "scholarly_paper.txt\r\n";
		}
		
		String integrityCheck = client.integrityCheck(protocol + fileName)+"\r\n";
		String finName = protocol + fileName + "\r\n" + integrityCheck;
		System.out.println(finName);
		return finName;
	}

	public void client() throws IOException {
		int timeout = 1000;
		client = new Client();
		String recieveMessage;
		client.sendDatagram();
		for (int i = 0; i < 4; i++) {
			client.recieveDatagram(timeout, 0);
		}
	}

	public void sendDatagram() throws IOException {
		clientSocket = new DatagramSocket();
		client = new Client();
		byte[] sendData = new byte[1024];
		InetAddress IPAddress = InetAddress.getByName("localhost");
		String sentence = client.createFileName();
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);

	}

	public void recieveDatagram(int timeout, int attempts) throws IOException {
		clientSocket.setSoTimeout(timeout);
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		while (i < 4 && true) {
			try {
				clientSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData());
			} catch (SocketTimeoutException se) {
				System.err.println("Session Timeout -- Attempt" + (i + 1));
				recieveDatagram(timeout * 2, i++);
			}

		}

	}

	public static void main(String[] args) throws IOException {
		Client client = new Client();
		String msg = client.createFileName();
	}

/*	public String integrityCheck(String msg) {
		String S = msg;
		int x[] = new int[S.length()];
		int p = 0;
		Integer s = 0;
		int index = 0;
		int c = 7919;
		int d = 65536;
		int length = 0;
		if (S.length() % 2 != 0) {
			length = S.length() / 2 + 1;
		} else {
			length = S.length() / 2;
		}
		for (int i = 0; i < length; i++) {
			if (p == (S.length() - 1)) {
				x[i] = (int) (S.charAt(p) << 8 | 0);
				break;
			} else {
				x[i] = (int) (S.charAt(p) << 8 | S.charAt(p + 1));
				p += 2;
			}
		}
		for (int j = 0; j < length; j++) {
			index = s ^ x[j];
			s = ((c * index) % d);
		}
		String fu = s.toString();
		System.out.println("fu----->"+fu);
		return fu;
	}
	*/
	
	public String integrityCheck(String name){
		char [] charSeq = name.toCharArray();
		List<String> list = new ArrayList<String>();
		String[]binaryArray=new String[charSeq.length+1];
		for (int i=0;i<charSeq.length;i=i+2){
			binaryArray[i] = append(Integer.toBinaryString((int)charSeq[i]));

			if(i+1 >= charSeq.length){
				binaryArray[i+1]="00000000";
			}
			else{
				binaryArray[i+1]= append((Integer.toBinaryString((int)charSeq[i+1])));
			}
			list.add(binaryArray[i]+binaryArray[i+1]);

		}
		String[]finArray= new String[list.size()];
		list.toArray(finArray);
		long s=0;
		for (int i=0;i<finArray.length;i++)
		{
			String str = "";
			for (int j=0;j<finArray[i].length();j++)
			{
				long index=s^(Integer.parseInt(finArray[i].charAt(j)+""));
				System.out.println("index " + index);
				str+=(7919*index)%65536;
				System.out.println(""+str+"\n");
			}
			System.out.println("chksum: "+str+"\n");
			System.out.println(""+str.length());


			char[] chksumArray= str.toCharArray();
			for (int k =0;k<chksumArray.length;k++){
				System.out.println(chksumArray[k]);
			}
			System.out.println("len :"+chksumArray.length);
		}
		return temp;

	}
	
	private static String append(String binaryStr) {
		int padNum= 8 - binaryStr.length();
		String appendStr= "";
		for(int j =0; j<padNum ;j++){
			appendStr+="0";
		}
		return appendStr+binaryStr;
	}

}
