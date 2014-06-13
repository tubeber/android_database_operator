package com.king.db.base;

/**
 * 查询参数
 * @author king
 * @time 2014-4-23 下午4:10:35
 */
public final class QueryValue extends BaseValue{

	/**
	 * 排序降序
	 */
	public static final String DESC = "desc";
	
	/**
	 * 排序升序
	 */
	public static final String ASC = "asc";
	
	/**
	 * 排序参数
	 */
	public String orderArg;
	
	/**
	 * 排序类型, 默认降序
	 */
	public String orderType = DESC;
}
