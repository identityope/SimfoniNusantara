package com.catulistiwa.simfoninusantara;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private MainGameView gameView;
	private DisplayMetrics metrics;
	private LinearLayout mylayout;
	private TextView songTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		DrawableManager.initInstance(this);
		metrics = getResources().getDisplayMetrics();
		gameView = new MainGameView(this, metrics.widthPixels, metrics.heightPixels);
		mylayout = (LinearLayout) findViewById(R.id.frame1);
		songTitle = (TextView) findViewById(R.id.textView1);
		songTitle.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Flavors-Regular.ttf"));
		mylayout.addView(gameView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onPause() {
		gameView.thread.setRunning(false); // matiin thread
		super.onPause();
	}
	@Override
	protected void onResume(){
		if(gameView.thread != null){
			gameView.initThread();
		}
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
