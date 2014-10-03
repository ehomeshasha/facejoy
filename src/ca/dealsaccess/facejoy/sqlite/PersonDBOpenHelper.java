package ca.dealsaccess.facejoy.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonDBOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "myDatabase.db";
	public static final String DATABASE_TABLE = "person";
	private static final int DATABASE_VERSION = 1;  
	public static final String KEY_ID = "_id";
	public static final String KEY_PERSON_ID = "PERSON_ID_COLUMN";
	public static final String KEY_PERSON_NAME = "PERSON_NAME_COLUMN";
	public static final String KEY_IS_TRAIN = "IS_TRAIN_COLUMN";
	public static final String KEY_START_TIME = "START_TIME_COLUMN";
	public static final String KEY_CREATE_TIME = "CREATE_TIME_COLUMN";
	public static final String KEY_FINISH_TIME = "FINISH_TIME_COLUMN";
	public static final String KEY_SESSION_ID = "SESSION_ID_COLUMN";
	
	
	
	

	// 创建新数据库的SQL语句
	private static final String DATABASE_CREATE = "CREATE TABLE" + DATABASE_TABLE 
			+"("+KEY_ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"
			+KEY_PERSON_ID+" TEXT NOT NULL,"
			+KEY_PERSON_NAME+" TEXT NOT NULL,"
			+KEY_IS_TRAIN+" INTEGER,"
			+KEY_START_TIME+" INTEGER,"
			+KEY_CREATE_TIME+" INTEGER,"
			+KEY_FINISH_TIME+" INTEGER,"
			+KEY_SESSION_ID+" TEXT NOT NULL"
			+")";

	public PersonDBOpenHelper(Context context) {
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public PersonDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	// 当磁盘不存在数据库，辅助类需要创建一个新数据库时调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	// 当数据库版本不一致，磁盘上的数据库版本需要升级到当前版本时调用
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
      
    }
}
