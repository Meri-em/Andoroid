package com.unisofia.androidcourse.launcher;

import java.util.ArrayList;
import java.util.List;


import fmi.androidcourse.launcher.R;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.pm.ApplicationInfo;


public class ApplicationsAdapter extends ArrayAdapter<ApplicationInfo> implements Filterable{
	private List<ApplicationInfo> filteredApplications;
	private List<ApplicationInfo> applications;
	private Context context;
	private PackageManager packageManager;
	private ApplicationFilter filter;

	

	public ApplicationsAdapter(Context context, int resourceId,
			List<ApplicationInfo> filtered) {
		super(context, resourceId, filtered);
		this.context = context;
		this.filteredApplications = new ArrayList<ApplicationInfo>();
		this.filteredApplications.addAll(filtered);
		this.applications = new ArrayList<ApplicationInfo>();
		this.applications.addAll(filtered);
		packageManager = context.getPackageManager();
	}


	@Override
	public ApplicationInfo getItem(int position) {
		return filteredApplications.get(position);
	}

	
	@Override
	  public Filter getFilter() {
	   if (filter == null){
	    filter  = new ApplicationFilter();
	   }
	   return filter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		ApplicationInfo data = filteredApplications.get(position);
		packageManager = context.getPackageManager();
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.list_item_application, parent,
					false);

			holder = new ViewHolder();
			holder.iconImageView = (ImageView) convertView
					.findViewById(R.id.app_icon);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.app_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.nameTextView.setText(data.loadLabel(packageManager));
		holder.iconImageView.setImageDrawable(data.loadIcon(packageManager));
		
		
		return convertView;
	}

	static class ViewHolder {
		ImageView iconImageView;
		TextView nameTextView;
	}
	
	private class ApplicationFilter extends Filter {
	  
	 
	   @Override
	   protected FilterResults performFiltering(CharSequence constraint) {
	 
	    constraint = constraint.toString().toLowerCase();
	    FilterResults result = new FilterResults();
	    if(constraint != null && constraint.toString().length() > 0)
	    {
	    ArrayList<ApplicationInfo> filteredItems = new ArrayList<ApplicationInfo>();
	 
	    for(int i = 0; i < applications.size(); i++)
	    {
	     ApplicationInfo currentApplication = applications.get(i);
	     if(currentApplication.toString().toLowerCase().contains(constraint))
	      filteredItems.add(currentApplication);
	    }
	    result.count = filteredItems.size();
	    result.values = filteredItems;
	    }
	    else {
	      result.values = applications;
	      result.count = applications.size();
	    }
	    return result;
	   }
	 
	   @SuppressWarnings("unchecked")
       @Override
       protected void publishResults(CharSequence constraint, FilterResults results) {
           filteredApplications = (ArrayList<ApplicationInfo>) results.values;
           notifyDataSetChanged();
           clear();
           for(int i = 0; i < filteredApplications.size(); i++){
               add(filteredApplications.get(i));
           }
           notifyDataSetInvalidated();
       }
	  }
	 
	

	 
	 
	 

	
	  
}

