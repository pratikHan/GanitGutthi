package com.example.kluggame2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unity3d.player.UnityPlayer;

public class AndroidUnityInterface {

	final public int join = 1;
	final public int idle = 0;
	final public int create = 2;
	final public int wait_turn = 3;
	final public int master = 4;
	final public int detective = 5;
	final public int turn_result = 7;

	ScreenJoinGame joingame = null;
	ScreenCreateGame creategame = null;
	StartGame screen = null;
	
	class GameState {
		public int state = idle;

		public GameState() {
			state = idle;
		}
	}

	GameState state = new GameState();

	public static AndroidUnityInterface uinterface;
	
	public static MainActivity act;
	public static String TAG = "AndroidUnityInterface";
	
	public static String opp="xx";

	public static AndroidUnityInterface getInstance() {
		if (uinterface == null) {
			uinterface = new AndroidUnityInterface();
		}
		

		return uinterface;
	}

	public AndroidUnityInterface(MainActivity _act){
		act=_act;
		
		
	}
	
	public AndroidUnityInterface() {

	}
	
	
	

	public class NetworkPlayer {
		public String ipAddress;
		public int port;
		public String guid;
		public String externalIP;
		public int externalPort;
	}

	public static class server_type {
		public static int server = 0;
		public static int client = 1;

	}

