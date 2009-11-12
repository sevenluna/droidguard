package com.ccmtc.android.droidguard;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    
    private Integer[] mImageIds = {
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.icon,
    };
    
    public ImageAdapter(Context c) {
        mContext = c;
//        TypedArray a = obtainStyledAttributes(android.R.styleable.Theme);
//        mGalleryItemBackground = a.getResourceId(
//                android.R.styleable.Theme_galleryItemBackground, 0);
//        a.recycle();
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        i.setImageResource(mImageIds[position]);
        i.setLayoutParams(new Gallery.LayoutParams(150, 100));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        //i.setBackgroundResource(mGalleryItemBackground);

        return i;
	}

}



