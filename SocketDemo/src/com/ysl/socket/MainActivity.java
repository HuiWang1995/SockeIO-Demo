package com.ysl.socket;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import com.ysl.socket.broad.SockeBroadcastReceiver;
import com.ysl.socket.interfaces.SocketCallback;

import android.app.Activity;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	MyDrawView myView;
	
	public static String str = "first message...";
	private SockeBroadcastReceiver mHeartBeatBroadCast;
	private TextView txtMsg;  
	private Button send;  
	private EditText etMsg;  
	private String content;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		init();
		socketTest();
		
		mHeartBeatBroadCast = new SockeBroadcastReceiver(this);
		setMessages("�������ӵ�������(" + mHeartBeatBroadCast.HOST + ":" + mHeartBeatBroadCast.PORT  +  ")...");
		mHeartBeatBroadCast.setSocketCallback(new SocketCallback()
		{
			@Override
			public void connected()
			{
				Log.i("ysl", "������������...");
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						setMessages("�����ӵ�������...");
					}
				});
			}	
			@Override
			public void disConnected(){
				Log.i("ysl", "�������Ͽ���...");
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						setMessages("�������Ͽ����Զ�������...");
					}
				});
				mHeartBeatBroadCast.initSocket();
			}
			@Override
			public void receiveMessage(String message)
			{
				Log.i("ysl", "�յ�����Ϣ:" + message);
				setMessages("JAVA�����: " + message);
			}
		});
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(SockeBroadcastReceiver.HEART_BEAT_ACTION);
		mIntentFilter.addAction(SockeBroadcastReceiver.MESSAGE_ACTION);
		registerReceiver(mHeartBeatBroadCast, mIntentFilter);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(mHeartBeatBroadCast);
	}

	private void init()
	{
		LinearLayout layout = (LinearLayout)findViewById(R.id.root);
		myView = new MyDrawView(this);
		myView.setMinimumHeight(500);
		myView.setMinimumWidth(300);
		myView.invalidate();
		layout.addView(myView);
	}
	
	//***socke���� start*****************
	private void socketTest(){
		setContentView(R.layout.activity_socket);  
        txtMsg = (TextView) findViewById(R.id.txt1);  
        txtMsg.setMovementMethod(ScrollingMovementMethod.getInstance());  //������
        
        send = (Button) findViewById(R.id.send);  
        etMsg = (EditText) findViewById(R.id.ed1);  
        send.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                content = etMsg.getText().toString();
                if (TextUtils.isEmpty(content)){
        			Toast.makeText(MainActivity.this,"��Ϣ���ݲ���Ϊ��", Toast.LENGTH_SHORT);
        			return;
        		}
                boolean sendState = mHeartBeatBroadCast.sendMsg(content);
                if (sendState){
                	Toast.makeText(getApplicationContext(), "���ͳɹ���", Toast.LENGTH_SHORT).show();
                	setMessages("APP�ͻ���: " + content);
                	etMsg.setText("");
				}else{
					Toast.makeText(getApplicationContext(), "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				}
            }  
        });  
	}
	
	/**
	 * ����TextView�ı�
	 * @param message
	 */
	public void setMessages(String message){
		txtMsg.append(message + "\n");
//		txtMsg.setText(txtMsg.getText().toString() + "\n" + message);
		int offset = txtMsg.getLineCount() * txtMsg.getLineHeight();
		if(offset > txtMsg.getHeight()){
			txtMsg.scrollTo(0,offset-txtMsg.getHeight());
		}
	}
	
	
	
	
	
	

	

}
