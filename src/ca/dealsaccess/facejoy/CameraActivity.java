package ca.dealsaccess.facejoy;

import java.io.IOException;

import com.example.facejoy.R;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
//import android.view.SurfaceHolder.Callback;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = "CameraActivity";
	
	private Camera camera;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_main_activity);
		
		
		SurfaceView surface = (SurfaceView) findViewById(R.id.surfaceView);
		SurfaceHolder holder = surface.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.setFixedSize(400, 300);
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "IO Exception", e);
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			camera.stopPreview();
		} catch (RuntimeException e) {}

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		camera.release();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		camera = Camera.open();
	}

}
