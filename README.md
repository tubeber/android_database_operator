android_database_operator
=========================
Simplify database operation mode and use a thread pool to manage operations, include query, insert, update, delete.

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
