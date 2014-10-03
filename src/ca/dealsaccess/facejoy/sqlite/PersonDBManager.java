package ca.dealsaccess.facejoy.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class PersonDBManager {
	private PersonDBOpenHelper helper;
	private SQLiteDatabase db;

	public PersonDBManager(Context context) {
		helper = new PersonDBOpenHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}
	/**
	 * 插入人物
	 * @param person
	 * @return lastInsertId
	 */
	public long insert(Person person) {
		long rst = -1;
		db.beginTransaction(); // 开始事务
		try {
			ContentValues values = new ContentValues();
			values.put(PersonDBOpenHelper.KEY_PERSON_ID, person.personId);
			values.put(PersonDBOpenHelper.KEY_PERSON_ID, person.personName);
			values.put(PersonDBOpenHelper.KEY_IS_TRAIN, person.isTrain);
			values.put(PersonDBOpenHelper.KEY_START_TIME, person.startTime);
			values.put(PersonDBOpenHelper.KEY_CREATE_TIME, person.createTime);
			values.put(PersonDBOpenHelper.KEY_FINISH_TIME, person.finishTime);
			values.put(PersonDBOpenHelper.KEY_SESSION_ID, person.sessionId);
			rst = db.insert(PersonDBOpenHelper.DATABASE_TABLE, null, values);
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
		return rst;
	}
	
	/**
	 * 
	 * @param person
	 * @param id
	 * @return affectRows
	 */
	
	public int update(Person person, long id) {
		int rst = -1;
		db.beginTransaction(); // 开始事务
		try {
			ContentValues values = new ContentValues();
			values.put(PersonDBOpenHelper.KEY_PERSON_ID, person.personId);
			values.put(PersonDBOpenHelper.KEY_PERSON_ID, person.personName);
			values.put(PersonDBOpenHelper.KEY_IS_TRAIN, person.isTrain);
			values.put(PersonDBOpenHelper.KEY_START_TIME, person.startTime);
			values.put(PersonDBOpenHelper.KEY_CREATE_TIME, person.createTime);
			values.put(PersonDBOpenHelper.KEY_FINISH_TIME, person.finishTime);
			values.put(PersonDBOpenHelper.KEY_SESSION_ID, person.sessionId);
			rst = db.update(PersonDBOpenHelper.DATABASE_TABLE, values, "?=?",
					new String[] {PersonDBOpenHelper.KEY_ID, String.valueOf(id)});
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
		return rst;
	}
	
	

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}