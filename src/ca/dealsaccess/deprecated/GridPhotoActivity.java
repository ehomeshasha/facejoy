package ca.dealsaccess.deprecated;

import java.io.File;
import java.util.ArrayList;

import com.example.facejoy.R;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.facejoy.image.ImageAdapter;

public class GridPhotoActivity extends ActionBarActivity {

	private static final String TAG = "GridPhotoActivity";
	
	private ArrayList<String> mList;
	
	private GridView gridView;
	
	private ImageAdapter imageAdapter;
	
	final private int TAKE_PICTURE = 1;
	
	Uri outputFileUri = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_grid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        if (savedInstanceState == null) {
        	initData();
        } else {
        	 Bundle map = savedInstanceState.getBundle(TAG);
             if (map != null) {
                 mList = map.getStringArrayList("mList");
             } else {
            	 initData();
             }
        }
		
		gridView = (GridView) findViewById(R.id.face_gridview);
		imageAdapter = new ImageAdapter(this,mList,R.layout.face_image);
		gridView.setAdapter(imageAdapter);
		
		
		//注册监听事件 
		gridView.setOnItemClickListener(new OnItemClickListener() { 
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) { 
            	Intent intent = new Intent(GridPhotoActivity.this, DisplayPhotoActivity.class);
            	intent.putExtra(AppConstants.EXTRA_MESSAGE, "openDisplayPhotoActivity");
            	intent.putExtra(AppConstants.CLICK_ID, id)
            		.putExtra(AppConstants.CLICK_PATH, mList.get((int) id));
        		startActivity(intent);
            }
		});
		
	}

	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putBundle(TAG, saveState());
    }


	 public Bundle saveState() {
        Bundle map = new Bundle();
        if(mList != null && mList.size() != 0) {
        	map.putStringArrayList("mList", mList);
        }
        return map;
    }
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		
		Cursor cursor = getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);
		if (cursor != null) {
			mList = new ArrayList<String>();
			while (cursor.moveToNext()) {
				String path = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaColumns.DATA));
				mList.add(path);
			}
			cursor.close();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.grid_photo_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    // Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.open_camera:
				openCamera();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		//Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();  
        //return true; 
	}
	
	
	
	private void openCamera() {
		//Intent intent = new Intent(this, CameraActivity.class);
		//intent.putExtra(AppConstants.EXTRA_MESSAGE, "openCameraActivity");
		//startActivity(intent);
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "test_"+System.currentTimeMillis()+".jpg");
		outputFileUri = Uri.fromFile(file);
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(AppConstants.EXTRA_MESSAGE, "openCameraActivity");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, TAKE_PICTURE);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == TAKE_PICTURE) {
/*			//检查结果是否包含缩略图
			if(data != null) {
				if(data.hasExtra("data")) {
					Bitmap thumbnail = data.getParcelableExtra("data");
					imageView.setImageBitmap()
				}
			}*/
			Intent intent = new Intent(this, DisplayPhotoActivity.class);
        	intent.putExtra(AppConstants.EXTRA_MESSAGE, "openDisplayPhotoActivity");
        	intent.putExtra(AppConstants.CLICK_PATH, outputFileUri.getPath());
    		startActivity(intent);
		}
	}
	

	@Override
	public void onDestroy() {  
		
        super.onDestroy();
        /*final GridView grid = gridView;
        final int count = grid.getChildCount();
        ImageView v = null;
        for (int i = 0; i < count; i++) {
        	
        	RelativeLayout r = (RelativeLayout) grid.getChildAt(i);
        	v = (ImageView) r.getChildAt(0);
            v.setBackgroundResource(0);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) v.getDrawable();
            //bitmapDrawable.setCallback(null);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if(bitmap != null && !bitmap.isRecycled()){
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
            //((BitmapDrawable) v.getDrawable()).setCallback(null);  
        }*/  
    }  
	
	
}
