package com.example.kluggame2;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.JSONObject;

import com.example.kluggame2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ScreenAboutYourself extends Fragment implements JsonInterface{

	public static String TAG="ScreenAboutYourself";
	PlayerInfo p;
	MainActivity act;
	private SeekBar seekbar;
	User x=new User();
	 private TextView resultage,txtname,txtAge,txtGender,txt00,txtmale,txtfemale,txtlabel;
	 private RadioButton rbtnmale;
	 private RadioButton rbtnfemale;
	 private Button btnsave,btnback;
	 private EditText edName;
	public static String jsonString;
	 
	SaveExternalStorage saveext;
	String playername="";

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);


			txtname=(TextView) view.findViewById(R.id.textName);
			txtAge=(TextView) view.findViewById(R.id.textAge);
			txtGender=(TextView) view.findViewById(R.id.textGender);
			txt00=(TextView) view.findViewById(R.id.textOO);
			txtmale=(TextView) view.findViewById(R.id.textmale);
			txtfemale=(TextView) view.findViewById(R.id.textFemale);
			txtlabel=(TextView)view.findViewById(R.id.textlabel);
			
			
			resultage=(TextView) view.findViewById(R.id.textResultAge);
			seekbar=(SeekBar)view.findViewById(R.id.seekBar1);
			rbtnmale=(RadioButton)view.findViewById(R.id.radioButtonMale);
			rbtnfemale=(RadioButton)view.findViewById(R.id.radioButtonFemale);
			btnsave=(Button)view.findViewById(R.id.btnSave);
			btnback=(Button)view.findViewById(R.id.Buttonbck);
			edName=(EditText)view.findViewById(R.id.edittext1);
			
			Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/beon.ttf");
		//	Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(),
					//"fonts/carterone.TTF");
			
			edName.setTypeface(face);
			resultage.setTypeface(face);
			txtlabel.setTypeface(face);
			//edName.setTypeface(face);
		//	edName.setTypeface(face);
			edName.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
			edName.setText("");
			edName.addTextChangedListener(new TextWatcher() {
	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {

	                // TODO Auto-generated method stub
	            	validate();
	            }

	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	                // TODO Auto-generated method stub
	            	//validate();
	            }

	            @Override
	            public void afterTextChanged(Editable s) {

	                // TODO Auto-generated method stub
	            	//validate();
	            }
	        });
			txtname.setTypeface(face);
			txtAge.setTypeface(face);
			txtGender.setTypeface(face);
			txt00.setTypeface(face);
			txtmale.setTypeface(face);
			txtfemale.setTypeface(face);
			resultage.setTypeface(face);
			
			btnback.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((MainActivity) getActivity()).play(2);
					((MainActivity) getActivity()).fragmentReplace(5);
				}
			});
			
			btnsave.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				//	Screen6 hello = new Screen6();
					//adding validation
					((MainActivity) getActivity()).play(2);
					if(validate())
					{
						//saving the data
						x=new User();
						x.name=edName.getText().toString();
						x.age=Integer.parseInt(resultage.getText().toString());
						x.gender=rbtnfemale.isChecked()?"F":"M";
						//Log.e("ZZ","user details are "+x.name+":"+x.age+":"+x.gender);
						
						p=new PlayerInfo();
						p.setName(edName.getText().toString());
						p.setAge(resultage.getText().toString());
						p.setGender(rbtnfemale.isChecked()?"F":"M");
						//p.setSc(new ScoreCardModel(111,211, 311, 411, "a", "b", "c", "d"));
						
						
						
						//store data in json
						jsonString=toJson();
						Log.d("Json String", jsonString);
						
						playername=edName.getText().toString();
						//save to external sd
						if(jsonString!=null){
							saveext=new SaveExternalStorage();
							SaveExternalStorage ses=new SaveExternalStorage();

							
							saveext.writeInternalStorage(jsonString,playername);
							
							//String readex=ses.readfromExternalStorage("sonscorecard.txt");
							//String readin=ses.readInternalStorage("sonscorecard.txt");
							
							//Log.d("READX", readex);
							//Log.d("READIN", readin);
							
							
							//Log.d(TAG,""+edName.getText().toString().concat(".txt"));
						}
						
						((MainActivity)getActivity()).sharedata(playername);

						((MainActivity) getActivity()).fragmentReplace(6);
						//act.fragmentReplace(6);
						
					}
					else
					{
						//show message dialog
					}
					
				}
			});
			
			btnsave.setVisibility(View.INVISIBLE);
			
			rbtnfemale.setChecked(false);
			rbtnmale.setChecked(false);
			rbtnfemale.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((MainActivity) getActivity()).play(4);
					if(rbtnfemale.isChecked()){
						rbtnmale.setChecked(false);
					}else{
						rbtnfemale.setChecked(true);
					}
					validate();
				}
			});
			
			
			
			rbtnmale.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((MainActivity) getActivity()).play(4);
					if(rbtnmale.isChecked()){
						rbtnfemale.setChecked(false);
					}else{
						rbtnmale.setChecked(true);
					}
					validate();
				}
			});
			
			
			resultage.setText(""+seekbar.getProgress());
			seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				int progressval=0;
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					resultage.setText(""+progressval);
					validate();
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					((MainActivity) getActivity()).play(3);
					progressval=progress;
					resultage.setText(""+progressval);
					validate();
					// TODO Auto-generated method stub
					
				}
			});
			
			
			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
							0);
					return true;

					
				}
			});
			
			PlayerInfo p=getUserProfile();
			if(p!=null && p.name.length()>=0)
			{
			resultage.setText(""+p.age);
			seekbar.setProgress(Integer.parseInt(p.age));
			if(p.gender.equals("M"))
			{
				rbtnmale.setChecked(true);
				rbtnfemale.setChecked(false);
			}
			else if(p.gender.equals("F"))
			{
				rbtnmale.setChecked(false);
				rbtnfemale.setChecked(true);
				
			}
			edName.setText(p.name);
			}
			
			
			

		}

		SortedSet<File> modificationOrder = new TreeSet<File>(new Comparator<File>() {
		    public int compare(File a, File b) {
		        return (int) (a.lastModified() - b.lastModified());
		    }
		});


		
     public PlayerInfo getUserProfile()
     {
    	String dir=MainActivity.getUserDir();
    	File myDir=new File(dir);
    	
 		for (File file : myDir.listFiles(new FilenameFilter() {
 		    public boolean accept(File dir, String name)
 		    {
 		        return (name.endsWith(".json")); 
 		    }
 		})){
		    modificationOrder.add(file);
		}
 		File last=null;
 		if(modificationOrder.size()>0)
 		{
		last = modificationOrder.last();
 		}
 		
		if(last!=null)
		{
			String path=last.getAbsolutePath();
			String profile=SaveExternalStorage.readFile(path);
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();			
			PlayerInfo p=gson.fromJson(profile, PlayerInfo.class);
			return p;			
		}
		else
		{
			return null;
		}
		
		
     }
     
	 public boolean validate()
	 {
		 try
		 {
			if(edName==null || edName.length()<=0 || edName.equals(""))
			{
				btnsave.setVisibility(View.INVISIBLE);
				return false;
			}
			if(Integer.parseInt(resultage.getText().toString())<=0)
			{
				btnsave.setVisibility(View.INVISIBLE);
				return false;
			}
			if(!rbtnfemale.isChecked() && !rbtnmale.isChecked()){
				btnsave.setVisibility(View.INVISIBLE);
				return false;
			}
		 }
		 catch(Exception e)
		 {
			 Log.e("ZZ",""+e.getMessage());
			 btnsave.setVisibility(View.INVISIBLE);
			 return false;
		 }
		 	btnsave.setVisibility(View.VISIBLE);
			return true;
	 }
	 
	 public class User
	 {
		 String name;
		 String gender;
		 int age;
		 
	 }

	 View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view =inflater.inflate(R.layout.layout_tell_me_about_yourself, container,false);
		
		
		
		
		return view;
	}
	
	
	@Override
	public String toJson() {
		
		
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONObject j;
		String l2 = new String();
		try {
			// j = new JSONObject(local);
			l2 = gson.toJson(p, PlayerInfo.class);
			
			// for(int i=0;i<l2.length;i++)
			// {
			// / l1.add(l2[i]);
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return l2;
		/*
	//PlayerInfo s=new PlayerInfo();
	JSONObject jsonobj =new JSONObject();
		try {
			
			jsonobj.put("name", p.getName());
			jsonobj.put("age", p.getAge());
			jsonobj.put("gender", p.getGender());
			
			//JSONObject json1=new JSONObject();
			//json1.put("task completed", ""+p.sc.getBubbleimage());
			
			//jsonobj.put("task", json1);
			
			return jsonobj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return jsonobj.toString();
		*/
	}
}
