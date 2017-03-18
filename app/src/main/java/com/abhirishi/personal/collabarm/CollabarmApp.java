package com.abhirishi.personal.collabarm;

import android.app.Application;
import com.facebook.stetho.Stetho;


public class CollabarmApp extends Application {

	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);
	}

}
