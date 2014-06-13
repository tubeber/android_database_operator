package com.king.db.base;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;

import com.king.db.BaseHandlerMessage;
import com.king.db.IDbOperate;
import com.king.db.util.LogX;

/**
 * 数据库执行者<br><br>
 * 使用方式:<br>
 * <code>
 * DbExecutor.create(MyNoteDbHelper.getInstance(), mHandler)<br>
 *			.setOperate(Operate.QUERY)<br>
 *			.setSelection("_id")<br>
 *			.setSelectionArg("0")<br>
 *			.setOrderBy("time", QueryValue.DESC)<br>
 *			.submit();
 * </code>
 * @author king
 * @time 2014-4-23 下午3:20:59
 */
public final class DbExecutor {

	/**
	 * 全局操作标签<br>
	 * 当操作成功时，将于{@link Handler}中作为arg1返回<br>
	 * 可以使用{@link #addTag()}设置
	 */
	private int mTag;
	
	/**
	 * 数据库操作类
	 */
	private IDbOperate<?> mDbOperate;

	/**
	 * 数据库操作参数
	 */
	private BaseValue mValue;
	
	/**
	 * {@link Handler}
	 */
	private Handler mHandler;

	/**
	 * 数据库操作类型
	 */
	private Operate mOperate;
	
	/**
	 * 线程池
	 */
	private ExecutorService mThreadPool = Executors.newCachedThreadPool();
	
	private <T> DbExecutor(IDbOperate<T> dbOperate) {
		this(dbOperate, null);
	}
	
	private <T> DbExecutor(IDbOperate<T> dbOperate, Handler handler) {
		this.mDbOperate = dbOperate;
		this.mHandler = handler;
	}
	
	/**
	 * 获取一个数据库执行实例
	 * @param dbOperate 数据库操作实现类
	 * @param handler 不解释
	 * @return
	 */
	public static <T> DbExecutor create(IDbOperate<T> dbOperate, Handler handler){
		DbExecutor executor = new DbExecutor(dbOperate, handler);
		return executor;
	}
	
	/**
	 * 获取一个数据库执行实例
	 * @param dbOperate 数据库操作实现类
	 * @return
	 */
	public static <T> DbExecutor create(IDbOperate<T> dbOperate){
		DbExecutor executor = new DbExecutor(dbOperate);
		return executor;
	}
	
	/**
	 * 添加操作类型
	 * @param operate
	 * @return
	 */
	public <T> DbExecutor setOperate(Operate operate){
		mOperate = operate;
		switch (operate) {
		case QUERY:
			mValue = new QueryValue();
			break;
		case DELETE:
			mValue = new BaseValue();
			break;
		case UPDATE:
		case INSERT:
		case INSERTALL:
			mValue = new UpdateValue<T>();
			break;
		default:
			break;
		}
		return this;
	}
	
	/**
	 * 添加操作标签<br>
	 * 用户区分不同情况下相同类型的请求
	 * @param tag
	 * @return
	 */
	public DbExecutor setTag(int tag){
		mTag = tag;
		return this;
	}

	/**
	 * 设置操作参数
	 * @param selection 对应数据库中列名
	 * @return
	 */
	public DbExecutor setSelection(String selection){
		mValue.selection = selection;
		return this;
	}
	
	/**
	 * 设置操作参数值
	 * @param selectionArg 对应数据库中列匹配值
	 * @return
	 */
	public DbExecutor setSelectionArg(String selectionArg){
		mValue.selectionArg = selectionArg;
		return this;
	}
	
	/**
	 * 设置操作参数集
	 * @param selections 对应数据库中列名几
	 * @return
	 */
	public DbExecutor setSelections(String[] selections){
		mValue.selections = selections;
		return this;
	} 
	
	/**
	 * 设置操作参数值集
	 * @param selectionArg 对应数据库中列匹配值集
	 * @return
	 */
	public DbExecutor setSelectionArg(String[] selectionArgs){
		mValue.selectionArgs = selectionArgs;
		return this;
	}
	
	/**
	 * 设置操作参数和操作参数值
	 * @param selection 对应数据库中列名
	 * @param selectionArg 对应数据库中列匹配值
	 * @return
	 */
	public DbExecutor setSelectionValue(String selection, String selectionArg){
		mValue.selection = selection;
		mValue.selectionArg = selectionArg;
		return this;
	}
	
