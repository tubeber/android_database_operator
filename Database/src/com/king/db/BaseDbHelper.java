package com.king.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.db.util.LogX;

/**
 * 数据库操作基类
 * 
 * @author King
 * @since 2014-4-1 下午2:50:17
 * @param <E> 实体对象
 */
public abstract class BaseDbHelper<E> implements IDbOperate<E> {

	/**
	 * 默认主键
	 */
	public static final String _ID = "_id";
	
    /**
     * 操作的表名
     */
    private String mTableName;

    /**
     * 数据库服务
     */
    protected DataBaseService mService;

    /**
     * 查询指针转化成实体数据<此处指针已经指向数据，只需要获取数据，无需处理指针>
     * 
     * @param cursor 查询指针
     * @return
     */
    protected abstract E cursor2Data(Cursor cursor);

    /**
     * 实体数据转化为ContentValues
     * 
     * @param data
     * @return
     */
    protected abstract ContentValues bean2Values(E data);

    /**
     * 插入操作是否处理为Replace<br>
     * <默认:false>
     * 
     * @return
     */
    protected abstract boolean isReplace();

    /**
     * 构造方法: 获取数据库操作服务
     */
    protected BaseDbHelper(String tableName) {
        mTableName = tableName;
        mService = DataBaseService.getInstance();
    }

    @Override
    public List<E> query(String[] selections, String[] selectionArgs, String orderBy) {
        List<E> data = new ArrayList<E>();
        if(mService == null){
        	LogX.e("database", "数据库初始化未完成");
        }
        try {
            SQLiteDatabase db = mService.getDataBase();
            synchronized (lock) {
                // 查询数据,区分in查询和普通查询
                Cursor cursor = null;
                if (selections != null && selectionArgs != null && selections.length == 1
                        && selections.length < selectionArgs.length) {
                    cursor = db.query(mTableName, null, appendSQLIn(selections[0], selectionArgs.length),
                            selectionArgs, null, null, orderBy);
                } else {
                    cursor = db.query(mTableName, null, appendSelections(selections), selectionArgs, null, null,
                            orderBy);
                }
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    data.add(cursor2Data(cursor));
                }
                // 关闭游标指针
                cursor.close();
            }
        } catch (Exception e) {
            LogX.je(this, e);
        }
        return data;
    }

    @Override
    public boolean update(E data, String[] whereClauses, String[] whereArgs) {
        boolean isUpdate = false;
        if(mService == null){
        	LogX.e("database", "数据库初始化未完成");
        }
        try {
            SQLiteDatabase db = mService.getDataBase();
            synchronized (lock) {
                // 更新数据
                long count = db.update(mTableName, bean2Values(data), appendSelections(whereClauses), whereArgs);
                if (count > 0) {
                    isUpdate = true;
                }
            }
        } catch (Exception e) {
            LogX.je(this, e);
        }
        return isUpdate;
    }

    @Override
    public boolean insertOrReplace(E data) {
        boolean isInsert = false;
        if(mService == null){
        	LogX.e("database", "数据库初始化未完成");
        }
        try {
            SQLiteDatabase db = mService.getDataBase();
            synchronized (lock) {
                // 插入数据
                long count = -1;
                if (isReplace()) {
                    count = db.replace(mTableName, null, bean2Values(data));
                } else {
                    count = db.insert(mTableName, null, bean2Values(data));
                }
                if (count > 0) {
                    isInsert = true;
                }
            }
        } catch (Exception e) {
            LogX.je(this, e);
        }
        return isInsert;
    }

    @Override
    public boolean insertOrReplace(List<E> datas) {
        boolean isInsert = true;
        if(mService == null){
        	LogX.e("database", "数据库初始化未完成");
        }
        for (E data : datas) {
            isInsert = isInsert && insertOrReplace(data);
        }
        return isInsert;
    }

    @Override
    public boolean delete(String[] whereClauses, String[] whereArgs) {
        boolean isDelete = false;
        if(mService == null){
        	LogX.e("database", "数据库初始化未完成");
        }
        try {
            SQLiteDatabase db = mService.getDataBase();
            synchronized (lock) {
                // 删除数据
                long count = db.delete(mTableName, appendSelections(whereClauses), whereArgs);
                if (count > 0) {
                    isDelete = true;
                }
            }
        } catch (Exception e) {
            LogX.je(this, e);
        }
        return isDelete;
    }

    /**
     * 处理选择项，转换为数据库操作的选择项<br>
     * e.g. { id, name } --> id=? and name=?
     * 
     * @param selections
     * @return
     */
    protected String appendSelections(String[] selections) {
        if (selections == null || selections.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (String selection : selections) {
            sb.append(selection).append("=? and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        return sb.toString();
    }

    /**
     * 功能描述: 拼接SQL中in语句<br>
     * 
     * @param arg 列名
     * @param size 大小
     * @return
     */
    protected String appendSQLIn(String arg, int size) {
        StringBuffer sb = new StringBuffer(arg);
        sb.append(" in (");
        for (int i = 0; i < size; i++) {
            sb.append(i == size - 1 ? "?)" : "?,");
        }
        return sb.toString();
    }
    
    @Override
    public String getTableName() {
    	return mTableName;
    }
}
