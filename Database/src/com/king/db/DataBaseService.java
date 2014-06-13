package com.king.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.king.db.util.LogX;

/**
 * 数据库启动服务<br><br>
 * 不能的数据库类型初始化方式不一样，此处已Android系统自带的数据库为例
 * 
 * @author king
 * @time 2014-6-13 下午6:39:38
 */
public class DataBaseService {
	
	/**
     * 数据库名
     */
    public static final String DATABASE_NAME = "test.db";

    /**
     * 数据库版本(必须》=1)
     */
    public static final int VERSION = 1;

    /**
     * 数据库
     */
    private SQLiteDatabase db;

    /**
     * 数据库单例
     */
    private static DataBaseService instance;

    private DataBaseService() {
    }

    public static DataBaseService getInstance() {
        if (instance == null) {
            instance = new DataBaseService();
        }
        return instance;
    }

    /**
     * 数据库初始化
     * 
     * @param context
     */
    public void initService(Context context) {
        // 数据库的初始化
    	DatabaseHelper helper = new DatabaseHelper(context, DATABASE_NAME, null, VERSION);
    	db = helper.getWritableDatabase();
    }

    public SQLiteDatabase getDataBase() {
        if (db == null) {
            LogX.e("database", "this db service did not init!");
        }
        return db;
    }

    /**
     * 数据库关闭
     * 
     * @param context
     */
    public void closeService() {
        if (null != db && db.isOpen()) {
            db.close();
        }
    }

    /**
     * DBHelper类
     * 
     * @author King
     * @since 2014-3-11 下午3:58:42
     */
    public class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

    /**
     * 建表
     * 
     * @param db
     */
    public void createTable(SQLiteDatabase db) {
        db.beginTransaction();
        try {
//        	数据库表创建  
//          db.execSQL(ExceptionDbHelper.CREATE_TABLE);
        } catch (Exception e) {
            LogX.je("database", e);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }
}
