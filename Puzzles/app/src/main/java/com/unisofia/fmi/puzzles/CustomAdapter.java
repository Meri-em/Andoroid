package com.unisofia.fmi.puzzles;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Integer>{
	private Activity context;
	private Integer[] titles;
	private Integer[] imageIds;

	public CustomAdapter(Activity context, Integer[] titles, Integer[] imageIds) {
		super(context, R.layout.puzzle_item, titles);
		this.context = context;
		this.titles = titles;
		this.imageIds = imageIds;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        //convertView is always null, because listView contains 2 rows, can not reuse a row
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.puzzle_item, null, true);

		TextView titleTextView = (TextView) rowView.findViewById(R.id.puzzle_name);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.puzzle_img);

        titleTextView.setText(titles[position]);
		imageView.setImageResource(imageIds[position]);
		return rowView;
	}
}