package client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import communicate.PushImport;

import fragment.FriendFragment;
import fragment.HelpFragment;
import fragment.MessageFragment;

public class ControlActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private List<Fragment> fragments;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private static ViewPager mViewPager;
	private PushImport pushimport;
	private final Handler handler = new Handler();
	private static int positiontag=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		fragments=new ArrayList<Fragment>();
		fragments.add(new HelpFragment());
		fragments.add(new MessageFragment());
		fragments.add(new FriendFragment());
		
		pushimport = new PushImport(this);
		pushimport.controlOnCreate(fragments, mViewPager);

	}
	
	@Override
	protected void onResume() {
		pushimport.controlOnResume();
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		positiontag=tab.getPosition();
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item){
		switch(item.getItemId()){
			case R.id.action_settings: 	
				Intent intent=new Intent();
				intent.setClass(ControlActivity.this,SetupActivity.class);
				startActivityForResult(intent,0);
				break;
				
			case R.id.action_tips : 	
				startActivity(new Intent(ControlActivity.this,AssistTipsActivity.class));
				break;
			case R.id.menu_add: 	
				startActivity(new Intent(ControlActivity.this,SearchfriendActivity.class));
				break;
			case R.id.history: 	
				startActivity(new Intent(ControlActivity.this,HistoryActivity.class));
				break;
			case R.id.validationMSG:
				startActivity(new Intent(ControlActivity.this, ValidationActivity.class));
				break;
				
			case R.id.menu_refresh:
				//选中刷新按钮后刷新一秒钟
				Log.i("test",Integer.toString(positiontag));
				if(positiontag==2){
					item.setActionView(R.layout.actionbar_progress);
					FriendFragment f = (FriendFragment) fragments.get(2);
					f.Refresh(item);
				}
						
				break;
				
			default:
				return super.onOptionsItemSelected(item);
		
		}
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	
	@Override
	protected void onPause() {
		pushimport.controlOnPause();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		pushimport.controlOnDestroy();
		super.onDestroy();
	}
	
	public static void SetPager(int position)
	{
		mViewPager.setCurrentItem(position);
	}
	
	public static Drawable base64ToDrawable(String base64Data) {  
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);  
		Drawable drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));  
		return drawable;
	}  
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
		   case 0:
			   finish();
			   break;
		   default:
			   break;
		    }
		}
}
