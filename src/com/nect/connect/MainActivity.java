package com.nect.connect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nect.connect.fragments.TabFragment;

public class MainActivity extends ActionBarActivity 
		 {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
//	SectionsPagerAdapter mSectionsPagerAdapter;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private RelativeLayout mDrawerRelative;
	
	
	
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sliding_drawer_chat);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		
//
//		// Create the adapter that will return a fragment for each of the three
//		// primary sections of the activity.
//		mSectionsPagerAdapter = new SectionsPagerAdapter(
//				getSupportFragmentManager());
//
//		// Set up the ViewPager with the sections adapter.
//		mViewPager = (ViewPager) findViewById(R.id.pager);
//		mViewPager.setAdapter(mSectionsPagerAdapter);
//
//		// When swiping between different sections, select the corresponding
//		// tab. We can also use ActionBar.Tab#select() to do this if we have
//		// a reference to the Tab.
//		mViewPager
//				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//					@Override
//					public void onPageSelected(int position) {
//						actionBar.setSelectedNavigationItem(position);
//					}
//				});
//
//		// For each of the sections in the app, add a tab to the action bar.
//		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
//			// Create a tab with text corresponding to the page title defined by
//			// the adapter. Also specify this Activity object, which implements
//			// the TabListener interface, as the callback (listener) for when
//			// this tab is selected.
//			actionBar.addTab(actionBar.newTab()
//					.setText(mSectionsPagerAdapter.getPageTitle(i))
//					.setTabListener(this));
//		}
//		

		//Setting up the drawer 
		mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerRelative=(RelativeLayout)findViewById(R.id.drawer_relative);
		 if(getSupportActionBar()!=null){
	        	Log.e("actoinna","its there");
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        getSupportActionBar().setHomeButtonEnabled(true);
	        }else
	        	Log.e("actoinna","its null");
		setUpDrawerToggle();
		
		mDrawerList=(ListView)findViewById(R.id.left_drawer_listView);
		
		initializeDrawerList();
		//mDrawerList.setAdapter(contactsAdapter);
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.fragment_container,TabFragment.newInstance(), "CHATS").commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chats, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if(id==R.id.search)
		{
			startActivity(new Intent(this,SeachActivity.class));
			return true;
		}
		if(id==R.id.contacts){
			startActivity(new Intent(this,Contacts.class));
			return true;
		}
		if(id==R.id.upload)
		{
			uploadFile();
		}
		if(mDrawerToggle.onOptionsItemSelected(item))
			return true;
		return super.onOptionsItemSelected(item);
	}

	public void uploadFile()
	{
		File file=new File(Environment.getExternalStorageDirectory(),"Folder");
		Intent intent=new Intent();
		intent.setDataAndType(Uri.fromFile(file),"*/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		// Always show the chooser (if there are multiple options available)
		//startActivityForResult(Intent.createChooser(intent, "Select Picture"), 17);
		new asyncUpload().execute();
	}
	public class asyncUpload extends AsyncTask<Void,Void,Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
			    // Set your file path here
			    FileInputStream fstrm = new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/DCIM/abc.mp3");

			    // Set your server page url (and the file title/description)
			    HttpFileUpload hfu = new HttpFileUpload(getBaseContext().getString(R.string.website)+"/connect/upload_file.php", "abc","mp3 file");

			    hfu.Send_Now(fstrm);

			  } catch (FileNotFoundException e) {
			    // Error: File not found
				  e.printStackTrace();
			  }
			return null;
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	 
	    if (requestCode == 17 && resultCode == RESULT_OK && data != null && data.getData() != null) {
	 
	    	Bundle bundle=data.getExtras();
	    	
	    	
	        Uri uri = data.getData();
	
	        try {
	            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
	             Log.d("d", String.valueOf(bitmap)+"");
	 
	           // ImageView imageView = (ImageView) findViewById(R.id.imageView);
	           // imageView.setImageBitmap(bitmap);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
//	@Override
//	public void onTabSelected(ActionBar.Tab tab,
//			FragmentTransaction fragmentTransaction) {
//		// When the given tab is selected, switch to the corresponding page in
//		// the ViewPager.
//		mViewPager.setCurrentItem(tab.getPosition());
//	}
//
//	@Override
//	public void onTabUnselected(ActionBar.Tab tab,
//			FragmentTransaction fragmentTransaction) {
//	}
//
//	@Override
//	public void onTabReselected(ActionBar.Tab tab,
//			FragmentTransaction fragmentTransaction) {
//	}
//
//	/**
//	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//	 * one of the sections/tabs/pages.
//	 */
//	public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//		public SectionsPagerAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public Fragment getItem(int position) {
//			// getItem is called to instantiate the fragment for the given page.
//			// Return a PlaceholderFragment (defined as a static inner class
//			// below).
//			Log.e("position",position+"");
//			if(position==0)
//			return ConversationFragment.newInstance();
//			else
//			return GroupsFragment.newInstance();
//		}
//
//		@Override
//		public int getCount() {
//			// Show 3 total pages.
//			return 2;
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			Locale l = Locale.getDefault();
//			switch (position) {
//			case 0:
//				return "chats".toUpperCase(l);
//			case 1:
//				return "group".toUpperCase(l);
//			
//			}
//			return null;
//		}
//	}
//
//	
//	

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
		                getSupportActionBar().setTitle("Your Groups");
		            }
		        };
		        mDrawerLayout.setDrawerListener(mDrawerToggle);
		       
		        
		       
		}
		
		
		 public void createGroup(View view)
	     {
			 Intent i = new Intent(this,CreateGroupActivity.class);
			 
	     	startActivity(i);
	     }
		 public void add_contact(View view)
		 {
			 startActivity(new Intent(this,AddContact.class));
		 }
	
}
