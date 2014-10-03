package ca.dealsaccess.deprecated;


import ca.dealsaccess.facejoy.common.AppConstants;

import com.example.facejoy.R;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.support.v7.app.ActionBarActivity;

/**
 * Android实现获取本机中所有图片
 * @Description: Android实现获取本机中所有图片

 * @FileName: MyDevicePhotoActivity.java 

 * @Package com.device.photo 

 * @Author Hanyonglu

 * @Date 2012-5-10 下午04:43:55 

 * @Version V1.0
 */
public class MyDevicePhotoActivity extends ActionBarActivity implements LoaderCallbacks<Cursor>{
    private Bitmap bitmap = null;
//    private byte[] mContent = null;
    
//    private ListView listView = null;
    
    private GridView gridView = null;
    
    private SimpleCursorAdapter simpleCursorAdapter = null;
    
    
    //private ImageView imageView = null;
    
    //private TextView textView = null;
    
//    private Bitmap img = null;
    
    static final String CLICK_ID = "CLICK_ID";
    
    private static final String[] STORE_IMAGES = {
        //MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        //MediaStore.Images.Media.LATITUDE,
        //MediaStore.Images.Media.LONGITUDE,
        MediaStore.Images.Media._ID,
        
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_grid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        //listView = (ListView)findViewById(R.id.ListView01);
        gridView = (GridView)findViewById(R.id.face_gridview);
        //textView = (TextView)this.findViewById(R.id.photo_textView);
        //imageView = (ImageView)this.findViewById(R.id.photo_imageView);
        //textView.setVisibility(View.INVISIBLE);
        //imageView.setVisibility(View.INVISIBLE);
        
        //gridView.setAdapter(new MyAdapter(this)); 
        //注册监听事件 
       gridView.setOnItemClickListener(new OnItemClickListener() { 
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) { 
            	Intent intent = new Intent(MyDevicePhotoActivity.this, DisplayPhotoActivity.class);
            	intent.putExtra(AppConstants.EXTRA_MESSAGE, "openDisplayPhotoActivity");
            	intent.putExtra(CLICK_ID, id);
        		startActivity(intent);
                /*Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                		appendPath(Long.toString(id)).build();
                
                Toast.makeText(MyDevicePhotoActivity.this, "pic=" + uri.toString(), Toast.LENGTH_SHORT).show();
                
                
                FileUtil file = new FileUtil();
                ContentResolver resolver = getContentResolver();
              
                // 从Uri中读取图片资源
                try {
                	mContent = file.readInputStream(resolver.openInputStream(Uri.parse(uri.toString())));
                	bitmap = file.getBitmapFromBytes(mContent, null);
                	ivImageShow.setImageBitmap(bitmap);
                } catch (Exception e) {
                	//TODO: handle exception
                	e.printStackTrace();
                }*/

                /*((ImageView) v).get
                Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
    			cursor.moveToFirst();
    			int idx = cursor.getColumnIndex(ImageColumns.DATA);
    			String fileSrc = cursor.getString(idx); 
    			//Log.d(TAG, "Picture:" + fileSrc);
    			
    			//just read size
    			Options options = new Options();
    			options.inJustDecodeBounds = true;
    			img = BitmapFactory.decodeFile(fileSrc, options);

    			//scale size to read
    			options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)options.outWidth / 1024f, (double)options.outHeight / 1024f)));
    			options.inJustDecodeBounds = false;
    			img = BitmapFactory.decodeFile(fileSrc, options);
    			textView.setText("Clik Detect. ==>");
    			v.setImageURI(Uri.parse(value));
    			
    			imageView.setImageBitmap(img);
    			buttonDetect.setVisibility(View.VISIBLE);*/
				
			
            } 
        });
        
       
       
       
       
       
       
/*     //textView.setText("Waiting ...");
		
		FaceppDetect faceppDetect = new FaceppDetect();
		faceppDetect.setDetectCallback(new DetectCallback() {
			
			public void detectResult(JSONObject rst) {
				//Log.v(TAG, rst.toString());
				
				//use the red paint
				Paint paint = new Paint();
				paint.setColor(Color.RED);
				paint.setStrokeWidth(Math.max(img.getWidth(), img.getHeight()) / 100f);

				//create a new canvas
				Bitmap bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());
				Canvas canvas = new Canvas(bitmap);
				canvas.drawBitmap(img, new Matrix(), null);
				
				
				try {
					//find out all faces
					final int count = rst.getJSONArray("face").length();
					for (int i = 0; i < count; ++i) {
						float x, y, w, h;
						//get the center point
						x = (float)rst.getJSONArray("face").getJSONObject(i)
								.getJSONObject("position").getJSONObject("center").getDouble("x");
						y = (float)rst.getJSONArray("face").getJSONObject(i)
								.getJSONObject("position").getJSONObject("center").getDouble("y");

						//get face size
						w = (float)rst.getJSONArray("face").getJSONObject(i)
								.getJSONObject("position").getDouble("width");
						h = (float)rst.getJSONArray("face").getJSONObject(i)
								.getJSONObject("position").getDouble("height");
						
						//change percent value to the real size
						x = x / 100 * img.getWidth();
						w = w / 100 * img.getWidth() * 0.7f;
						y = y / 100 * img.getHeight();
						h = h / 100 * img.getHeight() * 0.7f;

						//draw the box to mark it out
						canvas.drawLine(x - w, y - h, x - w, y + h, paint);
						canvas.drawLine(x - w, y - h, x + w, y - h, paint);
						canvas.drawLine(x + w, y + h, x - w, y + h, paint);
						canvas.drawLine(x + w, y + h, x + w, y - h, paint);
					}
					
					//save new image
					img = bitmap;

					MyDevicePhotoActivity.this.runOnUiThread(new Runnable() {
						
						public void run() {
							//show the image
							imageView.setImageBitmap(img);
							textView.setText("Finished, "+ count + " faces.");
						}
					});
					
				} catch (JSONException e) {
					e.printStackTrace();
					MyDevicePhotoActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							textView.setText("Error.");
						}
					});
				}
				
			}
		});
		faceppDetect.detect(img);*/
       
       
       
       
       
       
       
       
       
       
       
       
       
       
       
