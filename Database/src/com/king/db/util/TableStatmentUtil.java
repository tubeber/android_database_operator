package com.king.db.util;

import android.text.TextUtils;

/**
 * 建表语句工具类
 * @author King
 * @since 2014-3-11 下午7:14:44
 */
public class TableStatmentUtil {

	/**
	 * 创建表
	 * 
	 * @param tableName 表名
	 * @param columns 列<不包括主键>
	 * @param primaryKey 主键
	 * @return
	 */
	public static String createTable(String tableName, String [] columns, String primaryKey){
		
		if(TextUtils.isEmpty(tableName)){
			LogX.e("database", "创建表语法错误： 无tableName");
			return null;
		}
		
		if(columns == null || columns.length == 0){
			LogX.e("database", "创建表语法错误： 无columns");
			return null;
		}
		
		if(TextUtils.isEmpty(primaryKey)){
			primaryKey = "_id";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE  IF NOT EXISTS `")
		  .append(tableName).append("` (");
		for (String column : columns) {
			sb.append("`").append(column).append("` VARCHAR, ");
		}
		if("_id".equals(primaryKey)){
			sb.append("`").append(primaryKey).append("` INTEGER PRIMARY KEY AUTOINCREMENT)");
		}else{
			sb.append("`").append(primaryKey).append("` TEXT PRIMARY KEY)");
		}
		return sb.toString();
	}
	
	/**
	 * 创建表(主键默认_id)
	 * 
	 * @param tableName 表名
	 * @param columns 列
	 * @return
	 */
	public static String createTable(String tableName, String [] columns){
		return createTable(tableName, columns, "_id");
	}
}
