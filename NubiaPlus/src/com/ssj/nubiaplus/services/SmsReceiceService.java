package com.ssj.nubiaplus.services;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssj.nubiaplus.R;
import com.ssj.nubiaplus.engine.ContactInfoParser;

public class SmsReceiceService extends Service {
	
	private TextView tv_sms_content;
	private TextView tv_phonenumber;
	private TextView tv_name;
	private AlertDialog dialog;
	private EditText et_msg_content;
	
	private InnearReceiver innearReceiver;
	private String originatingAddress; 
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	public void onCreate() {
		Toast.makeText(getApplicationContext(), "短信提醒服务开启", Toast.LENGTH_SHORT).show();
		innearReceiver=new InnearReceiver();
		IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(1);
		registerReceiver(innearReceiver, filter);
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(getApplicationContext(), "短信提醒服务关闭", Toast.LENGTH_SHORT).show();
		unregisterReceiver(innearReceiver);
		innearReceiver=null;
		super.onDestroy();
		SystemClock.sleep(5000);
		Intent guardService=new Intent (this,GuardService.class);
		startService(guardService);
	}
	/**
	 * 设置对话框textview的显示
	 * @param context
	 * @param intent
	 */
	private void initSms(Intent intent ) {
		Object[]objs=(Object[]) intent.getExtras().get("pdus");
		for (Object object : objs) {
			SmsMessage sm=SmsMessage.createFromPdu((byte[]) object);
			originatingAddress = sm.getOriginatingAddress();
			//消息体
			tv_sms_content.setText(sm.getMessageBody());
			//电话号码
			tv_phonenumber.setText(originatingAddress);
			//联系人名字
			 String name = ContactInfoParser.getName(this, originatingAddress);
			 //设置名字
			 if(!TextUtils.isEmpty(name)){
				 tv_name.setText(name);
			 }else{
				 tv_name.setText("未知号码");
			 }
		}
	}
	
	private class InnearReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(final Context context,final Intent intent) {
			Log.i("innear", "收到短信了！");
			//检测屏幕是否开启
			PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
			//如果屏幕开启且已打开短信提醒
			if(pm.isScreenOn()){
				//显示对话框
				AlertDialog.Builder builder=new Builder(getApplicationContext());
				
				View view=View.inflate(getApplicationContext(), R.layout.dialog_smsnotify_noroot, null);
				
				tv_sms_content = (TextView) view.findViewById(R.id.tv_sms_content);
				tv_phonenumber = (TextView) view.findViewById(R.id.tv_phonenumber);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				et_msg_content = (EditText) view.findViewById(R.id.et_msg_content);
				
				initSms(intent);
				//取消按钮
				view.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				//进入短信界面查看
				view.findViewById(R.id.btn_gotoSms).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent=new Intent("android.intent.action.SENDTO");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setData(Uri.parse("smsto:"+originatingAddress));
						startActivity(intent);
						dialog.dismiss();
					}
				});
//				//标记已读
//				view.findViewById(R.id.btn_signread).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						//获取默认短信app
////						String defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(context);
////						//修改自己为默认短信app
////						Intent intent = new Intent(Sms.Intents.ACTION_CHANGE_DEFAULT);  
////						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////						intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName()	);  
////						startActivity(intent);  
//						
//						//设置已读
//						SmsUpdateUtils.signRead(context,originatingAddress);
//						abortBroadcast();
//						//恢复默认短信app
////						intent = new Intent( Sms.Intents.ACTION_CHANGE_DEFAULT);  
////						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////						intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsPackage);  
////						startActivity(intent);
//						dialog.dismiss();
//					}
//				});
//				//发送
//				view.findViewById(R.id.btn_send).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						  String content = et_msg_content.getText().toString();
//						  SmsManager.getDefault().sendTextMessage(tv_phonenumber.toString(),null, content, null, null);
//						  dialog.dismiss();
//					}
//				});
				builder.setView(view);
				dialog=builder.create();
				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				dialog.show();
			}

		}
		
	}

}
