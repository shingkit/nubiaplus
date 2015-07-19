package com.ssj.nubiaplus.utils;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Debug;

public class SmsUpdateUtils {

	/**
	 * 标记短信为 已读
	 * @param context 上下文
	 * @param number 电话号码
	 */
	public static void signRead(Context context,String number){
		ContentValues values=new ContentValues();
		values.put("seen",true);
		context.getContentResolver().update(Uri.parse("content://sms/"), values, "address=?", new String[]{number});
	}
	
	 
	
}
