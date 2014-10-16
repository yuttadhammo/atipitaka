package org.yuttadhammo.tipitaka;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class TipitakaBackupAgent extends BackupAgentHelper {
	// The name of the SharedPreferences file
	static final String PREFS = "org.yuttadhammo.tipitaka_preferences";

	// A key to uniquely identify the set of backup data
	static final String PREFS_BACKUP_KEY = "prefs";

	// Allocate a helper and add it to the backup agent
	public void onCreate() {
	    SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS);
	    addHelper(PREFS_BACKUP_KEY, helper);
	}
}
