package com.nect.connect.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nect.connect.R;

public class DialogViewAdapter extends ArrayAdapter<dialogview_rows>{

	private ArrayList<dialogview_rows> rows;
	private Context context;
	private int resource;


	public DialogViewAdapter(Context context, int resource, ArrayList<dialogview_rows> rows) {
		super(context, resource, rows);
		this.rows=rows;
		this.context=context;
		this.resource=resource;
		
		// TODO Auto-generated constructor stub
	}
	
	
	

	@Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
		View view=convertView;
		
		if(view==null)
		{
			LayoutInflater inflater;
			inflater=LayoutInflater.from(context);
		 view= inflater.inflate(resource,null);
		}
		dialogview_rows row = rows.get(position);
		if(row!=null)
		{
			ImageView im=(ImageView) view.findViewById(R.id.imageView1);
			TextView tv=(TextView) view.findViewById(R.id.textView1);
			tv.setText(row.getName()+"("+row.getPhone()+")");
			if(row.getBitmap()!=null)
				im.setImageBitmap(row.getBitmap());
		}
		
	return view;
		
  }
}
