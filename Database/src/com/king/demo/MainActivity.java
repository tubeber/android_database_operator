package com.king.demo;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.king.db.BaseHandlerMessage;
import com.king.db.DataBaseService;
import com.king.db.R;
import com.king.db.base.DbExecutor;
import com.king.db.base.DbExecutor.Operate;
import com.king.db.base.QueryValue;

public class MainActivity extends Activity {

	private DbExecutor mTestDBExecutor;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BaseHandlerMessage.MSG_DB_QUERY_SUCCESS:
				showToast("查询成功");
				// 查询结果
				List<Student> result = (List<Student>) msg.obj;
				
				break;
			case BaseHandlerMessage.MSG_DB_QUERY_FAILURE:
				showToast("查询失败或查询无结果");
				break;
			case BaseHandlerMessage.MSG_DB_INSERT_SUCCESS:
				showToast("插入成功");
				break;
			case BaseHandlerMessage.MSG_DB_INSERT_FAILURE:
				showToast("插入失败");
				break;
			case BaseHandlerMessage.MSG_DB_INSERTALL_SUCCESS:
				showToast("批量插入成功");
				break;
			case BaseHandlerMessage.MSG_DB_INSERTALL_FAILURE:
				showToast("批量插入失败");
				break;
			case BaseHandlerMessage.MSG_DB_UPDATE_SUCCESS:
				showToast("更新成功");
				break;
			case BaseHandlerMessage.MSG_DB_UPDATE_FAILURE:
				showToast("更新失败");
				break;
			case BaseHandlerMessage.MSG_DB_DELETE_SUCCESS:
				showToast("删除成功");
				break;
			case BaseHandlerMessage.MSG_DB_DELETE_FAILURE:
				showToast("删除失败");
				break;
			default:
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 数据库初始化
		// 有些数据库初始化非常耗时，建议在非UI线程中初始化
		DataBaseService.getInstance().initService(getApplicationContext());
		
		// 获取一个数据库中test表的操作类
		mTestDBExecutor = DbExecutor.create(TestDbhelper.getInstance(), mHandler);
		
//		initData();
	}

	private void initData(){
		Student student = new Student();
		student.name = "张三";
		student.sex = "男";
		student.time = String.valueOf(System.currentTimeMillis());
		
		// 数据插入
		mTestDBExecutor.setOperate(Operate.INSERT).setData(student).submit();
		
		// 数据更新
		mTestDBExecutor.setOperate(Operate.UPDATE).setData(student).submit();
		// 数据指定更新
		mTestDBExecutor.setOperate(Operate.UPDATE)
			.setData(student)
			.setSelectionValue(TestDbhelper.SEX, "男")
			.submit();
		
		// -----数据查询, 查询结果在Handler的消息队列中处理----
		
		// 查询表中 sex=男 的匹配项
		mTestDBExecutor.setOperate(Operate.QUERY)
			.setSelectionValue(TestDbhelper.SEX, "男")
			.submit();
		// 查询表中 sex=男 且time降序排列
		mTestDBExecutor.setOperate(Operate.QUERY)
			.setSelectionValue(TestDbhelper.SEX, "男")
			.setOrderBy(TestDbhelper.TIME, QueryValue.DESC)
			.submit();
		// 查询表中 sex=男 且 nan=张三
		mTestDBExecutor.setOperate(Operate.QUERY)
			.setSelectionsValue(new String[]{TestDbhelper.SEX, TestDbhelper.NAME}, new String[]{"男", "张三"})
			.submit();
		
		// ------数据删除------
		mTestDBExecutor.setOperate(Operate.DELETE)
			.setData(student)
			.setSelectionValue(TestDbhelper.SEX, "男")
			.submit();
		
		
		// 如果多种相同的操作类型，使用tag区分.操作成功后 tag会在Handler的消息的arg1返回
		mTestDBExecutor.setOperate(Operate.DELETE)
			.setData(student)
			.setSelectionValue(TestDbhelper.NAME, "张三")
			.setTag(1)
			.submit();
		//或者
		mTestDBExecutor.setOperate(Operate.DELETE)
			.setData(student)
			.setSelectionValue(TestDbhelper.NAME, "张三")
			.submit(1);
		
	};
	
	private void showToast(String text){
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
}
