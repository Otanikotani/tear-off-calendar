/**
 * 
 */
package com.tearoffcalendar.app;

import com.tearoffcalendar.themes.BasicThemeManager;
import com.tearoffcalendar.themes.ResourcesThemeManager;
import com.tearoffcalendar.themes.ThemeException;

import android.app.Application;
import android.util.Log;

/**
 * @author Servy
 *
 */
public class TearOffApp extends Application {

	private static TearOffApp singleton;
	private static final String TAG = "TearOffApp";

	private BasicThemeManager manager;
	
	public static TearOffApp getInstance() {
		return singleton;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		try {
			manager = new ResourcesThemeManager(getApplicationContext());
		} catch (ThemeException e) {
			Log.e(TAG, "Error initializing theme manager", e);
		}
		if (manager != null)
			Log.v(TAG, "Theme manager was succesfully initialized");
	}
	/**
	 * @return the theme manager
	 */
	public BasicThemeManager getThemeManager() {
		return manager;
	}	
	
}