	public String SerializeLocal() {
		Log.e("ZZ", "serialize local " + local.playername);
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONObject j;
		String l2 = new String();
		try {
			// j = new JSONObject(local);
			l2 = gson.toJson(local, PlayerData.class);

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

	public class PlayerData {
		public String gamename;
		public String playername;
		
		public int id;
		public NetworkPlayer player;
		public boolean turn;
		public int reward;
		public int timer;
		public boolean timer_active;
		public int type;
		public boolean enabled;
		public int master;
		public String prev_equation;
		public String equation;
		public String botequation;
		public String target;
		public int answer;
		// to be added in BE
		public int hints;
		public int MaxHint;
		public int MasterTimeOut;
		public int DetectiveTimeOut;
		public int round;
		public int maxRound;
		public String ScoreDir;
		// public puzzle puz;
		public int level;
		public int maxnumber;
		public String foulnumber;
		public String foulstatus;
		public boolean flagwrongpawn;
		public boolean flagnopawn;
		public boolean flagoutsidearena;
		// public ArrayList<ScoreItem> score;
	}

	public PlayerData[] list;
	public PlayerData local;

	public PlayerData[] getPlayerData() {
		return list;
	}

	public static final int StartServer = 0;
	public static final int JoinServer = 1;
	public static final int JoinList = 2;
	public static final int ConnectServer = 3;
	public static final int StartGame = 4;
	public static final int TurnEnd = 5;
	public static final int Disconnect = 6;
	public static final int MasterEnd = 7;
	public static final int DetectiveEnd = 8;
	 public static final int Hint = 9;
	// public static final int TimeOut = 10;
	public static final int Attempt = 11;
	// public static final int TimerStart = 12;
	// public static final int DetectiveStart = 13;
	public static final int EndBot = 14;
	public static final int GameWon = 15;
	// public static final int puzzleDrop =16;
	public static final int BotMusic1 = 17;
	
	
	
	public static final int StateFoul=18;
	public static final int StateFoulA=19;
	public static final int StateFoulB=22;
	public static final int FoulReinitiate=20;
	public static final int Reset=21;
	public static final int TurnChange=23;
	public static final int SendAnswer=24;
	public static final int Sendtarget=25;
	public static final int Leveltwo=26;
	public static final int Levelone=27;
	
	
	

	public class GSettings {
		public int level;
		public int maxRound;
	}

	public static String getmaxNumber() {
		return MainActivity.getmaxNumber();
	}

	public static String getLevel() {
		return MainActivity.getLevel();
	}

	public static int getMaxRound() {
		return MainActivity.getMaxRound();
	}

	public static String getUserDir() {
		return MainActivity.getUserDir();
	}

	public static String getExternDir() {
		return MainActivity.getExternDir();
	}

	public static String getSettingsDir() {
		return MainActivity.getSettingsDir();
	}

	public static String getPuzzlesDir() {
		return MainActivity.getPuzzlesDir();
	}

	public static String getGameName() {
		return MainActivity.getGameName();
	}

	public static String getPlayerName() {
		return MainActivity.getPlayerName();
	}

	public void sendUnityMessage(int code, String args) {
		switch (code) {
		case BotMusic1:
			UnityPlayer.UnitySendMessage("GameController", "BotMusic1", args);
			break;

		case StartServer:
			UnityPlayer.UnitySendMessage("GameController", "StartServer", "");
			break;
		case JoinServer:
			Log.e("ZZ", "calling JoinServer " + args + ":" + state.state);
			UnityPlayer.UnitySendMessage("GameController", "ConnectToServer",
					args);
			Log.e("ZZ", "completed JoinServer " + args + ":" + state.state);
			break;
		case JoinList:
			Log.e("ZZ", "calling joinPlayerList");
			UnityPlayer.UnitySendMessage("GameController", "joinPlayerList",
					args);
			break;

		case StartGame:
			Log.e("ZZ", "calling StartGame");
			UnityPlayer.UnitySendMessage("GameController", "StartGame", args);
			break;

		case TurnEnd:
			Log.e("ZZ", "calling TurnEnd");
			UnityPlayer.UnitySendMessage("GameController", "TurnEnd", args);
			break;
			
		case Reset:
			Log.e("ZZ", "calling Reset");
			UnityPlayer.UnitySendMessage("GameController", "Reset", args);
			break;
			
		case TurnChange:
			Log.e("ZZ", "calling TurnChange");
			//UnityPlayer.UnitySendMessage("GameController", "TurnChange", args);
			break;	

		case MasterEnd:
			Log.e("ZZ", "calling  MasterEnd");
			UnityPlayer.UnitySendMessage("GameController", "MasterEnd", args);
			break;

		case StateFoul:
			Log.e("ZZ","calling FoulRaised");
			UnityPlayer.UnitySendMessage("GameController", "FoulRaised", args);
			
			break;
			
		case StateFoulA:
			Log.e("ZZ","calling Wrong Number");
			UnityPlayer.UnitySendMessage("GameController", "FoulRaisedForNoPawn", args);
			
			break;
			
		case StateFoulB:
			Log.e("ZZ","calling WrongOperator");
			UnityPlayer.UnitySendMessage("GameController", "FoulOutsideArena", args);
			
			break;		
			
		case FoulReinitiate:
			Log.e("ZZ","calling FoulReinitiate");
			UnityPlayer.UnitySendMessage("GameController", "FoulReinitiate", args);
			
			break;	
		
		
		case DetectiveEnd:
			Log.e("ZZ", "calling DetectiveEnd");
			UnityPlayer
					.UnitySendMessage("GameController", "DetectiveEnd", args);
			break;
			
			

		case Disconnect:
			Log.e("ZZ", "calling Disconnect");
			UnityPlayer.UnitySendMessage("GameController", "DisconnectPlayer",
					args);
			break;

		case EndBot:
			Log.e("ZZ", "calling EndBot");
			UnityPlayer.UnitySendMessage("GameController", "EndBot", args);
			break;

		case Attempt:
			Log.e("ZZ", "calling Attempt");
			UnityPlayer.UnitySendMessage("GameController", "Attempt", args);
			break;
			
		
		//
		// case TimerStart:
		// Log.e("ZZ", "calling TimerStart");
		// UnityPlayer.UnitySendMessage("GameController", "TimerStart", args);
		// break;
		//
		// case DetectiveStart:
		// Log.e("ZZ", "calling TimerStart");
		// UnityPlayer.UnitySendMessage("GameController", "DetectiveStart",
		// args);
		// break;
		
		case GameWon:
			Log.e("ZZ", "calling Gamewon");
			UnityPlayer.UnitySendMessage("GameController", "GameWon", args);
			break;
			
		case SendAnswer:
			Log.e("ZZ", "calling SendAnswer");
			UnityPlayer.UnitySendMessage("GameController","SendAnswer", args);
			
			break;
			
		case Sendtarget:
			Log.e("ZZ", "calling Sendtarget");
			UnityPlayer.UnitySendMessage("GameController","SendTarget", args);
			
			break;
		case  Leveltwo:
			Log.e("ZZ", "calling Level2");
			UnityPlayer.UnitySendMessage("GameController","LevelTwo", args);
			
			break;
		case  Levelone:
			Log.e("ZZ", "calling Level1");
			UnityPlayer.UnitySendMessage("GameController","LevelOne", args);
			
			break;	

		}
	}

	public void GetEvent(String jsonData, String jlocal, int code) {

		switch (code) {

		case -1:
			updatePlayerData(jsonData, jlocal);
			if (local != null && local.type == 1) {

				state.state = wait_turn;

				Log.e("ZZ", "Starting the client ");
				joingame.wstart();
			}

			break;
			
		

		case 1:
			// starting the turn
			Log.e(TAG, "received data is " + jsonData + "-------------"
					+ jlocal);
			updatePlayerData(jsonData, jlocal);

			if (local.turn == true) {
				state.state = master;
			} else {
				state.state = detective;
			}

			if (local != null && local.type == 1) {

				// state.state = wait_turn;
				Log.e("ZZ", "Starting the client ");
				joingame.start();
			} else if (local != null && local.type == 0) {
				// state.state = wait_turn;
				creategame.start();
				Log.e("ZZ----", "Starting the server ");

			}

			break;

		case 2:
			updatePlayerData(jsonData, jlocal);
			if (local.turn == true) {
				state.state = master;
			} else {
				state.state = detective;
			}

			if (screen != null) {
				Log.e(TAG, "calling turn change " + local.turn + ":"
						+ state.state);
				 screen.TurnChange();
			}
			break;

		case 3:
			// detective start testing
			updatePlayerData(jsonData, jlocal);
			if (local.turn == true)
				state.state = master;
			else
				state.state = detective;

			if (screen != null) {
				Log.e(TAG, "calling detective start testing case 3 "
						+ local.turn + ":" + state.state);
				if (screen != null) {
					
					
					screen.UpdateUIAnswer();
					//screen.EndBotMove();
					// screen.DetectiveStart();
					//** screen.DetectiveStart_Testing();
				}
			}
			break;

		case 4:
			// Timer Start -- master END
			updatePlayerData_1(jsonData, jlocal);
			if (local.turn == true)
				state.state = master;
			else
				state.state = detective;

			 if (screen != null) {

			// if(screen!=null)
			// {
			// screen.DetectiveStart();
			// Master has ended screen
			Log.e(TAG, "calling Master End" + local.turn + ":" + state.state
					+ ":" + local.type);
		 screen.MasterEnd();
		 screen.startBotMove();
		 
		// screen.StartBot();
			 }else
				 Log.e(TAG, "BOTNOTSTART");
				 
			if (local.type == 0) {
				Log.e(TAG, "calling Start Bot");
				screen.StartBot();
				Log.e(TAG, "completed Start Bot");
			} else {
				Log.e(TAG, "NOT SERVER,not calling Start Bot");
			}
			// }
			break;

		case 5:
			// update the puzzle live
			Log.e("ZZ", "update live puzzle");
			updatePlayerData(jsonData, jlocal);
			if (screen != null) {
				// screen.updatePuzzle();
				// ***** screen.updateFooter();
				// screen.updateStatus();
				if (local.answer == 2 || local.answer == 3) {
					// ****** screen.updateDetectiveStatus();
				}
			}

			Log.e("ZZ", "completed update live puzzle");
			break;

		case 6:
			Log.e("ZZ", "showing scorecard");
			// show score card
			// Detective start -- after bot moves
			updatePlayerData(jsonData, jlocal);
			/*
			 * if (local.type == 0) { // state.state=master; // else //
			 * state.state=detective;
			 * 
			 * if (screen != null) { Log.e(TAG, "Start Bot"); if (screen !=
			 * null) { // screen.DetectiveStart(); screen.StartBot(); } } }
			 */
			break;
		// game completed view scorecard
		case 7:
			updatePlayerData(jsonData, jlocal);
			if (screen != null) {
				// **** screen.ShowScoreCard();
				screen.showlevel2();
			}

			break;

		case 8:
			if (screen != null) {
				// ***** act.play1(10);
				Log.e(TAG, "ss case 8");
				//screen.botVoice();
				
				screen.showlevel1();
			}
			break;

		case 9:
			if (act != null) {
				act.KlugDialogs(act.ExitGame, 0);
			}
			break;
			
			
		case 10:
			// Timer Start -- StateFoul
			
			Log.e(TAG,"State Foul For Wrong Pawn");
			updatePlayerData_1(jsonData, jlocal);
			if (local.turn == true)
				state.state = master;
			else
				state.state = detective;

			
			if(screen!=null){
			Log.e(TAG, "calling Master End" + local.turn + ":" + state.state
					+ ":" + local.type);
			
			
			Log.e("AndroidUnityInterface","Callback recieved");
			screen.UpdateUI();
			
		   
			
				
			
			}else{
					Log.e(TAG,"Unity screen is null");
			}
			
			
			break;	
			
			
		case 11:
			// Timer Start -- StateFoul
			
			Log.e(TAG,"State Foul for NoPawn");
			updatePlayerData_1(jsonData, jlocal);
			if (local.turn == true)
				state.state = master;
			else
				state.state = detective;

			
			if(screen!=null){
			Log.e(TAG, "calling Master End" + local.turn + ":" + state.state
					+ ":" + local.type);
			
			
		
			
				screen.wrongnumber();
				
			
			}else{
					Log.e(TAG,"Unity screen is null");
			}
			
			
			break;	
			
			
		case 12:
			updatePlayerData_1(jsonData, jlocal);
			if (local.turn == true)
				state.state = master;
			else
				state.state = detective;

			
			if(screen!=null){
			Log.e(TAG, "calling Foul_Reinitiate" + local.turn + ":" + state.state
					+ ":" + local.type);
			
			
		
			
				screen.Foul_Reinitiate();
				
			
			}else{
					Log.e(TAG,"Unity screen is null");
			}
			
			
			break;
			
		case 13:
			updatePlayerData_1(jsonData, jlocal);
			if (local.turn == true)
				state.state = master;
			else
				state.state = detective;
			
			if(screen!=null){
				Log.e(TAG, "calling BOt _Reset" + local.turn + ":" + state.state
						+ ":" + local.type);
				
				
			
				
					screen.ResetBot();
					
				
				}else{
						Log.e(TAG,"Unity screen is null");
				}
			
			break;
			
		case 14:
			updatePlayerData_1(jsonData, jlocal);
			if (local.turn == true)
				state.state = master;
			else
				state.state = detective;
			
			if(screen!=null){
				Log.e(TAG, "calling Stop Bot Ui Change" + local.turn + ":" + state.state
						+ ":" + local.type);
				
				
			
				
					screen.wrongoperator();
					
				
				}else{
						Log.e(TAG,"Unity screen is null");
				}
			
			break;	
			
			
		case 15:
//			updatePlayerData_1(jsonData, jlocal);
//			if (local.turn == true)
//				state.state = master;
//			else
//				state.state = detective;
			
			
			
			
			if (act != null) {
				act.ExitOnBack();
			}else{
						Log.e(TAG,"Unity ACT is null");
				}
			
			break;	
			
		case 16:
			updatePlayerData_1(jsonData, jlocal);
			if (local.turn == true)
				state.state = master;
			else
				state.state = detective;
			
			if(screen!=null){
			
				Log.e("AndroidUnityInterface","Callback showlevel2");
				screen.showlevel1();
				
			}
			
			
			
			
			break;
			
		case 17:
			updatePlayerData_1(jsonData, jlocal);
			if (local.turn == true)
				state.state = master;
			else
				state.state = detective;
			
			if(screen!=null){
			
				Log.e("AndroidUnityInterface","Callback showlevel1");
				screen.showlevel2();
				
			}
			
			
			
			
			break;	
			
			
		}
	}

	public void updatePlayerData(String jsonData, String jlocal) {

		// jsonData=new
		// String("[{\"gamename\":\"123\",\"playername\":\"server:0\",\"id\":0,\"player\":{\"ipAddress\":\"10.0.0.2\",\"port\":25000,\"guid\":\"67554002641092259\",\"externalIP\":\"UNASSIGNED_SYSTEM_ADDRESS\",\"externalPort\":65535},\"turn\":true,\"answer\":0,\"reward\":0,\"timer\":0,\"timer_active\":false,\"type\":0,\"enabled\":false}]");
		Log.e("ZZ", "calling deSerialize " + jsonData);
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONArray j;
		JSONObject j1;
		// list=new ArrayList<PlayerData>();
		try {
			j = new JSONArray(jsonData);
			j1 = new JSONObject(jlocal);
			list = gson.fromJson(j.toString(), PlayerData[].class);
			local = gson.fromJson(j1.toString(), PlayerData.class);
			for (int i = 0; i < list.length; i++) {
				Log.e(TAG, "" + list[i].playername + ":" + local.playername);
				if(!list[i].playername.equalsIgnoreCase(local.playername)){
				   opp=list[i].playername;
				   Log.e("OPPONENT name is :",""+opp);
				}
			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updatePlayerData_1(String jsonData, String jlocal) {
		Log.e(TAG, "updatePlayerData data is " + jsonData + "-------------"
				+ jlocal);
		// jsonData=new
		// String("[{\"gamename\":\"123\",\"playername\":\"server:0\",\"id\":0,\"player\":{\"ipAddress\":\"10.0.0.2\",\"port\":25000,\"guid\":\"67554002641092259\",\"externalIP\":\"UNASSIGNED_SYSTEM_ADDRESS\",\"externalPort\":65535},\"turn\":true,\"answer\":0,\"reward\":0,\"timer\":0,\"timer_active\":false,\"type\":0,\"enabled\":false}]");
		Log.e("ZZ", "calling deSerialize " + jsonData);
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		JSONArray j;
		JSONObject j1;
		// list=new ArrayList<PlayerData>();
		try {
			j = new JSONArray(jsonData);
			j1 = new JSONObject(jlocal);
			list = gson.fromJson(j.toString(), PlayerData[].class);
			local = gson.fromJson(j1.toString(), PlayerData.class);

			/*
			 * for (int i = 0; i < list.length; i++) { Log.e(TAG,
			 * "status in android is" + list[i].playername + ":" +
			 * local.playername + ":" + list[i].turn + ":" + local.turn); }
			 */
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
