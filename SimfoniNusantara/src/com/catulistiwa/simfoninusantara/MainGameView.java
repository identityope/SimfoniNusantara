package com.catulistiwa.simfoninusantara;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class MainGameView extends SurfaceView implements SurfaceHolder.Callback, GameView {
	private static final String TAG = MainGameView.class.getSimpleName();
	public GameLoopThread thread;
	private Matrix matrix;
	private int sw,sh;
	private Paint paint1,paint2,paint3,paint4,paint5,paint6,paint7,paintcombo;
	private RectF timeBar;
	//Menyiapkan Gambar-gambar
	private Bitmap background; //gambar latar belakang
	private Bitmap noteButtonImage;
	private Bitmap noteImage;
	private Bitmap noteEffectImage;
	private Bitmap timebarBg, timebarFg;
	private Bitmap buttonBg;
	private Button buttonBack, buttonPause;
	private ArrayList<NoteButton> noteButtons; //gambar tombol note
	private ArrayList<NoteEffect> noteEffects;
	private NoteIndicator noteIndicator;
	private long totalDuration,currentDuration,lastPlayhead,previousFrameTime,currentMusicTime;
	private int sectime,mintime;
	private MediaPlayer music1;
	private ArrayList<Note> noteList;
	private int score,combo;
	private double music_speed;
	private BPMLine bpmline;
	public MainGameView(Context context, int screenWidth, int screenHeight) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		matrix = new Matrix();
		sw = screenWidth;
		sh = screenHeight;
		//Untuk Grafik
		DrawableManager.getInstance();
		background = DrawableManager.getInstance().getBackgroundImage();
		background = DrawableManager.ResizeBitmap(background, sh, sw);
		noteButtonImage = DrawableManager.getInstance().getTombolImage();
		noteImage = DrawableManager.getInstance().getNote1Image();
		noteEffectImage = DrawableManager.getInstance().getEffect1Image();
		timebarBg = DrawableManager.getInstance().getTimebarBg();
		timebarFg = DrawableManager.getInstance().getTimebarFg();
		buttonBg = DrawableManager.getInstance().getButtonBg();
		buttonBack = new Button(sw/2-93,sh-90,buttonBg);
		buttonPause = new Button(sw/2+93,sh-90,buttonBg);
		noteButtons = new ArrayList<NoteButton>();
//		noteButtons.add(new NoteButton(sw/2-500,sh/2-110,noteButtonImage));
//		noteButtons.add(new NoteButton(sw/2-450,sh/2,noteButtonImage));
//		noteButtons.add(new NoteButton(sw/2-400,sh/2+110,noteButtonImage));
//		noteButtons.add(new NoteButton(sw/2+500,sh/2-110,noteButtonImage));
//		noteButtons.add(new NoteButton(sw/2+450,sh/2,noteButtonImage));
//		noteButtons.add(new NoteButton(sw/2+400,sh/2+110,noteButtonImage));
		int rangex1 = noteButtonImage.getWidth()/2,
			rangex2 = noteButtonImage.getWidth()*3/4,
			rangex3 = noteButtonImage.getWidth(),
			rangey1 = sh/2-noteButtonImage.getHeight(),
			rangey2 = sh/2,
			rangey3 = sh/2+noteButtonImage.getHeight();
		noteButtons.add(new NoteButton(rangex1,	rangey1,noteButtonImage));
		noteButtons.add(new NoteButton(rangex2,	rangey2,noteButtonImage));
		noteButtons.add(new NoteButton(rangex3,	rangey3,noteButtonImage));
		noteButtons.add(new NoteButton(sw-rangex1,rangey1,noteButtonImage));
		noteButtons.add(new NoteButton(sw-rangex2,rangey2,noteButtonImage));
		noteButtons.add(new NoteButton(sw-rangex3,rangey3,noteButtonImage));
		noteEffects = new ArrayList<NoteEffect>();
		noteEffects.add(new NoteEffect(rangex1,rangey1,noteEffectImage));
		noteEffects.add(new NoteEffect(rangex2,rangey2,noteEffectImage));
		noteEffects.add(new NoteEffect(rangex3,rangey3,noteEffectImage));
		noteEffects.add(new NoteEffect(sw-rangex1,rangey1,noteEffectImage));
		noteEffects.add(new NoteEffect(sw-rangex2,rangey2,noteEffectImage));
		noteEffects.add(new NoteEffect(sw-rangex3,rangey3,noteEffectImage));
		noteIndicator = new NoteIndicator(sw/2,sh/2-210,DrawableManager.getInstance().getNoteCool(),DrawableManager.getInstance().getNoteMiss());
		paint1 = new Paint();
		paint1.setColor(Color.WHITE);
		paint1.setTextSize(24);
		paint1.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Flavors-Regular.ttf"));
		paint1.setTextAlign(Align.CENTER);
		paint2 = new Paint();
		paint2.setColor(Color.parseColor("#222222"));
		paint3 = new Paint();
		paint3.setColor(Color.BLUE);
		paint4 = new Paint();
		paint4.setColor(Color.RED);
		paint5 = new Paint();
		paint5.setColor(Color.BLACK);
		paint5.setAlpha(220);
		paint6 = new Paint();
		paint6.setColor(Color.WHITE);
		paint6.setTextSize(36);
		paint6.setTextAlign(Align.CENTER);
		paint6.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Flavors-Regular.ttf"));
		paint7 = new Paint();
		paint7.setColor(Color.BLACK);
		paint7.setAlpha(60); 
		paintcombo = new Paint();
		paintcombo.setColor(Color.BLUE);
		paintcombo.setTextSize(76);
		paintcombo.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Chunkfive.otf"));
		paintcombo.setTextAlign(Align.CENTER);
		timeBar = new RectF(20+timebarBg.getWidth()/30,14+timebarBg.getHeight()/10,20+timebarBg.getWidth()/30,16+timebarBg.getHeight()*9/10);
		bpmline = new BPMLine(sw/2,sh/2-180,103,noteButtonImage);
		//list of note
		noteList = new ArrayList<Note>();
		int delay = 0;
		noteList.add(new Note(0,5300+delay,noteImage,sw,sh));
		noteList.add(new Note(3,5300+delay,noteImage,sw,sh));
		noteList.add(new Note(1,7642+delay,noteImage,sw,sh));
		noteList.add(new Note(4,7642+delay,noteImage,sw,sh));
		noteList.add(new Note(2,9967+delay,noteImage,sw,sh));
		noteList.add(new Note(5,9967+delay,noteImage,sw,sh));
		
		noteList.add(new Note(0,12266+delay,noteImage,sw,sh));
		noteList.add(new Note(1,14643+delay,noteImage,sw,sh));
		noteList.add(new Note(2,16994+delay,noteImage,sw,sh));
		
		noteList.add(new Note(0,19345+delay,noteImage,sw,sh));
		noteList.add(new Note(1,19632+delay,noteImage,sw,sh));
		noteList.add(new Note(2,19841+delay,noteImage,sw,sh));
		noteList.add(new Note(3,20102+delay,noteImage,sw,sh));
		noteList.add(new Note(4,20285+delay,noteImage,sw,sh));
		noteList.add(new Note(5,20755+delay,noteImage,sw,sh));
		noteList.add(new Note(4,21121+delay,noteImage,sw,sh));
		noteList.add(new Note(3,21330+delay,noteImage,sw,sh));
		noteList.add(new Note(2,21696+delay,noteImage,sw,sh));
		noteList.add(new Note(1,22009+delay,noteImage,sw,sh));
		noteList.add(new Note(0,22166+delay,noteImage,sw,sh));
		noteList.add(new Note(1,22479+delay,noteImage,sw,sh));
		noteList.add(new Note(2,22662+delay,noteImage,sw,sh));

		noteList.add(new Note(0,26502+delay,noteImage,sw,sh));
		noteList.add(new Note(5,28723+delay,noteImage,sw,sh));
		noteList.add(new Note(3,30499+delay,noteImage,sw,sh));
		noteList.add(new Note(2,31622+delay,noteImage,sw,sh));
		noteList.add(new Note(1,32850+delay,noteImage,sw,sh));
		noteList.add(new Note(0,33947+delay,noteImage,sw,sh));
		noteList.add(new Note(3,35149+delay,noteImage,sw,sh));
		noteList.add(new Note(4,36324+delay,noteImage,sw,sh));
		noteList.add(new Note(4,37552+delay,noteImage,sw,sh));
		noteList.add(new Note(2,38728+delay,noteImage,sw,sh));
		noteList.add(new Note(1,39877+delay,noteImage,sw,sh));
		noteList.add(new Note(1,41026+delay,noteImage,sw,sh));
		noteList.add(new Note(2,42228+delay,noteImage,sw,sh));
		noteList.add(new Note(2,43377+delay,noteImage,sw,sh));
		noteList.add(new Note(3,44553+delay,noteImage,sw,sh));
		noteList.add(new Note(3,45650+delay,noteImage,sw,sh));
		noteList.add(new Note(2,46277+delay,noteImage,sw,sh));
		noteList.add(new Note(0,46590+delay,noteImage,sw,sh));
		noteList.add(new Note(1,46930+delay,noteImage,sw,sh));
		noteList.add(new Note(2,47165+delay,noteImage,sw,sh));
		noteList.add(new Note(3,47479+delay,noteImage,sw,sh));
		noteList.add(new Note(4,48602+delay,noteImage,sw,sh));
		noteList.add(new Note(5,49777+delay,noteImage,sw,sh));
		noteList.add(new Note(0,51005+delay,noteImage,sw,sh));
		noteList.add(new Note(1,52155+delay,noteImage,sw,sh));
		
		//Variable dalam Game
		score = 0;
		combo = 0;
		music_speed = 1;
		music1 = MediaPlayer.create(context, R.raw.manuk_dadali);
		music1.setOnPreparedListener(new OnPreparedListener() {
		    public void onPrepared(MediaPlayer song) {
		        totalDuration = song.getDuration();
		        mintime = -1;
		    }
		});		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Log.d(TAG, "surface created ("+sw+"x"+sh+")");
		// inisialisasi thread
		initThread();
		music1.start();
		previousFrameTime = System.currentTimeMillis();
		lastPlayhead = 0;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {	
		releaseThread();
		Log.d(TAG, "surface destroyed");
	}
	
	// inisialisasi thread
	public void initThread() {
		if (thread == null || !thread.isAlive()) {
			// jika belom diinisialisasi threadnya atau threadnya sudah tidak hidup lagi, maka
			// instansiasi thread utama
			thread = new GameLoopThread(getHolder(), this);
			thread.start();
		}
		thread.setRunning(true);
	}
	 
	private void releaseThread() {
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
				thread = null;
			} catch (InterruptedException e) {
			}
		}
		music1.stop();
		music1.release();
	}

	public void render(Canvas canvas) {				
		canvas.setMatrix(matrix);
		canvas.drawColor(Color.BLACK);// clear screen		
		canvas.drawBitmap(background, 0, 0, paint5);
		//canvas.drawRect(0, sh/2-180, sw, sh/2+170, paint7);
		for(int i=0;i<noteButtons.size();i++){
			noteButtons.get(i).draw(canvas);
		}
		String zerobuf;
		if(sectime<10){ zerobuf = "0"; }else{ zerobuf = ""; } 
		canvas.drawBitmap(timebarBg, 20, 15, null);
		canvas.drawBitmap(timebarFg, new Rect(0, 0, 32, 32), timeBar, null);
		canvas.drawText(""+mintime+":"+zerobuf+sectime, 245, 44, paint1);
		canvas.drawRect(sw-265,20,sw-15,60,paint7);
		canvas.drawText(""+score, sw-125, 52, paint6);
		if(combo>2){
			canvas.drawText(""+combo, sw/2, sh/2-260, paintcombo);
		}
		for(int i=0;i<noteList.size();i++){
			if(noteList.get(i).getType()<3){
				if(noteList.get(i).getX()>=40 && noteList.get(i).getX()<sw/2-25){
					noteList.get(i).draw(canvas);
				}
			}else{
				if(noteList.get(i).getX()<=sw && noteList.get(i).getX()>sw/2+25){
					noteList.get(i).draw(canvas);
				}
			}
		}
		bpmline.drawSimple(canvas);
		for(int i=0;i<noteEffects.size();i++){
			if(noteEffects.get(i).isAnimation()){
				noteEffects.get(i).drawAnim(canvas);
			}
		}
		if(noteIndicator.isAnimation()){
			noteIndicator.drawAnim(canvas);
		}
		//footer
		buttonBack.draw(canvas);
		buttonPause.draw(canvas);
		canvas.drawText("Kembali", sw/2-buttonBg.getWidth()/2, sh-80, paint6);
		canvas.drawText("Berhenti", sw/2+buttonBg.getWidth()/2, sh-80, paint6);
	}
	public void update() {
		currentMusicTime = music1.getCurrentPosition();
		currentDuration += System.currentTimeMillis() - previousFrameTime;
		previousFrameTime = System.currentTimeMillis();
		if(currentMusicTime != lastPlayhead){
			currentDuration = (currentDuration + currentMusicTime)/2;
			lastPlayhead = currentMusicTime;
		}
		bpmline.update((int) currentDuration);
		sectime = (int) ((currentDuration/1000)%60);
		mintime = (int) ((currentDuration/1000)/60);
		float progresstime = currentDuration*430/totalDuration;
		if(progresstime<=totalDuration){
			timeBar.set(timeBar.left, timeBar.top, timeBar.left+progresstime, timeBar.bottom);
		}
		for(int i=0;i<noteList.size();i++){
			int ranget;
			if (noteList.get(i).getType() == 0 || noteList.get(i).getType() == 3)
				ranget = 500;
			else if (noteList.get(i).getType() == 1 || noteList.get(i).getType() == 4)
				ranget = 450;
			else
				ranget = 400;
			double xt = (currentDuration-noteList.get(i).getTimePos())*music_speed/2;
			if(noteList.get(i).getType()<3){
				noteList.get(i).moveX(sw/2-ranget-xt);
				if(noteList.get(i).getX()<40){
					noteList.remove(i);
					noteIndicator.startAnimation(1);
					combo = 0;
				}
			}else{
				noteList.get(i).moveX(sw/2+ranget+xt);
				if(noteList.get(i).getX()>sw){
					noteList.remove(i);
					noteIndicator.startAnimation(1);
					combo = 0;
				}
			}
		}
		if(currentDuration>=totalDuration){
			((Activity) getContext()).finish();
		}
	}
	public boolean onTouchEvent(MotionEvent event) {
		//final int actioncode = event.getAction() & MotionEvent.ACTION_MASK;	
		// get pointer index from the event object
	    int pointerIndex = event.getActionIndex();
	    // get pointer ID
	    //int pointerId = event.getPointerId(pointerIndex);
	    // get masked (not specific to a pointer) action
	    int maskedAction = event.getActionMasked();
	    switch (maskedAction) {
	    	case MotionEvent.ACTION_DOWN:{
	    		Log.d("TIME", "Time : " + currentMusicTime);
				for(int i=0;i<noteButtons.size();i++){
					if(noteButtons.get(i).getRectangle().contains(event.getX(), event.getY())){
						noteButtons.get(i).buttonDown();
					}
				}
				for(int i=0;i<noteList.size();i++){
					for(int j=0;j<noteButtons.size();j++){
						if(i<noteList.size() && noteList.get(i).getRectangle().intersect(noteButtons.get(j).getRectangle())
								&& noteButtons.get(j).getRectangle().contains(event.getX(), event.getY())){
								noteList.remove(i);
								score+=100;
								combo++;
								noteEffects.get(j).startAnimation();
								noteIndicator.startAnimation(0);
							}
					}
				}
				if(buttonBack.getRectangle().contains(event.getX(), event.getY())){
	    			((Activity) getContext()).finish();
	    		}else if(buttonPause.getRectangle().contains(event.getX(), event.getY())){
	    			((Activity) getContext()).finish();
	    		}
	    		break;
	    	}
	    	case MotionEvent.ACTION_POINTER_DOWN: {
	    		// We have a new pointer. Lets add it to the list of pointers
	    		Log.d(TAG, "down at " + event.getX(pointerIndex) + " " + event.getY(pointerIndex));
				for(int i=0;i<noteButtons.size();i++){
					if(noteButtons.get(i).getRectangle().contains(event.getX(pointerIndex), event.getY(pointerIndex))){
						noteButtons.get(i).buttonDown();
					}
				}
				for(int i=0;i<noteList.size();i++){
					for(int j=0;j<noteButtons.size();j++){
						if(i<noteList.size() && noteList.get(i).getRectangle().intersect(noteButtons.get(j).getRectangle())
								&& noteButtons.get(j).getRectangle().contains(event.getX(pointerIndex), event.getY(pointerIndex))){
								noteList.remove(i);
								score+=10;
								combo++;
								noteEffects.get(j).startAnimation();
								noteIndicator.startAnimation(0);
							}
					}
				}
	    		break;
	    	}
	    	case MotionEvent.ACTION_MOVE: { // a pointer was moved
	    		break;
	    	}
	    	case MotionEvent.ACTION_UP:{
	    		Log.d(TAG, "up at " + event.getX() + " " + event.getY());
				for(int i=0;i<noteButtons.size();i++){
					if(noteButtons.get(i).getRectangle().contains(event.getX(), event.getY())){
						noteButtons.get(i).buttonUp();
					}
				}
	    		break;
	    	}
	    	case MotionEvent.ACTION_POINTER_UP:{
	    		Log.d(TAG, "up at " + event.getX(pointerIndex) + " " + event.getY(pointerIndex));
				for(int i=0;i<noteButtons.size();i++){
					if(noteButtons.get(i).getRectangle().contains(event.getX(pointerIndex), event.getY(pointerIndex))){
						noteButtons.get(i).buttonUp();
					}
				}
	    		break;
	    	}
	    	case MotionEvent.ACTION_CANCEL: {
	    		//Cancel event
	    		break;
	    	}
	    }		
		return true;
	}
}
