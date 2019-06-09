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
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

	// PlayerModel[] modelItems = null;
	Context context;
	static boolean checkboxisChecked=false;
	int counter=0;
	ScreenCreateGame fragment;
	Typeface face =null;
	PlayerModel row_pos ;
	public List<PlayerModel> rowItem;
	
	//PlayerModel model=new PlayerModel();
	
	 
	public List <String> players=new ArrayList<String>();
	String item;


	public List <String> getCheckedList()
	{
		return players;
	}
	
	public CustomListAdapter(Context context, List<PlayerModel> rowItem,ScreenCreateGame fragment) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.rowItem = rowItem;
		this.fragment=fragment;
		face=Typeface.createFromAsset(context.getAssets(),"fonts/beon.ttf");
	}

	boolean mutex=false;
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		  
		
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_row_create_names,
					null);
			convertView.setOnClickListener(null);
		}

		// ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.textname);
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
		   	
		cb.setChecked((1==rowItem.get(position).value));
		cb.setTag(position);
		if(position==0)
		{
			   cb.setChecked(true);
			   item=rowItem.get(position).getName();
			   if(players.contains(item))
			   {
				   
			   }
			   else
			   {
			   players.add(item);
			   }			
		}
		   
		   //PlayerModel row_pos1 = rowItem.get(position);
		   
		 
		   
		   cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton v1, boolean isChecked1) {

				final CompoundButton v=v1;
				final boolean isChecked=isChecked1;
				new Handler().post(
				
				new Runnable()
				{

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int prev=rowItem.get((Integer) v.getTag()).getValue();
						if(mutex==true)
						{
							rowItem.get((Integer) v.getTag()).setValue(prev);
							mutex=true;
							return;
						}
						mutex=true;
						
						 if(isChecked){
								// cb.setChecked(true);
								 //cb.setSelectAllOnFocus(true);
								// Log.d("ISCHECKED",""+cb.isChecked());
								// Log.d("ISELECTED",""+cb.isSelected());
						//	 checkboxisChecked=true;
							 //counter=counter+1;
							   Log.e("cc", "counter checked "+v.getTag());
							   rowItem.get((Integer) v.getTag()).setValue(1);
							   item=rowItem.get((Integer) v.getTag()).getName();	
							   
							   if(players.contains(item))
							   {
								   
							   }
							   else
							   {
							   players.add(item);
							   }
								//cb.setChecked(true);
							   Log.d("ITEM", ""+item);
								Log.d("TAG", ""+(Integer)v.getTag());
								//row_pos.setValue(1);
								
								 fragment.changebuttonstate();	 
							 }
							 
						 else  {
					//		 checkboxisChecked=false;
							 //counter=counter-1;
							 
							 	 if((Integer) v.getTag()==0)
							 	 {
							 		rowItem.get((Integer) v.getTag()).setValue(1);
							 		v.setChecked(true);
							 		if(!players.contains(item))
							 		{
							 			item=rowItem.get((Integer) v.getTag()).getName();
							 			players.add(0,item);
							 		}
							 	 }
							 	 else
							 	 {
								 Log.e("cc", "counter checked "+v.getTag());
								 rowItem.get((Integer) v.getTag()).setValue(0);
								 item=rowItem.get((Integer) v.getTag()).getName();
								 players.remove(item);
								 
								 fragment.changebuttonstate();
							 	 }
									//players.remove(v.getTag());
								 //cb.setChecked(false);
								 	//row_pos.setValue(0);
									// model.setValue(0);
							 }
							
						mutex=false;
						
					}
					
				});
				
				// TODO Auto-generated method stub
				//Log.d("Wifi Player Elements", ""+players);	
				//Log.d("Size",""+players.size());
				
		
				
			}
		});
		   
		  	   
		/*   cb.setOnClickListener(new OnClickListener() {
			  

			   
			 @Override
			 public void onClick(View v) {
			 // TODO Auto-generated method stub
			
				 if(cb.isEnabled()){
					// cb.setChecked(true);
					 cb.setSelectAllOnFocus(true);
					 Log.d("ISCHECKED",""+cb.isChecked());
					 Log.d("ISELECTED",""+cb.isSelected());
					 rowItem.get((Integer) v.getTag()).setValue(1);
				   item=rowItem.get((Integer) v.getTag()).getName();	
	
				   if(players.contains(item))
				   {
					   
				   }else{
				   players.add(item);
				   }
					//cb.setChecked(true);
				   Log.d("ITEM", ""+item);
					Log.d("TAG", ""+(Integer)v.getTag());
					//row_pos.setValue(1);
					
					 
				 }
				 
				 if(!cb.isSelected()) {
					 Log.d("TAG1", ""+v.getTag());
					 rowItem.get((Integer) v.getTag()).setValue(0);
						//players.remove(v.getTag());
					 //cb.setChecked(false);
					 	//row_pos.setValue(0);
						// model.setValue(0);
				 }
				 else
					 Log.d("ELSE", "ELSE");
				
			Log.d("Wifi Player Elements", ""+players);	
			Log.d("Size",""+players.size());
			 notifyDataSetChanged();
			 }
			 });
		 
		*/ 
		 
		 
		   
		

		   Log.e("ZZZ ","counter XX "+position);
		   row_pos= rowItem.get(position);
			/*if(position==0){
				//counter=counter+1;
				//fragment.changebuttonstate();
				cb.setChecked(true);
				cb.setEnabled(false);
			}
			else
			{
				cb.setChecked(false);
				cb.setEnabled(true);
			}*/
			
			
		txtTitle.setText(row_pos.getName());
		txtTitle.setTypeface(face);

		
		

		
		//notifyDataSetChanged();
		

		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rowItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return rowItem.get(position);

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return rowItem.indexOf(getItem(position));
	}


	
}

