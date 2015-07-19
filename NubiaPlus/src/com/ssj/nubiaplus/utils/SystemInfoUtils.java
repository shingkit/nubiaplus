package com.ssj.nubiaplus.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class SystemInfoUtils {
		public static boolean isServiceRunning(Context context,String clazz){
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningServiceInfo> runningServices = am.getRunningServices(30);
			for (RunningServiceInfo runningServiceInfo : runningServices) {
				String className = runningServiceInfo.service.getClassName();
				if(clazz.equals(className)){
					return true;
				}
			}
			return false;
		}

}
