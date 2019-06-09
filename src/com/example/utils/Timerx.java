package com.example.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class Timerx  {
	
	int period =1000;
	int end_period;
	int ecount;
	int end;
	Timer timer;
	TimerInterface call;
	boolean status=false;
	int count=0;
	boolean ccstatus=false;
	int id=-1;
	boolean init=false;
	public interface TimerInterface
	{
		public void Complete(int id);
		//public boolean runTask(int id);
		public void End(int id);
		public void Cancel(int id);
		boolean runTask(int id, int TimeCounter);
	}
	boolean stopped=false;
	public void stop()
	{
		if(init==true && stopped==false)
		{
		timer.cancel();
		stopped=true;
		}
	}
	
	
	
	public Timerx(int _period,int _complete,int _end_period,TimerInterface _callback,int _id)
	{
		period=_period;
		end=_complete;
		call=_callback;
		end_period=_end_period;
		stopped=false;
		id=_id;
		
		
	}
	
	public void reset()
	{
		period=0;
	}
	
	public void start()
	{
		stopped=false;
		init=true;
		ecount=0;
		status=false;
		ccstatus=false;
		timer = new Timer();
		count=0;
		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {

			count=count+1;
			if(count*period>=end)
			{
				//Log.e("ZZ","calling complete");
				if(ccstatus==false)
				{
				call.Complete(id);
				ccstatus=true;
				//status=true;
				}
				
			}
			
			if(status==true)
			{
				ecount=ecount+1;
				if(ecount*period>=end_period)
				{
				call.End(id);
				}
			}
			
			if(ccstatus==true)
			{
				ecount=ecount+1;
				if(ecount*period>=end_period)
				{
				call.Cancel(id);
				}
			}

			Log.e("ZZ","calling runtask "+count+":");
			if(status==false )
			{
			int x=(end-(count*period))/1000;
			if(x<0)x=0;
			status=call.runTask(id,x);
			}
			}},0, period);
	}

	

}
 