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
import android.widget.ImageButton;

/**
 * Created by Owner on 24-Jun-15.
 */
public class Screen6 extends Fragment {

	MediaPlayer mpwarm,mpwooden;
	MainActivity act;
	// ImageButton left,right,guide,settings;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_screen6, null);
		
	//	mpwarm = MediaPlayer.create(getActivity(), R.raw.tap_warm);
		
	//	mpwooden = MediaPlayer.create(getActivity(), R.raw.tap_wooden);

		ImageButton left = (ImageButton) view.findViewById(R.id.imageButton1);
		ImageButton right = (ImageButton) view.findViewById(R.id.imageButton2);
		ImageButton back =(ImageButton)view.findViewById(R.id.imageButtonbck);

		Log.d("Screen 6", "Screen6");
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Right Clicked",
				// Toast.LENGTH_SHORT).show();
				//mpwooden.start();
			//	ScreenJoinGame hello = new ScreenJoinGame();
				//act.fragmentReplace(8,"");
				((MainActivity)getActivity()).play(8);
				((MainActivity) getActivity()).fragmentReplace(8,"");

			}
		});

		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Left Clicked",
				// Toast.LENGTH_SHORT).show();
				//mpwarm.start();
			//	Screen7 hello = new Screen7();
				((MainActivity)getActivity()).play(7);
				((MainActivity) getActivity()).fragmentReplace(7);
				
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).play(2);
				((MainActivity)getActivity()).fragmentReplace(8);
			}
		});

		return view;
	}

}
