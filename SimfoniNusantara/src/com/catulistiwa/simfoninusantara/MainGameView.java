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
	private Paint paint1,paint2,paint3,paint4,paint5;
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
	private int xline,music_speed,lastNotePos;
	private BPMLine bpmline;
	public MainGameView(Context context, int screenWidth, int screenHeight) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		
		matrix = new Matrix();
		sw = screenWidth;
		sh = screenHeight;
		background = DrawableManager.getInstance().getBackgroundImage1();
		background = DrawableManager.getInstance().ResizeBitmap(background, sh, sw);
		noteButtonImage = DrawableManager.getInstance().getTombolImage();
		noteImage = DrawableManager.getInstance().getNote1Image();
		score = 0;
		music_speed = 10;
		noteButtons = new ArrayList<NoteButton>();
		noteButtons.add(new NoteButton(sw/2-500,sh/2-110,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2-450,sh/2,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2-400,sh/2+110,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2+500,sh/2-110,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2+450,sh/2,noteButtonImage));
		noteButtons.add(new NoteButton(sw/2+400,sh/2+110,noteButtonImage));
		//list of note
		noteList = new ArrayList<Note>();
		noteList.add(new Note(sw/2,sh/2-110,0,2250,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,2500,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,2750,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,3000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,3250,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,3500,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,3750,noteImage));		
		noteList.add(new Note(sw/2,sh/2-110,0,4000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,4250,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,4500,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,4750,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,5000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,5250,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,5500,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,5750,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,6000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,6250,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,6500,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,6750,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,7000,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,7250,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,7500,noteImage));
		noteList.add(new Note(sw/2,sh/2-110,0,7750,noteImage));
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
		paint5 = new Paint();
		paint5.setColor(Color.BLACK);
		paint5.setAlpha(120);
		timeBar = new RectF(150,15,150,30);
		music1 = MediaPlayer.create(context, R.raw.gnr_track1);
		music1.setOnPreparedListener(new OnPreparedListener() {
		    public void onPrepared(MediaPlayer song) {
		        totalDuration = song.getDuration();
		        mintime = -1;
		        //music1.start();
		        xline = sw/2;
		    }
		});
		isShowBPMLine= true;
		bpmline = new BPMLine(sw/2,sh/2-180,120,noteButtonImage);
		lastNotePos = 0;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// inisialisasi thread
		initThread();
		begintime = System.currentTimeMillis();
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
		canvas.drawBitmap(background, 0, 0, paint5);
		for(int i=0;i<noteButtons.size();i++){
			noteButtons.get(i).draw(canvas);
		}
		String zerobuf;
		if(sectime<10){ zerobuf = "0"; }else{ zerobuf = ""; } 
		canvas.drawText("Time  "+mintime+":"+zerobuf+sectime, 20, 30, paint1);
		canvas.drawRect(150, 15, 550, 30, paint2);
		canvas.drawRect(timeBar, paint3);
		//canvas.drawRect(xline, sh/2-160, xline+1, sh/2-60, paint4);
		canvas.drawText("Score : "+score, 1100, 30, paint1);
		if(!visibleNotes.isEmpty()){
			for(int i=visibleNotes.size()-1;i>=0;i--){
				visibleNotes.get(i).draw(canvas);
			}
		}
		bpmline.drawSimple(canvas);
	}
	public void update() {
		//currentDuration = music1.getCurrentPosition();
		currentDuration = (int) (System.currentTimeMillis()-begintime);
		bpmline.update(currentDuration);
		if(!isPlaying && currentDuration>=3000){
			music1.start();
			isPlaying = true;
		}
		if(isShowBPMLine){
			xline-=music_speed;
			if(xline<=sw/2-500){
				xline=sw/2;
			}
		}
		//Log.d("Current miliseconds", ""+currentDuration+" ms");
		
		sectime = (currentDuration/1000)%60;
		mintime = (currentDuration/1000)/60;
		float progresstime = currentDuration*400/totalDuration;
		if(progresstime<=totalDuration){
			timeBar.set(timeBar.left, timeBar.top, timeBar.left+progresstime, timeBar.bottom);
		}
		if(lastNotePos < noteList.size() && currentDuration >= noteList.get(lastNotePos).getTimePos()){
			visibleNotes.add(noteList.get(lastNotePos));
			lastNotePos++;
		}
		/*
		for(int i=0;i<noteList.size();i++){
			if(currentDuration >= noteList.get(i).getTimePos()){
				if(!noteList.get(i).isVisible()){
					noteList.get(i).show();
					visibleNotes.add(noteList.get(i));
				}
			}
		}
		*/
		for(int i=0;i<visibleNotes.size();i++){
			if(visibleNotes.get(i).getX()<40){
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
