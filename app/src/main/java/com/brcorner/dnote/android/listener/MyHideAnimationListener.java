package com.brcorner.dnote.android.listener;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class MyHideAnimationListener implements AnimationListener
{
	private View[] views;
	public MyHideAnimationListener(View[] views)
	{
		this.views = views;
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		for (View view : views) {
			view.clearAnimation();
			view.setVisibility(View.GONE);
		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
	}
	
}
