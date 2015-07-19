package com.ssj.nubiaplus;

import com.edmodo.cropper.CropImageView;
import com.ssj.nubiaplus.utils.WallpaperSetUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

public class CropActivity extends Activity {
	private static final int SET_WALLPAPER_REQUEDTCODE = 100;
	CropImageView civ;
	private Bitmap selectedPic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);
		civ=(CropImageView) findViewById(R.id.cropImageView);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100&&resultCode!=0){
			String uri=data.getDataString();
			Cursor cursor = getContentResolver().query(Uri.parse(uri), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
			if(cursor.moveToNext()){
				//找到图片资源路径
				String path = cursor.getString(0);
				System.out.println(path);
				selectedPic = BitmapFactory.decodeFile(path);
				civ.setImageBitmap(selectedPic);
			}
		}
	}
	public void cropPic(View v){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select image"),SET_WALLPAPER_REQUEDTCODE);
	}
	public void setWallPaper(View v){
		try {
			Bitmap croppedImage = civ.getCroppedImage();
			if(selectedPic!=null&&croppedImage!=null)
				WallpaperSetUtils.setWallpaper(this, croppedImage);
		} catch (Exception e) {
			Toast.makeText(this, "请先选择图片！", Toast.LENGTH_SHORT).show();
		}
	}
}
