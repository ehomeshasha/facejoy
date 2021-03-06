package ca.dealsaccess.deprecated;

import ca.dealsaccess.facejoy.common.AppConstants;

import com.example.facejoy.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AlbumActivity extends ActionBarActivity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);

		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		Intent intent = getIntent();
	    String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
	    Toast.makeText(this, "Selected Item: " + title, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.album, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();
		//if (id == R.id.action_settings) {
		//	return true;
		//}
		return super.onOptionsItemSelected(item);
	}

	
}
