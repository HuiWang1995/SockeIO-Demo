package com.ysl.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ģ�������  ��ֱ������
 * @author ��ѧϰdi������
 * 2016��11��24�� ����3:34:47
 */
public class AndroidService
{
	
	public static void main(String[] args) throws IOException
	{
		ServerSocket serivce = new ServerSocket(30003);  
		System.out.println("������������...");
        while (true) {  
            //�ȴ��ͻ�������  
            Socket socket = serivce.accept();  
            new Thread(new AndroidRunable(socket)).start();  
        } 
	}
}
