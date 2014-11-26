package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	private static final String DATABASE_NAME = "sqlite.db";
	private static final int DATABASE_VERSION = 1;
	
	public class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String help_message = "CREATE TABLE " + HelpDbAdapter.TABLE_NAME + " ("
					+ HelpDbAdapter.EVENT_ID + " INTEGER, "
					+ HelpDbAdapter.USER_ID + " INTEGER, "
					+ HelpDbAdapter.USERNAME + " text, "
					+ HelpDbAdapter.CONTENT + " text, "
					+ HelpDbAdapter.TIME + " text, "
					+ HelpDbAdapter.KIND + " INTEGER, "
					+ HelpDbAdapter.STATE + " INTEGER"
					+ ");";
			db.execSQL(help_message);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + HelpDbAdapter.TABLE_NAME + ";");
			onCreate(db);
		}
	}
	
	public void logCursorInfo(Cursor c) {
		Log.i("PushReceiver", "*** Cursor Begin *** " + " Results:" + c.getCount() + "Columns: " + c.getColumnCount());
		
		// 打印列名称
		String rowHeaders = "|| ";
		for (int i = 0; i < c.getColumnCount(); i++) {
			rowHeaders = rowHeaders.concat(c.getColumnName(i) + " || ");
		}
		Log.i("PushReceiver", "COLUMNS" + rowHeaders);
		
		// 打印所有记录
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			String rowResults = "|| ";
			for (int i = 0; i < c.getColumnCount(); i++) {
				rowResults = rowResults.concat(c.getString(i) + " || ");
			}
			Log.i("PushReceiver", c.getPosition() + ": " + rowResults);
			c.moveToNext();
		}
		Log.i("PushReceiver", "*** Cursor End ***");
	}
}