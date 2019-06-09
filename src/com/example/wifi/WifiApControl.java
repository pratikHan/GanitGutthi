package com.example.wifi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;
 
/**
 * This class is use to handle all Hotspot related information.
 * 
 *
 * 
 */
public class WifiApControl {
	public static String TAG="WifiApControl";
    private static Method getWifiApState;
    private static Method isWifiApEnabled;
    private static Method setWifiApEnabled;
    private static Method getWifiApConfiguration;
 
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
 
    public static final int WIFI_AP_STATE_DISABLED = WifiManager.WIFI_STATE_DISABLED;
    public static final int WIFI_AP_STATE_DISABLING = WifiManager.WIFI_STATE_DISABLING;
    public static final int WIFI_AP_STATE_ENABLED = WifiManager.WIFI_STATE_ENABLED;
    public static final int WIFI_AP_STATE_ENABLING = WifiManager.WIFI_STATE_ENABLING;
    public static final int WIFI_AP_STATE_FAILED = WifiManager.WIFI_STATE_UNKNOWN;
 
    public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = WifiManager.EXTRA_PREVIOUS_WIFI_STATE;
    public static final String EXTRA_WIFI_AP_STATE = WifiManager.EXTRA_WIFI_STATE;
 
    static {
        // lookup methods and fields not defined publicly in the SDK.
        Class<?> cls = WifiManager.class;
        for (Method method : cls.getDeclaredMethods()) {
        	Log.e("ZZ",method.getName());
            String methodName = method.getName();
            if (methodName.equals("getWifiApState")) {
            	method.setAccessible(true);
                getWifiApState = method;
            } else if (methodName.equals("isWifiApEnabled")) {
            	method.setAccessible(true);
                isWifiApEnabled = method;
            } else if (methodName.equals("setWifiApEnabled")) {
            	method.setAccessible(true);
                setWifiApEnabled = method;
            } else if (methodName.equals("getWifiApConfiguration")) {
            	method.setAccessible(true);
                getWifiApConfiguration = method;
            }
        }
    }
 
    public static boolean isApSupported() {
        return (getWifiApState != null && isWifiApEnabled != null
                && setWifiApEnabled != null && getWifiApConfiguration != null);
    }
 
    private WifiManager mgr;
    Activity act;
    private WifiApControl(WifiManager mgr,Activity _act) {
        this.mgr = mgr;
        this.act=_act;
    }
 
    public static WifiApControl getApControl(WifiManager mgr,Activity act) {
        if (!isApSupported())
            return null;
        return new WifiApControl(mgr,act);
    }
 
    public boolean isWifiApEnabled() {
        try {
            return (Boolean) isWifiApEnabled.invoke(mgr);
        } catch (Exception e) {
            Log.e("BatPhone", e.toString(), e); // shouldn't happen
            return false;
        }
    }
 
    public int getWifiApState() {
        try {
            return (Integer) getWifiApState.invoke(mgr);
        } catch (Exception e) {
            Log.e("BatPhone", e.toString(), e); // shouldn't happen
            return -1;
        }
    }
 
    public WifiConfiguration getWifiApConfiguration() {
        try {
        	if(isWifiApEnabled())
        	{
        		Log.e("ZZ","wifi ap is enabled");  
        		
        	}
        	else
        	{
        		Log.e("ZZ","wifi ap is disabled");
        	}
        	WifiConfiguration wifiConfiguration =(WifiConfiguration) getWifiApConfiguration.invoke(mgr);
               	
            return (wifiConfiguration) ;
        } catch (Exception e) {
            Log.e("BatPhone", e.toString(), e); // shouldn't happen
            e.printStackTrace();
            return null;
        }
        
    }
    

    public boolean CheckStatus()
    {
    	if(isWifiApEnabled())
    		return true;
    	else
    		return false;
    }
 
    public void setWifiAPDisabled()
    {
    	WifiConfiguration netConfig = new WifiConfiguration();
    	WifiManager wifi = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);

    	Method enableWifi;
		try {
			enableWifi = wifi.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class,boolean.class);
			enableWifi.invoke(wifi,null,false);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public boolean setWifiApEnabled(WifiConfiguration config, boolean enabled,String name) {
    	Log.e("ZZ","setWifiApEnabled "+name);
    	WifiManager wifi = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);
    	 WifiConfiguration netConfig = new WifiConfiguration();
    	 netConfig.SSID = name;
    	 try {
    	wifi.setWifiEnabled(false);
    	if(isWifiApEnabled())
    	{
    		Log.e(TAG,"hotspot is enabled ");
    		Method enableWifi = wifi.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class,boolean.class);
        	enableWifi.invoke(wifi,null,false);
    	}
    	Method setWifiConfig = null;
    	setWifiConfig = wifi.getClass().getMethod("setWifiApConfiguration",WifiConfiguration.class);
    	setWifiConfig.invoke(wifi,netConfig);

    	Method enableWifi = wifi.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class,boolean.class);
    	enableWifi.invoke(wifi,null,true);
		
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	/*
    	wifi.setWifiEnabled(false);
    	Method[] wmMethods = wifi.getClass().getDeclaredMethods();
    	for(Method method: wmMethods){
    	  if(method.getName().equals("setWifiApEnabled")){
    	    WifiConfiguration netConfig = new WifiConfiguration();
    	    netConfig.SSID = "\"klug\"";
    	    netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
    	    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
    	    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
    	    netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
    	    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
    	    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
    	    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  

    	    try {
    	    	Method mSetWifiApEnabled = wifi.getClass().getMethod(
    					"setWifiApEnabled", WifiConfiguration.class, boolean.class);
    			mSetWifiApEnabled.invoke(wifi, netConfig, true);
    	    } catch (IllegalArgumentException e) {
    	      e.printStackTrace();
    	    } catch (IllegalAccessException e) {
    	      e.printStackTrace();
    	    } catch (InvocationTargetException e) {
    	      e.printStackTrace();
    	    } catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	  }
    	}    	*/
    	return false;
//        try {
//            return (Boolean) setWifiApEnabled.invoke(mgr, config, enabled);
//        } catch (Exception e) {
//            Log.e("BatPhone", e.toString(), e); // shouldn't happen
//            e.printStackTrace();
//            return false;
//        }
    }
}