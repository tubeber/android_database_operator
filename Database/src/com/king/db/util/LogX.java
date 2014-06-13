package com.king.db.util;

import android.util.Log;

/**
 * 自定义日志工具类
 * 
 * @author King
 * @since 2014-3-11 上午11:52:02
 */
public class LogX {
	
	/**
	 * 日志开关<br>
	 * 发布的项目需要调用{@link #disable()}设置为false
	 */
	private static boolean logEnabled = true;

	/**
	 * 禁用日志
	 */
	public static void disable(){
		logEnabled = false;
	}
	
	public static void e(Object object, String err) {
		if (logEnabled && err != null) {
			Log.e(getPureClassName(object), err);
		}
	}

	public static void d(Object object, String debug) {
		if (logEnabled && debug != null) {
			Log.d(getPureClassName(object), debug);
		}
	}

	public static void i(Object object, String info) {
		if (logEnabled && info != null) {
			Log.i(getPureClassName(object), info);
		}
	}

	public static void w(Object object, String info) {
		if (logEnabled && info != null) {
			Log.w(getPureClassName(object), info);
		}
	}

	public static void jw(Object object, Throwable tr) {
		if (logEnabled && tr != null) {
			Log.w(getPureClassName(object), "", filterThrowable(tr));
		}
	}

	public static void je(Object object, Throwable tr) {
		if (logEnabled && tr != null) {
			Log.e(getPureClassName(object), "", tr);
		}
	}

	private static Throwable filterThrowable(Throwable tr) {
		StackTraceElement[] ste = tr.getStackTrace();
		tr.setStackTrace(new StackTraceElement[] { ste[0] });
		return tr;
	}

	private static String getPureClassName(Object object) {
		if (object == null) {
			return "";
		}
		String name = object.getClass().getName();
		if ("java.lang.String".equals(name)) {
			return object.toString();
		}
		int idx = name.lastIndexOf('.');
		if (idx > 0) {
			return name.substring(idx + 1);
		}
		return name;
	}
}
