package com.example.kluggame2;

import java.util.ArrayList;
import java.util.List;




import com.example.kluggame2.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class CustomListAdapterForJoinGame extends ArrayAdapter<PlayerModel> implements View.OnClickListener {

	// PlayerModel[] modelItems = null;
	static boolean radioisChecked=false;
	Context context;
	List<PlayerModel> rowItem, rowItem1;
	ScreenJoinGame fragment;

	ArrayList<PlayerModel> filteredrowItem = new ArrayList<PlayerModel>();

	int selectedPosition = 20;
	RadioButton r;
	String item = "";
	PlayerModel row_pos;
	Typeface face = null;
	public CustomListAdapterForJoinGame(Context context,
			List<PlayerModel> rowItem,ScreenJoinGame fragment) {
		super(context, 0,rowItem);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.rowItem = rowItem;
		this.fragment=fragment;
		
		rowItem1=new ArrayList<PlayerModel>();
		face=Typeface.createFromAsset(context.getAssets(),
				"fonts/beon.ttf");


	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_row_join_names, null);
			convertView.setOnClickListener(null);
		}

		// ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.textname);
		r = (RadioButton) convertView.findViewById(R.id.radio1);

		r.setChecked(position == selectedPosition);
		r.setTag(position);
		
		r.setOnClickListener(this);


				
				
		


		row_pos = rowItem.get(position);
		// setting the image resource and title
		// imgIcon.setImageResource(row_pos.getIcon());
		txtTitle.setText(row_pos.getName());
	
		txtTitle.setTypeface(face);

		// if(row_pos.getValue() == 1)
		// cb.setChecked(true);
		// else
		// cb.setChecked(false);
		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rowItem.size();
	}

	@Override
	public PlayerModel getItem(int position) {
		// TODO Auto-generated method stub
		return rowItem.get(position);

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return rowItem.indexOf(getItem(position));
	}

	@Override
	public void notifyDataSetChanged() {
		if (cs != null&&cs.length()!=0 ){

			//Log.d("Notify", "IF");

			filter(cs);
		
		}
		else {

			if(rowItem1!=null)
			{
			rowItem.clear();
			for(int i=0;i<rowItem1.size();i++)
			{
				rowItem.add(rowItem1.get(i));
			}			
			}
//			Log.e("Notify", "ELSE");
		}
		// rowitem1->rowitem

		super.notifyDataSetChanged();


	}

	public String cs;

	public void setfilter(String cs) {
		this.cs = cs;
		this.notifyDataSetChanged();
	}

	public void filter(CharSequence cs) {

		
		
		String cs1 = cs.toString();

		Log.e("SEARCH ELEMENT", ""+cs1);
		filteredrowItem.clear();
		if(rowItem1!=null)
		{

		for (int i = 0; i < rowItem1.size(); i++) {

			final String valueText = rowItem1.get(i).name.toString()
					.toLowerCase();

			Log.e("FILTER VALUE:", ""+valueText);


			if (valueText.startsWith(cs1)) {
				
				filteredrowItem.add(rowItem1.get(i));
				
				
			}
		}

		rowItem.clear();
		for(int i=0;i<filteredrowItem.size();i++)
		{
			rowItem.add(filteredrowItem.get(i));
		}
		//rowItem=filteredrowItem;
		
		/*for(int i=0;i < filteredrowItem.size();i++){
			
			Log.e("FILTER ARR", ""+filteredrowItem.get(i).name.toString());
		}*/


		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
			final View view=v;
			new Handler().post(new Runnable()
			{

				
				;
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					int prev=selectedPosition;
					
					
					selectedPosition = (Integer) view.getTag();
					if(prev!=selectedPosition)
					{
						rowItem.get(selectedPosition).setValue(0);
						notifyDataSetChanged();
					}
					if (isEnabled(selectedPosition)) {
						rowItem.get((Integer) view.getTag()).setValue(1);
						radioisChecked=true;
						fragment.changebuttonstate();
						((RadioButton)view).setChecked(true);
						item = rowItem.get((Integer) view.getTag()).getName();
						
						//set all other status to false
						//Log.d("PLAYER ITEM", "" + item);
					} else
					{
						rowItem.get((Integer) view.getTag()).setValue(0);
					}
					notifyDataSetChanged();
				}
			});

	}
	
}
