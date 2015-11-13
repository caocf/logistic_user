package com.cn.xtouch.fodelforuser.utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import com.cn.xtouch.fodelforuser.BaseApplication;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import android.graphics.PorterDuff;
import android.os.Environment;

/**
 * Created by tfl on 2015/7/20.
 */
public class UIUtils {

	public static Context getContext() {
		return BaseApplication.getContext();
	}

	public static void showToast(String content) {
		Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 从资源文件中获取对象
	 * 
	 * @return
	 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	public static String getString(int id) {
		return getResources().getString(id);
	}

	public static String[] getStringArray(int id) {
		return getResources().getStringArray(id);
	}

	public static Drawable getDrawable(int id) {
		return getResources().getDrawable(id);
	}

	

	/**
	 * 加载图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap loadImageFromUrl(String url) {
		Bitmap bm = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = (HttpResponse) httpClient
					.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
						httpEntity);
				InputStream inputStream = bufferedHttpEntity.getContent();
				if (inputStream != null) {
					bm = BitmapFactory.decodeStream(inputStream);
					inputStream.close();
				}
			} else {
				return null;
			}
			// inputStream.reset();

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bm;
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            转化成圆角的图片
	 * @param pixels
	 *            圆角的数值 数值越大 圆角越大 20首页圆角 180 商城
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
	
	public static File getDiskCacheDir(Context context, String uniqueName) {

		// 检查是否安装或存储媒体是内置的,如果是这样,试着使用
		// 外部缓存 目录
		// 否则使用内部缓存 目录

		String cachePath = context.getCacheDir().getPath();
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					|| !isExternalStorageRemovable()) {
				cachePath = getExternalCacheDir(context).getPath();
			}
		} catch (Exception e) {

		}

		return new File(cachePath + File.separator + uniqueName);
	}

	public static File getExternalCacheDir(Context context) {
		if (AndroidVersionCheckUtils.hasFroyo()) {
			return context.getExternalCacheDir();
		}
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	/**
	 * 检查如果外部存储器是内置的或是可移动的。
	 * 
	 * @return 如果外部存储是可移动的(就像一个SD卡)返回为 true,否则false。
	 */
	public static boolean isExternalStorageRemovable() {
		if (AndroidVersionCheckUtils.hasGingerbread()) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}
	
	/**
	 * 验证有效是否有效
	 * 
	 * @param mail
	 * @return
	 */
	public  static boolean isValidEmail(String mail) {
		Pattern pattern = Pattern
				.compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
		Matcher mc = pattern.matcher(mail);
		return mc.matches();
	}
}