        //MyAdapter myAdapter = new MyAdapter(this, R.layout.activity_gridview, null);
        
        simpleCursorAdapter = new SimpleCursorAdapter(
                this, 
                //R.layout.activity_gridview,
                R.layout.face_image,
                null, 
                STORE_IMAGES, 
                new int[] { R.id.image_body}, 
                0
                );
        
        //simpleCursorAdapter.setViewBinder(new ImageLocationBinder());
        gridView.setAdapter(simpleCursorAdapter);
        // 注意此处是getSupportLoaderManager()，而不是getLoaderManager()方法。
        getSupportLoaderManager().initLoader(0, null, this);
        
        // 单击显示图片
        //listView.setOnItemClickListener(new ShowItemImageOnClickListener());
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        // TODO Auto-generated method stub
        // 为了查看信息，需要用到CursorLoader。
        CursorLoader cursorLoader = new CursorLoader(
                this, 
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
                STORE_IMAGES, 
                null, 
                null, 
                null);
        return cursorLoader;
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
        simpleCursorAdapter.swapCursor(null);
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        // TODO Auto-generated method stub
        // 使用swapCursor()方法，以使旧的游标不被关闭．
        simpleCursorAdapter.swapCursor(cursor);
    }
    
    // 将图片的位置绑定到视图
    @SuppressWarnings("unused")
	private class ImageLocationBinder implements ViewBinder{ 
        @Override
        public boolean setViewValue(View view, Cursor cursor, int arg2) {
            // TODO Auto-generated method stub
            if (arg2 == 1) {
                // 图片经度和纬度
                double latitude = cursor.getDouble(arg2);
                double longitude = cursor.getDouble(arg2 + 1);
                
                if (latitude == 0.0 && longitude == 0.0) {
                    ((TextView)view).setText("位置：未知");
                } else {
                    ((TextView)view).setText("位置：" + latitude + ", " + longitude);
                }
                
                // 需要注意：在使用ViewBinder绑定数据时，必须返回真；否则，SimpleCursorAdapter将会用自己的方式绑定数据。
                return true;
            } else {
                return false;
            }
        }
    }
    
    /*// 单击项显示图片事件监听器
    private class ShowItemImageOnClickListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // TODO Auto-generated method stub
            final Dialog dialog = new Dialog(MyDevicePhotoActivity.this);
            // 以对话框形式显示图片
            dialog.setContentView(R.layout.image_show);
            dialog.setTitle("图片显示");

            ImageView ivImageShow = (ImageView) dialog.findViewById(R.id.ivImageShow);
            Button btnClose = (Button) dialog.findViewById(R.id.btnClose);

            btnClose.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    
                    // 释放资源
                    if(bitmap != null){
                        bitmap.recycle();
                    }
                }
            });
            
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                      appendPath(Long.toString(id)).build();
            FileUtil file = new FileUtil();
            ContentResolver resolver = getContentResolver();
            
            // 从Uri中读取图片资源
            try {
                mContent = file.readInputStream(resolver.openInputStream(Uri.parse(uri.toString())));
                bitmap = file.getBitmapFromBytes(mContent, null);
                ivImageShow.setImageBitmap(bitmap);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            dialog.show();
        }
    }*/
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        
        if(bitmap != null){
            bitmap.recycle();
        }
        
    }
    
    /*class MyPhotoAdapter extends SimpleCursorAdapter {

		public MyPhotoAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);
			
		}

		
		@Override
	    public void bindView(View view, Context context, Cursor cursor) {
	        final ViewBinder binder = getViewBinder();
	        final int count = mTo.length;
	        final int[] from = mFrom;
	        final int[] to = mTo;

	        for (int i = 0; i < count; i++) {
	            final View v = view.findViewById(to[i]);
	            if (v != null) {
	                boolean bound = false;
	                if (binder != null) {
	                    bound = binder.setViewValue(v, cursor, from[i]);
	                }

	                if (!bound) {
	                    String text = cursor.getString(from[i]);
	                    if (text == null) {
	                        text = "";
	                    }

	                    if (v instanceof TextView) {
	                        setViewText((TextView) v, text);
	                    } else if (v instanceof ImageView) {
	                        setViewImage((ImageView) v, text);
	                    } else {
	                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
	                                " view that can be bounds by this SimpleCursorAdapter");
	                    }
	                }
	            }
	        }
	    }
    }*/
    
  /*//自定义适配器 
    class MyAdapter extends ResourceCursorAdapter{ 
        public MyAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
		// TODO Auto-generated constructor stub
	}
		//上下文对象 
        private Context context; 
        //图片数组 
        private Integer[] imgs = { 
                R.drawable.pic0, R.drawable.pic1, R.drawable.pic2,  
                R.drawable.pic3, R.drawable.pic4, R.drawable.pic5,                
                R.drawable.pic6, R.drawable.pic7, R.drawable.pic8,  
                R.drawable.pic0, R.drawable.pic1, R.drawable.pic2,  
                R.drawable.pic3, R.drawable.pic4, R.drawable.pic5,                
                R.drawable.pic6, R.drawable.pic7, R.drawable.pic8, 
        }; 
        //MyAdapter(Context context){ 
         //   this.context = context; 
        //} 
        public int getCount() { 
            return imgs.length; 
        } 
 
        public Object getItem(int item) { 
            return item; 
        } 
 
        public long getItemId(int id) { 
            return id; 
        } 
         
        //创建View方法 
        public View getView(int position, View convertView, ViewGroup parent) { 
             ImageView imageView; 
                if (convertView == null) { 
                    imageView = new ImageView(context); 
                    imageView.setLayoutParams(new GridView.LayoutParams(75, 75));//设置ImageView对象布局 
                    imageView.setAdjustViewBounds(false);//设置边界对齐 
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型 
                    imageView.setPadding(8, 8, 8, 8);//设置间距 
                }  
                else { 
                    imageView = (ImageView) convertView; 
                } 
                //imageView.setImageResource(imgs[position]);//为ImageView设置图片资源 
                //imageView.setImageURI(uri);
                return imageView; 
        }
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			
		} 
    }*/
    
    
    
    
    
    /*private class FaceppDetect {
    	DetectCallback callback = null;
    	
    	public void setDetectCallback(DetectCallback detectCallback) { 
    		callback = detectCallback;
    	}

    	public void detect(final Bitmap image) {
    		
    		new Thread(new Runnable() {
				
				public void run() {
					HttpRequests httpRequests = new HttpRequests("4480afa9b8b364e30ba03819f3e9eff5", "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M", true, false);
		    		//Log.v(TAG, "image size : " + img.getWidth() + " " + img.getHeight());
		    		
		    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    		float scale = Math.min(1, Math.min(600f / img.getWidth(), 600f / img.getHeight()));
		    		Matrix matrix = new Matrix();
		    		matrix.postScale(scale, scale);

		    		Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, false);
		    		//Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " " + imgSmall.getHeight());
		    		
		    		imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		    		byte[] array = stream.toByteArray();
		    		
		    		try {
		    			//detect
						JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(array));
						//finished , then call the callback function
						if (callback != null) {
							callback.detectResult(result);
						}
					} catch (FaceppParseException e) {
						e.printStackTrace();
						MyDevicePhotoActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								textView.setText("Network error.");
							}
						});
					}
					
				}
			}).start();
    	}
    }

    interface DetectCallback {
    	void detectResult(JSONObject rst);
	}*/
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}