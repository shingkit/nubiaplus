package com.ssj.nubiaplus.receiver;

import com.ssj.nubiaplus.services.SmsReceiceService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.sax.StartElementListener;

public class BootReceiver extends BroadcastReceiver {
private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp=context.getSharedPreferences("config",context.MODE_PRIVATE);
		
		//ø™∆Ù∂Ã–≈Ã·–—
//		if(sp.getBoolean("smsnotify", false)){
//			Intent service=new Intent (context,SmsReceiceService.class);
//			context.startService(service);
//		}
		
	}

}
