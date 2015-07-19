package com.ssj.nubiaplus.utils;

import java.io.IOException;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.widget.Toast;

public class WallpaperSetUtils {
	public static void setWallpaper(Context context,Bitmap selectedPic){
		//拉伸到1080p
		boolean filter=true;
		selectedPic=Bitmap.createScaledBitmap(selectedPic, 720, 1280, filter);
		//获取选择的壁纸宽高
		int selectedPicHeight = selectedPic.getHeight();
		int selectedPicWidth = selectedPic.getWidth();
		System.out.println(selectedPicHeight+","+selectedPicWidth);
		//1080P bitmap 在上面花出selectedPic
		Bitmap bitmap=Bitmap.createBitmap(1280, 1280, Config.ARGB_8888);
//		int[] pix = new int[bitmap.getHeight() * bitmap.getWidth()];  
//		for (int y = 0; y < bitmap.getHeight(); y++)  
//	          for (int x = 0; x < bitmap.getWidth(); x++)  
//	              {  
//	              int index = y * bitmap.getWidth() + x;  
//	              int r = ((pix[index] >> 16) & 0xff)|0xff;  
//	              int g = ((pix[index] >> 8) & 0xff)|0xff;  
//	              int b =( pix[index] & 0xff)|0xff;  
//	             pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;  
//	            //  pix[index] = 0xff000000;  
//	                
//	              }  
//		bitmap.setPixels(pix, 
//				0, 
//				bitmap.getWidth(),
//				0, 0, 
//				bitmap.getWidth(), bitmap.getHeight());
		Paint paint=new Paint();
		Canvas canvas=new Canvas(bitmap);
		canvas.drawBitmap(selectedPic, 280, 0, paint);
		
		try {
			WallpaperManager.getInstance(context).setBitmap(bitmap);
			Toast.makeText(context, "设置成功!", Toast.LENGTH_SHORT).show();;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
