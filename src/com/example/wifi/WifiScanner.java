package com.example.wifi;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;








import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WifiScanner  {
	  // ListView lv;
	
		public static String TAG="WifiScanner";
	
		
	   boolean connectFlag=false;
	   boolean connectStatusFlag=false;
	   
	   WifiManager wifi;
	   String wifis[];
	   WifiScanReceiver wifiReciever=null;
	   public static WifiScanner scanner=null;
	   boolean startWifi=false;
	   Time t=new Time();
	   Activity act;
	   List<ScanResult> wifiScanList;
	   private static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");
	   
	   public static WifiScanner getInstance()
	   {
		   if(scanner==null)
		   {
			   scanner=new WifiScanner();
		   }
		   return scanner;
		   //return scanner;
	   }
	   
	   KlugCallback call;
	   
	   public void setCallback( KlugCallback _c)
	   {
		   call=_c;
	   }
	   
	  // @Override
	   public boolean StartWifi(Activity _act) {
		   
	    //  super.onCreate(savedInstanceState);
	      //setContentView(R.layout.activity_main);
	     // lv=(ListView)findViewById(R.id.listView);
		   act=_act;
		Log.e("ZZ","onCreate");
	    wifi=(WifiManager)act.getSystemService(Context.WIFI_SERVICE);
	    if(!wifi.isWifiEnabled())
	    {
	    	
	    	startWifi=false;
	    	if(wifiReciever!=null)
	    		act.unregisterReceiver(wifiReciever);
	    	Log.e("ZZ","wifi is not enabled");	    	
	    	startWifi=wifi.setWifiEnabled(true);
	    	startWifi=true;
	    }
	    else
	    {
	    	startWifi=true;
	    	Log.e("ZZ","wifi is null");	    		    	
	    }
	     wifiReciever = new WifiScanReceiver();
	     act.registerReceiver(wifiReciever,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	     wifi.startScan();	    
	    return startWifi;
	   
	   }
	   
	   public int getWifiCount()
	   {
		   if(wifis!=null && wifis.length>0)
		   {
			   return wifis.length;
		   }
		   else
		   {
			   return 0;
		   }
		   
	   }
	   
	   public Object getWifiList1()
	   {
		   if(wifis!=null && wifis.length>0)
		   {
			   return wifis;
			   
		   }
		   else
		   {
			   return null;
		   }
		   
	   }
	   
	   public String[] getWifiList()
	   {
		   if(wifis!=null && wifis.length>0)
		   {
			   return wifis;
			   
		   }
		   else
		   {
			   return null;
		   }
		   
	   }
	   
	   /**
	    * @param value input to check
	    * @param allowedLengths allowed lengths, if any
	    * @return true if value is a non-null, non-empty string of hex digits, and if allowed lengths are given, has
	    *  an allowed length
	    */
	   private static boolean isHexOfLength(CharSequence value, int... allowedLengths) {
	     if (value == null || !HEX_DIGITS.matcher(value).matches()) {
	       return false;
	     }
	     if (allowedLengths.length == 0) {
	       return true;
	     }
	     for (int length : allowedLengths) {
	       if (value.length() == length) {
	         return true;
	       }
	     }
	     return false;
	   }

	

	   private static String quoteNonHex(String value, int... allowedLengths) {
		    return isHexOfLength(value, allowedLengths) ? value : convertToQuotedString(value);
		  }
	   
	   /**
	    * Encloses the incoming string inside double quotes, if it isn't already quoted.
	    * @param string the input string
	    * @return a quoted string, of the form "input".  If the input string is null, it returns null
	    * as well.
	    */
	   private static String convertToQuotedString(String string) {
	     if (string == null || string.length() == 0) {
	       return null;
	     }
	     // If already quoted, return as-is
	     if (string.charAt(0) == '"' && string.charAt(string.length() - 1) == '"') {
	       return string;
	     }
	     return '\"' + string + '\"';
	   }
	   
	   
	   /**
	    * If the given ssid name exists in the settings, then change its password
	    * to the one given here, and save
	    * 
	    * @param ssid
	    */
	   private static Integer findNetworkInExistingConfig(WifiManager wifiManager, String ssid) {
		    List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
		    for (WifiConfiguration existingConfig : existingConfigs) {
		      if (existingConfig.SSID.equals(ssid)) {
		        return existingConfig.networkId;
		      }
		    }
		    return null;
		  }
	   
	   
	   
	   public class checkConnectionStatus implements Runnable {
		    Time t1=new Time();
		    @Override
		    public void run() {
		    	try {
		    	while(true)
		    	{
			    	float time=t1.getDelta();
			    	Log.e(TAG,"checking status"+time+":"+connectStatusFlag+":"+t1.nextTime+":"+t1.startTime);
			    	if(time<50000)
			    	{
				    	if(checkStatus())
				    	{
				    		call.connectStatus(connectStatusFlag);
				    		break;
				    	}
				    	
				    	else
				    	{
				    		call.connectStatus(connectStatusFlag);
				    		//break;
				    	}
			    	}
			    	else
			    	{
			    		call.connectStatus(connectStatusFlag);
			    		Log.e(TAG,"completed checking status"+time+":"+connectStatusFlag);
			    		break;
			    	}
			    	
			    	Thread.sleep(100);
					
			    	
			    	}
		        
		    	} 
		    	catch (InterruptedException e) {
					// TODO Auto-generated catch block
					call.connectStatus(connectStatusFlag);
		    		
				}
		    	
		    
		}
	   };
	   
	   public boolean checkStatus()
	   {
		   
	        ConnectivityManager connectivityManager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
	         NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);     
	 
	        String ssid= "\"" + current + "\"";
	    	Log.e(TAG,"xstatus is"+mWifi.isConnected()+":"+connectFlag+":"+wifi.getConnectionInfo().getSSID().equals(ssid)+":"+wifi.getConnectionInfo().getSSID());
	    	if(!mWifi.isConnectedOrConnecting()|| connectFlag!=true || !wifi.getConnectionInfo().getSSID().equals(ssid)){
	    		connectStatusFlag=false;
	    	 }
	    	else
	    	{
	    		connectStatusFlag=true;
	    	}
	    	Log.e("ZZ","connection status is "+connectStatusFlag);
	    	return connectStatusFlag;
	   }
	   
	   
	   public String getScanResultSecurity(ScanResult scanResult) {
		    Log.i(TAG, "* getScanResultSecurity");

		    final String cap = scanResult.capabilities;
		    final String[] securityModes = { "WEP", "PSK", "EAP" };

		    for (int i = securityModes.length - 1; i >= 0; i--) {
		        if (cap.contains(securityModes[i])) {
		            return securityModes[i];
		        }
		    }

		    return "OPEN";
		}
	   
	   /**
	    * Update the network: either create a new network or modify an existing network
	    * @param config the new network configuration
	    * @return network ID of the connected network.
	    */
	   private  void updateNetwork(WifiManager wifiManager, WifiConfiguration config) {
		   
		   
	     Integer foundNetworkID = findNetworkInExistingConfig(wifiManager, config.SSID);
	     connectFlag=false;
	     boolean found=false;
	     if (foundNetworkID != null) {
	       Log.e(TAG, "Removing old configuration for network " + config.SSID);
	     //  wifiManager.removeNetwork(foundNetworkID);
	      // wifiManager.saveConfiguration();
	       //connectFlag=true;
	       //return;
	     }
	     
	     for (ScanResult result : wifiScanList) {
	    	 Log.e("XX","scanning "+result.SSID+":"+config.SSID);
	         if (result.SSID.equals(config.SSID)) {

	             String securityMode = getScanResultSecurity(result);
	             Log.e("ZZ","security mode is "+securityMode);
	             if (securityMode.equalsIgnoreCase("OPEN")) {

	            	 int networkId=0;
	            	 if(foundNetworkID==null)
	            	 {
	            		 config.SSID = "\"" + config.SSID + "\"";
	            		 networkId= wifiManager.addNetwork(config);
	            	 }
	            	 else
	            	 {
	            		 networkId=foundNetworkID;
	            	 }
	        	     if (networkId >= 0) {
	        	       // Try to disable the current network and start a new one.
	        	       if (wifiManager.enableNetwork(networkId, true)) {
	        	         Log.e(TAG, "Associating to network " + config.SSID);
	        	         wifiManager.saveConfiguration();
	        	         //wifiManager.reconnect();
	        	         wifiManager.setWifiEnabled(true);
	        	         connectFlag=true;
	        	       } else {
	        	         Log.e(TAG, "Failed to enable network " + config.SSID);
	        	         connectFlag=false;
	        	       }
	        	     } else {
	        	       Log.e(TAG, "Unable to add network " + config.SSID);
	        	       connectFlag=false;
	        	     }

	                 

	             }
	         }
	     }
	    
	  
	     
	   
	             
	   }
	   
	   String current="";
	   public void connect(String item)
	   {
		   if(wifi!=null)
		   {
			   WifiConfiguration wifiConfiguration = new WifiConfiguration();
		       wifiConfiguration.SSID = item;//quoteNonHex(item);
		      // wifiConfiguration.SSID = String.format("\"{0}\"", item);
		       Log.e("AA","is is "+wifiConfiguration.SSID);
		       current=item;
		       wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		       updateNetwork(wifi, wifiConfiguration);
		       handler.post(new checkConnectionStatus());
		   }
			
		   
	   }
	   
	   Handler handler=new Handler();
	   public String getHostIP()
	   {
	    int x=wifi.getDhcpInfo().serverAddress;
	    
		x = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) ? 
                Integer.reverseBytes(x) : x;		    
	    byte[] bytes = BigInteger.valueOf(x).toByteArray();
	    InetAddress addressStr = null;
		try {
		
			addressStr = InetAddress.getByAddress(bytes);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	      Log.e("ZZ","Host ip address is"+addressStr.getHostAddress());
	      return addressStr.getHostAddress();

	      
	   }
	   
	   private class WifiScanReceiver extends BroadcastReceiver{
		      public void onReceive(Context c, Intent intent) {
		    	  Log.e("ZZ->","LL"+t.getDelta());
		    	 if(t.getDelta() < 30000)
		    	 {
		         wifiScanList = wifi.getScanResults();
		         wifis = new String[wifiScanList.size()];
		         
		         for(int i = 0; i < wifiScanList.size(); i++){
		        	 ScanResult r=wifiScanList.get(i);		        	 
		             wifis[i] = r.SSID;
		             Log.e("ZZ->","scan result is "+wifis[i]);
		         }
		         call.listUpdate();
		    	 }
		    	 else
		    	 {
		    		 if(act!=null)
		    		 act.unregisterReceiver(this);
		    	 }
		         //lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,wifis));
		      }
		   };
	   };
	   
	   
	   //protected void onPause() {
	    ///  unregisterReceiver(wifiReciever);
	  //    super.onPause();
	  // }
	   
	   //protected void onResume() {
	   //   registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	      //super.onResume();
	   //}
	   
	/*   @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	    //  getMenuInflater().inflate(R.menu.menu_main, menu);
	      return true;
	   }
	   /*
//	   @Override
//	   public boolean onOptionsItemSelected(MenuItem item) {
//	      // Handle action bar item clicks here. The action bar will
//	      // automatically handle clicks on the Home/Up button, so long
//	      // as you specify a parent activity in AndroidManifest.xml.
//	      
//	      int id = item.getItemId();
//	      
//	      //noinspection SimplifiableIfStatement
//	      if (id == R.id.action_settings) {
//	         return true;
//	      }
//	      return super.onOptionsItemSelected(item);
//	   }*/
	   

	//}  
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		turnOnOffHotspot(this,true);
//	}
//
//	public  void turnOnOffHotspot(Context context, boolean isTurnToOn) {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiApControl apControl = WifiApControl.getApControl(wifiManager);
//        apControl.act=this;
//        String currentSSID = wifiManager.getConnectionInfo().getSSID();
//        
//        if(apControl.isApSupported())
//        {
//        	
//            apControl.setWifiApEnabled(apControl.getWifiApConfiguration(),isTurnToOn);   
//            Log.e("ZZ","hotspot is supported "+currentSSID);
//        }
//        else
//        {
//        	Log.e("ZZ","hotspot is noy supported ");
//        }
//        if (apControl != null) {
//        	
//                 // TURN OFF YOUR WIFI BEFORE ENABLE HOTSPOT
//        //    if (apControl.isWifiApEnabled()) {
//            //	apControl.setWifiApEnabled(apControl.getWifiApConfiguration(),enabled)
//          // }
// 
//
//        }
//    }
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//}
