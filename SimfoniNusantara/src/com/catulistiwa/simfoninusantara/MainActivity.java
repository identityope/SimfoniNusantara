package com.catulistiwa.simfoninusantara;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity {
	private MainGameView testmap;
	private DisplayMetrics metrics;
	private LinearLayout mylayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		DrawableManager.initInstance(this);
		metrics = getResources().getDisplayMetrics();
		testmap = new MainGameView(this, metrics.widthPixels, metrics.heightPixels);
		mylayout = (LinearLayout) findViewById(R.id.frame1);
		mylayout.addView(testmap);
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
		testmap.thread.setRunning(false); // matiin thread
		Log.d("A", "PAUSE");
		super.onPause();
	}
	@Override
	protected void onResume(){
		Log.d("A", "RESUME");
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
