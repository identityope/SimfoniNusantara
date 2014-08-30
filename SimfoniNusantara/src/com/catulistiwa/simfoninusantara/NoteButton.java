package com.catulistiwa.simfoninusantara;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class NoteButton {
	private int x;
	private int y;
	private Bitmap bitmap;	
	
	public NoteButton(int _x, int _y, Bitmap button) {		
		bitmap = button;
		x = _x;
		y = _y;
	}
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x-bitmap.getWidth()/2, y-bitmap.getHeight()/2, null);
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
