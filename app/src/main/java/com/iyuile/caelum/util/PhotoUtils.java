package com.iyuile.caelum.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * 
 * @Description 照片选取
 */
public class PhotoUtils {
	public static final int PHOTO_PICKED_WITH_DATA = 0x00001101;// 选择图片窗口
	public static final int CAMERA_WITH_DATA = 0x00001102;// 照相机
	
	private static File mCurrentPhotoFile;

	public static File getmCurrentPhotoFile() {
		return mCurrentPhotoFile;
	}

	public static void setmCurrentPhotoFile(File mCurrentPhotoFile) {
		PhotoUtils.mCurrentPhotoFile = mCurrentPhotoFile;
	}

	/**
	 * 获取截图的Intent
	 * 
	 * @param f
	 *            图片路径
	 * @param tempFile
	 *            缓存路径
	 * @param outX
	 *            切割宽度
	 * @param outY
	 *            切割高度
	 * @return
	 */
	public static Intent doCropPhoto(File f, File tempFile, int aspectX, int aspectY, int outX, int outY) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(f), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
			intent.putExtra("outputX", outX);
			intent.putExtra("outputY", outY);
			if (!tempFile.exists()) {
				intent.putExtra("return-data", true);
			}
			intent.putExtra("output", Uri.fromFile(tempFile));
			intent.putExtra("outputFormat", "JPEG");
			return intent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 选择本地图片并裁
	 * 
	 * @param tempFile
	 *            路径
	 * @param outX
	 * @param outY
	 * @return
	 */
	public static Intent doPickPhotoFromGallery(File tempFile, int aspectX, int aspectY, int outX, int outY) {
		try {

			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
			intent.putExtra("outputX", outX);
			intent.putExtra("outputY", outY);
			intent.putExtra("outputFormat", "JPEG");
			intent.putExtra("output", Uri.fromFile(tempFile));
			if (!tempFile.exists()) {
				intent.putExtra("return-data", true);
			}
			return intent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 * 选择本地图片
	 * 
	 * @param tempFile
	 * @return
	 */
	public static Intent doPickPhoto() {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			intent.putExtra("return-data", true);
			return intent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 拍照
	 * 
	 * @param PHOTO_DIR
	 *            存储路径
	 * @param mCurrentPhotoFile
	 *            图片路径
	 * @return
	 */
	public static Intent doTakePhoto(File mCurrentPhotoFile) {
		try {
			// Launch camera to take photo for selected contact
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
			return intent;
		} catch (Exception e) {
		}
		return null;
	}

	
	public static String getSDCardPath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;
	}
	
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
}
