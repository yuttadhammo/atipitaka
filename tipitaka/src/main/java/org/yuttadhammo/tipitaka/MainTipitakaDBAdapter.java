package org.yuttadhammo.tipitaka;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;

public class MainTipitakaDBAdapter {
    private static final String TAG = "MainTipitakaDBAdapter";
	private static final String DATABASE_NAME = "atipitaka.db";
	//private static final int DATABASE_VERSION = 1;	
	private static String DEFAULT_DATABASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ATPK";
	private static String DATABASE_PATH = null;
    private final SharedPreferences prefs;
    private SQLiteDatabase db = null;
	private final Activity context;
    private int version = 0;

	public MainTipitakaDBAdapter(Activity _context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(_context);
		DATABASE_PATH = prefs.getString("data_dir", DEFAULT_DATABASE_PATH);
		//dbHelper = new MainTipitakaDBHelper(DATABASE_PATH + File.separator + DATABASE_NAME);
		context = _context;
	}

	public MainTipitakaDBAdapter open() {
        File f = new File(DATABASE_PATH + File.separator + DATABASE_NAME);
        if(!f.exists()) {
        	f = new File(DEFAULT_DATABASE_PATH + File.separator + DATABASE_NAME);
        	Log.w("Tipitaka","Reverting to default database file at"+f.getAbsolutePath());
        }

       // if(!f.exists()) f = copyExpansionDBFile();


        if(f.exists()) {
            //Log.i("Tipitaka","package version: "+pversion);

            getDBFile(f);

            //Log.i("Tipitaka","db version: "+version);

            // version check

            if(version < 11) {
                Log.i("Tipitaka","outdated db version: "+version);

                db = null;
            }
        } else {
        	db = null;
        }
		return this;
	}

    private void getDBFile(File f) {
        try {
            //db = SQLiteDatabase.openDatabase(DATABASE_PATH + File.separator + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            db = SQLiteDatabase.openDatabase(f.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
            version = db.getVersion();

        }
        catch(Exception e) {
            e.printStackTrace();
            db = null;
            version = 0;
        }
    }

