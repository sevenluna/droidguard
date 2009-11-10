package com.ccmtc.android.droidguard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DroidGuard extends Activity {
   // private static final int CHOICE_MODE_SINGLE = 1;
    
	private static final int DIALOG_SENSITIVITY_ID = 0;

	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	  super.onCreate(savedInstanceState);   
    	    setContentView(R.layout.settings);   
    	    //绑定XML中的ListView，作为Item的容器   
    	    ListView list = (ListView) findViewById(R.id.lvSettings);   
    	       
    	    //生成动态数组，并且转载数据   
    	    ArrayList<HashMap<String, Object>> mylist = 
    	    	new ArrayList<HashMap<String, Object>>();   
    	    for(int i=0;i<10;i++)   
    	    {   
    	        HashMap<String, Object> map = new HashMap<String, Object>(); 
    	        map.put("ItemImage", R.drawable.icon);
    	        map.put("OpTitle", "This is OpTitle");   
    	        map.put("CurrentOp", "This is CurrentOp");   
    	        mylist.add(map);   
    	    }   
    	    //生成适配器，数组===》ListItem   
    	    SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释   
    	                                                mylist,//数据来源    
    	                                                R.layout.lvitem,//ListItem的XML实现   
    	                                                   
    	                                                //动态数组与ListItem对应的子项           
    	                                                new String[] {"ItemImage","OpTitle", "CurrentOp"},    
    	                                                   
    	                                                //ListItem的XML文件里面的两个TextView ID   
    	                                                new int[] {R.id.ItemImage,R.id.OpTitle,R.id.CurrentOp});   
    	    //添加并且显示   
    	    list.setAdapter(saImageItems);   
    	    
    	   // list.setChoiceMode(CHOICE_MODE_SINGLE);

            list.setOnItemClickListener(
            		new AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> av, View view,
						int position, long viewId) {
					// TODO Auto-generated method stub
					
					
					int poid = av.getSelectedItemPosition();
					
					Log.v("position","click at"+poid);
					showDialog(DIALOG_SENSITIVITY_ID);
				}
            });
        
        
    }
    
    protected Dialog onCreateDialog(int id) {
    	AlertDialog dialog;
        switch(id) {
        case DIALOG_SENSITIVITY_ID:
            
        	AlertDialog.Builder builder;
        	

        	Context mContext = getApplicationContext();
        	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        	View layout = inflater.inflate(R.layout.sen_dialog,
        	                               (ViewGroup) findViewById(R.id.layoutDlg));

        	TextView text = (TextView) layout.findViewById(R.id.text);
        	text.setText("Hello, this is a custom dialog!");
        	ImageView image = (ImageView) layout.findViewById(R.id.logoIcon);
        	image.setImageResource(R.drawable.icon);

        	builder = new AlertDialog.Builder(mContext);
        	builder.setView(layout);
        	dialog = builder.create();
            break;
        default:
            dialog = null;
        }
        return dialog;
    }

}