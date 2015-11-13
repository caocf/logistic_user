package com.cn.xtouch.fodelforuser.manager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler crashHandler;

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (crashHandler != null) {
			try {
				// 将crash log写入文件
				FileOutputStream fileOutputStream = new FileOutputStream(
						"/mnt/sdcard/crash_log.txt", true);
				PrintStream printStream = new PrintStream(fileOutputStream);
				ex.printStackTrace(printStream);
				printStream.flush();
				printStream.close();
				fileOutputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 设置默认处理�?
	public void init(Context context) {
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	private CrashHandler() {
	}

	// 单例
	public static CrashHandler instance() {
		if (crashHandler == null) {
			synchronized (crashHandler) {
				crashHandler = new CrashHandler();
			}
		}
		return crashHandler;
	}
}
