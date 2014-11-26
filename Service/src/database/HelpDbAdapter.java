package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class HelpDbAdapter extends DbAdapter {
	
	public static final String TABLE_NAME = "help_message";
	public static final String EVENT_ID = "eid";
	public static final String USER_ID = "uid";
	public static final String USERNAME = "username";
	public static final String CONTENT = "content";
	public static final String TIME = "time";
	public static final String KIND = "kind";
	public static final String STATE = "state";
	public static final String AUDIO = "audio";
	public static final String VIDEO = "video";
	
	private DatabaseHelper mDbHelper;
	private final Context mCtx;
	
	public HelpDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public HelpDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	public void addEvent(String name, String time, String content, int eventid, int kind) {
		ContentValues values = new ContentValues();
		values.put(USERNAME, name);
		values.put(TIME, time);
		values.put(CONTENT, content);
		values.put(EVENT_ID, eventid);
		values.put(KIND, kind);
		values.put(STATE, 1);
		mDbHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
	}
	
	public Cursor getEvents() {
		String[] col = {USERNAME, TIME, CONTENT, EVENT_ID, KIND};
		Cursor c = mDbHelper.getReadableDatabase().query(TABLE_NAME, col, null, null, null, null, TIME, null);
		return c;
	}
	
}