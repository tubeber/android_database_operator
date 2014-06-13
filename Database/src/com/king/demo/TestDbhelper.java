package com.king.demo;

import android.content.ContentValues;
import android.database.Cursor;

import com.king.db.BaseDbHelper;
import com.king.db.util.TableStatmentUtil;

/**
 * 数据库表实现类<单例模式>
 * 
 * @author king
 * @time 2014-6-13 下午6:53:08
 */
public class TestDbhelper extends BaseDbHelper<Student>{

	/**
	 * 表名
	 */
	public static final String TABLE_NAME = "test";
	
	/**
	 * Id列<主键、自增>
	 */
	public final static String _ID = "_id";
	
	/**
	 * 姓名列
	 */
	public final static String NAME = "name";
	
	/**
	 * 性别列
	 */
	public final static String SEX = "sex";
	
	/**
	 * 时间列
	 */
	public final static String TIME = "time";
	
	/**
	 * 建表语句
	 */
	public static final String CREATE_TABLE = TableStatmentUtil.createTable(TABLE_NAME, new String[]{NAME, SEX, TIME}, _ID);
	
	/**
	 * 单例
	 */
	public static TestDbhelper mDbHelper;
	
	/**
	 * 私有构造方法
	 */
	private TestDbhelper(){
		super(TABLE_NAME);
	}
	
	protected TestDbhelper(String tableName) {
		super(tableName);
	}

	/**
	 * 获取实例
	 * @return
	 */
	public static TestDbhelper getInstance(){
		if(mDbHelper == null){
			mDbHelper = new TestDbhelper();
		}
		return mDbHelper;
	}
	
	@Override
	protected Student cursor2Data(Cursor cursor) {
		// 查询结果封装
		Student bean = new Student();
		bean.name = cursor.getString(cursor.getColumnIndex(NAME));
		bean.sex = cursor.getString(cursor.getColumnIndex(SEX));
		bean.time = cursor.getString(cursor.getColumnIndex(TIME));
		return bean;
	}

	@Override
	protected ContentValues bean2Values(Student data) {
		// 插入数据封装
		ContentValues values = new ContentValues();
		values.put(NAME, data.name);
		values.put(SEX, data.sex);
		values.put(TIME, data.time);
		return values;
	}

	@Override
	protected boolean isReplace() {
		// 主键相同的记录将被替换
		return true;
	}

}
