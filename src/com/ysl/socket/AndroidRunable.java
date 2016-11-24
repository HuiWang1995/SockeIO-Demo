package com.ysl.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * ����socket�õ��߳�
 * @author ��ѧϰdi������
 * 2016��11��18�� ����2:51:56
 */
public class AndroidRunable implements Runnable
{

	Socket socket = null;
	String str = "ok";

	public AndroidRunable(Socket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run()
	{
		// ��android�ͻ������hello worild
		String line = null;
		InputStream input;
		OutputStream output;
		try
		{
			// ��ͻ��˷�����Ϣ
			output = socket.getOutputStream();
			input = socket.getInputStream();
			byte[] buffer = new byte[1024 * 4];
			int length = 0;
//			output.write(str.getBytes("UTF-8"));
//			output.flush();
			// ��ر�socket
//			socket.shutdownOutput();
			//��ȡ�ͻ��˷�������Ϣ
			while (!socket.isClosed() && ((length = input.read(buffer)) != -1))
			{
				String message = new String(Arrays.copyOf(buffer, length)).trim();
				System.out.print("���������յ�����Ϣ��\n" + message + "\n");
				if ("xt".equals(message))  //�յ�����
				{
					output.write("�յ�����".getBytes("UTF-8"));
					output.flush();
				}
			}
			// �ر����������
			output.close();
			input.close();
			socket.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	/**
	 * input-->string
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream in) throws IOException
	{
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		int n;
		while ((n = in.read(b)) != -1)
		{
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
}