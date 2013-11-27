package it.open.techne;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class Info extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
	}
	
	public void onStartDemo(View v)
	{
		Intent intent = new Intent(getApplicationContext(), MultipleImage.class);
		startActivity(intent);
	}




}
