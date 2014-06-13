package com.king.db.base;

/**
 * 数据库操作参数定义基类
 * @author king
 * @time 2014-4-23 下午4:21:57
 */
public class BaseValue implements IOperateValue {

	/**
	 * 操作参数
	 */
	public String selection;
	
	/**
	 * 操作参数值
	 */
	public String selectionArg;
	
	/**
	 * 操作参数集
	 */
	public String[] selections; 
	
	/**
	 * 操作参数值集
	 */
	public String[] selectionArgs;
}
