package com.ysl.socket.broad;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ysl.socket.MainActivity;
import com.ysl.socket.interfaces.SocketCallback;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Socket�����Ӻ�����
 * 1.��ʱ���������������(ʱ������Զ���)
 * 2.�����Զ�����
 * 3.���ӳɹ����Ͽ����ӡ��յ���Ϣ�ص�����
 * 4.��Ϣ����״̬��ȡ(�ɹ�true or ʧ��false)
 * 5.ע��㲥
 * @author ��ѧϰdi������
 *  2016��11��22�� ����4:23:17
 */
public class SockeBroadcastReceiver extends BroadcastReceiver
{
	private static final String TAG = "ysl";
	/** �������ʱ�� */
	private static final long HEART_BEAT_RATE = 10 * 1000;
	/** ����IP��ַ */
	public static final String HOST = "192.168.1.109";
	/** �˿ں� */
	public static final int PORT = 30003;
	/** ��ʱ���� **/
	public static final int SOCKET_TIME_OUT = 10 * 1000;
	/** ��Ϣ�㲥 */
	public static final String MESSAGE_ACTION = "com.ysl.message_ACTION";
	/** �����㲥 */
	public static final String HEART_BEAT_ACTION = "com.ysl.heart_beat_ACTION";
	/** �̳߳� **/
	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	/** Ϊ�˽�ʡ��������������ʱ��������������ʱ���򲻷����� */
	private long sendTime = 0L;

	private ReadThread mReadThread;
	private MainActivity mMainActivity;
	private Socket socket;
	private boolean isConnected = true; //�Ƿ�������״̬
	private SocketCallback callback; // �ص�

	/**
	 * ��ʼ��
	 * @param mMainActivity
	 */
	public SockeBroadcastReceiver(MainActivity mMainActivity)
	{
		this.mMainActivity = mMainActivity;
		initSocket();
	}

	/**
	 * ����״̬�ص�
	 * @param callback
	 */
	public void setSocketCallback(SocketCallback callback){
		this.callback = callback;
	}

	/**
	 * ��ʼ��Socake
	 */
	public void initSocket()
	{
		executorService.execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					socket = new Socket();
					SocketAddress socAddress = new InetSocketAddress(HOST,PORT);
					socket.connect(socAddress, SOCKET_TIME_OUT);
					if (callback != null)  //���ӳɹ�
					{  
						isConnected = true;
						callback.connected();
					}
					mReadThread = new ReadThread();
					mReadThread.start();
					mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);// ��ʼ���ɹ��󣬾�׼������������
				} catch (UnknownHostException e)
				{
					if (callback != null)  //����ʧ��
					{  
						isConnected = false;
						callback.disConnected();
					}
					e.printStackTrace();
				} catch (IOException e)
				{
					if (callback != null)  //����ʧ��
					{  
						isConnected = false;
						callback.disConnected();
					}
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		if (action.equals(MESSAGE_ACTION)){ // ��Ϣ�㲥
			String stringExtra = intent.getStringExtra("message");
//			Log.i(TAG, "�յ���������Ϣ��" + stringExtra);
			if (callback != null)
			{
				callback.receiveMessage(stringExtra);
			}
		} else if (action.equals(HEART_BEAT_ACTION)){// �����㲥
			Log.i("ysl", "�յ�����������������");
		}

	}

	/**
	 * ����������
	 */
	private Handler mHandler = new Handler();
	private Runnable heartBeatRunnable=new Runnable(){
		@Override 
		public void run(){
			if(System.currentTimeMillis()-sendTime>=HEART_BEAT_RATE){
				boolean isSuccess=sendMsg("xt");// �����������������������ݡ�����
				if(!isSuccess){ // ������Ͳ��ɹ�����
					mHandler.removeCallbacks(heartBeatRunnable);
					mReadThread.release();
					releaseLastSocket(socket);
					if (callback != null)
					{
						callback.disConnected();
					}
				}
			}
				mHandler.postDelayed(this,HEART_BEAT_RATE);
			}
		};

	/**
	 * ������Ϣ
	 * @param msg
	 * @return
	 */
	public boolean sendMsg(String msg)
	{
		if (null == socket){
			return false;
		}
		try
		{
			if (!socket.isClosed() && !socket.isOutputShutdown() && isConnected)
			{
				OutputStream os = socket.getOutputStream();
				String message = msg + "\r\n";
				os.write(message.getBytes());
				os.flush();
				sendTime = System.currentTimeMillis();// ÿ�η��ͳɹ����ݣ��͸�һ�����ɹ����͵�ʱ�䣬��ʡ�������ʱ��
				Log.i(TAG, "���ͳɹ���ʱ�䣺" + sendTime);
			} else
			{
				return false;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * �ͷ�socket
	 * @param mSocket
	 */
	private void releaseLastSocket(Socket mSocket)
	{
		try
		{
			if (null != mSocket)
			{
				if (!mSocket.isClosed())
				{
					mSocket.close();
				}
				mSocket = null;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ������Ϣ
	 */
	public class ReadThread extends Thread
	{
		private boolean isStart = true;
		public void release()
		{
			isStart = false;
			releaseLastSocket(socket);
		}

		@SuppressLint("NewApi")
		@Override
		public void run()
		{
			if (null != socket && isConnected)
			{
				try
				{
					InputStream is = socket.getInputStream();
					byte[] buffer = new byte[1024 * 4];
					int length = 0;
					while (!socket.isClosed() && !socket.isInputShutdown() && isStart && ((length = is.read(buffer)) != -1))
					{
						isConnected = true;
						if (length > 0)
						{
							String message = new String(Arrays.copyOf(buffer, length)).trim();
							// �յ���������������Ϣ����ͨ��Broadcast���ͳ�ȥ
							if (message.equals("ok"))
							{// ���������ظ�
								Intent intent = new Intent(HEART_BEAT_ACTION);
								mMainActivity.sendBroadcast(intent);
							} else
							{ // ������Ϣ�ظ�
								Intent intent = new Intent(MESSAGE_ACTION);
								intent.putExtra("message", message);
								mMainActivity.sendBroadcast(intent);
							}
						}
					}
					isConnected = false;
					if (callback != null)
					{
						callback.disConnected();
					}
				} catch (IOException e)
				{
					isConnected = false;
					if (callback != null)
					{
						callback.disConnected();
					}
					e.printStackTrace();
					Log.i("ysl", "�Ѿ��Ͽ�IOException...");
				}
			}
		}
	}
}
