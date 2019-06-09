package com.example.bluetooth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;



class BluetoothLibrary
{
	
	public static String TAG="BluetoothLibraryX";
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
    public BluetoothAdapter BA;
    Activity act;
    private ArrayAdapter<String> btArrayAdapter;
    BluetoothDevice device;
    private ArrayList<BluetoothDevice> mDeviceList;
    
    BluetoothSocket mSocket;
    public Set<BluetoothDevice> pairedDevices;
    
    private ConnectThread mConnectThread;
    public CommunicationThreadX mConnectedThread;
    
	  private Context mContext;
	  private Handler mHandler;

	    // Key names sent to the main activity
	    public static final String DEVICE_NAME = "device_name";
	    public static final String TOAST = "toast";

	    
	    // Message types sent to the main activity
	    public static final int MESSAGE_STATE_CHANGE = 1;
	    public static final int MESSAGE_READ = 2;
	    public static final int MESSAGE_WRITE = 3;
	    public static final int MESSAGE_DEVICE_NAME = 4;
	    public static final int MESSAGE_TOAST = 5;

	  
		// Constants that indicate the current connection state
	    public static final int STATE_NONE = 0;       // we're doing nothing
	    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
	    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
	    public static final int STATE_RECONNECT = 4;  // reconnecting to remote device
	    private int mState;

	    
	    public int getState() {
	        synchronized (this) {
	            return this.mState;
	        }
	    }

	    
        /**
         * Indicate that the connection was lost and notify the UI Activity.
         */
        private void connectionLost() {
            // Send a failure message back to the Activity
            Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
            Bundle bundle = new Bundle();
            bundle.putString(TOAST, "Device connection was lost");
            msg.setData(bundle);
            mHandler.sendMessage(msg);

            // Start the service over to restart listening mode
            start();
        }
	    /**
	     * Indicate that the connection attempt failed and notify the UI Activity.
	     */
	    private void connectionFailed() {
	        // Send a failure message back to the Activity
	        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
	        Bundle bundle = new Bundle();
	        bundle.putString(TOAST, "Unable to connect device");
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);
	        
	        // Start the service over to restart listening mode
	        //start();
	    }

	 
	    /**
         * Start the chat service. Specifically start AcceptThread to begin a
         * session in listening (server) mode. Called by the Activity onResume() */
        public synchronized void start() {
            Log.d(TAG, "start");
            
         // Cancel any thread attempting to make a connection
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

            // Cancel any thread currently running a connection
            if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
            
            setState(STATE_NONE);

        }

	    /**
	     * Start the ConnectedThread to begin managing a Bluetooth connection
	     * @param socket  The BluetoothSocket on which the connection was made
	     * @param device  The BluetoothDevice that has been connected
	     */
	    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
	            device, final String socketType) {
	        Log.e(TAG, "connected, Socket Type:" + socketType);

	        // Cancel the thread that completed the connection
	        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

	        // Cancel any thread currently running a connection
	        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

	        // Start the thread to manage the connection and perform transmissions
	        mConnectedThread = new CommunicationThreadX(socket,mHandler);
	        
	        

	        // Send the name of the connected device back to the UI Activity
	        /*Message msg = mHandler.obtainMessage(MESSAGE_DEVICE_NAME);
	        Bundle bundle = new Bundle();
	        bundle.putString(DEVICE_NAME, device.getName());
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);*/
	        setState(STATE_CONNECTED);
	        
	        mConnectedThread.start();
	    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
              tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
        }

        
        public void run() {
            Log.e(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            BA.cancelDiscovery();
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothLibrary.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }
    
    public BluetoothLibrary(final Activity act,final Handler handler) {
        this.mSocket = null;
        this.mDeviceList = new ArrayList<BluetoothDevice>();
        this.act = null;                
        this.act = act;
        
	    mContext = act;
	    mHandler = handler;
	    mState = STATE_NONE;

        
    }
    
     void access(final BluetoothLibrary bluetoothLibrary, final ArrayList mDeviceList) {
        bluetoothLibrary.mDeviceList = (ArrayList<BluetoothDevice>)mDeviceList;
    }
    

    
    public void On() {
        //Log.w("GG", "onnnn ");
        this.init();
        //Log.w("GG", "completed onnnn ");
    }
    
    public synchronized boolean connect(final String s) {

    	start();
    	init();
        if (this.pairedDevices != null) {
            for (final BluetoothDevice device : this.pairedDevices) {
            	
                Log.w("GG", "present devices is " + device.getName() + ":" + s);
                
                if (device!=null && device.getName()!=null && device.getName().equals(s)) {
                    this.device = device;
                    break;
                }
            }
        }
        else {
            Log.w("GG", "cannot find device");
        }
        if (this.device == null) {
            return false;
        }
        Log.w("GG", "connecting to device name  " + this.device.getName());
        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device,false);
        mConnectThread.start();
        setState(STATE_CONNECTING);
        return true;
    }
    
    private synchronized void setState(int state) {
        Log.e(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    
    public Context getApplicationContext() {
        return this.act.getApplicationContext();
    }
    
    public ArrayAdapter<String> getList() {
        this.pairedDevices = (Set<BluetoothDevice>)this.BA.getBondedDevices();
        this.btArrayAdapter = (ArrayAdapter<String>)new ArrayAdapter<String>((Context)this.act, 0);
        for (final BluetoothDevice bluetoothDevice : this.pairedDevices) {
            this.btArrayAdapter.add(bluetoothDevice.getName());
            //Log.w("GG", "device name  " + bluetoothDevice.getName());
        }
        return this.btArrayAdapter;
    }
    
    
    public void reconnect()
    {
    	
    }
    public boolean init() {
        if (this.BA != null && this.BA.isEnabled()) {
            Log.e(TAG, "disabled enabling");
            BA.disable();
        }
        
        
        
            try {
            	
            	Thread.sleep(1000L);
            	(this.BA = BluetoothAdapter.getDefaultAdapter()).enable();
                Thread.sleep(3000L);
                this.getList();
                return true;
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
                return false;
            }                        
    }
    
    public boolean isOn() {
    	Log.e("ZZ","state is "+this.BA.isEnabled()+":"+getState()+":"+STATE_CONNECTED);
    	if(this.BA.isEnabled() && getState()==STATE_CONNECTED)
    	{
        return true;
    	}
    	else
    	{
    	return false;
    	}
    }
    
    public void off() {
        this.BA.disable();
    }
    
    
    /**
     * Stop all threads
     */
    public synchronized void stop() {
       Log.d(TAG, "stop");
    
       if (mConnectedThread != null) {
           mConnectedThread.cancel();
           mConnectedThread = null;
       }

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        
    
        setState(STATE_NONE);
        off();
    }
    

    
}




