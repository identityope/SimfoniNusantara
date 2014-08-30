package com.catulistiwa.simfoninusantara;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
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
	private Paint paint1,paint2,paint3,paint4;
	private RectF timeBar;
	//Menyiapkan Gambar-gambar
	private Bitmap background; //gambar latar belakang
	private Bitmap noteButtonImage;
	private Bitmap noteImage;
	private ArrayList<NoteButton> noteButtons; //gambar tombol note
	private int totalDuration,currentDuration;
	private int sectime,mintime;
	private MediaPlayer music1;
	private ArrayList<Note> noteList;
	private ArrayList<Note> visibleNotes;
	private int score;
	private long begintime;
	private boolean isPlaying,isShowBPMLine;
	private int xline,music_speed;
	public MainGameView(Context context, int screenWidth, int screenHeight) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		matrix = new Matrix();
		sw = screenWidth;
		sh = screenHeight;
		background = DrawableManager.getInstance().getBackgroundImage();
		noteButtonImage = DrawableManager.getInstance().getTombolImage();
		noteImage = DrawableManager.getInstance().getNote1Image();
		score = 0;
		music_speed = 36;
		noteButtons = new ArrayList<NoteButton>();
		noteButtons.add(new NoteButton(sw/2-500,sh/2-110,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2-450,sh/2,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2-400,sh/2+110,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2+500,sh/2-110,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2+450,sh/2,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2+400,sh/2+110,noteButtonImage));
		//list of note
		noteList = new ArrayList<Note>();
		noteList.add(new Note(sw/2,sh/2-110,0,1000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,2687.5,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,2875,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,4187.5,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,4375,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,12000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,12500,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,13000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,13500,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,14000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,18000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,18125,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,18250,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,18375,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,18500,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,18625,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,18750,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,18875,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,19000,noteImage));
		
		visibleNotes = new ArrayList<Note>();
		paint1 = new Paint();
		paint1.setColor(Color.WHITE);
		paint1.setTextSize(24);
		paint2 = new Paint();
		paint2.setColor(Color.BLACK);
		paint3 = new Paint();
		paint3.setColor(Color.BLUE);
		paint4 = new Paint();
		paint4.setColor(Color.RED);
		timeBar = new RectF(150,15,150,30);
		music1 = MediaPlayer.create(context, R.raw.manuk_dadali);
		music1.setOnPreparedListener(new OnPreparedListener() {
		    public void onPrepared(MediaPlayer song) {
		        totalDuration = song.getDuration();
		        mintime = -1;
		        //music1.start();
		        begintime = System.currentTimeMillis();
		        xline = sw/2;
		    }
		});
		isShowBPMLine= true;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// inisialisasi thread
		initThread();
		Log.d(TAG, "surface created ("+sw+"x"+sh+")");
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
		canvas.drawBitmap(background, 0, 0, null);
		for(int i=0;i<noteButtons.size();i++){
			noteButtons.get(i).draw(canvas);
		}
		String zerobuf;
		if(sectime<10){ zerobuf = "0"; }else{ zerobuf = ""; } 
		canvas.drawText("Time  "+mintime+":"+zerobuf+sectime, 20, 30, paint1);
		canvas.drawRect(150, 15, 550, 30, paint2);
		canvas.drawRect(timeBar, paint3);
		canvas.drawRect(xline, sh/2-160, xline+1, sh/2-60, paint4);
		canvas.drawText("Score : "+score, 600, 30, paint1);
		if(!visibleNotes.isEmpty()){
			for(int i=visibleNotes.size()-1;i>=0;i--){
				visibleNotes.get(i).draw(canvas);
			}
		}
	}
	public void update() {
		//currentDuration = music1.getCurrentPosition();
		currentDuration = (int) (System.currentTimeMillis()-begintime);
		if(!isPlaying && currentDuration>=1000){
			music1.start();
			isPlaying = true;
		}
		if(isShowBPMLine){
			xline-=music_speed;
			if(xline<=sw/2-500){
				xline=sw/2;
			}
		}
		Log.d("Current miliseconds", ""+(System.currentTimeMillis()-begintime)+" ms");
		sectime = (currentDuration/1000)%60;
		mintime = (currentDuration/1000)/60;
		float progresstime = currentDuration*400/totalDuration;
		timeBar.set(timeBar.left, timeBar.top, timeBar.left+progresstime, timeBar.bottom);
		for(int i=0;i<noteList.size();i++){
			if(currentDuration >= noteList.get(i).getTimePos()){
				if(!noteList.get(i).isVisible()){
					noteList.get(i).show();
					visibleNotes.add(noteList.get(i));
				}
			}
		}
		for(int i=0;i<visibleNotes.size();i++){
			if(visibleNotes.get(i).getX()<40){
				//visibleNotes.get(i).hide();
				visibleNotes.remove(i);
			}else{
				visibleNotes.get(i).move(music_speed);
			}
		}
	}
	public boolean onTouchEvent(MotionEvent event) {
		final int actioncode = event.getAction() & MotionEvent.ACTION_MASK;				
		switch (actioncode) {
			case MotionEvent.ACTION_DOWN:
				Log.d(TAG, "down at " + event.getX() + " " + event.getY());
				score = currentDuration;
				for(int i=0;i<visibleNotes.size();i++){
					if(visibleNotes.get(i).getRectangle().contains(event.getX(), event.getY())
						&& noteButtons.get(0).getRectangle().contains(event.getX(), event.getY())){
						visibleNotes.remove(i);
						score+=10;
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				Log.d(TAG, "move at " + event.getX() + " " + event.getY());
				break;
			case MotionEvent.ACTION_UP:
				Log.d(TAG, "up at " + event.getX() + " " + event.getY());
				break;		
		}				
		return true;
	}
}
