package com.ccmtc.android.droidguard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DroidGuard extends Activity {
   // private static final int CHOICE_MODE_SINGLE = 1;
    
	public static final int DIALOG_SENSITIVITY_ID = 0;
	protected static final int LISTITEM_SEN_DIALOG = 0;
	protected static final int DIALOG_ALT_ID = 1;
	protected static final int LISTITEM_ALT_DIALOG = 1;

	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	  super.onCreate(savedInstanceState);   
    	    setContentView(R.layout.settings);   
    	    //绑定XML中的ListView，作为Item的容器   
    	    ListView list = (ListView) findViewById(R.id.lvSettings);   
    	       
    	    SimpleAdapter sa = CreatSettingList();
    	    list.setAdapter(sa);   
    	    
    	   // list.setChoiceMode(CHOICE_MODE_SINGLE);

            list.setOnItemClickListener(
            		new AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> av, View view,
						int pos, long viewId) {
					// TODO Auto-generated method stub
					Log.d("before","before swtich");
					switch(pos){
					case LISTITEM_SEN_DIALOG:
						showDialog(DIALOG_SENSITIVITY_ID);
						break;
					case LISTITEM_ALT_DIALOG:
						showDialog(DIALOG_ALT_ID);
						break;
					default:
						;
					}	
					
					Log.d("position","click at "+pos);					
				}
            });
        
        
    }
    
    /* 对话框生成器
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog(int id) {
    	AlertDialog dialog;
    	Builder builder;
        switch(id) {
        case DIALOG_SENSITIVITY_ID:
            
        	Context mContext = getApplicationContext();
        	LayoutInflater inflater = 
        		(LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        	View contentlayout = 
        		inflater.inflate(R.layout.sen_dialog,
        	                               (ViewGroup) findViewById(R.id.layoutDlg));

       	    ImageView image = 
       	    	(ImageView) contentlayout.findViewById(R.id.logoIcon);
        	image.setImageResource(R.drawable.icon);
        	
        	SeekBar sb = (SeekBar)contentlayout.findViewById(R.id.seekBar);
        	sb.setMax(4);
        	sb.setProgress(2);
        	sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
        		public void onProgressChanged(SeekBar s, int progress, boolean touch){
        			Log.d("seekbar",""+progress);
        		}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
        	});
        	
        	builder = new AlertDialog.Builder(this);
        	builder.setTitle(R.string.sensitivity)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	//保存灵敏度
                }
            })
        	.setView(contentlayout);

        	dialog = builder.create();
        	return dialog;
        	
        case DIALOG_ALT_ID:
        	
        	final CharSequence[] items = {"Ringtone", "Vibration", "Calling"};
        	final boolean[] checkedItems = {true,false,false};
        	
        	
        	
        	builder = new AlertDialog.Builder(this);
        	builder.setTitle("Alert ways")
        	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	//确认，并保存checkedItems
                }
            });
        	builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which,
						boolean isChecked) {
					// TODO Auto-generated method stub
					checkedItems[which] = isChecked;
					Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_SHORT).show();
				}
        	});      	
        	return builder.create();        	
        default:
            return null;
        }
    }
    
private SimpleAdapter CreatSettingList(){
	    ArrayList<HashMap<String, Object>> mylist = 
	    	new ArrayList<HashMap<String, Object>>(); 
	    
        HashMap<String, Object> map0 = new HashMap<String, Object>();       
        map0.put("ItemImage", R.drawable.icon);
        map0.put("OpTitle", getText(R.string.sensitivity));   
        map0.put("CurrentOp", "This is sensitivity"); 
        mylist.add(map0);  
        
        HashMap<String, Object> map1 = new HashMap<String, Object>();       
        map1.put("ItemImage", R.drawable.icon);
        map1.put("OpTitle", getText(R.string.alt_ways));   
        map1.put("CurrentOp", "This is alt_ways"); 
        mylist.add(map1);  
        
        HashMap<String, Object> map2 = new HashMap<String, Object>();       
        map2.put("ItemImage", R.drawable.icon);
        map2.put("OpTitle", getText(R.string.about_us));   
        map2.put("CurrentOp", ""); 
        mylist.add(map2);  
        
	    return new SimpleAdapter(this,   
                mylist,//数据来源    
                R.layout.lvitem,//ListItem的XML实现   
                   
                //动态数组与ListItem对应的子项           
                new String[] {"ItemImage","OpTitle", "CurrentOp"},    
                new int[] {R.id.ItemImage,R.id.OpTitle,R.id.CurrentOp});      
}

private void InitSettings(){
	
}

}