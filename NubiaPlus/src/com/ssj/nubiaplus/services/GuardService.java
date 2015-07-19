package com.ssj.nubiaplus.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class GuardService extends Service {
private SharedPreferences sp;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		sp=getSharedPreferences("config", MODE_PRIVATE);
		if(sp.getBoolean("smsnotify", false)){
			Intent intent=new Intent(this,SmsReceiceService.class);
			startService(intent);
		}
		stopSelf();
	}
	
}
