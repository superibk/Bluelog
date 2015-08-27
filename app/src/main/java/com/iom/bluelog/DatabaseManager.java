package com.iom.bluelog;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseManager {
	
//	DECLATION OF ALL THE VARIABLES AND CONSTANT THAT WILL BE USED TO CREATE THE TABLE
	
	private static final String DATABASE_NAME = "Bluetooth_database";
	private static final String DATABASE_TABLE = "Bluetooth_table";
	private static final int DATABASE_VERSION = 1;
	
	
//	DECLARATION OF ALL THE COLUMN REQUIRED TO BE CREATED
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DEVICE_NAME = "devicename";
	public static final String KEY_DATE = "date";
	public static final String KEY_TIME = "time";
	public static final String KEY_TIME_STOP = "timestop";
	public static final String KEY_FILE_TRANSFERRED  = "file";

	
	
	
				
	
	private DatabaseHelper mDbHelper; 
	private SQLiteDatabase ourDatabase;
	private final Context ourContext;
	
//	THIS IS THE ACTUAL CLASS USED TO CREATE THE DATABASE AND TABLE, IT IS NESTED IN THIS CLASS
	
//		Beginning if this class

//	This is creating a database dynamically, but in this case the database has been preloaded
	
	public class DatabaseHelper extends SQLiteOpenHelper{		
		
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}
		
		
			@Override
			public void onCreate(SQLiteDatabase db) {
				
			db.execSQL("create table " + DATABASE_TABLE + " ("
					+ KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_DEVICE_NAME + " text not null, "
					+ KEY_DATE + " text not null, "
					+ KEY_TIME + " text not null, "
					+ KEY_TIME_STOP + " text not null, "
					+ KEY_FILE_TRANSFERRED + " text not null);");
			
			}
			
			
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXIT "+ DATABASE_TABLE);
				onCreate(db);
			}

			

	}
	
//	End of this class
	
	
//	//	Constructor of this external class
	
	
	public DatabaseManager(Context context){
		ourContext = context;		
		
	}
	
//	constructor terminated
	
//	open the database for access
	
	public  DatabaseManager open() throws SQLException {
		mDbHelper = new DatabaseHelper(ourContext);
		ourDatabase = mDbHelper.getWritableDatabase();
		return this;
		}
	
//	Enter Values into the database or create database values
	
	public long createRecords(String deviceName, String date , String time, String timeStop,  String file) {
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DEVICE_NAME, deviceName);
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_TIME, time);
		initialValues.put(KEY_TIME_STOP, timeStop);
		initialValues.put(KEY_FILE_TRANSFERRED, file);
		return ourDatabase.insert(DATABASE_TABLE, null, initialValues);	
		
	}
	
//	close the database after creating the values for security purposes
	public void close() {
		mDbHelper.close();
		}
	
//	Return all data via the cursor to the calling function
	public Cursor getAllData() throws SQLException{
		String[] columns = {KEY_ROWID, KEY_DEVICE_NAME, KEY_DATE, KEY_TIME, KEY_TIME_STOP, KEY_FILE_TRANSFERRED};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);		
		return c ;
		}
		
      public boolean deleteRecord(long rowId) {
	  return ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0 ;	 }
	
	
	 public Cursor fetchSingleRecord(long rowId) throws SQLException
	 {	String[] columns = {KEY_ROWID, KEY_DEVICE_NAME, KEY_DATE, KEY_TIME, KEY_TIME_STOP, KEY_FILE_TRANSFERRED};
	 	Cursor c =  ourDatabase.query(true, DATABASE_TABLE, columns , KEY_ROWID + "=" +  rowId, null, null, null, null, null);
	 	if (c != null) { c.moveToFirst(); }
	 	return c;
	 }
	 
	 
	//This session of code msy or may not be written here. i mean in this side of the database 
	 // this will get the string result of author
	public String getDeviceName(Long rowId) {
		Cursor c2 = fetchSingleRecord(rowId);
		int rowIndex = c2.getColumnIndex(KEY_DEVICE_NAME);
		return c2.getString(rowIndex);
	}
	
	public String getDate(Long rowId) {
		Cursor c2 = fetchSingleRecord(rowId);
		int rowIndex = c2.getColumnIndex(KEY_DATE);
		return c2.getString(rowIndex);
	}

	public String getTime(Long rowId) {
		Cursor c2 = fetchSingleRecord(rowId);
		int rowIndex = c2.getColumnIndex(KEY_TIME);
		return c2.getString(rowIndex);
	}

	public String getTimeStop(Long rowId) {
		Cursor c2 = fetchSingleRecord(rowId);
		int rowIndex = c2.getColumnIndex(KEY_TIME_STOP);
		return c2.getString(rowIndex);
	}

	public String getFile(Long rowId) {
		Cursor c2 = fetchSingleRecord(rowId);
		int rowIndex = c2.getColumnIndex(KEY_FILE_TRANSFERRED);
		return c2.getString(rowIndex);
	}

    public boolean updateStopTime(long rowId, String stopTime)
    {
			ContentValues updatedValues = new ContentValues();
			updatedValues.put(KEY_TIME_STOP, stopTime);
			return 	ourDatabase.update(DATABASE_TABLE, updatedValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateDeviceName(long rowId, String deviceName)
    {
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_DEVICE_NAME, deviceName);
		return 	ourDatabase.update(DATABASE_TABLE, updatedValues, KEY_ROWID + "=" + rowId, null) > 0;
	}




	public void deleteDatabase(){
		ourContext.deleteDatabase(DATABASE_NAME);
	}


	// a method to filter the result in the database based on what will be supplied by title
	
	public Cursor getFilterdResultByDevice(String value){
		String[] from ={KEY_ROWID, KEY_DEVICE_NAME, KEY_DATE, KEY_TIME, KEY_FILE_TRANSFERRED };

		Cursor c= ourDatabase.query(true, DATABASE_TABLE, from, KEY_DEVICE_NAME + " LIKE ?", new String[] {"%"+ value + "%" }, null, null, null, null);
		return c;
		
	}

	public Cursor getFilterdResultByFile(String value){
		String[] from ={KEY_ROWID, KEY_DEVICE_NAME, KEY_DATE, KEY_TIME, KEY_FILE_TRANSFERRED };

		Cursor c= ourDatabase.query(true, DATABASE_TABLE, from, KEY_FILE_TRANSFERRED + " LIKE ?", new String[] {"%"+ value + "%" }, null, null, null, null);
		return c;

	}

	public long getLastRowId(){
		String[] columns = {KEY_ROWID, KEY_DEVICE_NAME, KEY_DATE, KEY_TIME, KEY_TIME_STOP, KEY_FILE_TRANSFERRED};
		Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns,null, null, null, null, null);
		cursor.moveToLast();
		int rowIndex = cursor.getColumnIndex(KEY_ROWID);
		return cursor.getLong(rowIndex);
	}



	


}    


