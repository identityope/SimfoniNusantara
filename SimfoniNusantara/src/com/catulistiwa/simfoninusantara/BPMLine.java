package com.catulistiwa.simfoninusantara;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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
	
	public BPMLine(int _x, int _y, Bitmap b) {		
		bitmap = b;
		x = _x;
		y = _y;
		currentFrame = 0;
		frameNr = 8;
		spriteWidth = bitmap.getWidth() / frameNr;
		spriteHeight = bitmap.getHeight();
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
		destRect = new Rect(x-spriteWidth/2, y-spriteHeight/2, x+spriteWidth/2, y+spriteHeight/2);
	}
	public void draw(Canvas canvas) {
		this.destRect.set(getX()-spriteWidth/2,getY()-spriteHeight/2,getX()+spriteWidth/2,getY()+spriteHeight/2);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
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
