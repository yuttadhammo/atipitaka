package  org.yuttadhammo.tipitaka;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class BookmarkTabWidget extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark);
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		//~ intent = new Intent().setClass(this, BookmarkThaiActivity.class);
		//~ spec = tabHost.newTabSpec("thai").setIndicator(this.getString(R.string.th_lang)).setContent(intent);
		//~ tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, BookmarkPaliActivity.class);
		spec = tabHost.newTabSpec("pali").setIndicator(this.getString(R.string.pl_lang)).setContent(intent);
		tabHost.addTab(spec);
		if(BookmarkTabWidget.this.getIntent().getExtras() != null) {
			tabHost.setCurrentTab(0);
		}
		
	}
}