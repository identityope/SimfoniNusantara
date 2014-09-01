package com.catulistiwa.simfoninusantara;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class BPMLine {
	private int x;
	private int y;
	private Bitmap bitmap;	
	private Rect sourceRect,destRect;    // the rectangle to be drawn from the animation bitmap
	private int frameNr;        // number of frames in animation
	private int currentFrame;   // the current frame
	private int spriteWidth;    // the width of the sprite to calculate the cut out rectangle
	private int spriteHeight;   // the height of the sprite
	//untuk versi sederhana
	private int linewidth,alpha,bpm,frame_counter,max_counter,prevTime;
	private Paint paint;
	public BPMLine(int _x, int _y, int _bpm, Bitmap b) {		
		bitmap = b;
		x = _x;
		y = _y;
		currentFrame = 0;
		frameNr = 8;
		spriteWidth = bitmap.getWidth() / frameNr;
		spriteHeight = bitmap.getHeight();
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
		destRect = new Rect(x-spriteWidth/2, y-spriteHeight/2, x+spriteWidth/2, y+spriteHeight/2);
		linewidth = 0;
		alpha = 255;
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAlpha(alpha);
		bpm = _bpm;
		max_counter = 60000/bpm;
		frame_counter = 0;
		prevTime = 0;
	}
	public void draw(Canvas canvas) {
		this.destRect.set(getX()-spriteWidth/2,getY()-spriteHeight/2,getX()+spriteWidth/2,getY()+spriteHeight/2);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}
	public void drawSimple(Canvas canvas){
		canvas.drawRect(x-linewidth/2, y, x+linewidth/2, y+350, paint);
	}
	public void update(int gameTime){
		linewidth++;
		alpha-=10;
		if(alpha<=0){
			alpha=0;
		}
		paint.setAlpha(alpha);
		frame_counter = gameTime/max_counter;
		if(frame_counter>prevTime){
			resetLineWidth();
			prevTime=frame_counter;
		}
	}
	public int getCurrentFrame(){
		return frame_counter;
	}
	public void resetLineWidth(){
		linewidth = 0;
		alpha = 255;
		//frame_counter = 0;
	}
	public void nextbeat() {
		currentFrame++;
		if (currentFrame >= frameNr) {
		  	currentFrame = 0;
		}
		// define the rectangle to cut out sprite
		this.sourceRect.left = currentFrame * spriteWidth;
		this.sourceRect.right = this.sourceRect.left + spriteWidth;
	}
	public RectF getRectangle() {
		return new RectF(x - bitmap.getWidth()/2, y - bitmap.getHeight() / 2, x + bitmap.getWidth()/2, y + bitmap.getHeight() / 2);
	}
	public int getX(){
			return x;
	}
	public int getY(){
		return y;
	}
	public void setBitmap(Bitmap nb){
		bitmap = nb;
	}
}
