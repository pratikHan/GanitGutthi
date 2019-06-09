package com.example.kluggame2;

import java.util.ArrayList;
import java.util.List;

import com.example.kluggame2.R;
import com.example.utils.Timerx;
import com.example.utils.Timerx.TimerInterface;
import com.example.wifi.KlugCallback;
import com.example.wifi.WifiScanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

//import android.support.v7.widget.SearchView;

public class ScreenJoinGame extends ListFragment implements KlugCallback,
		TimerInterface {

	private static final String TAG = "ScreenJoinGame";
	CustomListAdapterForJoinGame adapter;

	List<PlayerModel> modelItems;

	WifiScanner scanner;

	private String _playername;
	private int _value = 0;
	TextView text;
	RelativeLayout lay;
	EditText et;
	ImageButton right;
	MainActivity act;
	AndroidUnityInterface uinterface;
	Timerx timer, utimer;

	public ScreenJoinGame() {
	}

	public ScreenJoinGame(MainActivity _act) {
		// TODO Auto-generated constructor stub
		act = _act;
		uinterface = AndroidUnityInterface.getInstance();
	}

	public void LeaveGame() {
		uinterface.state.state = uinterface.idle;
		Log.e(TAG, "uinterface state is" + uinterface.state.state);

		showStartingGameDialog(false);
		hostingGameDialog(false);

		if (utimer != null)
			utimer.stop();

		if (uinterface != null && uinterface.local != null) {
			uinterface.sendUnityMessage(AndroidUnityInterface.Disconnect, ""
					+ uinterface.local.id);
		} else {
			Log.e(TAG, "uinterface is null");
		}

	}

	// ImageButton left,right,guide,settings,create;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_screen_join_game, null);
		text = (TextView) view.findViewById(R.id.textView1);

		et = (EditText) view.findViewById(R.id.SearchView1);
		 Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
		 "fonts/beon.ttf");

		// SearchView.SearchAutoComplete searchAutoComplete
		// =(SearchView.SearchAutoComplete)
		// et.findViewById(android.support.v7.appcompat.R.id.search_src_text);

		 text.setTypeface(face);
		text.setText("Game Rooms Available");

		// searchAutoComplete.setTypeface(face);
		// searchAutoComplete.setText("Search for players");
		et.setGravity(Gravity.CENTER);
		et.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				Log.d("TEXT CHANGED", "" + cs);
				adapter.setfilter(cs.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

		});

		// lay=(RelativeLayout) view.findViewById(R.id.alertlayout);

		ImageButton left = (ImageButton) view.findViewById(R.id.imageButton1);
		right = (ImageButton) view.findViewById(R.id.imageButton2);
		right.setEnabled(false);

		uinterface.state.state = uinterface.idle;

		// Button leave=(Button) view.findViewById(R.id.btnleave);

		Log.d("Screen Join Game", "ScreenJoinGame");
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).play(7);
				Log.e("IN Joins Activity", "" + adapter.item);
				// Toast.makeText(getActivity(), "" + adapter.item,
				// Toast.LENGTH_LONG).show();
				// lay.setVisibility(View.VISIBLE);

				RetryC();

			}
		});

		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Toast.makeText(getActivity(), "Button Left Clicked",
				// Toast.LENGTH_SHORT).show();
				((MainActivity) getActivity()).play(1);
				if (utimer != null) {
					utimer.stop();
					showStartingGameDialog(false);
					hostingGameDialog(false);
				}
				if (uinterface != null && uinterface.local != null) {
					uinterface.sendUnityMessage(
							AndroidUnityInterface.Disconnect, ""
									+ uinterface.local.id);
					uinterface.state.state = uinterface.idle;
				}

				((MainActivity) getActivity()).fragmentReplace(6);
			}
		});
		if (MainActivity.gdata != null)
			MainActivity.gdata.gamename = MainActivity.TAG + ":";

		modelItems = new ArrayList<PlayerModel>();
		// modelItems.add(new PlayerModel("Prashant", 0)) ;
		// modelItems.add(new PlayerModel("chintan", 0)) ;
		// modelItems.add(new PlayerModel("pratik", 0)) ;
		// modelItems.add(new PlayerModel("Ansh", 0)) ;
		// modelItems.add(new PlayerModel("Sneh", 0)) ;
		// modelItems.add(new PlayerModel("Prashant", 0)) ;
		// modelItems.add(new PlayerModel("chintan", 0)) ;

		adapter = new CustomListAdapterForJoinGame(getActivity(), modelItems,
				this);

		// adapter.setfilter("Klug");

		Init();
		errorJoiningGame(false);
		showStartingGameDialog(false);
		hostingGameDialog(false);

		// adapter = new CustomListAdapterForJoinGame(getActivity(),modelItems);
		uinterface.joingame = this;
		return view;
	}

	public void Init() {
		scanner = new WifiScanner();
		scanner.setCallback(this);
		scanner.StartWifi(act);
		if (utimer != null)
			utimer.stop();
		utimer = new Timerx(1000, 50000, 4000, this, 1);

	}

	public void RetryC() {
		if (utimer != null)
			utimer.stop();

		scanner.connect(adapter.item);
		// lay.setVisibility(View.GONE);
		// text.setText("Connected");
		// Toast.makeText(getActivity(), "Connection "+adapter.item,
		// Toast.LENGTH_LONG).show();

		// lay.setVisibility(View.VISIBLE);
		hostingGameDialog(true);
		utimer = new Timerx(1000, 20000, 4000, this, 1);
		utimer.start();

		uinterface.state.state = uinterface.join;
		Log.e(TAG, "chaining the state to " + uinterface.state.state);

	}

	public void errorJoiningGame(boolean status) {
		Log.e("ZZ", "showing error dialog");
		final boolean status1 = status;
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (status1 == false) {
					act.KlugDialogs(act.ErrorJoiningGame, 1);

				} else {
					act.KlugDialogs(act.ErrorJoiningGame, 0);
				}
			}
		});
	}

	public void showStartingGameDialog(boolean status) {
		if (status == false) {
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					act.KlugDialogs(act.StartGame, 1);
				}

			});
			// lay1.setVisibility(View.GONE);
		} else {
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					act.KlugDialogs(act.StartGame, 0);
				}

			}); // lay1.setVisibility(View.VISIBLE);
		}
		// lay1.invalidate();

	}

	public void hostingGameDialog(boolean status) {
		if (status == false) {
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					act.KlugDialogs(act.WaitHost, 1);
				}

			});
			// lay.setVisibility(View.GONE);
		} else {
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					act.KlugDialogs(act.WaitHost, 0);
				}

			});
		}
		// lay1.invalidate();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Log.d("On Activity", "Entered");
		ListView lv = getListView();

		lv.setAdapter(adapter);

		// Log.d("Join Model", ""+modelItems);

		/*
		 * getListView().setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * pos, long arg3) { // TODO Auto-generated method stub
		 * 
		 * Object obj = getListView().getAdapter().getItem(pos); String value =
		 * obj.toString();
		 * 
		 * Log.d("List items", "" + value); // Toast.makeText(getActivity(), //
		 * ""+pm.getName().toString(),Toast.LENGTH_SHORT).show();
		 * Log.d("item is", "" + getListView().getItemIdAtPosition(pos));
		 * 
		 * Log.d("item is", "" + value); Log.d("Model Items name", "" +
		 * adapter.rowItem.get(pos).getName()); Log.d("Model Items value", "" +
		 * adapter.rowItem.get(pos).getValue());
		 * 
		 * Log.d("Model Items name1", "" + modelItems.get(pos).getName());
		 * Log.d("Model Items value1", "" + modelItems.get(pos).getValue());
		 * 
		 * _playername = modelItems.get(pos).getName(); _value =
		 * modelItems.get(pos).getValue();
		 * 
		 * } });
		 */

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void listUpdate() {
		// TODO Auto-generated method stub

		// Log.e("ZZ->", "Received callback");
		Log.e("ZZ->", "Received callback + wifi count" + scanner.getWifiCount());
		if (scanner.getWifiCount() > 0) {

			if (adapter != null) {
				// adapter.clear();
				// products.clear();

				adapter.rowItem1.clear();
				String list[] = scanner.getWifiList();

				for (int i = 0; i < list.length; i++) {
					Log.e("ZZ->", "Received callback + updating list "
							+ list[i]);
					if (list[i].startsWith("Klug")) {
						// products.add(list[i]);
						// adapter.add(list[i]);
						adapter.rowItem1.add(new PlayerModel(list[i], 0));
					}

				}

				adapter.notifyDataSetChanged();
			}

		}

	}

	@Override
	public void connectStatus(boolean status) {
		// TODO Auto-generated method stub

	}

	public void changebuttonstate() {

		if (CustomListAdapterForJoinGame.radioisChecked) {
			// Log.d("trueee",""+adapter.radioisChecked);
			right.getBackground().setColorFilter(null);
			right.setEnabled(true);
			right.setClickable(true);
		} else {
			// Log.d("false",""+adapter.radioisChecked);
			right.getBackground().setColorFilter(Color.GRAY,
					PorterDuff.Mode.MULTIPLY);
			right.setEnabled(false);

		}
	}

	public void wstart() {
		Log.e("ZZ", "changing screen " + uinterface.state.state + ":"
				+ uinterface.wait_turn);
		if (uinterface.state.state == uinterface.wait_turn) {
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					errorJoiningGame(false);
					hostingGameDialog(false);
					showStartingGameDialog(true);
				}

			});
			if (utimer != null)
				utimer.stop();

			((MainActivity) getActivity()).fragmentReplace(10, "");
		}

	}

	public void start() {
		Log.e("ZZ", "changing screen 2" + uinterface.state.state + ":"
				+ uinterface.wait_turn);
		if (uinterface.state.state == uinterface.wait_turn
				|| uinterface.state.state == uinterface.detective
				|| uinterface.state.state == uinterface.master) {
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					// errorJoiningGame(false);
					// hostingGameDialog(false);
					// showStartingGameDialog(true);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			});
			if (utimer != null)
				utimer.stop();
			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					((MainActivity) getActivity()).fragmentReplace(10, "");
				}
			});
		}
	}

	@Override
	public void Complete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void End(int id) {
		// TODO Auto-generated method stub
		Log.e("ZZ", "timeout has occured");
		if (utimer != null) {
			utimer.stop();
		}
		errorJoiningGame(true);
	}

	@Override
	public void Cancel(int id) {
		// TODO Auto-generated method stub
		Log.e("ZZ", "timeout has occured");
		if (utimer != null) {
			utimer.stop();
			errorJoiningGame(true);
		}
	}

	@Override
	public boolean runTask(int id, int TimeCounter) {
		// TODO Auto-generated method stub

		switch (id) {

		case 1:
			Log.e(TAG, "connection status is" + scanner.checkStatus());
			if (scanner.checkStatus()) {

				Log.e("ZZ", "host ip address is " + scanner.getHostIP() + ":"
						+ uinterface.state.state + ":" + uinterface.join);

				if (uinterface.state.state == uinterface.join) {
					Log.e("ZZ", "before calling uinterface");
					uinterface.sendUnityMessage(
							AndroidUnityInterface.JoinServer,
							scanner.getHostIP());
					uinterface.state.state = uinterface.wait_turn;
					// return true;
				}
			}

			break;

		}
		return false;
	}

	public void Retry() {
		// retry
		RetryC();
	}

	public void Cancel() {
		// go back to main screen
	}

}
