package android.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ScrollDotView extends RelativeLayout  {

	private ViewPager viewpager=null;
	private LinearLayout linearlayout=null;
	private LinearLayout linearlayout1=null;
	private ImageButton  imgclose=null;
	//图片的那些点
	private List<View> dots=new ArrayList<View>();
	private ScrollAdapter vpAdapter;
	private int currentindex=0;
	private ArrayList<View> views;
	private ScheduledExecutorService scheduledExecutorService;
	private int oldPosition = 0;
	private Boolean isshowclose=true;
	//轮播时间（秒)
	private int loopTime=2;
	public ScrollDotView(Context context)
	{
		super(context,null);
	}
	public ScrollDotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		viewpager=new ViewPager(context);
		viewpager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		viewpager.setOnPageChangeListener(new ScrollPageChangeListener());
		this.addView(viewpager);
		imgclose=new ImageButton(context);
		imgclose.setBackgroundResource(R.drawable.close);
		LayoutParams imgparms=new LayoutParams(30,30);
		imgparms.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		imgparms.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		imgparms.topMargin=5;
		imgparms.rightMargin=5;
		imgclose.setLayoutParams(imgparms);
		
		this.addView(imgclose);
		imgclose.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout llLayout=((RelativeLayout)imgclose.getParent());
				llLayout.removeAllViews();
				ViewGroup group=(ViewGroup) llLayout.getParent();
				if(group!=null)
				{
				  group.removeView(llLayout);
				}
			}
		});
		linearlayout=new LinearLayout(context);
		LayoutParams parms=new LayoutParams(LayoutParams.MATCH_PARENT,35);
		parms.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		linearlayout.setLayoutParams(parms);
		linearlayout.setGravity(Gravity.CENTER);
		linearlayout.setBackgroundColor(Color.parseColor("#33000000"));
		linearlayout.setOrientation(LinearLayout.VERTICAL);
		
		
		linearlayout1=new LinearLayout(context);
		LayoutParams parms1=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		parms.topMargin=3;
		linearlayout1.setLayoutParams(parms1);
		linearlayout1.setGravity(Gravity.CENTER);
		linearlayout.addView(linearlayout1);
		addView(linearlayout);
		
		// TODO Auto-generated constructor stub
	}
	// 切换当前显示的图片
		private Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				viewpager.setCurrentItem(currentindex);// 切换当前显示的图片
				
				
			};
		};
	/*
	 * 设置图片
	 */
	public void setBackImages(List<ImageView> images) {
		views = new ArrayList<View>();  
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
	    LinearLayout.LayoutParams.WRAP_CONTENT);
		for(int i=0;i<images.size();i++)
		{
		  ImageView img=images.get(i);
		  img.setLayoutParams(mParams);
		  views.add(img);
		}
		vpAdapter=new ScrollAdapter(views);
		viewpager.setAdapter(vpAdapter);
		InitDots();
	}
	/*
	 * 开始轮播
	 */
	public void StartLoop()
	{
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, loopTime, TimeUnit.SECONDS);
	}
	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewpager) {
			
				currentindex = (currentindex + 1) % views.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}
	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentindex = position;
			
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
    /**
     *这只当前引导小点的选中 
     */
    private void setCurDot(int positon)
    {
		if (positon < 0 || positon > views.size() - 1 || currentindex == positon) {
			return;
		}
		currentindex = positon;
		dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
		dots.get(positon).setBackgroundResource(R.drawable.dot_focused);
		oldPosition = positon;
    }
	/*
	 * 初始化底部小点
	 */
	private void InitDots() {
		
		for(int i=0;i<views.size();i++)
		{
			View dot=new ImageView(this.getContext());
			LinearLayout.LayoutParams parms=new LinearLayout.LayoutParams(10, 10);
			parms.leftMargin=2;
			dot.setLayoutParams(parms);
			dot.setBackgroundResource(R.drawable.dot_normal);
			dots.add(dot);
			linearlayout1.addView(dot);
		
		}
		dots.get(currentindex).setBackgroundResource(R.drawable.dot_focused);
		
	}
	public int getLoopTime() {
		return loopTime;
	}
	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}
	public Boolean getIsshowclose() {
		return isshowclose;
	}
	public void setIsshowclose(Boolean isshowclose) {
		this.isshowclose = isshowclose;
		if(!isshowclose)
		{
			imgclose.setVisibility(View.GONE);
		}
	}
	
}
