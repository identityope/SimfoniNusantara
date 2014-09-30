package com.catulistiwa.simfoninusantara;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class NoteButton {
	private static final int BMP_ROWS = 1;		// Number of Rows in spritesheet
	private static final int BMP_COLUMNS = 2;	// Number of Columns in spritesheet
	private int x;
	private int y;
	private int currentFrame = 0;
	private Bitmap bitmap;	
	private int width;
	private int height;
	
	public NoteButton(int _x, int _y, Bitmap button) {		
		bitmap = button;
		x = _x;
		y = _y;
		width = bitmap.getWidth() / BMP_COLUMNS;
		height = bitmap.getHeight() / BMP_ROWS;
	}
	public void draw(Canvas canvas) {
		int srcX = currentFrame * width;
		Rect src = new Rect(srcX, 0, srcX + width, height);
		Rect dst = new Rect(x-width/2, y-height/2, x + width/2, y + height/2);
		canvas.drawBitmap(bitmap, src, dst, null);
	}
	public void buttonDown() {
		currentFrame = 1;
	}
	public void buttonUp(){
		currentFrame = 0;
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
