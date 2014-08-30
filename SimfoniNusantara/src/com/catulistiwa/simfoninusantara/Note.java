package com.catulistiwa.simfoninusantara;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Note {
	private int x;
	private int y;
	private Bitmap noteimg;	
	private int noteType;
	private double timePosition;
	private boolean visible;
	
	public Note(int _x, int _y, int type, double time, Bitmap note) {		
		noteimg = note;
		x = _x;
		y = _y;
		noteType = type;
		timePosition = time;
		visible = false;
	}
	public void draw(Canvas canvas) {
		canvas.drawBitmap(noteimg, x-noteimg.getWidth()/2, y-noteimg.getHeight()/2, null);
	}
	public RectF getRectangle() {
		return new RectF(x - noteimg.getWidth()/2, y - noteimg.getHeight() / 2, x + noteimg.getWidth()/2, y + noteimg.getHeight() / 2);
	}
	public int getX(){
			return x;
	}
	public int getY(){
		return y;
	}
	public double getTimePos(){
		return timePosition;
	}
	public boolean isVisible(){
		return visible;
	}
	public void show(){
		visible = true;
	}
	public void hide(){
		visible = false;
	}
	public void move(int speed){
		if(noteType==0){
			x -= speed;
		}else{
			x += speed;
		}
	}
	public void setnoteimg(Bitmap nb){
		noteimg = nb;
	}
}
