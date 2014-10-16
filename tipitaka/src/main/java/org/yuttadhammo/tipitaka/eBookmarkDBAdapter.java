package  org.yuttadhammo.tipitaka;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class eBookmarkDBAdapter {
	private static final String DATABASE_NAME = "ebookmark.db";
	private static final String DATABASE_TABLE = "bookmark";
	private static final int DATABASE_VERSION = 2;
	
	public static final String KEY_ID = "_id";
	public static final int ID_COL = 0;
	public static final String KEY_TITLE = "title";
	public static final int TITLE_COL = 1;
	public static final String KEY_URL = "url";
	public static final int URL_COL = 2;
	
	private SQLiteDatabase db;
	private final Context context;
	private eBookmarkDBHelper dbHelper;
	
	public static final String DATABASE_CREATE = "create table " + 
		DATABASE_TABLE + 
		" (" + KEY_ID + " integer primary key autoincrement, " +
		KEY_TITLE + " text not null, " +
		KEY_URL + " text not null);";
	
	public eBookmarkDBAdapter(Context _context) {
		context = _context;
		dbHelper = new eBookmarkDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public eBookmarkDBAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		db.close();
	}

	public eBookmarkItem getEntry(long _rowIndex) throws SQLException {
		Cursor cursor = db.query(true, DATABASE_TABLE, 
				new String[] {KEY_ID, KEY_TITLE, KEY_URL}, 
				KEY_ID + "=" + _rowIndex, 
				null, null, null, null, null);
		
		if((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No bookmark items found for row: " + _rowIndex);
		}
		
		String title = cursor.getString(TITLE_COL);
		String url = cursor.getString(URL_COL);
		eBookmarkItem result = new eBookmarkItem(title,url);
		return result;
	}
	
	public boolean isDuplicated(eBookmarkItem item) {
		String title = item.getTitle();
		String url = item.getUrl();
	
		String where = String.format("%s='%s' AND %s='%s'",
				KEY_TITLE, title,
				KEY_URL, url);
		
		int count = db.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_TITLE, KEY_URL}, 
				where, null, null, null, null).getCount();

		if(count > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Cursor getAllEntries() {
		return db.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_TITLE, KEY_URL}, null, null, null, null, null);
	}

	public Cursor getEntries(String _title) {
		String where = KEY_TITLE + "=" + "'" + _title + "'";
		return db.query(DATABASE_TABLE, 
				new String[] {KEY_ID, KEY_TITLE, KEY_URL}, 
				where, null, null, null, null);
	}	
	
	public Cursor getEntries(String sortKey, boolean isDesc) {
		String orderby;
		if(isDesc) {
			orderby = sortKey + " DESC, " + KEY_TITLE + " DESC";
		}
		else {
			orderby = sortKey + " ASC,"  + KEY_TITLE + " ASC";
		}		
		
		return db.query(DATABASE_TABLE, 
				new String[] {KEY_ID, KEY_TITLE, KEY_URL}, 
				null, null, null, null, orderby);
	}
	
	public long insertEntry(eBookmarkItem item) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_TITLE, item.getTitle());
		newValues.put(KEY_URL, item.getUrl());
		return db.insert(DATABASE_TABLE, null, newValues);
	}
	
	public boolean removeEntry(long _rowIndex) {
		return db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null) > 0;
	}

	
	public boolean updateEntry(long _rowIndex, eBookmarkItem item) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_TITLE, item.getTitle());
		newValues.put(KEY_URL, item.getUrl());
		
		String where = KEY_ID + "=" + _rowIndex;
		
		return db.update(DATABASE_TABLE, newValues, where, null) > 0;
	}
	
	private static class eBookmarkDBHelper extends SQLiteOpenHelper {
		
		public eBookmarkDBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("UPGRADE", oldVersion+" --> "+newVersion);
			if(oldVersion == 1) {
				ArrayList<ContentValues> tmp = new ArrayList<ContentValues>();
				Cursor cursor = db.query(DATABASE_TABLE, 
						new String[] {KEY_ID, KEY_TITLE, KEY_URL}, 
						null, null, null, null, null);
				if(cursor.getCount() > 0 && cursor.moveToFirst()) {
					ContentValues newValues;
					while(!cursor.isAfterLast()) {
						newValues = new ContentValues();
						newValues.put(KEY_TITLE, cursor.getString(TITLE_COL));
						newValues.put(KEY_URL, cursor.getString(URL_COL));

						tmp.add(newValues);
						cursor.moveToNext();
					}
				}
				db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
				onCreate(db);	
				for(ContentValues values : tmp) {
					db.insert(DATABASE_TABLE, null, values);
				}
			}
		}
	}
	
	
}