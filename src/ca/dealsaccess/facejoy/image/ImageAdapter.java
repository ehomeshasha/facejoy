package ca.dealsaccess.facejoy.image;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.example.facejoy.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Activity uiActivity;
	private ArrayList<String> paths;
	private int size;
	private ImageShowManager imageManager;
	private LayoutInflater li;
	private int width;
	private int height;
	private int layoutResource;

	public ImageAdapter(Activity a, ArrayList<String> paths, int layoutResource) {
		this(a, paths, 100, 100, layoutResource);
	}
	
	public ImageAdapter(Activity a, ArrayList<String> paths, int layoutResource, int width, int height) {
		this.uiActivity = a;
		this.paths = paths;
		this.width = width;
		this.height = height;
		this.layoutResource = layoutResource;
		size = paths.size();
		imageManager = ImageShowManager.from(uiActivity);
		li = LayoutInflater.from(uiActivity);
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int position) {
		return paths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		SurfaceHolder surfaceHolder = new SurfaceHolder();
		if (convertView != null) {
			surfaceHolder = (SurfaceHolder) convertView.getTag();
		} else {
			convertView = li.inflate(layoutResource, null);
			surfaceHolder.iv = (ImageView) convertView
					.findViewById(R.id.image_body);
		}
		convertView.setTag(surfaceHolder);

		String path = paths.get(position);
		// 首先检测是否已经有线程在加载同样的资源（如果则取消较早的），避免出现重复加载
		if (cancelPotentialLoad(path, surfaceHolder.iv)) {
			AsyncLoadImageTask task = new AsyncLoadImageTask(surfaceHolder.iv);
			surfaceHolder.iv.setImageDrawable(new LoadingDrawable(task));
			task.execute(path);
		}
		return convertView;
	}

	static class SurfaceHolder {
		ImageView iv;
	}

	/**
	 * 判断当前的imageview是否在加载相同的资源
	 * 
	 * @param url
	 * @param imageview
	 * @return
	 */
	private boolean cancelPotentialLoad(String url, ImageView imageview) {

		AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
		if (loadImageTask != null) {
			String bitmapUrl = loadImageTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				loadImageTask.cancel(true);
			} else {
				// 相同的url已经在加载中.
				return false;
			}
		}
		return true;
	}

	/**
	 * 负责加载图片的异步线程
	 * 
	 * @author Administrator
	 * 
	 */
	class AsyncLoadImageTask extends AsyncTask<String, Void, Bitmap> {

		private final WeakReference<ImageView> imageViewReference;
		private String url = null;

		public AsyncLoadImageTask(ImageView imageview) {
			super();
			imageViewReference = new WeakReference<ImageView>(imageview);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			/**
			 * 具体的获取bitmap的部分，流程： 从内存缓冲区获取，如果没有向硬盘缓冲区获取，如果没有从sd卡/网络获取
			 */

			Bitmap bitmap = null;
			this.url = params[0];

//			// 从内存缓存区域读取
//			bitmap = imageManager.getBitmapFromMemory(url);
//			if (bitmap != null) {
//				//Log.d("dqq", "return by 内存");
//				return bitmap;
//			}
//			// 从硬盘缓存区域中读取
//			bitmap = imageManager.getBitmapFormDisk(url);
//			if (bitmap != null) {
//				imageManager.putBitmapToMemery(url, bitmap);
//				//Log.d("dqq", "return by 硬盘");
//				return bitmap;
//			}
			
			// 没有缓存则从原始位置读取
			bitmap = BitmapUtilities.getBitmapThumbnail(url,
					width,
					height);
			imageManager.putBitmapToMemery(url, bitmap);
			imageManager.putBitmapToDisk(url, bitmap);
			//Log.d("dqq", "return by 原始读取");
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap resultBitmap) {
			if (isCancelled()) {
				// 被取消了
				resultBitmap = null;
			}
			if (imageViewReference != null) {
				ImageView imageview = imageViewReference.get();
				AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
				if (this == loadImageTask) {
					imageview.setImageDrawable(null);
					imageview.setImageBitmap(resultBitmap);
				}

			}

			super.onPostExecute(resultBitmap);
		}
	}

	/**
	 * 根据imageview，获得正在为此imageview异步加载数据的函数
	 * 
	 * @param imageview
	 * @return
	 */
	private AsyncLoadImageTask getAsyncLoadImageTask(ImageView imageview) {
		if (imageview != null) {
			Drawable drawable = imageview.getDrawable();
			if (drawable instanceof LoadingDrawable) {
				LoadingDrawable loadedDrawable = (LoadingDrawable) drawable;
				return loadedDrawable.getLoadImageTask();
			}
		}
		return null;
	}

	/**
	 * 记录imageview对应的加载任务，并且设置默认的drawable
	 * 
	 * @author Administrator
	 * 
	 */
	public static class LoadingDrawable extends ColorDrawable {
		// 引用与drawable相关联的的加载线程
		private final WeakReference<AsyncLoadImageTask> loadImageTaskReference;

		public LoadingDrawable(AsyncLoadImageTask loadImageTask) {
			super(Color.WHITE);
			loadImageTaskReference = new WeakReference<AsyncLoadImageTask>(
					loadImageTask);
		}

		public AsyncLoadImageTask getLoadImageTask() {
			return loadImageTaskReference.get();
		}
	}

}
