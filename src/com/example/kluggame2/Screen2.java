package com.example.kluggame2;


import com.example.kluggame2.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;



public class Screen2 extends Fragment{

  //  ImageButton left,right;
	MediaPlayer mp;
	//MediaPlayer mp1=MediaPlayer .create(getActivity(), R.raw.tapcrisp);
	Context context;
	MainActivity act;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_screen2, null);

        
        
       
        ImageButton right=(ImageButton)view.findViewById(R.id.imageButton2);

        
        
        Log.d("Screen 2","Screen2");
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
    
            	
            	//mp=MediaPlayer .create(getActivity(), R.raw.tappercrussive);
            //	mp.start();
  
            	Log.d("ONclickk", "click");
            	Screen3 hello=new Screen3();
            	
            	//act.fragmentReplace(3);
            	((MainActivity)getActivity()).play(2);
            	((MainActivity)getActivity()).fragmentReplace(3);

            }
        });


     
        return view;


    }




}
