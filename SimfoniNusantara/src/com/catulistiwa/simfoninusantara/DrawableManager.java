package com.catulistiwa.simfoninusantara;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;


public class DrawableManager{
	/**
	 * construct pake aplication context
	 * @param context
	 */
	private Resources res;
	private static DrawableManager instance;
	//Gambar Map
	private Bitmap background;
	private Bitmap tombol;
	private Bitmap note1;
	private DrawableManager(Context context) {
		// TODO ganti gambar
		res = context.getResources();
	}
	/**
	 * must be called only once
	 * @param context
	 */
	public static void initInstance(Context context) {
		assert instance == null;
		instance = new DrawableManager(context);
	}
	/**
	 * must have called initInstance() before
	 * @return
	 */
	public static DrawableManager getInstance() {
		assert instance != null;
		return instance;
	}
	public Bitmap getBackgroundImage() {
		if (background == null) {
			background = BitmapFactory.decodeResource(res, R.drawable.bgimage);
		}
		return background;
	}
	public Bitmap getTombolImage() {
		if (tombol == null) {
			tombol = BitmapFactory.decodeResource(res, R.drawable.tombol);
		}
		return tombol;
	}
	public Bitmap getNote1Image() {
		if (note1 == null) {
			note1 = BitmapFactory.decodeResource(res, R.drawable.note);
		}
		return note1;
	}
	public static Bitmap ResizeBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);
	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
}
