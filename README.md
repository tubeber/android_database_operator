android_database_operator
=========================
Simplify database operation mode and use a thread pool to manage operations, include query, insert, update, delete.

正常数据库的操作，需要自己拼写sql语句，然后使用AsyncTask执行。
这样做，容易造成大量代码冗余，而且容易混乱逻辑，失去了代码的优雅之色。

本开源项目，对数据库的操作做了细致的封装。
所有数据库操作使用线程池统一管理
包括插入、删除、更新、查询等操作，可直接方法调用！

本开源项目的优点：
##无需手动建表、无需拼写sql语句、无需处理Cursor、无需开异步线程

## insert
```java
	Student student = new Student();
    	student.name = "shangsan";
    	student.sex = "1";
	student.time = String.valueOf(System.currentTimeMillis());
		
	// insert
	mTestDBExecutor.setOperate(Operate.INSERT).setData(student).submit();

```

## update
```java
    mTestDBExecutor.setOperate(Operate.UPDATE)
			.setData(student)
			.setSelectionValue(TestDbhelper.SEX, "1")
			.submit();
```

## query
```java
    mTestDBExecutor.setOperate(Operate.QUERY)
			.setSelectionValue(TestDbhelper.SEX, "1")
			.setOrderBy(TestDbhelper.TIME, QueryValue.DESC)
			.submit();
```

## delete
```java
    mTestDBExecutor.setOperate(Operate.DELETE)
			.setData(student)
			.setSelectionValue(TestDbhelper.SEX, "1")
			.submit();
```
