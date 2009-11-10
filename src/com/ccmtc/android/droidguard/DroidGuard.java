package com.ccmtc.android.droidguard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DroidGuard extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	  super.onCreate(savedInstanceState);   
    	    setContentView(R.layout.settings);   
    	    //��XML�е�ListView����ΪItem������   
    	    ListView list = (ListView) findViewById(R.id.lvSettings);   
    	       
    	    //���ɶ�̬���飬����ת������   
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
    	    //����������������===��ListItem   
    	    SimpleAdapter saImageItems = new SimpleAdapter(this, //ûʲô����   
    	                                                mylist,//������Դ    
    	                                                R.layout.lvitem,//ListItem��XMLʵ��   
    	                                                   
    	                                                //��̬������ListItem��Ӧ������           
    	                                                new String[] {"ItemImage","OpTitle", "CurrentOp"},    
    	                                                   
    	                                                //ListItem��XML�ļ����������TextView ID   
    	                                                new int[] {R.id.ItemImage,R.id.OpTitle,R.id.CurrentOp});   
    	    //��Ӳ�����ʾ   
    	    list.setAdapter(saImageItems);   

            list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long viewId) {
					// TODO Auto-generated method stub
					
				}
            });
        
        
    }
}