	/**
	 * 设置操作参数集和操作参数值集
	 * @param selections 对应数据库中列名集
	 * @param selectionArgs 对应数据库中列匹配值集
	 * @return
	 */
	public DbExecutor setSelectionsValue(String[] selections, String[] selectionArgs){
		mValue.selections = selections;
		mValue.selectionArgs = selectionArgs;
		return this;
	}
	
	/**
	 * 设置查询结果排序方式，默认降序
	 * @param orderArg 排序参照列
	 * @param orderType 排序类型{@link QueryValue#ASC}、{@link QueryValue#DESC}
	 * @return
	 */
	public DbExecutor setOrderBy(String orderArg, String orderType){
		((QueryValue)mValue).orderArg = orderArg;
		((QueryValue)mValue).orderType = orderType;
		return this;
	}
	
	/**
	 * 设置查询结果排序，默认降序
	 * @param orderArg 排序参照列
	 * @return
	 */
	public DbExecutor setOrderArg(String orderArg){
		((QueryValue)mValue).orderArg = orderArg;
		return this;
	}
	
	/**
	 * 设置查询结果排序，默认降序
	 * @param orderType 排序类型{@link QueryValue#ASC}、{@link QueryValue#DESC}
	 * @return
	 */
	public DbExecutor setOrderType(String orderType){
		((QueryValue)mValue).orderType = orderType;
		return this;
	}
	
	/**
	 * 设置批量插入的数据集
	 * @param datas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> DbExecutor setDatas(List<T> datas){
		((UpdateValue<T>)mValue).datas = datas;
		return this;
	}
	
	/**
	 * 设置插入、更新操作的数据
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> DbExecutor setData(T data){
		((UpdateValue<T>)mValue).data = data;
		return this;
	}
	
	/**
	 * 设置查询参数
	 * @param selections 对应数据库中列名集
	 * @param selectionArgs 对应数据库中列匹配值集
	 * @param orderArg 排序参照列
	 * @param orderType 排序类型
	 * @return
	 */
	public DbExecutor setQueryValues(String[] selections, String[] selectionArgs, String orderArg, String orderType){
		mValue.selections = selections;
		mValue.selectionArgs = selectionArgs;
		((QueryValue)mValue).orderArg = orderArg;
		((QueryValue)mValue).orderType = orderType;
		return this;
	}
	
	/**
	 * 设置查询参数
	 * @param selections 对应数据库中列名
	 * @param selectionArgs 对应数据库中列匹配值
	 * @param orderArg 排序参照列
	 * @param orderType 排序类型{@link QueryValue#ASC}、{@link QueryValue#DESC}
	 * @return
	 */
	public DbExecutor setQueryValues(String selection, String selectionArg, String orderArg, String orderType){
		mValue.selection = selection;
		mValue.selectionArg = selectionArg;
		((QueryValue)mValue).orderArg = orderArg;
		((QueryValue)mValue).orderType = orderType;
		return this;
	}
	
	/**
	 * 设置更新参数
	 * @param selections 对应数据库中列名
	 * @param selectionArgs 对应数据库中列匹配值
	 * @param data 更新实体数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> DbExecutor setUpdateValues(String selection, String selectionArg, T data){
		mValue.selection = selection;
		mValue.selectionArg = selectionArg;
		((UpdateValue<T>)mValue).data = data;
		return this;
	} 
	
	/**
	 * 操作提交<br>
	 * 操作提交之前需要检查{@link Operate}以及对应的参数
	 */
	public void submit(){
		this.submit(mTag);
	}
	
	/**
	 * 操作提交<br>
	 * 操作提交之前需要检查{@link Operate}以及对应的参数
	 * @param tag 当前操作标签
	 */
	public void submit(final int tag){
		mTag = tag;
		mThreadPool.submit(new Runnable() {
			
			@Override
			public void run() {
				execute(mDbOperate, tag);
			}
		});
	}
	
