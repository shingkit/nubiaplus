package com.ssj.nubiaplus;
import com.ssj.nubiaplus.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IInterface;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.ssj.nubiaplus.receiver.MyDeviceAdminReceiver;
import com.ssj.nubiaplus.services.SmsReceiceService;
import com.ssj.nubiaplus.utils.SystemInfoUtils;
import com.ssj.nubiaplus.utils.WallpaperSetUtils;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;

public class MainActivity extends PreferenceActivity {

protected static final int ONEKEY_LOCK_REQUEDTCODE = 101;
protected static final int SET_WALLPAPER_REQUEDTCODE = 100;
//	private SettingView sv_smsnotify;
private DevicePolicyManager dpm;
private ComponentName componentName;
	private static boolean running;
	private Editor edit;
	Context context=null;
	private CheckBoxPreference cbp_onekeyLock;
	private boolean mAdminActive;
	private Preference p_create_onekeylock_shortcut;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//加载设置项
		 addPreferencesFromResource(R.xml.setting);
		 //设置管理器相关
		 dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		 componentName=new ComponentName(this,MyDeviceAdminReceiver.class);
		 context=this;
		 //短信dialog提示
//		 CheckBoxPreference cbp_smsnotify=(CheckBoxPreference)findPreference("smsnotify");
//		 cbp_smsnotify.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				boolean ischecked=(boolean) newValue;
//				if(ischecked){//打开服务
//					running = SystemInfoUtils.isServiceRunning(MainActivity.this, "com.ssj.nubiaplus.services.SmsReceiceService");
//					if(!running){
//						Intent intent =new Intent (MainActivity.this,SmsReceiceService.class);
//						startService(intent);
//					}
//				}else{
//					running = SystemInfoUtils.isServiceRunning(MainActivity.this, "com.ssj.nubiaplus.services.SmsReceiceService");
//					if(running){
//						Intent intent =new Intent (MainActivity.this,SmsReceiceService.class);
//						stopService(intent);
//					}
//				}
//				return true;
//			}
//		});
//		if(running){
//			cbp_smsnotify.setChecked(true);
//		}else{
//			cbp_smsnotify.setChecked(false);
//		}
		 //一键锁屏
		 mAdminActive = isActiveAdmin();
		 cbp_onekeyLock = (CheckBoxPreference) findPreference("onekeylock");
		 cbp_onekeyLock.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				boolean isChecked=(boolean) newValue;
				if(isChecked){
					Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"卸载前请关闭此选项");
                    startActivityForResult(intent, ONEKEY_LOCK_REQUEDTCODE);
				}else{
					dpm.removeActiveAdmin(componentName);
				}
				p_create_onekeylock_shortcut.setEnabled(isChecked);
				return true;
			}
		});
		 p_create_onekeylock_shortcut = findPreference("create_onekeylock_shortcut");
		 p_create_onekeylock_shortcut.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				createShortcut();
				return true;
			}
		});
		 p_create_onekeylock_shortcut.setEnabled(cbp_onekeyLock.isChecked());
		 //设置竖屏壁纸
		 Preference p_setwallpaper =findPreference("setwallpaper");
		 p_setwallpaper.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent =new Intent(MainActivity.this,CropActivity.class);
				startActivity(intent);
				
//				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		        intent.setType("image/*");
//		        startActivityForResult(Intent.createChooser(intent, "Select image"),SET_WALLPAPER_REQUEDTCODE);
				return true;
			}
		});
		 Preference p_clearBlur=findPreference("clearblur");
		 p_clearBlur.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				//清除blur特效
				try {
					if(RootTools.isAccessGiven()){
						Log.i("ROOT", "挂载。。。");
						RootTools.sendShell("mount -o remount,rw /data/system/blur/blur_wallpaper", 30000);
						Log.i("ROOT", "删除文件。。。");
						RootTools.sendShell("rm -r /data/system/blur/blur_wallpaper", 30000);
						Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), "没有获得root权限，操作失败！", Toast.LENGTH_SHORT).show();
					}
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RootToolsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		});
	 }
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			
			Log.i("MainActivity", "requestcode"+requestCode+",resultcode"+resultCode);
			switch (requestCode) {
			case SET_WALLPAPER_REQUEDTCODE:
				 if(resultCode!=0){
					 
					 cropAndSetBitmap(data);
				 }
				break;
			 
			}
		super.onActivityResult(requestCode, resultCode, data);
		}
	private void createShortcut() {
		Intent intent =new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		//1.名称2.图标3.意图
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "一键锁屏");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(),R.drawable.lock));
		intent.putExtra("duplicate", false);//不允许存在多个图标
		Intent shortcutintent=new Intent();
		//用隐式意图 Intent——filter
		shortcutintent.setAction("com.ssj.nubiaplus.OneKeyLock");
		shortcutintent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutintent);
		sendBroadcast(intent);
		Toast.makeText(getApplicationContext(), "创建“一键锁屏”快捷方式成功!", Toast.LENGTH_LONG).show();
	}
	private void cropAndSetBitmap(Intent result) {
		String uri=result.getDataString();
		Cursor cursor = getContentResolver().query(Uri.parse(uri), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
		if(cursor.moveToNext()){
			//找到图片资源路径
			String path = cursor.getString(0);
			System.out.println(path);
			Bitmap selectedPic=BitmapFactory.decodeFile(path);
			
			//剪裁壁纸
			//设置壁纸
			WallpaperSetUtils.setWallpaper(context, selectedPic);
		}
	}
	 private boolean isActiveAdmin() {
		    return dpm.isAdminActive(componentName);
		}
	 @Override
		protected void onResume() {
			super.onResume();
			mAdminActive = isActiveAdmin();
			cbp_onekeyLock.setChecked(mAdminActive);
		}
}
//		@Override
//		protected void onCreate(Bundle savedInstanceState) {
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.activity_main);
//			edit=getSharedPreferences("config",MODE_PRIVATE).edit();
//			
//			sv_smsnotify = (SettingView) findViewById(R.id.sv_smsnotify);
//			sv_smsnotify.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Switch sw = (Switch) sv_smsnotify.findViewById(R.id.sw);
//					if(sw.isChecked()){
//						//关闭短信提醒服务，设置sharedpref为false
//						running = SystemInfoUtils.isServiceRunning(MainActivity.this, "com.ssj.nubiaplus.services.SmsReceiceService");
//						if(running){
//							Intent intent =new Intent (MainActivity.this,SmsReceiceService.class);
//							stopService(intent);
//						}
//						sv_smsnotify.setChecked(false);
//						edit.putBoolean("smsNotify", false);
//					}else{
//						running = SystemInfoUtils.isServiceRunning(MainActivity.this, "com.ssj.nubiaplus.services.SmsReceiceService");
//						if(!running){
//							Intent intent =new Intent (MainActivity.this,SmsReceiceService.class);
//							startService(intent);
//						}
//						sv_smsnotify.setChecked(true);
//						edit.putBoolean("smsNotify",true );
//					}
//					edit.commit();
//				}
//			});
//		}
//		@Override
//		protected void onStart() {
//			// TODO Auto-generated method stub
//			super.onStart();
//			running = SystemInfoUtils.isServiceRunning(this, "com.ssj.nubiaplus.services.SmsReceiceService");
//			if(running){
//				sv_smsnotify.setChecked(true);
//			}else{
//				sv_smsnotify.setChecked(false);
//			}
//		}

