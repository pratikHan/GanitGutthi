package com.example.kluggame2;

import android.app.Activity;
import android.app.Fragment;

public interface FragmentCallback {

	//public void fragmentReplace(Fragment fragment);

	public void fragmentReplace(int code);

	

	public void fragmentReplace(int code, String args);
	
};
