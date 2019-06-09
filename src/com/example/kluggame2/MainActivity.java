package com.example.kluggame2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.bluetooth.BluetoothInterface;
import com.example.kluggame2.R;
import com.example.wifi.WifiApControl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unity3d.player.UnityPlayerNativeActivity;

import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.Paint.Join;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends UnityPlayerNativeActivity implements
		FragmentCallback, OnClickListener, KDialogInterface, SharedataInterface {

	public static String TAG = "KlugTek/Chakraviuh";
	public static String sharedata;
	String frag1;

	public static Boolean wrongpawntouched = false;
	public static Boolean nopawnstouched = false;

	MediaPlayer soundangle1, soundintro, soundsteps, soundmainscreen;

	Animation animation;
	WifiApControl wifiApControl;
	WifiManager wifi ;
	String frag;

	public class GameData {
		String playername;
		String gamename;
		int max_players;
		int level;
		int maxRound;
		int maxNumber;

		public GameData() {
			max_players = 4;
		}
	}

	public static String toJson(GameSettings p) {

		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONObject j;
		String l2 = new String();
		try {
			// j = new JSONObject(local);
			l2 = gson.toJson(p, GameSettings.class);

			// for(int i=0;i<l2.length;i++)
			// {
			// / l1.add(l2[i]);
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return l2;

	}

	public class GameSettings {
		public String GameName;
		public int max_players;
		int level;
		int maxRound;
		int maxNumber;

	}

	public static void SaveSettings() {
		GameSettings s1 = getSettings1();
		// String
		// settings=SaveExternalStorage.readFile(getSettingsDir()+"/game_settings.json");
		if (s1 != null) {
			// saveext=new SaveExternalStorage();
			// SaveExternalStorage ses=new SaveExternalStorage();

			s1.level = gdata.level;
			s1.maxNumber = gdata.maxNumber;
			SaveExternalStorage.writeFile(MainActivity.toJson(s1),
					getSettingsDir() + "/game_settings.json");
			Log.e("ZZ", "completed writing to file " + s1.level + ":"
					+ s1.maxNumber);
			// String readex=ses.readfromExternalStorage("sonscorecard.txt");
			// String readin=ses.readInternalStorage("sonscorecard.txt");

			// Log.d("READX", readex);
			// Log.d("READIN", readin);

			// Log.d(TAG,""+edName.getText().toString().concat(".txt"));
		}

	}

	public static GameSettings getSettings1() {
		String settings = SaveExternalStorage.readFile(getSettingsDir()
				+ "/game_settings.json");
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONObject j1;
		try {
			j1 = new JSONObject(settings);
			GameSettings s = gson.fromJson(j1.toString(), GameSettings.class);
			return s;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;

		}

	}

	public void getSettings() {
		String settings = SaveExternalStorage.readFile(getSettingsDir()
				+ "/game_settings.json");
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONObject j1;
		try {
			j1 = new JSONObject(settings);
			GameSettings s = gson.fromJson(j1.toString(), GameSettings.class);
			
			
			Log.e("ZZZ", "AXAX" + gdata.max_players + ":" + gdata.maxNumber
					+ ":" + gdata.level + ":" + gdata.level);
			TAG = s.GameName;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static GameData gdata;
	public static String ExternDir = "/mnt/sdcard/" + TAG;// Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+TAG;
	BluetoothInterface binterface;
	static AndroidUnityInterface uinterface;
	static ScreenCreateGame screencreategame = null;
	static ScreenJoinGame screenjoingame = null;
	static StartGame startgame = null;
	static AndroidUnityInterface uinterfacex=null;

	// static ScoreCard sc=null;

	public static String getmaxNumber() {
		return "" + gdata.maxNumber;
	}

	public static int getMaxRound() {
		if (gdata != null) {
			return gdata.maxRound;
		}
		return 5;
	}

	public static String getLevel() {
		if (gdata != null) {
			return "" + gdata.level;
		}
		return "" + 3;
	}

	public static String getExternDir() {
		ExternDir = "/mnt/sdcard/" + TAG + "/";// Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+TAG;
		return ExternDir;
	}

	public static String getGameName() {
		return gdata.gamename;
	}

	public static String getPlayerName() {
		return gdata.playername;
	}

	public static String getUserDir() {
		return getExternDir() + "Users" + "/";

	}

	public static String getSettingsDir() {
		return getExternDir() + "Settings" + "/";
	}

	public static String getPuzzlesDir() {
		return getExternDir() + "Puzzles" + "/";
	}

	Context context;
	SoundHandler sound;

	ScreenLetsPlay letsplay;
	Screen2 s2;
	Screen3 s3;
	Screen4 s4;
	Screen6 s6;
	Screen7 s7;
	ScreenAboutYourself s51;

	private WakeLock wakeLock;

	public int resetBot = R.layout.popup_resetbot;
	public int botMoving = R.layout.popup_botmoving;
	public int observebot = R.layout.popup_observe;
	public int OutsideArena = R.layout.popup_outsidearena;

	public int WrongPawnTouched = R.layout.popup_wrongpawn_touched;
	public int NoPawnTouched = R.layout.popup_nopawns_were_touched;

	public int PlayerWaiting = R.layout.popup_waitingforplayerto_agree;
	public int Foulgame = R.layout.layout_foul_screen;
	public int CreatingGame = R.layout.popup_connecting_to_drive;
	public int WaitHost = R.layout.alert_layer;

	public int ErrorCreatingGame = R.layout.popup_error_creating_game;
	public int StartGame = R.layout.popup_startgame;

	public int ErrorJoiningGame = R.layout.popup_error_joining_game;
	public int Disconnected = R.layout.popup_game_disconnected;
	public int ExitGame = R.layout.popup_do_you_want_to_exit;
	
	 public int Instructions=R.layout.popup_instructions;

	public void BluetoothCallback1() {
		Log.e("ZZZZ", "Calling end bot");
		if (startgame != null) {
			startgame.EndBot();
		}
	}

	public void BluetoothCallback2() {
		Log.e("ZZZZ", "Calling reset bot");
		// if (startgame != null) {
		// startgame.TurnChange();
		// }

		if (uinterface != null) {
			uinterface.sendUnityMessage(AndroidUnityInterface.Reset, "");
		}
	}

	public void BluetoothCallback(int status) {
		Log.e("ZZ", "bluetooth is enabled status is " + status);
		if (screencreategame != null) {
			if (status == 0) {
				screencreategame.create_game_flag = true;
			} else if (status == 1) {
				screencreategame.create_game_flag = false;
			} else if (status == 2) {
				screencreategame.create_game_flag = false;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		SoundHandler.setContext(this);
		final long delay = 1000;// ms
		Handler handler = new Handler();

		Runnable runnable = new Runnable() {
			public void run() {
				ViewGroup rootView = (ViewGroup) MainActivity.this
						.findViewById(android.R.id.content);
				rootView.setKeepScreenOn(true);
				// find the first leaf view (i.e. a view without children)
				// the leaf view represents the topmost view in the view stack
				View topMostView = getLeafView(rootView);
				// let's add a sibling to the leaf view
				ViewGroup leafParent = (ViewGroup) topMostView.getParent();
				// Button sampleButton = new Button(MainActivity.this);
				// sampleButton.setText("Press Me");

				View view = getLayoutInflater().inflate(R.layout.activity_main,
						null, false);
				Log.d("Main", "Main Activity starts");
				view.setKeepScreenOn(true);
				SoundHandler.setContext(MainActivity.this);
				// mBackgroundSound = new BackgroundSound();

				leafParent.addView(view, new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				SplashScreenActivity hello = new SplashScreenActivity();

				fragmentTransaction.add(R.id.fragment_container, hello);
				fragmentTransaction.commit();

				init();
				sound = new SoundHandler();

				Log.e("ZZ", "External Directory is " + MainActivity.ExternDir);
				gdata = new GameData();

				copyAssets();
				off();
				// copyFile("chakraviu");

				// ***** getSettings();

			}
		};

		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				TAG);
		uinterface = AndroidUnityInterface.getInstance();
		uinterfacex=new AndroidUnityInterface(this);
		handler.postDelayed(runnable, delay);

		soundangle1 = MediaPlayer.create(this, R.raw.angle1);
		soundintro = MediaPlayer.create(this, R.raw.intro);
		soundsteps = MediaPlayer.create(this, R.raw.stepone);
		soundmainscreen = MediaPlayer.create(this, R.raw.mainscreen);

		tappercrussive = MediaPlayer.create(this, R.raw.tappercrussive);

		tap_crisp = MediaPlayer.create(this, R.raw.tapcrisp);

		slidescissors = MediaPlayer.create(this, R.raw.slide_scissors);
		slide_rock = MediaPlayer.create(this, R.raw.slide_rock);
		slide_network = MediaPlayer.create(this, R.raw.slide_network);
		tap_warm = MediaPlayer.create(this, R.raw.tap_warm);
		tap_wooden = MediaPlayer.create(this, R.raw.tap_wooden);
		letsplaysound = MediaPlayer.create(this, R.raw.wrong_answer1);
		right_answer = MediaPlayer.create(this, R.raw.right_answer1);
		wrong_answer = MediaPlayer.create(this, R.raw.wrong_answer);
		maddition=MediaPlayer.create(this, R.raw.additon);
		msubtract=MediaPlayer.create(this,R.raw.subtraction);
		mdivide=MediaPlayer.create(this,R.raw.division);
		mmultiply=MediaPlayer.create(this,R.raw.multiplication);
		moperatorchange=MediaPlayer.create(this,R.raw.operator_change);
		
	}

	private View getLeafView(View view) {
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			for (int i = 0; i < vg.getChildCount(); ++i) {
				View chview = vg.getChildAt(i);
				View result = getLeafView(chview);
				if (result != null)
					return result;
			}
			return null;
		} else {

			Log.e("ZZ", "Found leaf view");
			return view;
		}
	}

	public void init() {
		startgame = new StartGame(this);
		screenjoingame = new ScreenJoinGame(this);
		screencreategame = new ScreenCreateGame("", this);
		
		

		s2 = new Screen2();
		s3 = new Screen3();
		s4 = new Screen4();
		letsplay = new ScreenLetsPlay(this);
		s51 = new ScreenAboutYourself();
		s6 = new Screen6();
		s7 = new Screen7(this);

	}

	//
	// @Override
	// public void fragmentReplace(Fragment fragment) {
	// // TODO Auto-generated method stub
	// Log.d("REPLACED", "FRAGMENT");
	// FragmentManager fragmentManager = getFragmentManager();
	// FragmentTransaction fragmentTransaction = fragmentManager
	// .beginTransaction();
	//
	// fragmentTransaction.replace(R.id.fragment_container, fragment);
	// fragmentTransaction.commit();
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Log.e("ZZ", "onClick " + v.getId());
		if (v.getTag() == null) {
			// *** if(v.getId()==R.layout.hint_dialog)
			{
				// *** KlugDialogs(HintDialog,1);
				return;
			}
			/*
			 * if(v.getId()==R.layout.alert_layer) // need to add host waiting
			 * dialog { KlugDialogs(WaitHost,1); }
			 */

			// return;
		}
		String tag = (String) v.getTag();
		if ((((String) (v.getTag())).equals("puzzlenotwonbtnOk"))) {
			// *** KlugDialogs(PuzzleNotWon,1);
		} else if ((((String) (v.getTag())).equals("wonpuzzlebtn"))) {
			// *** KlugDialogs(PuzzleWon,1);
			if (startgame != null) {
				// *** startgame.detectiveAnswering(false);
				// *** startgame.footerDisplay(true);//15* screen to be designed
			}
		} else if (tag.equals("createGamebtnleave")) {
			if (screencreategame != null) {
				screencreategame.createGameDialog(false);
			}
		} else {
			if (startgame != null) {
				startgame.onClick(v);
			}

		}

	}

	@Override
	public ArrayList<String> getDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHints() {
		// TODO Auto-generated method stub
		return 0;
	}

	public class KDialog extends Dialog implements
			android.view.View.OnClickListener {

		RadioButton rbswap, rbstart;

		String message = "";
		TextView num, opponentname, txtstatus, t1, t2, t3, t4, t6, t7, t5;
		String pawnstatus = "SWAP PAWNS", pawnsnum = "";

		int theme = 0;
		KDialogInterface call;
		View child;
		LinearLayout myLinearLayout;
		RelativeLayout mainLayout;
		
		
		Animation animation1 ,animation2,animation3,animation4,animation5,animation6,animation7,animation8;
		RotateAnimation r;
		

		public KDialog(Context context, int _theme, KDialogInterface callback) {
			super(context, R.style.FullHeightDialog);
			theme = _theme;
			call = callback;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			Log.e(TAG, "Creating content");
			child = getLayoutInflater().inflate(theme, null);

			setContentView(child);
			WindowManager.LayoutParams params1 = new WindowManager.LayoutParams();
			params1.copyFrom(getWindow().getAttributes());
			// Log.e("ZZ","windows size is"+displaymetrics.widthPixels+":"+displaymetrics.heightPixels+":"+lp.width+":"+lp.height);
			params1.width = width;
			params1.height = height;

			getWindow().setAttributes(params1);

			getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

			setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					// Clear the not focusable flag from the window
					// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

					// Update the WindowManager with the new attributes (no
					// nicer way I know of to do this)..
					// WindowManager wm = (WindowManager)
					// MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
					// wm.updateViewLayout(getWindow().getDecorView(),getWindow().getAttributes());
					// /getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
				}
			});
			setCancelable(false);
			// this.setOnKeyListener(onKeyListener)

			// this.findViewById(R.id.creatingGame).getLayoutParams().width=LayoutParams.MATCH_PARENT;
			// this.findViewById(R.id.creatingGame).getLayoutParams().height=LayoutParams.MATCH_PARENT;
			// getWindow().setLayout(width, height);
			// Log.e("ZZ","windows size is"+displaymetrics.widthPixels+":"+displaymetrics.heightPixels+":"+params1.width+":"+params1.height);
			Log.e("VV", "displaying dialogs 1 " + System.currentTimeMillis());
			
			
			final ImageView step =(ImageView)child.findViewById(R.id.thumb);
			final ImageView botforward=(ImageView) child.findViewById(R.id.botforward);
			final ImageView pointer=(ImageView) child.findViewById(R.id.pointer);
			final Button btnguideback=(Button)child.findViewById(R.id.btnguideback);
			final ImageView botclockwise=(ImageView) child.findViewById(R.id.botclockwise);

			t1 = (TextView) child.findViewById(R.id.dialoglabel1);
			t2 = (TextView) child.findViewById(R.id.dialoglabel2);
			t3 = (TextView) child.findViewById(R.id.dialoglabel3);
			t4 = (TextView) child.findViewById(R.id.dialoglabel4);
			t5 = (TextView) child.findViewById(R.id.textlabel);
			t6 = (TextView) child.findViewById(R.id.textswap);
			t7 = (TextView) child.findViewById(R.id.textRestart);

			Typeface face = Typeface.createFromAsset(getAssets(),
					"fonts/beon.ttf");

			if (t1 != null)
				t1.setTypeface(face);

			if (t2 != null)
				t2.setTypeface(face);

			if (t3 != null)
				t3.setTypeface(face);

			if (t4 != null)
				t4.setTypeface(face);

			if (t5 != null)
				t5.setTypeface(face);

			if (t6 != null)
				t6.setTypeface(face);

			if (t7 != null)
				t7.setTypeface(face);

			final RelativeLayout layoutcheckboxes = (RelativeLayout) child
					.findViewById(R.id.layout1);
			final RelativeLayout layoutradios = (RelativeLayout) child
					.findViewById(R.id.layout2);
			final LinearLayout linearhead1 = (LinearLayout) child
					.findViewById(R.id.layoutA);
			final LinearLayout linearhead2 = (LinearLayout) child
					.findViewById(R.id.layoutB);
			final LinearLayout linearhead3 = (LinearLayout) child
					.findViewById(R.id.layoutC);
			// final RadioGroup radiogroup = (RadioGroup) child
			// .findViewById(R.id.radioGroup1);
			rbswap = (RadioButton) child.findViewById(R.id.radioButtonSwap);
			rbstart = (RadioButton) child.findViewById(R.id.radioButtonRestart);

			num = (TextView) child.findViewById(R.id.number);
			opponentname = (TextView) child.findViewById(R.id.opponentname);
			txtstatus = (TextView) child.findViewById(R.id.textnopawn);

			if (txtstatus != null) {

				String temp = "X";

				for (int i = 0; i < uinterface.list.length; i++) {
					Log.e("ZZ", "changint turn  X " + uinterface.list[i].turn
							+ ":" + uinterface.list[i].foulstatus);
					if (uinterface.list[i].turn == true) {

						temp = uinterface.list[i].foulstatus;

						Log.e(TAG, "FoulNopawns :" + temp);
					} else {
						Log.e(TAG, "turn is false");
					}
				}

				txtstatus.setText(temp.toString());

				txtstatus.setTypeface(face);

			}

			if (opponentname != null) {

				opponentname.setText(uinterface.opp.toString());
				opponentname.setTypeface(face);
			}

			if (num != null) {
				String temp = "X";
				for (int i = 0; i < uinterface.list.length; i++) {
					Log.e("ZZ", "changint turn  X " + uinterface.list[i].turn
							+ ":" + uinterface.list[i].foulnumber);
					if (uinterface.list[i].turn == true) {

						temp = uinterface.list[i].foulnumber;
						Log.e(TAG, "FOULNUMBER :" + temp);
					} else {
						Log.e(TAG, "turn is false");
					}
				}

				num.setText(temp.toString());
				num.setTypeface(face);
			}

			CheckBox cbcheckwrong = (CheckBox) child
					.findViewById(R.id.checkBox1);
			CheckBox cbnopawns = (CheckBox) child.findViewById(R.id.checkBox2);

			CheckBox cbout = (CheckBox) child.findViewById(R.id.checkBox3);

			if (rbswap != null && rbstart != null) {
				rbswap.setOnClickListener(this);
				rbstart.setOnClickListener(this);

			}

			/*
			 * radiogroup .setOnCheckedChangeListener(new
			 * RadioGroup.OnCheckedChangeListener() {
			 * 
			 * @Override public void onCheckedChanged(RadioGroup group, int
			 * checkedId) { // TODO Auto-generated method stub
			 * 
			 * Log.d("Onchckedchange", "CheckedId :" + checkedId + " Group :  "
			 * + group); switch (checkedId) { case R.id.radio1: Log.d("radio1",
			 * "radio1"); rb2.setChecked(false); rb3.setChecked(false);
			 * rb4.setChecked(false); rb5.setChecked(false); break;
			 * 
			 * case R.id.radio2: Log.d("radio2", "radio2");
			 * rb1.setChecked(false); rb3.setChecked(false);
			 * rb4.setChecked(false); rb5.setChecked(false); break;
			 * 
			 * case R.id.radio3: rb2.setChecked(false); rb1.setChecked(false);
			 * rb4.setChecked(false); rb5.setChecked(false); break;
			 * 
			 * case R.id.radio4: rb2.setChecked(false); rb3.setChecked(false);
			 * rb1.setChecked(false); rb5.setChecked(false); break;
			 * 
			 * case R.id.radio5: rb2.setChecked(false); rb3.setChecked(false);
			 * rb4.setChecked(false); rb1.setChecked(false); break; default:
			 * break; } radiogroup.clearCheck();
			 * 
			 * } });
			 */

			//Guide Animation
			animation1=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.steps);
			animation2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveforward);
			animation3=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateangle);
			animation4=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_a);
			
			animation5=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.steps1);
			animation6=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveforwardfive);
			animation7=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateangle1);
			animation8=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate1);
			
			
			
			
			
			if(step!=null){
				Log.e("Animation", "step-->");
				
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
						
						Log.e("Animation", "AnimationEnd-->");
						botforward.startAnimation(animation2);
					}
				});
				
			}
			
			
			if(animation2!=null){
				//animation3=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateangle);
				animation2.setAnimationListener(new AnimationListener() {
					
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
						pointer.startAnimation(animation3);
					}
				});
			}
			
			
			

			
			if(animation3!=null){
				//animation4=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateangle);
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
			}
			
			
			if(animation4!=null){
				//animation5=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateangle);
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
		
			}
			
			
			
			
			if(animation5!=null){
				//animation6=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateangle);
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
			}
			
			
			
		

		
		
			if(animation6!=null){
			//	animation7=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateangle);
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
			}
			
			
			
			if(animation7!=null){
			//	animation8=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateangle);
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
			}
			
			
			if(animation8!=null){
			//	animation1=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateangle);
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
			}
			
			
			if(btnguideback!=null){
				
				btnguideback.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
			}
			
			
			ImageView img = (ImageView) child.findViewById(R.id.load);
			if (img != null) {
				animation = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.rotate);
				img.startAnimation(animation);
			}

			

			if (cbout != null) {
				cbout.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (isChecked) {
							// layoutcheckboxes.setVisibility(View.VISIBLE);
							if (uinterface != null)
								uinterface.local.flagoutsidearena = true;
							linearhead1.setVisibility(View.GONE);
							linearhead2.setVisibility(View.GONE);
							linearhead3.setVisibility(View.VISIBLE);

						} else {
							// layoutcheckboxes.setVisibility(View.GONE);
							if (uinterface != null)
								uinterface.local.flagoutsidearena = false;
							linearhead1.setVisibility(View.VISIBLE);
							linearhead2.setVisibility(View.VISIBLE);
							linearhead3.setVisibility(View.VISIBLE);

						}
					}
				});
			}

			if (cbcheckwrong != null) {
				cbcheckwrong
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub

								if (isChecked) {
									wrongpawntouched = true;
									if (uinterface != null)
										uinterface.local.flagwrongpawn = true;

									linearhead1.setVisibility(View.VISIBLE);
									linearhead2.setVisibility(View.GONE);
									linearhead3.setVisibility(View.GONE);

								} else {
									wrongpawntouched = false;
									if (uinterface != null)
										uinterface.local.flagwrongpawn = false;
									linearhead1.setVisibility(View.VISIBLE);
									linearhead2.setVisibility(View.VISIBLE);
									linearhead3.setVisibility(View.VISIBLE);

								}

							}
						});
			}

			if (cbnopawns != null) {
				cbnopawns
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub

								if (isChecked) {
									nopawnstouched = true;
									if (uinterface != null)
										uinterface.local.flagnopawn = true;
									linearhead1.setVisibility(View.GONE);
									layoutradios.setVisibility(View.VISIBLE);

									linearhead2.setVisibility(View.VISIBLE);
									linearhead3.setVisibility(View.GONE);

								} else {

									nopawnstouched = false;
									if (uinterface != null)
										uinterface.local.flagnopawn = false;
									layoutradios.setVisibility(View.GONE);
									linearhead1.setVisibility(View.VISIBLE);
									linearhead2.setVisibility(View.VISIBLE);
									linearhead3.setVisibility(View.VISIBLE);
								}
							}
						});
			}
			ImageButton back = (ImageButton) child
					.findViewById(R.id.imageButtonBack);
			if (back != null) {
				Log.d("ZZ", "ONclickofpopupinstructions");
				back.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});

			}

			//
			// Button back1=(Button)child.findViewById(R.id.btnBack);
			// if(back!=null){
			// Log.d("ZZ", "ONclickofpopupinstructions");
			// back.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// dismiss();
			// }
			// });

			// }

			Button next = (Button) child.findViewById(R.id.btnnext);
			if (next != null) {
				next.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.e("NEXT", "button next");

						if (startgame != null) {

							// startgame.FoulRaised(pawnsnum);
							if (uinterface != null) {

								if (uinterface.local.flagwrongpawn) {
									Log.e("NEXT", "send unity statefoul");
									uinterface
											.sendUnityMessage(
													AndroidUnityInterface.StateFoul,
													"");
								} else if (uinterface.local.flagnopawn) {
									uinterface.sendUnityMessage(
											AndroidUnityInterface.StateFoulA,
											pawnstatus);

								} else if (uinterface.local.flagoutsidearena) {
									uinterface.sendUnityMessage(
											AndroidUnityInterface.StateFoulB,
											"");
								}

								else
									Log.e(TAG,
											"flagwrongpawn and flagnopawn false");
							} else {
								Log.e("NEXT", "uinterface is null");
								// Foul();
							}

						} else {
							Log.e("NEXT", "startgame is null");
						}
					}
				});

			} else {
				Log.e("XXX", "SETTING ON CLICK LISTENER is null");
			}

			Button btnexityes =(Button) child.findViewById(R.id.exityes);
			if(btnexityes!=null){
			btnexityes.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("EXIT","yes");
					finish();
				}
			});
			}
			
			Button btnexitno =(Button) child.findViewById(R.id.exitno);
			
			if(btnexitno!=null){
			btnexitno.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
			}
			
			
			
			
			Button btnyesreset = (Button) child
					.findViewById(R.id.btnyesforexit);
			if (btnyesreset != null) {
				btnyesreset.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (startgame != null) {

							startgame.TurnChange();
							dismiss();

						} else {
							Log.e(TAG, "Startgame is nulll");
						}

					}
				});
			}

			Button btnyesfoul = (Button) child.findViewById(R.id.btnyesforfoul);
			if (btnyesfoul != null) {
				btnyesfoul.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (uinterface != null) {
							uinterface.sendUnityMessage(
									AndroidUnityInterface.TurnChange, "");
						}
					}
				});
			}

			Button btnyes = (Button) child.findViewById(R.id.buttonyes);

			if (btnyes != null) {

				btnyes.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.e("YES", "is Pressed");
						dismiss();
						uinterface.sendUnityMessage(
								AndroidUnityInterface.TurnEnd, "");
					}
				});
			} else
				Log.e(TAG, "btn YES is Null");

			Button btncancel = (Button) child.findViewById(R.id.btncancel);
			if (btncancel != null) {

				btncancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.e("Cancel", "is Pressed");
						dismiss();

					}
				});
			} else
				Log.e(TAG, "btn Cancel is Null");

			Button btnno = (Button) child.findViewById(R.id.buttonno);
			if (btnno != null) {

				btnno.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.e("No", "is Pressed");
						dismiss();
						uinterface.sendUnityMessage(
								AndroidUnityInterface.FoulReinitiate, "");
					}
				});
			} else
				Log.e(TAG, "btn No is Null");
			
			
			Button btnnoforfoul = (Button) child.findViewById(R.id.btnnoforfoul);
			if (btnnoforfoul != null) {
				
				btnnoforfoul.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.e("No", "is Pressed");
						dismiss();
						uinterface.sendUnityMessage(
								AndroidUnityInterface.FoulReinitiate, "");
					}
				});
				
			} else
				Log.e(TAG, "btn No is Null");

			Button b = (Button) child.findViewById(R.id.cancelbtn);
			if (b != null) {
				Log.e("ZZ", "SETTING ON CLICK LISTENER");
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (v.getId() == R.id.cancelbtn) {
							// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

							// Update the WindowManager with the new attributes
							// (no nicer way I know of to do this)..
							// WindowManager wm = (WindowManager)
							// MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
							// wm.updateViewLayout(getWindow().getDecorView(),getWindow().getAttributes());
							play(1);
							if (call != null)
								call.leave(theme);

							dismiss();
						}

					}

				});

				Button b1 = (Button) child.findViewById(R.id.retrybtn);
				if (b1 != null) {
					Log.e("ZZ", "SETTING ON CLICK LISTENER");
					b1.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (v.getId() == R.id.retrybtn) {
								// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

								// Update the WindowManager with the new
								// attributes (no nicer way I know of to do
								// this)..
								// WindowManager wm = (WindowManager)
								// MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
								// wm.updateViewLayout(getWindow().getDecorView(),getWindow().getAttributes());
								play(2);
								if (call != null)
									call.retry(theme);

								dismiss();
							}

						}

					});
				}

			} else {
				Log.e("ZZ", "SETTING ON CLICK LISTENER is null");
			}

		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (v.getTag() == "1" || v.getTag() == "2" || v.getTag() == "3"
					|| v.getTag() == "4" || v.getTag() == "5") {

				Log.e(TAG, "Onclick in getTags");
				pawnsnum = v.getTag().toString();
				Log.e(TAG, "Onclick in getTags");
			}

			int code = v.getId();
			switch (code) {
			case R.id.radioButtonSwap:

				if (rbswap.isChecked()) {
					pawnstatus = "SWAP PAWNS";
					rbstart.setChecked(false);
				}

				break;
			case R.id.radioButtonRestart:
				if (rbstart.isChecked()) {
					pawnstatus = "RESTART";
					rbswap.setChecked(false);
				}

				break;

			case R.id.btnnext:

				// Log.e("NEXT", "button next");
				// if (startgame != null) {
				//
				// startgame.FoulRaised(pawnsnum);
				//
				// } else {
				// Log.e("NEXT", "startgame is null");
				// }

				break;

			
			default:
				break;
			}

		}

	}

	boolean init = false;
	KDialog dialog = null;
	// ArrayList<String> _sum;
	// int hints;
	int width, height;

	public void KlugDialogs(int dialogs, int status) {
		if (init == false) {
			init = true;
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

			if (dialogs == this.Foulgame || dialogs==this.Instructions) {
				width = (int) (displaymetrics.widthPixels);
				height = (int) (displaymetrics.heightPixels);
			} else {
				width = (int) (displaymetrics.widthPixels * 0.90);
				height = (int) (displaymetrics.heightPixels * 0.90);
			}

		}

		if (status == 0) {
			if (dialog != null) {
				Log.e("ZZ", "calling dialog dismiss");
				if (dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			} else {
				Log.e("ZZ", "KlugDialogs");
			}

		}
		/*
		 * if(dialogs==this.GameWon) { Log.e("ZZ","Creating games");
		 * if(status==0) { dialog=new KDialog(this,R.layout.game_won,null);
		 * dialog.show(); } else {
		 * 
		 * if (dialog!=null && dialog.isShowing()) {
		 * Log.e("ZZ","calling dialog dismiss"); dialog.dismiss(); dialog=null;
		 * }
		 * 
		 * } }
		 */

		if (dialogs == this.PlayerWaiting) {
			Log.e("ZZ", "Player Waiting to Accept  Foul Dialog");
			if (status == 0) {
				dialog = new KDialog(this, PlayerWaiting, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		if (dialogs == this.NoPawnTouched) {
			Log.e("ZZ", "No Pawn Touched Dialog");
			if (status == 0) {
				dialog = new KDialog(this, NoPawnTouched, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		if (dialogs == this.OutsideArena) {
			Log.e("ZZ", "Outside Arena Dialog");
			if (status == 0) {
				dialog = new KDialog(this, OutsideArena, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		if (dialogs == this.observebot) {
			Log.e("ZZ", "Observe Bot Dialog");
			if (status == 0) {
				dialog = new KDialog(this, observebot, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		if (dialogs == this.botMoving) {
			Log.e("ZZ", "Bot is Moving Dialog");
			if (status == 0) {
				dialog = new KDialog(this, botMoving, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		if (dialogs == this.WrongPawnTouched) {
			Log.e("ZZ", "Wrong Pawn Touched Dialog");
			if (status == 0) {
				dialog = new KDialog(this, WrongPawnTouched, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		if (dialogs == this.Foulgame) {
			Log.e("ZZ", "Foulgame Dialog");
			if (status == 0) {
				dialog = new KDialog(this, Foulgame, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}
		
		
		

		if(dialogs==this.Instructions)
		{
			Log.e("ZZ","Instructions Dialog");
			if(status==0)
			{
			dialog=new KDialog(this,Instructions,null);
			dialog.show();
			}
			else
			{
				
			     if (dialog!=null && dialog.isShowing()) {
			    	 Log.e("ZZ","calling dialog dismiss");
			    	 dialog.dismiss();
			    	 dialog=null;
			        }
				
			}
		}	
		

		if (dialogs == this.ExitGame) {
			Log.e("ZZ", "EXIT GAMES");
			if (status == 0) {
				dialog = new KDialog(this, ExitGame, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		if (dialogs == this.resetBot) {
			Log.e("ZZ", "Reset Dialog");
			if (status == 0) {
				dialog = new KDialog(this, resetBot, null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		if (dialogs == CreatingGame) {
			Log.e("ZZ", "Creating games");
			if (status == 0) {
				dialog = new KDialog(this, R.layout.popup_connecting_to_drive,
						null);
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		} else if (dialogs == ErrorCreatingGame) {
			Log.e("ZZ", "ErrorCreating games");
			if (status == 0) {
				dialog = new KDialog(this, R.layout.popup_error_creating_game,
						this);
				Log.e("ZZ", "calling dialog show");
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		} else if (dialogs == ErrorJoiningGame) {
			Log.e("ZZ", "ErrorJoiningGame games " + status);
			if (status == 0) {
				dialog = new KDialog(this, ErrorJoiningGame, this);
				Log.e("ZZ", "calling dialog show");
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		else if (dialogs == StartGame) {
			Log.e("ZZ", "StartGame games");
			if (status == 0) {
				dialog = new KDialog(this, StartGame, null);
				Log.e("ZZ", "calling dialog show");
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

		else if (dialogs == WaitHost) {
			Log.e("ZZ", "Wait Host dialog");
			if (status == 0) {
				dialog = new KDialog(this, WaitHost, this);
				Log.e("ZZ", "calling dialog show");
				dialog.show();
			} else {

				if (dialog != null && dialog.isShowing()) {
					Log.e("ZZ", "calling dialog dismiss");
					dialog.dismiss();
					dialog = null;
				}

			}
		}

	}

	
	public void play1(final int code){
		
		switch(code){
		
		case 10:
			//addition
			Log.e("BotVoice","addition");
			maddition.start();
			
			break;
		case 11:
			//subtraction
			Log.e("BotVoice","subtraction");
			msubtract.start();
			
			break;
		case 12:
			//multiply
			mmultiply.start();
			break;
		case 13:
			//divide
			mdivide.start();
			break;
		case 14:
			//operator change
			break;
	
			
			
		
		}
	}
	
	
	
	public void play(final int code) {
		Log.d("entersplay", "imgbtnbck");

		final long delay = 1;

		Handler handler = new Handler();

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				switch (code) {

				case 1:

					// button back
					Log.d("imgbtnbck", "imgbtnbck");

					tappercrussive.start();
					break;
				// case R.id.imageButtonForward:
				// case R.id.imageButtonLetsPlay:
				case 2:
					// button forward,letsplay,buttonsave
					Log.d("imgbtnforward", "imgbtnforward");

					tap_crisp.start();
					break;
				case 3:
					// age slider seekbar

					Log.d("Button", "pressed");
					slidescissors.start();
					break;
				// case R.id.radioButtonFemale:
				case 4:
					// gender radio buttons

					tappercrussive.start();
					Log.d("Button", "pressed");
					break;

				case 5:
					// btnguide

					slide_rock.start();
					Log.d("Button", "pressed");
					break;
				case 6:
					// button settings

					slide_network.start();
					break;
				// case R.id.imageButtonIwill:
				case 7:
					// button iwill,create

					tap_warm.start();
					break;
				case 8:
					// button myfriend

					tap_wooden.start();

					break;
				// case R.id.btnred:
				case 9:
					// button submit

					letsplaysound.start();

					break;
				case 10:
					//addition
					Log.e("BotVoice","addition");
					maddition.start();
					break;
				case 11:
					//subtraction
					Log.e("BotVoice","subtraction");
					msubtract.start();
					
					break;
				case 12:
					//multiply
					mmultiply.start();
					break;
				case 13:
					//divide
					mdivide.start();
					break;
				case 14:
					//operator change
					break;
					

				/*
				 * case 10: Log.e("ZZZ","XXXXXXXXXXXXXx");
				 * 
				 * wrong_answer.start(); break;
				 * 
				 * case 11: right_answer.start(); break; default: break;
				 */

				}

			}
		};
		handler.postDelayed(runnable, delay);

	}

	@Override
	protected void onResume() {
		Log.e("onResume", "onResume");
		super.onResume();
		wakeLock.acquire();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		wakeLock.release();
		Log.e("Onpause", "Paused");
		// sound.pause();

	}

	@Override
	public void onDestroy() {
		off();
		super.onDestroy();
	}
	
public void ExitOnBack(){
		
	Log.e("XXXX","XXXXXX")	;
	
	runOnUiThread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			KlugDialogs(ExitGame, 0);
		}
	});
	
		
	}
	
	public  void off() {
		// TODO Auto-generated method stub
		
		wifi= (WifiManager) this.getSystemService(this.WIFI_SERVICE);
		wifiApControl = WifiApControl.getApControl(wifi, this);
		
		if(binterface!=null)
		{
			binterface.onDestroy();
			
		}else Log.e("OFF","Binterface is null");
		
		if(wifiApControl!=null){
			wifiApControl.setWifiAPDisabled();
			
			Log.e("OFF","HOTSPOT DISABLED");
			
		}else Log.e("OFF","WIFIApControl status null");
		
		wifi.setWifiEnabled(false);
		Log.e("OFF","WIFI DISABLED");
	}


	public void onn(){
		
		wifi= (WifiManager) this.getSystemService(this.WIFI_SERVICE);
		wifiApControl = WifiApControl.getApControl(wifi, this);
		wifi.setWifiEnabled(true);
		Log.e("OFF","WIFI ENABLED");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e("OnStop", "Stop");
		sound.quit();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Log.e("ZZZZZ", "Back Pressed");
		sound.pause();
		KlugDialogs(this.ExitGame, 0);
		// super.onBackPressed();
		// onPause();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// No call for super(). Bug on API Level > 11.
	}

	@Override
	public void leave(int id) {
		// TODO Auto-generated method stub
		// calling screen7 on click met
		if (id == this.WaitHost) {
			if (screenjoingame != null) {
				screenjoingame.LeaveGame();
			}
		}
		if (id == ErrorJoiningGame) {
			if (screenjoingame != null) {
				// need callback for leave
				off();
			}
		}

		if (id == ExitGame) {
			// close bluetooth and Wifi
			off();
			Log.e("EEEEE", "closing the activity");
			this.finish();

		}

		if (id == ErrorCreatingGame) {
			if (screencreategame != null) {
				Log.e(TAG, "leaving creating game");
				off();
				screencreategame.onClick(0);
			}

		}
		// ******if(id==GameWon)
		// {
		//
		// Log.e("EEEEE","closing the activity");
		// this.finish();
		// //show game won dialog
		// /*if(screencreategame!=null)
		// {
		// Log.e(TAG,"leaving creating game");
		// screencreategame.onClick(0);
		// }*/
		//
		// }

	}

	@Override
	public void retry(int id) {
		// TODO Auto-generated method stub
		if (id == this.ErrorCreatingGame) {
			if (screencreategame != null) {
				off();
				Log.e(TAG, "retruing attempt to connect");
				screencreategame.onClick(1);
			}
		}
		if (id == ErrorJoiningGame) {
			off();
			onn();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					screenjoingame.Retry();
				}
			}, 1000);
			
		}
		if(id== ExitGame){
			Log.e(TAG,"in retry exit");
			dismissDialog(ExitGame);
		}

	}

	@Override
	public void sharedata(String s) {
		// TODO Auto-generated method stub

		gdata.playername = s;
		// Log.d("sharedata", sharedata);
	}

	MediaPlayer beep_hightone, beep_plucked, beepbrightpop, button_letsplay,
			coolclicker, game_play, setupgame, slide_network, slide_rock,
			slidescissors, splash, tap_warm, tap_wooden, tap_crisp,
			tappercrussive, toys, walkthrough, letsplaysound, wrong_answer,
			right_answer,maddition,msubtract,mmultiply,mdivide,moperatorchange;

	public void BotMusic() {
		// play1(10);
		Log.e(TAG,"ss botmusic");
		if (uinterface != null) {
			uinterface.sendUnityMessage(AndroidUnityInterface.BotMusic1, ""
					+ uinterface.local.id);
		}
		
		//if(startgame!=null)
		//	startgame.botVoice();
	}

	public void Foul() {
		if (uinterface != null) {

			Log.e(TAG, "send unity statefoul");
			uinterface.sendUnityMessage(AndroidUnityInterface.StateFoul, ""
					+ uinterface.local.id);
		} else {
			Log.e(TAG, "uinterface is null");
		}
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		super.onAttachFragment(fragment);
		frag = fragment.getClass().getName().toString();
		Log.d("FRAGMENT_TAG", "" + fragment.getTag());
		Log.d("FRAGMENT", "" + frag);

		if (frag.equalsIgnoreCase("com.example.kluggame2.Screen2")) {
			// mBackgroundSound.cancel(true);
			// sound.quit();
			Log.d("FRAGMENT", "xxxx");
			sound.playMusic(R.raw.intro);
			Log.d("MainActivity", "onAttached-->Screen2");

		}

		if (frag.equalsIgnoreCase("com.example.kluggame2.StartGame")) {
			// mBackgroundSound.cancel(true);
			sound.quit();
			//sound.playMusic(R.raw.mainscreen);
			Log.d("stop", "music");

		}

	}

	@Override
	public void fragmentReplace(int code) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Fragment replace int code");
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		// fragmentTransaction.setCustomAnimations(R.anim.enter_anim,
		// R.anim.exit_anim);
		fragmentTransaction.addToBackStack(null);
		switch (code) {
		// case 1:
		// Log.d("REPLACED", "FRAGMENT");

		// Screen3 hello = new Screen3();
		// fragmentTransaction.replace(R.id.fragment_container,fragment );
		// fragmentTransaction.commit();
		// break;

		case 2:
			// Screen2 s2=new Screen2();

			// Screen3 hello = new Screen3();

			// TESTING to be made s2
			if (s2.isAdded()) {
				Log.d("Fragment already added", "XXXXXX");
			} else {
				fragmentTransaction.replace(R.id.fragment_container, s2);
				fragmentTransaction.commit();
			}
			break;

		case 3:

			// Screen3 hello = new Screen3();
			fragmentTransaction.replace(R.id.fragment_container, s3);
			fragmentTransaction.commit();
			break;

		case 4:

			// Screen3 hello = new Screen3();
			fragmentTransaction.replace(R.id.fragment_container, s4);
			fragmentTransaction.commit();
			break;

		case 5:

			// Screen3 hello = new Screen3();
			fragmentTransaction.replace(R.id.fragment_container, letsplay);
			fragmentTransaction.commit();
			break;

		case 51:

			fragmentTransaction.replace(R.id.fragment_container, s51)
					.commitAllowingStateLoss();

			break;

		case 6:

			// Screen3 hello = new Screen3();
			fragmentTransaction.replace(R.id.fragment_container, s6);
			fragmentTransaction.commit();
			break;

		case 7:

			// Screen3 hello = new Screen3();
			fragmentTransaction.replace(R.id.fragment_container, s7);
			fragmentTransaction.commit();
			break;

		case 8:
			fragmentTransaction.replace(R.id.fragment_container, s51);
			fragmentTransaction.commit();
		}
	}

	@Override
	public void fragmentReplace(int code, String args) {
		// TODO Auto-generated method stub
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		// setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );

		// fragmentTransaction.setCustomAnimations(R.anim.enter_anim,
		// R.anim.exit_anim);

		fragmentTransaction.addToBackStack(null);
		// transaction.replace(R.id.listFragment, new YourFragment());

		switch (code) {

		// case 7:
		// if (s7 == null) {
		// s7 = new Screen7(this);
		// }
		//
		// // Screen3 hello = new Screen3();
		// fragmentTransaction
		// .replace(R.id.fragment_container, s7);
		// fragmentTransaction.commit();
		// break;

		case 8:
			if (screenjoingame == null) {
				screenjoingame = new ScreenJoinGame(this);
			}

			// Screen3 hello = new Screen3();
			fragmentTransaction
					.replace(R.id.fragment_container, screenjoingame);
			fragmentTransaction.commit();
			break;

		case 9:
			if (screencreategame == null) {
				screencreategame = new ScreenCreateGame(args, this);
			}
			screencreategame.GameName = args;

			// Screen3 hello = new Screen3();
			fragmentTransaction.replace(R.id.fragment_container,
					screencreategame);
			fragmentTransaction.commit();
			break;

		case 10:
			if (startgame == null) {
				startgame = new StartGame(this);
			}

			// Screen3 hello = new Screen3();
			fragmentTransaction.replace(R.id.fragment_container, startgame);
			fragmentTransaction.commit();
			break;
		}

	}

	private void copyAssets() {
		
		String folder = "/mnt/sdcard/KlugTek";
		File dir = new File(folder);
		if (!dir.exists()) {
			dir.mkdir(); // This line will create new blank line.
		
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		
		if (files != null) {
			String filename = "KlugTek.zip";
			InputStream in = null;
			OutputStream out = null;
			try {

				File checkfile = new File("/mnt/sdcard/KlugTek/" + filename);
				if (!checkfile.exists()) {
					in = assetManager.open(filename);
					File outFile = new File("/mnt/sdcard/KlugTek", filename);
					out = new FileOutputStream(outFile);
					copyFile(in, out);
					unpackZip("/mnt/sdcard/KlugTek/", filename);
				} else
					Log.e("ASSETS", "Zip File already exists");

			} catch (IOException e) {
				Log.e("tag", "Failed to copy asset file: " + filename, e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Log.e("Exception", "in" + e.getMessage());
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// NOOP
						Log.e("Exception", "out" + e.getMessage());
					}
				}
			}
		} else
			Log.e("ASSETS", "files is null");
		}else{
			Log.e("ASSETS","folder already exists");
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		Log.e("ASSETS", "Copied file");
	}
	
	private boolean unpackZip(String path, String zipname)
	{       
	     InputStream is;
	     ZipInputStream zis;
	     try 
	     {
	         String filename;
	         is = new FileInputStream(path + zipname);
	         zis = new ZipInputStream(new BufferedInputStream(is));          
	         ZipEntry ze;
	         byte[] buffer = new byte[1024];
	         int count;

	         while ((ze = zis.getNextEntry()) != null) 
	         {
	             // zapis do souboru
	             filename = ze.getName();

	             // Need to create directories if not exists, or
	             // it will generate an Exception...
	             if (ze.isDirectory()) {
	                File fmd = new File(path + filename);
	                fmd.mkdirs();
	                continue;
	             }

	             FileOutputStream fout = new FileOutputStream(path + filename);

	             // cteni zipu a zapis
	             while ((count = zis.read(buffer)) != -1) 
	             {
	                 fout.write(buffer, 0, count);             
	             }

	             fout.close();               
	             zis.closeEntry();
	         }

	         zis.close();
	     } 
	     catch(IOException e)
	     {
	         e.printStackTrace();
	         return false;
	     }

	    return true;
	}

	/*
	 * private void copyFile(String filename) { final String TARGET_BASE_PATH =
	 * "/mnt/sdcard/"; AssetManager assetManager = this.getAssets();
	 * 
	 * InputStream in = null; OutputStream out = null; String newFileName =
	 * "/mnt/sdcard/";
	 * 
	 * String arr[]=null;
	 * 
	 * 
	 * 
	 * 
	 * 
	 * try { arr=assetManager.list("");
	 * 
	 * for (int i = 0; i < arr.length; ++i) {
	 * 
	 * Log.e("ASSET", "ASSETS LENGTH"+arr.length);
	 * 
	 * 
	 * 
	 * // if(arr[i].equalsIgnoreCase(file)){
	 * 
	 * 
	 * Log.e("ASSET", "ASSETS"+filename+"Location :"+arr[i]);
	 * 
	 * in = assetManager.open(filename); Log.e("ASSET","ASSET STREAM"+in);
	 * 
	 * 
	 * out = new FileOutputStream(newFileName);
	 * 
	 * byte[] buffer = new byte[1024]; int read; while ((read = in.read(buffer))
	 * != -1) { out.write(buffer, 0, read); } in.close(); in = null;
	 * out.flush(); out.close(); out = null;
	 * 
	 * // }
	 * 
	 * } Log.i("tag", "copyFile() "+filename);
	 * 
	 * } catch (Exception e) { Log.e("tag",
	 * "Exception in copyFile() of "+newFileName); Log.e("tag",
	 * "Exception in copyFile() "+e.toString()); }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * private void copyFolder(String name) throws IOException { // "Name" is
	 * the name of your folder! AssetManager assetManager = getAssets();
	 * String[] files = null;
	 * 
	 * String state = Environment.getExternalStorageState();
	 * 
	 * if (Environment.MEDIA_MOUNTED.equals(state)) { // We can read and write
	 * the media // Checking file on assets subfolder try { files =
	 * assetManager.list(name); } catch (IOException e) { Log.e("ERROR",
	 * "Failed to get asset file list.", e); } // Analyzing all file on assets
	 * subfolder for(String filename : files) { InputStream in = null;
	 * OutputStream out = null; // First: checking if there is already a target
	 * folder File folder = new File(Environment.getExternalStorageDirectory() +
	 * name); boolean success = true; if (!folder.exists()) { success =
	 * folder.mkdir(); } if (success) { // Moving all the files on external SD
	 * try { in = assetManager.open(name + "/" +filename); out = new
	 * FileOutputStream(Environment.getExternalStorageDirectory());
	 * Log.i("WEBVIEW", Environment.getExternalStorageDirectory()+filename );
	 * copyFile(in, out); in.close(); in = null; out.flush(); out.close(); out =
	 * null; } catch(IOException e) { Log.e("ERROR",
	 * "Failed to copy asset file: " + filename, e); } finally { // Edit 3
	 * (after MMs comment) in.close(); in = null; out.flush(); out.close(); out
	 * = null; } } else { // Do something else on failure Log.e("XX","fail"); }
	 * } } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) { // We
	 * can only read the media Log.e("XX","only read"); } else { // Something
	 * else is wrong. It may be one of many other states, but all we need // is
	 * to know is we can neither read nor write\ Log.e("XX","sd card absent"); }
	 * }
	 * 
	 * //Method used by copyAssets() on purpose to copy a file. private void
	 * copyFile(InputStream in, OutputStream out) throws IOException { byte[]
	 * buffer = new byte[1024]; int read; while((read = in.read(buffer)) != -1)
	 * { out.write(buffer, 0, read); } }
	 */

}
