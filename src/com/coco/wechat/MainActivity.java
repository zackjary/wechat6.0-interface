package com.coco.wechat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private ViewPager mViewPager;
	private String[] mTitles;
	private List<TabFragment> mTabs;
	private FragmentPagerAdapter mAdapter;
	private List<ChangeColorIconWithText> mTabIndicators;
	private ChangeColorIconWithText mCurrSelected;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置actionbar不显示图标
		getActionBar().setDisplayShowHomeEnabled(false);
		setOverFlowButtonAlways();
		setContentView(R.layout.activity_main);

		initView();
		initDatas();

		mViewPager.setAdapter(mAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
						if (positionOffset > 0) {
							ChangeColorIconWithText left = mTabIndicators
									.get(position);
							ChangeColorIconWithText right = mTabIndicators
									.get(position + 1);
							left.setAlpha(1 - positionOffset);
							right.setAlpha(positionOffset);
						}

					}

					@Override
					public void onPageScrollStateChanged(int state) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
	}

	private void initDatas() {
		mTitles = new String[] { "微信", "通讯录", "发现", "我" };
		mTabs = new ArrayList<TabFragment>();
		for (String title : mTitles) {
			TabFragment frag = new TabFragment();
			Bundle args = new Bundle();
			args.putString(TabFragment.TITLE, title);
			frag.setArguments(args);
			mTabs.add(frag);
		}

		mTabIndicators = new ArrayList<ChangeColorIconWithText>();
		mTabIndicators
				.add((ChangeColorIconWithText) findViewById(R.id.id_indicator_one));
		mTabIndicators
				.add((ChangeColorIconWithText) findViewById(R.id.id_indicator_two));
		mTabIndicators
				.add((ChangeColorIconWithText) findViewById(R.id.id_indicator_three));
		mTabIndicators
				.add((ChangeColorIconWithText) findViewById(R.id.id_indicator_four));

		for (ChangeColorIconWithText tab : mTabIndicators) {
			tab.setOnClickListener(this);
		}

		mCurrSelected = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mCurrSelected.setAlpha(1.0f);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mTabs.get(arg0);
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_search) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 设置Actionbar的OverFlowButton一直显示
	 */
	private void setOverFlowButtonAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// 设置menu显示Icon
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method method = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					method.setAccessible(true);
					method.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public void onClick(View v) {
		if (mCurrSelected.getId() == v.getId()) {
			return;
		}
		mCurrSelected.setAlpha(0);
		switch (v.getId()) {
		case R.id.id_indicator_one:
			mViewPager.setCurrentItem(0, false);
			mCurrSelected = (ChangeColorIconWithText) v;
			mCurrSelected.setAlpha(1.0f);
			break;

		case R.id.id_indicator_two:
			mViewPager.setCurrentItem(1, false);
			mCurrSelected = (ChangeColorIconWithText) v;
			mCurrSelected.setAlpha(1.0f);
			break;

		case R.id.id_indicator_three:
			mViewPager.setCurrentItem(2, false);
			mCurrSelected = (ChangeColorIconWithText) v;
			mCurrSelected.setAlpha(1.0f);
			break;

		case R.id.id_indicator_four:
			mViewPager.setCurrentItem(3, false);
			mCurrSelected = (ChangeColorIconWithText) v;
			mCurrSelected.setAlpha(1.0f);
			break;
		}

	}
}
