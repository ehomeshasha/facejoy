package ca.dealsaccess.facejoy;



import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore.Images.ImageColumns;

public class FaceMainActivity extends ActionBarActivity {

	private static final String TAG = "FaceMainActivity";

	final private int PICTURE_CHOOSE = 1;
	
	//private ImageView imageView = null;
	//private Bitmap img = null;
	//private Button detectButton = null;
	//private TextView textView = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face_main_activity);
		

	
        //if (savedInstanceState == null) {
		//	getSupportFragmentManager().beginTransaction()
		//			.add(R.id.container, new PlaceholderFragment()).commit();
		//}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.face_main_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    // Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.album_gallery:
				openAlbum();
				return true;
	        case R.id.search:
	            openSearch();
	            return true;
	        case R.id.settings:
	            openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		//Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();  
        //return true; 
	}
	

	


	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_face_main, container,
					false);
			return rootView;
		}
	}*/
	
	
	private void openAlbum() {
		//open album page
		//get a picture form your phone
		//Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //photoPickerIntent.setType("image/*");
        //startActivityForResult(photoPickerIntent, PICTURE_CHOOSE);
		Intent intent = new Intent(this, GridPhotoActivity.class);
		intent.putExtra(AppConstants.EXTRA_MESSAGE, "openAlbumActivity");
		startActivity(intent);
	}
	
	public void openSearch() {
		//open search page
		Intent intent = new Intent(this, MyDevicePhotoActivity.class);
		intent.putExtra(AppConstants.EXTRA_MESSAGE, "openSearchActivity");
		startActivity(intent);
	}
	
	public void openSettings() {
		//open setting page
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		intent.putExtra(AppConstants.EXTRA_MESSAGE, "openSettingsActivity");
		startActivity(intent);
	}
	
	

}
