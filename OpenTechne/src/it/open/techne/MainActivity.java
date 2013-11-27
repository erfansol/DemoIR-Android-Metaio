// Copyright 2007-2013 metaio GmbH. All rights reserved.
package it.open.techne;

import it.open.techne.BuildConfig;
import it.open.techne.R;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;

public class MainActivity extends Activity
{
	//TODO: Select the template, i.e. Native Android or AREL
	public static final boolean NATIVE = false;
	
	/**
	 * Task that will extract all the assets
	 */
	private AssetsExtracter mTask;
	View mProgress;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		// Enable metaio SDK debug log messages based on build configuration
		MetaioDebug.enableLogging(BuildConfig.DEBUG);
		mProgress = findViewById(R.id.progressBar1);

		
		// extract all the assets
		mTask = new AssetsExtracter();
		mTask.execute(0);


	}

	/**
	 * This task extracts all the assets to an external or internal location
	 * to make them accessible to metaio SDK
	 */
	private class AssetsExtracter extends AsyncTask<Integer, Integer, Boolean>
	{

		@Override
		protected void onPreExecute() 
		{
			mProgress.setVisibility(View.VISIBLE);
		
		}
		
		@Override
		protected Boolean doInBackground(Integer... params) 
		{
			try 
			{
				// TODO: Extract all assets and override existing files
				AssetsManager.extractAllAssets(getApplicationContext(), true);
			} 
			catch (IOException e) 
			{
				MetaioDebug.log(Log.ERROR, "Error extracting assets: "+e.getMessage());
				MetaioDebug.printStackTrace(Log.ERROR, e);
				return false;
			}
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) 
		{
			mProgress.setVisibility(View.GONE); 

			Intent intent = new Intent(getApplicationContext(), Info.class);
			startActivity(intent);
			
			finish();
	    }
		
	}
	
}

