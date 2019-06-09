package com.example.kluggame2;



import com.example.kluggame2.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Owner on 24-Jun-15.
 */
public class ScreenLetsPlay extends Fragment {

	// ImageButton left,right;
	MediaPlayer mp, mp1;
	MainActivity act;
	Animation animation1 ,animation2,animation3,animation4,animation5,animation6,animation7,animation8;
	RotateAnimation r;
	public ScreenLetsPlay() {
		// TODO Auto-generated constructor stub
	}
	public ScreenLetsPlay(MainActivity _act){
		act=_act;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_screen5, null);

		ImageButton left = (ImageButton) view.findViewById(R.id.imageButton1);
		ImageButton right = (ImageButton) view.findViewById(R.id.imageButton2);
		
		final ImageView step =(ImageView)view.findViewById(R.id.thumb);
		final ImageView botforward=(ImageView) view.findViewById(R.id.botforward);
		final ImageView pointer=(ImageView) view.findViewById(R.id.pointer);
		
		final ImageView botclockwise=(ImageView) view.findViewById(R.id.botclockwise);
		
		

		Log.d("Screen 5", "Screen5");
		
		
		
		
		
		animation1=AnimationUtils.loadAnimation(getActivity(), R.anim.steps);
		animation2=AnimationUtils.loadAnimation(getActivity(), R.anim.moveforward);
		animation3=AnimationUtils.loadAnimation(getActivity(), R.anim.rotateangle);
		animation4=AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_a);
		
		animation5=AnimationUtils.loadAnimation(getActivity(), R.anim.steps1);
		animation6=AnimationUtils.loadAnimation(getActivity(), R.anim.moveforwardfive);
		animation7=AnimationUtils.loadAnimation(getActivity(), R.anim.rotateangle1);
		animation8=AnimationUtils.loadAnimation(getActivity(), R.anim.rotate1);
		
		
		
		step.startAnimation(animation1);
		animation1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				botforward.startAnimation(animation2);
			}
		});
		
		
		
		
	

	
	
		
		animation2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg) {
				// TODO Auto-generated method stub
				Log.e("ANIMATINO", "animation start");
			}
			
			@Override
			public void onAnimationRepeat(Animation arg) {
				// TODO Auto-generated method stub
				Log.e("ANIMATINO", "animation rep");
			}
			
			@Override
			public void onAnimationEnd(Animation arg) {
				// TODO Auto-generated method stub
				Log.e("ANIMATINO", "animation end");
				
				pointer.startAnimation(animation3);
			}
		});
	
		
		
		
		
		animation3.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				botclockwise.startAnimation(animation4);
			}
		});
		
		
		animation4.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				step.startAnimation(animation5);
			}
		});
	
		
		
		
		
		
		
		animation5.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				botforward.startAnimation(animation6);
			}
		});
		
		
		
		
	

	
	
		
		animation6.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg) {
				// TODO Auto-generated method stub
				Log.e("ANIMATINO", "animation start");
			}
			
			@Override
			public void onAnimationRepeat(Animation arg) {
				// TODO Auto-generated method stub
				Log.e("ANIMATINO", "animation rep");
			}
			
			@Override
			public void onAnimationEnd(Animation arg) {
				// TODO Auto-generated method stub
				Log.e("ANIMATINO", "animation end");
				
				pointer.startAnimation(animation7);
			}
		});
	
		
		
		
		
		animation7.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				botclockwise.startAnimation(animation8);
			}
		});
		
		
		animation8.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				step.startAnimation(animation1);
			}
		});
	
		
		
		
		
		
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Right Clicked",
				// Toast.LENGTH_SHORT).show();
				
			//	mp = MediaPlayer.create(getActivity(), R.raw.tappercrussive);
			//	mp.start();

			//	((MainActivity) getActivity()).sound.quit();
				((MainActivity)getActivity()).play(1);
			
				((MainActivity) getActivity()).fragmentReplace(51);
			}
		});

		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Left Clicked",
				// Toast.LENGTH_SHORT).show();
			
			//	mp1 = MediaPlayer.create(getActivity(), R.raw.tapcrisp);
			//	mp1.start();

				((MainActivity)getActivity()).play(1);
				//Screen4 hello = new Screen4();
				((MainActivity) getActivity()).fragmentReplace(4);
				
			}
		});

		return view;
	}

}
