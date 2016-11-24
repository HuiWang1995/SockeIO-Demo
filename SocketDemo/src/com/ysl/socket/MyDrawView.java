package com.ysl.socket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;

public class MyDrawView extends View
{

	public MyDrawView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//��β��ǻ���
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Log.i("ysl", "onDraw����------");
		 /*
         * ���� ˵�� drawRect���ƾ���   drawCircle����Բ��  drawOval������Բ   drawPath������������
         * drawLine����ֱ��   drawPoin���Ƶ�
         */
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		
		canvas.drawText("��Բ��",10,20, paint);
		canvas.drawCircle(60, 20, 10, paint);
		paint.setAntiAlias(true);
		canvas.drawCircle(120, 20, 20, paint);
		
		canvas.drawText("���߼����ߣ�", 10, 60, paint);
		paint.setColor(Color.GREEN);
		canvas.drawLine(60, 40, 100, 40, paint); //ֱ��
		canvas.drawLine(110, 40, 190, 80, paint);
		//��Ц������
		paint.setStyle(Paint.Style.STROKE); //����
		RectF ovall = new RectF(150, 20, 180, 40);
		canvas.drawArc(ovall, 180, 180, false, paint);
		ovall.set(190, 20, 220, 40);
		canvas.drawArc(ovall, 180, 180, false, paint);
		ovall.set(160,30,210,60);
		canvas.drawArc(ovall, 0,180, true, paint);
		//�������Ρ�����
		paint.setColor(Color.BLUE);
		canvas.drawText("�����Σ�", 10, 80, paint);
		paint.setColor(Color.GRAY);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(60, 60, 80, 80, paint); //�� �� �� ��   ---- �������20  �������20 = ������
		canvas.drawRect(60, 90, 160, 120, paint); //�������100  �������30
		//�����κ���Բ
		canvas.drawText("�����κ���Բ", 10, 140, paint);
		/* ���ý���ɫ */
		Shader mShader = new LinearGradient(0, 0, 100, 100, new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                Color.LTGRAY }, null, Shader.TileMode.REPEAT);
		paint.setShader(mShader);
		
		RectF oval2 = new RectF(60, 100, 200, 240);
		canvas.drawArc(oval2, 200, 130, true, paint);
		
		//����Բ
		oval2.set(210,100,300,130);
		canvas.drawOval(oval2, paint);
		
		//��������
		canvas.drawText("��������", 10, 180, paint);
		Path path = new Path();
		path.moveTo(80, 200);// �˵�Ϊ����ε����
        path.lineTo(120, 250);
        path.lineTo(80, 250);
		path.close();
		canvas.drawPath(path, paint);
		
		paint.reset();
		paint.setColor(Color.LTGRAY);
		paint.setStyle(Paint.Style.STROKE);
		Path path2 = new Path();
		path2.moveTo(180, 200);
		path2.lineTo(200, 200);
		path2.lineTo(210, 210);
		path2.lineTo(200, 220);
        path2.lineTo(180, 220);
        path2.lineTo(170, 210);
        path2.close();//���
        canvas.drawPath(path2,paint);
		
	    /*
         * Path���װ����(����������ͼ�ε�·��
         * ��ֱ�߶�*����������,�����η����ߣ�Ҳ�ɻ����ͻ���drawPath(·��������),Ҫô�����Ļ���
         * (��������ķ��),���߿������ڼ��ϻ򻭻����ı���·����
         */
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.YELLOW);
		paint.setAntiAlias(true);
		canvas.drawText("��Բ�Ǿ���", 10, 250, paint);
		RectF oval3 = new RectF(80,260,300,300);
		canvas.drawRoundRect(oval3, 20, 15, paint);
		
		canvas.drawText("������������:", 10, 400, paint);
		paint.reset();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.GREEN);
		Path path3 = new Path();
		path3.moveTo(200, 320);
		path3.quadTo(250,310,270,400); //���ñ��������ߵĿ��Ƶ�������յ�����
		canvas.drawPath(path3, paint);
		
		//����
		canvas.drawText("����:",10, 450, paint);
		canvas.drawPoint(40, 450, paint);
		 canvas.drawPoints(new float[]{45,450,55,450,70,400}, paint);//�������
	
		 //��ͼƬ ������ͼ
		 Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		 canvas.drawBitmap(bitmap, 200,450, paint); //������� ���붥��
	}
	
	//�������м���
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.i("ysl", "onMeasure����------");
	}
}
