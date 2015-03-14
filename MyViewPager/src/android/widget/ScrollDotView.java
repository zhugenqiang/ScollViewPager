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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/** 
* @ClassName: ScrollDotView 
* @Description:轮播控件组件类 
* @author 朱根强
* @date 2015年3月14日 上午11:31:54 
*  
*/
public class ScrollDotView extends RelativeLayout  {

	private ViewPager viewPager=null;
	private LinearLayout linearLayout=null;
	private LinearLayout linearLayout1=null;
	private ImageButton  imgClose=null;
	//图片的那些点
	private List<View> dotImages=new ArrayList<View>();
	private ScrollAdapter vpAdapter;
	private int currentIndex=0;
	private ArrayList<View> imageViews;
	private ScheduledExecutorService scheduledExecutorService;
	private int oldPosition = 0;
	private Boolean isShowClose=true;
	
	/** 
	* @Fields loopTime : 轮播周期（秒) 
	*/ 
	private int loopTime=2;
	public ScrollDotView(Context context)
	{
		super(context,null);
	}
	public ScrollDotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		viewPager=new ViewPager(context);
		viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		viewPager.setOnPageChangeListener(new ScrollPageChangeListener());
		this.addView(viewPager);
		imgClose=new ImageButton(context);
		imgClose.setBackgroundResource(R.drawable.close);
		LayoutParams imgParms=new LayoutParams(30,30);
		imgParms.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		imgParms.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		imgParms.topMargin=5;
		imgParms.rightMargin=5;
		imgClose.setLayoutParams(imgParms);
		
		this.addView(imgClose);
		imgClose.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout llLayout=((RelativeLayout)imgClose.getParent());
				llLayout.removeAllViews();
				ViewGroup viewGroup=(ViewGroup) llLayout.getParent();
				if(viewGroup!=null)
				{
				  viewGroup.removeView(llLayout);
				}
			}
		});
		linearLayout=new LinearLayout(context);
		LayoutParams parms=new LayoutParams(LayoutParams.MATCH_PARENT,35);
		parms.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		linearLayout.setLayoutParams(parms);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setBackgroundColor(Color.parseColor("#33000000"));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		
		
		linearLayout1=new LinearLayout(context);
		LayoutParams layoutParms1=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		parms.topMargin=3;
		linearLayout1.setLayoutParams(layoutParms1);
		linearLayout1.setGravity(Gravity.CENTER);
		linearLayout.addView(linearLayout1);
		addView(linearLayout);
		
		// TODO Auto-generated constructor stub
	}
	// 切换当前显示的图片
		private Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				viewPager.setCurrentItem(currentIndex);// 切换当前显示的图片
				
				
			};
		};
	/*
	 * 设置图片
	 */
	public void setBackImages(List<ImageView> images) {
		imageViews = new ArrayList<View>();  
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
	    LinearLayout.LayoutParams.WRAP_CONTENT);
		for(int i=0;i<images.size();i++)
		{
		  ImageView img=images.get(i);
		  img.setLayoutParams(mParams);
		  imageViews.add(img);
		}
		vpAdapter=new ScrollAdapter(imageViews);
		viewPager.setAdapter(vpAdapter);
		initDots();
	}
	/*
	 * 开始轮播
	 */
	public void startLoop()
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
			synchronized (viewPager) {
			
				currentIndex = (currentIndex + 1) % imageViews.size();
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
			currentIndex = position;
			
			dotImages.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dotImages.get(position).setBackgroundResource(R.drawable.dot_focused);
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
		if (positon < 0 || positon > imageViews.size() - 1 || currentIndex == positon) {
			return;
		}
		currentIndex = positon;
		dotImages.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
		dotImages.get(positon).setBackgroundResource(R.drawable.dot_focused);
		oldPosition = positon;
    }
	/*
	 * 初始化底部小点
	 */
	private void initDots() {
		
		for(int i=0;i<imageViews.size();i++)
		{
			View dot=new ImageView(this.getContext());
			LinearLayout.LayoutParams parms=new LinearLayout.LayoutParams(10, 10);
			parms.leftMargin=2;
			dot.setLayoutParams(parms);
			dot.setBackgroundResource(R.drawable.dot_normal);
			dotImages.add(dot);
			linearLayout1.addView(dot);
		
		}
		dotImages.get(currentIndex).setBackgroundResource(R.drawable.dot_focused);
		
	}
	public int getLoopTime() {
		return loopTime;
	}
	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}
	public Boolean getIsshowclose() {
		return isShowClose;
	}
	public void setIsshowclose(Boolean isshowclose) {
		this.isShowClose = isshowclose;
		if(!isshowclose)
		{
			imgClose.setVisibility(View.GONE);
		}
	}
	
}
