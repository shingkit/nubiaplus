package com.ssj.nubiaplus.utils;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Debug;

public class SmsUpdateUtils {

	/**
	 * ��Ƕ���Ϊ �Ѷ�
	 * @param context ������
	 * @param number �绰����
	 */
	public static void signRead(Context context,String number){
		ContentValues values=new ContentValues();
		values.put("seen",true);
		context.getContentResolver().update(Uri.parse("content://sms/"), values, "address=?", new String[]{number});
	}
	
	 
	
}
