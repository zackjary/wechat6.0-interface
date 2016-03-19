package com.coco.wechat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabFragment extends Fragment {
	public static final String TITLE = "title";
	private String mTitle = "title";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			mTitle = arguments.getString(TITLE);
		}
		TextView tv = new TextView(getActivity());
		tv.setText(mTitle);
		tv.setTextColor(Color.parseColor("#ff6200"));
		tv.setTextSize(20);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

}
