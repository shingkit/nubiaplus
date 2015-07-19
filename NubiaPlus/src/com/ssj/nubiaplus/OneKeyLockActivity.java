package com.ssj.nubiaplus;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.os.Bundle;
import android.widget.Toast;

public class OneKeyLockActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			DevicePolicyManager dpm=(DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
			dpm.lockNow();
			finish();
		} catch (Exception e) {
		}
	}
}
