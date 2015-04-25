package com.unisofia.fmi.puzzles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView list;
	private Integer[] titles = { R.string.duckpuzzle, R.string.eightpuzzle };
	private Integer[] imageIds = { R.drawable.duck_puzzle,
			R.drawable.eight_puzzle
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_puzzle);
		CustomAdapter adapter = new CustomAdapter(MainActivity.this, titles,
				imageIds);
		list = (ListView) findViewById(R.id.current_games);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					Intent duckPuzzleIntent = new Intent(
							MainActivity.this, DuckPuzzleActivity.class);
					startActivity(duckPuzzleIntent);
				} else {
					Intent eightPuzzleIntent = new Intent(
							MainActivity.this,
							EightPuzzleActivity.class);
					startActivity(eightPuzzleIntent);
				}
			}
		});
	}

}
