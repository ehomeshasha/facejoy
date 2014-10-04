package ca.dealsaccess.facejoy.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class PersonTrainVerifyDBManager {
	private PersonTrainVerifyDBOpenHelper helper;
	private SQLiteDatabase db;

	public PersonTrainVerifyDBManager(Context context) {
		helper = new PersonTrainVerifyDBOpenHelper(context);
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
	public long insert(PersonTrainVerify person) {
		long rst = -1;
		db.beginTransaction(); // 开始事务
		try {
			ContentValues values = new ContentValues();
			values.put(PersonTrainVerifyDBOpenHelper.KEY_PERSON_ID, person.personId);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_PERSON_NAME, person.personName);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_STATUS, person.status);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_START_TIME, person.startTime);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_CREATE_TIME, person.createTime);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_FINISH_TIME, person.finishTime);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_SESSION_ID, person.sessionId);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_RESULT, person.result);
			rst = db.insert(PersonTrainVerifyDBOpenHelper.DATABASE_TABLE, null, values);
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
	
	public int update(PersonTrainVerify person, long id) {
		int rst = -1;
		db.beginTransaction(); // 开始事务
		try {
			ContentValues values = new ContentValues();
			values.put(PersonTrainVerifyDBOpenHelper.KEY_PERSON_ID, person.personId);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_PERSON_NAME, person.personName);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_STATUS, person.status);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_START_TIME, person.startTime);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_CREATE_TIME, person.createTime);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_FINISH_TIME, person.finishTime);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_SESSION_ID, person.sessionId);
			values.put(PersonTrainVerifyDBOpenHelper.KEY_RESULT, person.result);
			rst = db.update(PersonTrainVerifyDBOpenHelper.DATABASE_TABLE, values, "?=?",
					new String[] {PersonTrainVerifyDBOpenHelper.KEY_ID, String.valueOf(id)});
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