	/**
	 * 数据库操作执行
	 */
	@SuppressWarnings("unchecked")
	private <T> void execute(IDbOperate<T> operate, final int tag){
		// 统一处理selections和selectionArgs的封装
		if(mValue.selection != null){
			mValue.selections = new String[]{mValue.selection};
		}
		if(mValue.selectionArg != null){
			mValue.selectionArgs = new String[]{mValue.selectionArg};
		}
		long beginTime = System.currentTimeMillis();
		switch (mOperate) {
		case INSERT:
			if(operate.insertOrReplace(((UpdateValue<T>)mValue).data)){
				sendMessage(BaseHandlerMessage.MSG_DB_INSERT_SUCCESS, tag, 0);
				LogX.d("database", "数据库操作[成功] -- 插入  -- " + operate.getTableName() + " 耗时：" + (System.currentTimeMillis() - beginTime)+ "ms");
			}else {
				sendMessage(BaseHandlerMessage.MSG_DB_INSERT_FAILURE, tag, 0);
				LogX.d("database", "数据库操作[失败] -- 插入  -- " + "请检查表 " + operate.getTableName() + " 或操作类 " + operate.getClass().getSimpleName());
			};
			break;
		case INSERTALL:
			if(operate.insertOrReplace(((UpdateValue<T>)mValue).datas)){
				sendMessage(BaseHandlerMessage.MSG_DB_INSERTALL_SUCCESS, tag, 0);
				LogX.d("database", "数据库操作[成功] -- 批量插入  -- " + operate.getTableName() + " 耗时：" + (System.currentTimeMillis() - beginTime)+ "ms");
			}else {
				sendMessage(BaseHandlerMessage.MSG_DB_INSERTALL_FAILURE, tag, 0);
				LogX.d("database", "数据库操作[失败] -- 批量插入  -- " + "请检查表 " + operate.getTableName() + " 或操作类 " + operate.getClass().getSimpleName());
			};
			break;
		case DELETE:
			if(operate.delete(mValue.selections, mValue.selectionArgs)){
				sendMessage(BaseHandlerMessage.MSG_DB_DELETE_SUCCESS, tag, 0);
				LogX.d("database", "数据库操作[成功] -- 删除  -- " + operate.getTableName() + " 耗时：" + (System.currentTimeMillis() - beginTime)+ "ms");
			}else {
				sendMessage(BaseHandlerMessage.MSG_DB_DELETE_FAILURE, tag, 0);
				LogX.d("database", "数据库操作[失败] -- 删除  -- " + "请检查表 " + operate.getTableName() + " 或操作类 " + operate.getClass().getSimpleName());
			};
		case UPDATE:
			if(operate.update(((UpdateValue<T>)mValue).data, mValue.selections, mValue.selectionArgs)){
				sendMessage(BaseHandlerMessage.MSG_DB_UPDATE_SUCCESS, tag, 0);
				LogX.d("database", "数据库操作[成功] -- 更新  -- " + operate.getTableName() + " 耗时：" + (System.currentTimeMillis() - beginTime)+ "ms");
			}else {
				sendMessage(BaseHandlerMessage.MSG_DB_UPDATE_FAILURE, tag, 0);
				LogX.d("database", "数据库操作[失败] -- 更新  -- " + "请检查表 " + operate.getTableName() + " 或操作类 " + operate.getClass().getSimpleName());
			};
			break;
		case QUERY:
			String orderBy = ((QueryValue)mValue).orderArg != null ? ((QueryValue)mValue).orderArg + " " + ((QueryValue)mValue).orderType : null; 
			List<T> data = operate.query(mValue.selections, mValue.selectionArgs, orderBy);
			if(data != null){
				if(data.size() > 0){
					sendMessage(BaseHandlerMessage.MSG_DB_QUERY_SUCCESS, data, tag, 0);
				}else{
					sendMessage(BaseHandlerMessage.MSG_DB_QUERY_FAILURE, tag, 0);
				}
				LogX.d("database", "数据库操作[成功] -- 查询  -- " + data.size() + "条记录" + operate.getTableName() + " 耗时：" + (System.currentTimeMillis() - beginTime) + "ms");
			}else {
				sendMessage(BaseHandlerMessage.MSG_DB_QUERY_FAILURE, tag, 0);
				LogX.d("database", "数据库操作[失败] -- 查询  -- " + "请检查表 " + operate.getTableName() + " 或操作类 " + operate.getClass().getSimpleName());
			};
			break;
		default:
			break;
		}
	}
	
	private void sendMessage(int what, Object obj, int arg1, int arg2){
		if(mHandler != null){
			mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2, obj));
		}
	}
	
	private void sendMessage(int what, int arg1, int arg2){
		if(mHandler != null){
			mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2));
		}
	}
	
	
	
	/**
	 * 数据库操作类型
	 * 
	 * @author 13071499
	 * @since 2014-4-1 下午2:21:51
	 */
	public enum Operate {

		/**
		 * 增
		 */
		INSERT,

		/**
		 * 增批量
		 */
		INSERTALL,

		/**
		 * 删
		 */
		DELETE,

		/**
		 * 改
		 */
		UPDATE,

		/**
		 * 查
		 */
		QUERY,

	}
}
