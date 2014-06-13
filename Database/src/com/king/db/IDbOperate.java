package com.king.db;

import java.util.List;

/**
 * 
 * 功能描述: <br>
 * 数据库操作接口 <br>
 * <br>
 * <strong>Note:</strong> <br>
 * 数据库操作中，只有查询操作无需加锁，其余操作均需加锁
 * 
 * @author 发疯的小鸡
 * @since 2014年1月2日 下午3:08:41
 */
public interface IDbOperate<E> {

    /**
     * 数据库操作锁
     */
    public static final Object lock = new Object();

    /**
     * 功能描述: <br>
     * 获取数据库相关数据<br>
     * <br>
     * <strong>Note:</strong> <br>
     * 此操作无需加锁
     * 
     * @param selections 数据记录选择
     * @param selectionArgs 对应要查找的数据类型的参数
     * @param orderBy 排序
     * @return 查找到所有相关数据
     * @since 2014年1月2日 下午3:07:54
     */
    public List<E> query(String[] selections, String[] selectionArgs, String orderBy);

    /**
     * 
     * 功能描述: <br>
     * 更新数据库数据<br>
     * <br>
     * <strong>Note:</strong> <br>
     * 此操作需加锁
     * 
     * @param whereClauses 数据记录选择
     * @param whereArgs 对应要查找的数据类型的参数
     * @param data 要更新的数据
     * @return 数据库是否更新成功
     * @since 2014年1月2日 下午3:22:59
     */
    public boolean update(E data, String[] whereClauses, String[] whereArgs);

    /**
     * 功能描述: <br>
     * 数据插入操作<br>
     * <br>
     * <strong>Note:</strong> <br>
     * 此操作需加锁
     * 
     * @param data 插入的数据
     * @return 是否插入数据成功
     * @see [相关类/方法](可选)
     * @since 2014年1月2日 下午3:16:24
     */
    public boolean insertOrReplace(E data);
    
    /**
     * 功能描述: <br>
     * 数据批量插入操作<br>
     * <br>
     * <strong>Note:</strong> <br>
     * 此操作需加锁
     * 
     * @param data 插入的数据
     * @since 2014年1月2日 下午3:16:24
     */
    public boolean insertOrReplace(List<E> datas);

    /**
     * 功能描述: <br>
     * 删除数据库中数据操作<br>
     * <br>
     * <strong>Note:</strong> <br>
     * 此操作需加锁
     * 
     * @param whereClauses 数据记录选择
     * @param whereArgs 对应要查找的数据类型的参数
     * @return 是否删除数据成功
     * @since 2014年1月2日 下午3:21:01
     */
    public boolean delete(String[] whereClauses, String[] whereArgs);
    
    /**
     * 获取数据库操作表名
     * 
     * @return 表名
     */
    public String getTableName();
}
