package com.example.weatherappdemo.utils;

import android.app.Activity;
import android.app.Application;


public class AppContext {
	private static AppContext INSTANCE = null;
	private Application mApplication;
	private Activity mActivity;
	private String showCompleteProfile;
	
	public static AppContext getInstance (){
		
		if(INSTANCE == null){
			INSTANCE = new AppContext();
		}
		return INSTANCE;
	}
	
	public void setContext(Application application){
		this.mApplication = application;
	}
	
	public Application getContext(){
		return this.mApplication;
	}

	public void setActivity(Activity activity){
		this.mActivity = activity;
	}

	public Activity getActivity(){
		return this.mActivity;
	}
	public String getShowCompleteProfile() {
		return this.showCompleteProfile;
	}

	public void setShowCompleteProfile(String showCompleteProfile) {
		this.showCompleteProfile = showCompleteProfile;
	}

}
