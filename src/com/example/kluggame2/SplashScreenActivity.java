package com.example.kluggame2;



import com.example.kluggame2.R;

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SplashScreenActivity extends Fragment {

	
    private static int SPLASH_TIME_OUT = 3000;
    MediaPlayer mp;
   
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	
    	View view=inflater.inflate(R.layout.activity_splash_screen, null);
    	// mp = MediaPlayer.create(getActivity(), R.raw.splash);
			//mp.start();
    	 new Handler().postDelayed(new Runnable() {

             /*
              * Showing splash screen with a timer. This will be useful when you
              * want to show case your app logo / company
              */

             @Override
             public void run() {
                 // This method will be executed once the timer is over
                 // Start your app main activity
            	//Screen6 hello=new Screen6();
            //	 Screen2 hello=new Screen2();
            	// Screen7 hello=new Screen7();
            	// StartGame hello=new StartGame();
             	//ScreenCreateGame hello=new ScreenCreateGame();
            	
            	 
            	 
             ((MainActivity)getActivity()).fragmentReplace(2);
             }
         }, SPLASH_TIME_OUT);
    	
    	
    	
    	return view;
    }
    
    
    
    
    
    
  



   

   }
