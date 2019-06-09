package com.example.wifi;

public class Time {
    public  float deltaTime = 0;         
    public float curTime =0;
    public float startTime=0;
    public float nextTime=0;
    public static long frameCount = 0;                      
    public static float fixedTime = 0;                     
    public static float fixedDeltaTime = 0.1f;      
    public static float maxDeltaTime = 0.25f;      
    
    public void reset()
    {
    	init=false;
    	startTime=0;
    }
    
    public float getDeltaTime()
    {
    	if(init==false)
    	{
    		init=true;
    		curTime=System.currentTimeMillis();;
    		return curTime;
    	}
    	nextTime=System.currentTimeMillis();
    	deltaTime=nextTime-curTime;
    	curTime=nextTime;
    	return deltaTime;
    }
    
    public boolean init=false;
    public float getDelta()
    {
    	if(init==false)
    	{
    		init=true;
    		startTime=System.nanoTime();;
    		return 0;
    	}
    	nextTime=System.nanoTime();
    	deltaTime=(nextTime-startTime)/1000000;
    	//curTime=nextTime;
    	return deltaTime;
    	
    }
}
