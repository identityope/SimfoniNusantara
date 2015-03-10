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
	private Bitmap background,backgroundmenu;
	private Bitmap tombol;
	private Bitmap note1;
	private Bitmap effect1;
	private Bitmap notecool;
	private Bitmap notemiss;
	private Bitmap timebarBg,timebarFg;
	private Bitmap buttonBg;
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
			background = BitmapFactory.decodeResource(res, R.drawable.gedung_sate);
		}
		return background;
	}
	public Bitmap getBackgroundMenuImage() {
		if (backgroundmenu == null) {
			backgroundmenu = BitmapFactory.decodeResource(res, R.drawable.mockup_menu);
		}
		return backgroundmenu;
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
	public Bitmap getEffect1Image() {
		if (effect1 == null) {
			effect1 = BitmapFactory.decodeResource(res, R.drawable.note_click_sprite1);
		}
		return effect1;
	}
	public Bitmap getNoteCool() {
		if (notecool == null) {
			notecool = BitmapFactory.decodeResource(res, R.drawable.note_cool);
		}
		return notecool;
	}
	public Bitmap getNoteMiss() {
		if (notemiss == null) {
			notemiss = BitmapFactory.decodeResource(res, R.drawable.note_miss);
		}
		return notemiss;
	}
	public Bitmap getTimebarBg() {
		if (timebarBg == null) {
			timebarBg = BitmapFactory.decodeResource(res, R.drawable.timebarbg);
		}
		return timebarBg;
	}
	public Bitmap getTimebarFg() {
		if (timebarFg == null) {
			timebarFg = BitmapFactory.decodeResource(res, R.drawable.timebarfg);
		}
		return timebarFg;
	}
	public Bitmap getButtonBg() {
		if (buttonBg == null) {
			buttonBg = BitmapFactory.decodeResource(res, R.drawable.buttonback);
		}
		return buttonBg;
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
