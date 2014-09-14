package ca.dealsaccess.facejoy;

import java.util.ArrayList;

import com.example.facejoy.R;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.facejoy.image.ImageAdapter;

public class GridPhotoActivity extends ActionBarActivity {

	
	private ArrayList<String> mList;
	
	private GridView gridView;
	
	private ImageAdapter imageAdapter;
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydevice_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        
        
        initData();
		
		gridView = (GridView) findViewById(R.id.photo_gridview);
		imageAdapter = new ImageAdapter(this,mList);
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
