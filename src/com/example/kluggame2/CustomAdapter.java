package com.example.kluggame2;

import com.example.kluggame2.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter{   
   
    Context context;
 int [] imageId;
      private static LayoutInflater inflater=null;
    public CustomAdapter(Context context, int[] prgmImages) {
        // TODO Auto-generated constructor stub
     
        this.context=context;
        imageId=prgmImages;
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageId.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
       
        ImageButton img;
    }
   
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 Holder holder=new Holder();
	        View rowView;       
	             rowView = inflater.inflate(R.layout.puzzle_list, null);
	            
	             holder.img=(ImageButton) rowView.findViewById(R.id.image);       
	             //holder.img.setOnTouchListener(new StartGame().new MyTouchListener());
	            
	       //      holder.img.setOnLongClickListener(new StartGame().new MyLongClickLstener());
	       
	         holder.img.setBackgroundResource(imageId[position]);     
	         rowView.setOnClickListener(new OnClickListener() {            
	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	            	
	            	Log.d("You Clicked ",""+imageId[position]);
	                Toast.makeText(context.getApplicationContext(), "You Clicked "+imageId[position], Toast.LENGTH_LONG).show();
	            }
	        });   
	        return rowView;
	}

}
