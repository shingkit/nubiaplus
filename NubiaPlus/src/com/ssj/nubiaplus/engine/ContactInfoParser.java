package com.ssj.nubiaplus.engine;

import java.util.ArrayList;
import java.util.List;

import com.ssj.nubiaplus.domain.ContactInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public class ContactInfoParser {
	/**
	 * 获取联系人名字
	 * 
	 * @param context
	 * @return
	 */
	public static String  getName(Context context,String number) {
		ContentResolver resolver = context.getContentResolver();
		// 1. 查询raw_contacts表，把联系人的id取出来
		Uri datauri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(datauri, new String[] { "raw_contact_id" },
				"data1=?", new String[]{number}, null);
		if(cursor.moveToNext()){
			//存在联系人
			String  raw_contact_id = cursor.getString(0);
			Log.i("smsparser:raw_contact_id",raw_contact_id );
			Cursor cursor2 = resolver.query(datauri,
					new String[] { "data1","mimetype" }, "raw_contact_id=?",
					new String[] { raw_contact_id }, null);
			while (cursor2.moveToNext()) {
				String data1 = cursor2.getString(0);
				String mimetype = cursor2.getString(1);
				if ("vnd.android.cursor.item/name".equals(mimetype)) {
					String name=data1;
					cursor2.close();
					Log.i("smsparser", name);
					return name;
				} 
			}
			
		}
		cursor.close();
		 return null;
	}
}