    private File copyExpansionDBFile() {
        // try getting from expansion file

        File f = new File(DATABASE_PATH, DATABASE_NAME);

        int mainVersion = 0;

        //int mainVersion = prefs.getInt("latest_extension_version",0);

        if(mainVersion == 0) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/obb/org.yuttadhammo.tipitaka");
            File[] directoryListing = dir.listFiles();
            if(directoryListing != null) {
                for (File i : directoryListing) {
                    if (i.getName().contains("main.")) {
                        int newV = 0;
                        try {
                            newV = Integer.parseInt(i.getName().replaceFirst("^main\\.([0-9]+)\\..+$", "$1"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (newV > mainVersion)
                            mainVersion = newV;
                    }
                }
            }
        }
        Log.i("Tipitaka","latest extension version: "+mainVersion);

        InputStream in = null;
        try {
            ZipResourceFile expansionFile = APKExpansionSupport.getAPKExpansionZipFile(context, mainVersion, 0);
            in = expansionFile.getInputStream("atipitaka.db");
        } catch (Exception e) {
            //Intent intent = new Intent(context, PlayDownloaderActivity.class);
            //context.startActivity(intent);
            e.printStackTrace();
        }
        if(in != null) { // copy file to expected path
            Log.i("Tipitaka","expansion file found");
            File folder = new File (DATABASE_PATH);
            folder.mkdir();
            OutputStream out = null;
            try {
                out = new FileOutputStream(f);

                byte[] buffer = new byte[1024];
                int read;
                while((read = in.read(buffer)) != -1){
                    out.write(buffer, 0, read);
                }

                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                Log.e(TAG, "Failed to copy expansion database");
            }
        }
        return f;
    }

	public void close() {
		if(db != null) {
			db.close();
		}
	}	
	
    public boolean isOpened() {
    	return db == null ? false : true;
    }	
	
    public Cursor getContent(int volume) {
 		//Log.i ("Tipitaka","db lookup: volume: "+volume+", page: "+page);

    	String selection = String.format("volume = '%s'", volume);
 		
    	final Cursor cursor = this.db.query(
    			"pali", 
    			new String[] {"_id","title","item"}, 
    			selection,
    			null, 
    			null, 
    			null, 
    			"_id");
    	return cursor;    	
    }    

    public Cursor getContent(int volume, int page, String lang) {
 		//Log.i ("Tipitaka","db lookup: volume: "+volume+", page: "+page);

    	String selection = String.format("pali.volume = '%s' AND pali.item = '%s'", volume, page);
 		
   	
    	final Cursor cursor = db.rawQuery(
                "SELECT item, content, title, nikaya, codes.volume, hier, code, rel FROM pali, codes " +
                "WHERE pali._id = codes._id AND " +
                selection,
                null);
    	return cursor;
    }    
    
    public Cursor search(int volume, String query, String lang) {
 		query = PaliUtils.toUni(query);
 		//volume--;
    	String selection = "";
    	
    	String[] tokens = query.split("\\+");
    	
    	selection = selection + "volume = '" + volume + "'";
    	for(int i=0; i<tokens.length; i++) {
    		//Log.i("Tokens", tokens[i].replace('+', ' '));
    		selection = selection + " AND content LIKE " + "'%" + tokens[i].replace('+', ' ') + "%'";
    	}
    	
    	final Cursor cursor = this.db.query(
    			lang, 
    			new String[] {"_id","volume", "item", "content"}, 
    			selection,
    			null, 
    			null, 
    			null, 
    			null);
    	return cursor;
    }

    public Cursor searchAll(String query, String lang) {
    	final Cursor cursor = this.db.query(
    			lang, 
    			new String[] {"_id","volumn", "page", "items"}, 
    			"content LIKE " + "'%" + query + "%'", 
    			null, 
    			null, 
    			null, 
    			null);
    	return cursor;
    }    
    

    public Cursor dictQuery(String table, String query) {
		final Cursor cursor = db
		.rawQuery(
			"SELECT entry, text FROM "+table+" WHERE entry LIKE '"+query+"%'",
			null
		);		

    	return cursor;
    }    
 
    public Cursor dictQuizQuery(String table, int row) {
		final Cursor cursor = db
		.rawQuery(
			"SELECT entry, text FROM "+table+" LIMIT 1 OFFSET "+row,
			null
		);		

    	return cursor;
    }       

	public Cursor dictQueryEndings(String table, String endings) {
    	final Cursor cursor = this.db.rawQuery(
    			"SELECT entry, text FROM "+table+" WHERE REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(entry,'^',''),'1',''),'2',''),'3',''),'4',''),'5','') IN ("+endings+")",
    			null
    		);	
    	return cursor;		
	}

    public Cursor gotoFromCode(String nik, String vol, String hier, String code) {
        final Cursor cursor = this.db.rawQuery(
                "SELECT pali.volume, item, title FROM pali, codes WHERE pali._id = codes._id AND codes.nikaya = '"+nik+"' AND codes.volume = '"+vol+"' AND codes.hier = '"+hier+"' AND codes.code = '"+code+"'",
                null
        );
        return cursor;
    }

    // The shared path to all app expansion files
    private final static String EXP_PATH = "/Android/obb/";

    static String[] getAPKExpansionFiles(Context ctx, int mainVersion, int patchVersion) {
        String packageName = ctx.getPackageName();
        Vector<String> ret = new Vector<String>();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // Build the full path to the app's expansion files
            File root = Environment.getExternalStorageDirectory();
            File expPath = new File(root.toString() + EXP_PATH + packageName);

            // Check that expansion file path exists
            if (expPath.exists()) {
                if ( mainVersion > 0 ) {
                    String strMainPath = expPath + File.separator + "main." +
                            mainVersion + "." + packageName + ".obb";
                    File main = new File(strMainPath);
                    if ( main.isFile() ) {
                        ret.add(strMainPath);
                    }
                }
                if ( patchVersion > 0 ) {
                    String strPatchPath = expPath + File.separator + "patch." +
                            mainVersion + "." + packageName + ".obb";
                    File main = new File(strPatchPath);
                    if ( main.isFile() ) {
                        ret.add(strPatchPath);
                    }
                }
            }
        }
        String[] retArray = new String[ret.size()];
        ret.toArray(retArray);
        return retArray;
    }

}




/*
CREATE TABLE pali (
    "_id" INTEGER,
    "volume" VARCHAR(3),
    "item" VARCHAR(100),
    "content" TEXT
);
CREATE INDEX idx_pali_volume ON pali (volume);
CREATE INDEX idx_pali_volume_item ON pali (volume, item);
*/