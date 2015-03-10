package com.catulistiwa.simfoninusantara;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

public class Note {
	private int x;
	private int y;
	private Bitmap noteimg;	
	private int noteType;
	private double timePosition;
	private boolean visible;
	private int screenWidth = 1280;
	private int screenHeight = 752;
	
	public Note(int _x, int _y, int type, double time, Bitmap note) {		
		noteimg = note;
		x = _x;
		y = _y;
		noteType = type;
		timePosition = time;
		visible = false;
	}
	public Note(int type, double time, Bitmap note, int sw, int sh) {
		noteimg = note;
		noteType = type;
		timePosition = time;
		visible = false;
		screenWidth = sw;
		screenHeight = sh;
		x = screenWidth/2;
		int rangey1 = screenHeight/2-note.getHeight(),
				rangey2 = screenHeight/2,
				rangey3 = screenHeight/2+note.getHeight();
		switch (noteType) {
		case 0:
			y = rangey1;
			break;
		case 1:
			y = rangey2;
			break;
		case 2:
			y = rangey3;
			break;
		case 3:
			y = rangey1;
			break;
		case 4:
			y = rangey2;
			break;
		case 5:
			y = rangey3;
			break;

		default:
			break;
		}
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
	public int getType(){
		return noteType;
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
		if(noteType==0 || noteType==1 || noteType==2){
			x -= speed;
		}else if (noteType==3 || noteType==4 || noteType==5){
			x += speed;
		}
	}
	public void moveX(double pos){
		x = (int) pos;
	}
	public void setnoteimg(Bitmap nb){
		noteimg = nb;
	}
}
