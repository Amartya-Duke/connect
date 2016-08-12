package com.nect.connect;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class GroupSlidingDrawer extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private AbsListView mDrawerList;
	private RelativeLayout mDrawerRelative;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_sliding_drawer);
		
		
		//Setting up the drawer 
				mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
				mDrawerRelative=(RelativeLayout)findViewById(R.id.drawer_relative);
				
				setUpDrawerToggle();
				
				mDrawerList=(ListView)findViewById(R.id.left_drawer_listView);
				
				initializeDrawerList();
				//mDrawerList.setAdapter(contactsAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_sliding_drawer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }

	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }

	    private void initializeDrawerList() {
			// Insert the fragment by replacing any existing fragment at the end of drawer
			Fragment fragment = new DrawerFragment();
		    FragmentManager fragmentManager = getSupportFragmentManager();
		    fragmentManager.beginTransaction()
		                   .replace(R.id.frame_below_drawer, fragment)
		                   .commit();

			mDrawerList.setOnItemClickListener(new drawerListItemClickListener());
		}
		
		
		
		private class drawerListItemClickListener implements ListView.OnItemClickListener {
		    @Override
		    public void onItemClick(AdapterView parent, View view, int position, long id) {
		        selectItem(position);
		    }
		}

		/** Swaps fragments in the main content view */
		private void selectItem(int position) {
		    

		    mDrawerList.setItemChecked(position, true);
		    setTitle(getConversationName(position));
		    mDrawerLayout.closeDrawer(mDrawerRelative );
		}
	 
		private CharSequence getConversationName(int position) {
			// TODO Auto-generated method stub
			return null;
		}
	 


		@Override
		public void setTitle(CharSequence title) {
		    CharSequence mTitle = title;
		    if( getSupportActionBar()!=null)
		    getSupportActionBar().setTitle(mTitle);
		}

		
		
		public void setUpDrawerToggle()
		{
			 mDrawerToggle = new ActionBarDrawerToggle(
		                this,                  /* host Activity */
		                mDrawerLayout,         /* DrawerLayout object */
		                  /* nav drawer icon to replace 'Up' caret */
		                R.string.navigation_drawer_open,  /* "open drawer" description */
		                R.string.navigation_drawer_close  /* "close drawer" description */
		                ) {

		            /** Called when a drawer has settled in a completely closed state. */
		            public void onDrawerClosed(View view) {
		                super.onDrawerClosed(view);
		                if(getSupportActionBar()!=null)
		                getSupportActionBar().setTitle("Connect");
		            }

		            /** Called when a drawer has settled in a completely open state. */
		            public void onDrawerOpened(View drawerView) {
		                super.onDrawerOpened(drawerView);
		                if(getSupportActionBar()!=null)
		                getSupportActionBar().setTitle("Participants");
		            }
		        };
		        mDrawerLayout.setDrawerListener(mDrawerToggle);
		       
		        if(getSupportActionBar()!=null){
		        	Log.e("actoinna","its there");
		        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		        getSupportActionBar().setHomeButtonEnabled(true);
		        }else
		        	Log.e("actoinna","its null");
		       
		}
		
		
}
