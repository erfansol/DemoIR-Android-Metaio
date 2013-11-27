package it.open.techne;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.GestureHandlerAndroid;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.GestureHandler;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

public class MultipleImage extends ARViewActivity 
{

	
	//private IGeometry mMetaioMan;
	private IGeometry mAltare;
	String trackingConfigFile;
	private MetaioSDKCallbackHandler mCallbackHandler;
	private GestureHandlerAndroid mGestureHandler;
	private int mGestureMask;
	private IGeometry mMoviePlane;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mMoviePlane=null;
		mGestureMask = GestureHandler.GESTURE_ALL;
		mCallbackHandler = new MetaioSDKCallbackHandler();
		mGestureHandler = new GestureHandlerAndroid(metaioSDK, mGestureMask);
	}
	
	@Override
	protected int getGUILayout() 
	{
		return R.layout.multiple; 
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		super.onTouch(v, event);

		mGestureHandler.onTouch(v, event);

		return true;
	}

	@Override
	public void onDrawFrame() 
	{
		super.onDrawFrame();
		
		if (metaioSDK != null)
		{
			// get all detected poses/targets
			TrackingValuesVector poses = metaioSDK.getTrackingValues();
			
			//if we have detected one, attach our metaio man to this coordinate system Id
			if (poses.size() != 0){
				mMoviePlane.startMovieTexture();
				mMoviePlane.setCoordinateSystemID(2);

				mAltare.setCoordinateSystemID(1);
			}
		}
	}

	public void onButtonClick(View v)
	{
		finish();
	}
	

	
	
	@Override
	protected void loadContents() 
	{
		try
		{
			
			// Load desired tracking data for planar marker tracking
			trackingConfigFile = AssetsManager.getAssetPath("TrackingData_MultipleFast.xml");
			
			
			boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile); 
			MetaioDebug.log("Markerless tracking data loaded: " + result); 

			
			// Load all the geometries. First - Model
						String altare = AssetsManager.getAssetPath("altare_scalato.obj");			
						if (altare != null) 
						{
							mAltare = metaioSDK.createGeometry(altare);
							if (mAltare != null) 
							{
								// Set geometry properties
								mAltare.setScale(new Vector3d(15.0f, 15.0f, 15.0f));
								mGestureHandler.addObject(mAltare, 1);
								MetaioDebug.log("Loaded geometry "+altare);
							}
							else
								MetaioDebug.log(Log.ERROR, "Error loading geometry: "+altare);
						}
						
						// Loading movie geometry
						final String moviePath = AssetsManager.getAssetPath("colosseo.3g2");
						if (moviePath != null)
						{
							mMoviePlane = metaioSDK.createGeometryFromMovie(moviePath, false);
							if (mMoviePlane != null)
							{
								mMoviePlane.setScale(5.0f);
								mMoviePlane.setRotation(new Rotation(0f, 0f, 0f));
								MetaioDebug.log("Loaded geometry "+moviePath);
							}
							else {
								MetaioDebug.log(Log.ERROR, "Error loading geometry: "+moviePath);
							}
						}
			
			
			
		
		}       
		catch (Exception e)
		{
			
		}
	}
	
  
	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() 
	{
		return mCallbackHandler;
	}
	
	final class MetaioSDKCallbackHandler extends IMetaioSDKCallback 
	{

		@Override
		public void onSDKReady() 
		{
			// show GUI
			runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					mGUIView.setVisibility(View.VISIBLE);
				}
			});
		}
	}
	
}
