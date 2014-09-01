package com.catulistiwa.simfoninusantara;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoopThread extends Thread {
	private boolean running;
	private SurfaceHolder surfaceHolder;
	private GameView mainGameView;	
	private final static int MAX_FPS = 50; //fps yang diinginkan	
	private final static int MAX_FRAME_SKIPS = 10; //maksimum jumlah frame yang bisa diskip
	private final static int FRAME_PERIOD = 1000/MAX_FPS;	
	//test
	/*public int BPM = 120;
	private double BEAT_PERIOD;*/
	public GameLoopThread(SurfaceHolder surfaceHolder,GameView gameView) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.mainGameView = gameView;
		//BEAT_PERIOD = 60000/BPM/4;
	}
	
	public void setRunning(boolean val) {
		running = val;
	}
	
	public void run() {	
		Canvas canvas;
		long beginTime; //waktu mulai siklus
		long timeDiff; //waktu yang diperlukan satu siklus untuk selesai
		int sleepTime; //ms untuk tidur(<0 jika ketinggalan)
		int framesSkipped; //jumlah frame yang akan diskip
		//int beatSkipped;
		sleepTime = 0;
		
		while(running) {
			canvas = null;
			//ngunci canvas untuk digambar
			try{
				canvas = this.surfaceHolder.lockCanvas();
				synchronized(surfaceHolder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0; // reset jumlah frame yang pengen diskip
					//update game state
					//draw canvas di panel
					mainGameView.update();					
					mainGameView.render(canvas);							
					//hitung berapa lama satu siklus
					timeDiff = System.currentTimeMillis() - beginTime;
					//hitung waktu tidur
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					
					if(sleepTime > 0) {
						//kalo waktu tidur positif
						//tidurin thread selama waktu tidur tsb
						//cycle lebih cepat dari fps
						try{							
							Thread.sleep(sleepTime);
						}catch(InterruptedException e) {							
						}
					}
					while(sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						//ketinggalan fps, update tanpa manggil render
						this.mainGameView.update();
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
				}
			}finally{
				// in case of an exception the surface is not left in
                // an inconsistent state
				if(canvas!=null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}			
		}
	}
}
