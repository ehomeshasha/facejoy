package ca.dealsaccess.facejoy;

import ca.dealsaccess.facejoy.common.AppConstants;

import com.example.facejoy.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class UseExistPersonActivity extends ActionBarActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.activity_display_message);

	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    // If your minSdkVersion is 11 or higher, instead use:
	    // getActionBar().setDisplayHomeAsUpEnabled(true);

	    Intent intent = getIntent();
	    String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
	    String checkedListStr = intent.getStringExtra(AppConstants.FACE_CHECKED_LIST);
	    Toast.makeText(this, "Selected Item: " + title+", checkedListStr="+checkedListStr, Toast.LENGTH_SHORT).show();
	    
	    
	    
	    
	}

}
