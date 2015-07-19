package com.ssj.nubiaplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ssj.nubiaplus.R;

public class SettingView extends RelativeLayout {

	private TextView tv_title;
	private TextView tv_desc;
	private Switch sw;

	private String []descs;
	public SettingView(Context context) {
		super(context);
		init(context);
	}
	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.ssj.nubiaplus", "Title");
		String desc = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.ssj.nubiaplus", "Desc");
		descs = desc.split("#");
		setDesc(descs,true);
		setTitle(title);
	}

	private void setTitle(String title) {
		tv_title.setText(title);
	}
	public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		View.inflate(context, R.layout.ui_settingview, this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_desc = (TextView)findViewById(R.id.tv_desc);
		sw = (Switch) findViewById(R.id.sw);
	}

	public boolean isChecked(){
		return sw.isChecked();
	}

	public void setChecked(boolean checked){
		sw.setChecked(checked);
		if(checked){
			tv_desc.setText(descs[0]);
		}else{
			tv_desc.setText(descs[1]);
			
		}
	}
	private void setDesc(String[] descs,boolean checked){
		this.descs=descs;
		if(checked){
			tv_desc.setText(descs[0]);
		}else{
			tv_desc.setText(descs[1]);
		}
	}
}
