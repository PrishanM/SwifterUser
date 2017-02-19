package com.evensel.swyftr.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;

import java.util.List;

public class DetectApplicationFunctionsAvailability {

	private static Context mContext;
    
    public DetectApplicationFunctionsAvailability(Context context){
        setmContext(context);
    }
    
	private static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		DetectApplicationFunctionsAvailability.mContext = mContext;
	}

	/**
	 * Method checks if the devices have network or not
	 */
	public static boolean isConnected(){
        ConnectivityManager conManager = (ConnectivityManager) getmContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		return conManager.getActiveNetworkInfo() != null && conManager.getActiveNetworkInfo().isAvailable() && conManager.getActiveNetworkInfo().isConnected();
    }

	/**
	 * Method checks if the Location services are enabled or not
	 */
	public static boolean isLocationEnabled(Context context) {
		int locationMode = 0;

		try {
			locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

		} catch (Settings.SettingNotFoundException e) {
			e.printStackTrace();
		}

		return locationMode == Settings.Secure.LOCATION_MODE_OFF;


	}

	/**
	 * Method checks if the app is in background or not
	 */
	public static boolean isAppIsInBackground(Context context) {
		boolean isInBackground = true;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
			List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
				if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					for (String activeProcess : processInfo.pkgList) {
						if (activeProcess.equals(context.getPackageName())) {
							isInBackground = false;
						}
					}
				}
			}
		} else {
			List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			if (componentInfo.getPackageName().equals(context.getPackageName())) {
				isInBackground = false;
			}
		}

		return isInBackground;
	}
	
}
