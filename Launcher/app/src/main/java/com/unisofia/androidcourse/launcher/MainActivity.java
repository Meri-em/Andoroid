package com.unisofia.androidcourse.launcher;

import java.util.List;
import fmi.androidcourse.launcher.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;



public class MainActivity extends Activity {
	
	private final String TAG = "Launcher";
	private PackageManager packageManager;
	private ListView listView;
	private ApplicationsAdapter applicationsAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        packageManager = getPackageManager();
        
        listView = (ListView) findViewById(R.id.current_applications);
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        applicationsAdapter = new ApplicationsAdapter(this, R.layout.list_item_application, applications);
        listView.setAdapter(applicationsAdapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	   public void onItemClick(AdapterView<?> parent, View view,
        	     int position, long id) {
        		   ApplicationInfo app = (ApplicationInfo) applicationsAdapter.getItem(position);
        			try {
        				Intent intent = packageManager
        						.getLaunchIntentForPackage(app.packageName);
        	
        				if (null != intent) {
        					startActivity(intent);
        				}
        			} 
        			catch (ActivityNotFoundException e) {
        				Log.e(TAG, "The activity is not found");
        			} catch (Exception e ) {
        				Log.e(TAG, e.getMessage());
        			}
        			
        	   }
        	  });
        
        listView.setTextFilterEnabled(true);
       
        EditText filter = (EditText) findViewById(R.id.filter);
        filter.addTextChangedListener(new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
        	
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
       
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        	applicationsAdapter.getFilter().filter(s.toString());
        }

		
        });
        
       }

   
}
