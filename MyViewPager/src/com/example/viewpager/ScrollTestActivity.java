
package com.example.viewpager;

import java.util.ArrayList;
import java.util.List;

import com.example.R;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollDotView;
import android.widget.ImageView.ScaleType;
public class ScrollTestActivity extends Activity {
	private ScrollDotView scrollView=null;
	private int[] imageResId;
	public ScrollTestActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scrolltest);
		List<ImageView> imageViews=new ArrayList<ImageView>();
		imageResId = new int[] { R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e };
		Resources res=getResources();
		//Drawable drawable=Drawable.
		for(int i=0;i<imageResId.length;i++)
		{
		  ImageView imageView = new ImageView(this);
		  Bitmap bmp=BitmapFactory.decodeResource(res, imageResId[i]);
		  //imageView.setImageResource(imageResId[i]);
		  imageView.setImageBitmap(bmp);
		  imageView.setScaleType(ScaleType.CENTER_CROP);
		  imageViews.add(imageView);
		}
		scrollView=(ScrollDotView) findViewById(R.id.view_scroll);
		scrollView.setBackImages(imageViews);
		scrollView.setLoopTime(2);
		scrollView.setIsShowClose(true);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		scrollView.startLoop();
	}

}
