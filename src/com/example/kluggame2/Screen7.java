package com.example.kluggame2;





import com.example.kluggame2.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * Created by Owner on 24-Jun-15.
 */
public class Screen7 extends Fragment {
	EditText et;
	 MediaPlayer mptapcrus,mpcrisp,mprock,mpwooden,mpwarm,mpnetwork;
	 MainActivity act;
	 Button red,blue;
	 ImageButton right;
	 String name="";
	 public void validate()
		{
			String s=name;
			if(s.equals("Klug-Red")||s.equals("Klug-Blue"))
			{
				right.getBackground().setColorFilter(null);				
			 	right.setEnabled(true);
			 	right.setClickable(true);
			}
		}
	 
	  public Screen7() {
		// TODO Auto-generated constructor stub
	}
	  
	 public Screen7(MainActivity _act){
		 act=_act;
		 
	 }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_screen7, null);
		
		
		
		//sound
	/*	mpnetwork=MediaPlayer.create(getActivity(), R.raw.slide_network);
		mpwarm = MediaPlayer.create(getActivity(), R.raw.tap_warm);
		mprock = MediaPlayer.create(getActivity(), R.raw.slide_rock);
		mpwooden = MediaPlayer.create(getActivity(), R.raw.tap_wooden);
		mpcrisp = MediaPlayer.create(getActivity(), R.raw.tapcrisp);
		mptapcrus = MediaPlayer.create(getActivity(), R.raw.tappercrussive);

		et = (EditText) view.findViewById(R.id.edittext1);

		
		//et.setRawInputType(InputType.TYPE_CLASS_TEXT);
		//et.setImeOptions(EditorInfo.IME_ACTION_GO);
		
	//	Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
			//	"fonts/beon.otf");
		//et.setTypeface(face);
		et.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		et.setHint("Enter game name");
		et.setGravity(Gravity.CENTER);
		
		et.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Log.d("OnClick Listener", "edittext");
				et.setHint("");
			}
		});*/

		ImageButton left = (ImageButton) view.findViewById(R.id.imageButton1);
		right = (ImageButton) view.findViewById(R.id.imageButton2);

		red=(Button)view.findViewById(R.id.btnred);
		blue=(Button)view.findViewById(R.id.btnblue);
		
		red.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).play(8);			
				name="Klug-Red";
				validate();
			//	blue.setBackgroundResource(R.drawable.blueu);
				//red.setBackgroundResource(R.drawable.redp);
				
			}});
		
		blue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).play(7);
				name="Klug-Blue";
				validate();
			//	blue.setBackgroundResource(R.drawable.bluep);
			//	red.setBackgroundResource(R.drawable.redu);
				
			}});
		

		Log.d("Screen 7", "Screen7");
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Right Clicked",
				// Toast.LENGTH_SHORT).show();
				//String s=et.getText().toString();
				
				((MainActivity)getActivity()).play(1);
					//mpwarm.start();
			//	ScreenCreateGame hello = new ScreenCreateGame();
				//	Log.e("VV","displaying dialogs "+System.currentTimeMillis());
					act.KlugDialogs(act.CreatingGame,0);
					

		 	//	act.fragmentReplace(9,name);	 
				((MainActivity) getActivity()).fragmentReplace(9,name);
					Log.e("VV","displaying dialogs 3 "+System.currentTimeMillis());
					
				
			}
		});
		right.getBackground().setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
		right.setEnabled(false);

		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			
				//mpcrisp.start();
				((MainActivity)getActivity()).play(2);
				Screen6 hello = new Screen6();
				((MainActivity) getActivity()).fragmentReplace(6);
				
			}
		});

	/*	view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						0);
				return true;

				
			}
		});*/

		return view;
	}

}
