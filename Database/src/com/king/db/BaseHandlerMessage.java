package com.king.db;

import com.king.db.base.DbExecutor;

/**
 * Handle异步处理标识
 * 
 * @author king
 * @time 2014-5-30 下午8:46:12
 */
public class BaseHandlerMessage {

	/**
	 * 数据库处理标志<br>
	 * {@link DbExecutor}
	 */
	public static final int MSG_DB = 0x010;

	/**
	 * 数据库插入成功
	 */
	public static final int MSG_DB_INSERT_SUCCESS = MSG_DB + 1;

	/**
	 * 数据库插入失败
	 */
	public static final int MSG_DB_INSERT_FAILURE = MSG_DB + 2;

	/**
	 * 数据库删除成功
	 */
	public static final int MSG_DB_DELETE_SUCCESS = MSG_DB + 3;

	/**
	 * 数据库删除失败
	 */
	public static final int MSG_DB_DELETE_FAILURE = MSG_DB + 4;

	/**
	 * 数据库批量插入成功
	 */
	public static final int MSG_DB_INSERTALL_SUCCESS = MSG_DB + 5;

	/**
	 * 数据库批量插入失败
	 */
	public static final int MSG_DB_INSERTALL_FAILURE = MSG_DB + 6;

	/**
	 * 数据库更新成功
	 */
	public static final int MSG_DB_UPDATE_SUCCESS = MSG_DB + 7;

	/**
	 * 数据库更新失败
	 */
	public static final int MSG_DB_UPDATE_FAILURE = MSG_DB + 8;

	/**
	 * 数据库查询成功
	 */
	public static final int MSG_DB_QUERY_SUCCESS = MSG_DB + 9;

	/**
	 * 数据库查询失败
	 */
	public static final int MSG_DB_QUERY_FAILURE = MSG_DB + 10;

}
