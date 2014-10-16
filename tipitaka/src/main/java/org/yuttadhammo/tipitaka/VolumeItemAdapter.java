package org.yuttadhammo.tipitaka;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import android.graphics.Typeface;

public class VolumeItemAdapter extends ArrayAdapter<String>
{
    private Typeface font;
	private int ti;
	private int lp;
	private Context context;
	private String[] list;

    public VolumeItemAdapter(Context context, int layoutId, int textViewResourceId, String[] list) 
    {
        super(context, layoutId, textViewResourceId, list);
        
        this.context = context;
        this.list = list;
        font = Typeface.createFromAsset(context.getAssets(), "verajjan.ttf");
        this.ti = textViewResourceId;
    }

	@Override  
	public View getView(int position, View view, ViewGroup viewGroup)
	{
		View v = super.getView(position, view, viewGroup);
		
		String[] names = context.getResources().getStringArray(R.array.volume_names);

        int scriptIndex = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("text_script","0"));

        String name = names[Integer.parseInt(list[position])];
        name = PaliUtils.translit(name,scriptIndex);

        TextView tv = (TextView)v.findViewById(ti);
		tv.setText(name);
		tv.setTypeface(font);
		return v;
	}

}
