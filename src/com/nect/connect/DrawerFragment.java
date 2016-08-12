package com.nect.connect;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerFragment extends Fragment{

	
	 private ImageView imageView;

	public DrawerFragment() {
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	    	
	        View view = inflater.inflate(R.layout.fragment_belowdrawer, null);
	        
	        
	        getActivity();
			SharedPreferences pref=getActivity().getSharedPreferences(
					"Logindata", Context.MODE_PRIVATE);
	    	    	
	       TextView username=(TextView) view.findViewById(R.id.userName);
	       TextView phone=(TextView) view.findViewById(R.id.userPhone);
	       username.setText(pref.getString("name","Empty"));
	       phone.setText(pref.getString("phone",null));
	        return view;
	    }
}
