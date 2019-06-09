package com.example.bluetooth;

import android.os.*;

import java.nio.*;

import android.widget.*;
import android.util.*;
import android.app.*;
import android.content.Context;

import java.net.*;
import java.io.*;

public class ROVStateX
{
	
	public CommunicationThreadX comm;
	public boolean comm_flag =false;
	public static String TAG="ROVStateX";
    public static String back;
    private static int delta;
    public static String move4;
    public static String front;
    public static String move3;
    public static String left;
    private static int max_torpedo;
    public static String nitro;
    public static String ping;
    public static String move1;
    public static String right;
    public static String move2;
    
    int heave;
    int id;
    //private Handler mHandler;
    private  Runnable mProgressChecker;
    int surge;
    int torpedo_left;
    int torpedo_right;
    int yaw;
    
    BluetoothLibrary library;
    static {
        ROVStateX.delta = 10;
        ROVStateX.max_torpedo = 3;
        ROVStateX.move4 = "move4";
        ROVStateX.front = "front";//
        ROVStateX.back = "back";//
        ROVStateX.left = "left";//
        ROVStateX.right = "right";//
        ROVStateX.nitro = "dance";//
        ROVStateX.move1 = "move1";
        ROVStateX.move2 = "move2";
        ROVStateX.ping = "ping";//
        ROVStateX.move3 = "delay";
    }
    
    public ROVStateX() {

        this.id = 0;
    }
    
    
    
    public void connect(final String s) {
        library.connect(s);
    }
    
    byte[] createMotionCommand(final String s, final int n,final int vel, final int n2) {
        final ByteBuffer allocate = ByteBuffer.allocate(7);
        
        Log.e("ZZ","motion command is "+n+":"+vel);
        allocate.put((byte)('<'));
        if (s.equals(ROVStateX.front)) {
            allocate.put((byte)1);
        }
        if (s.equals(ROVStateX.right)) {
            allocate.put((byte)4);
        }
        if (s.equals(ROVStateX.back)) {
            allocate.put((byte)2);
        }
        if (s.equals(ROVStateX.left)) {
            allocate.put((byte)3);
        }
        if (s.equals(ROVStateX.nitro)) {
            allocate.put((byte)5);
        }
        if (s.equals(ROVStateX.move1)) {
            allocate.put((byte)6);
        }
        if (s.equals(ROVStateX.move2)) {
            allocate.put((byte)7);
        }
        if (s.equals(ROVStateX.move3)) {
            allocate.put((byte)8);
        }
        if (s.equals(ROVStateX.move4)) {
            allocate.put((byte)9);
        }
        if (s.equals(ROVStateX.ping)) {
            allocate.put((byte)0);
        }
       // if (s.equals(ROVStateX.right)||s.equals(ROVStateX.left)) {
        //value
        //	allocate.putShort((short)n);
        //}
       // else
        //{
        	 allocate.put((byte)n);
             //velocity
             allocate.put((byte)vel);
        	
        //}
        //id
        allocate.putShort((short)n2);
        //
        allocate.put((byte)('>'));
        //reset
        allocate.position(0);
        return allocate.array();
    }
    
    public ArrayAdapter<String> getList() {
        return library.getList();
    }
    
    public boolean getStatus() {
    	
        boolean cstatus = false;
        if(comm!=null)
        {
        	cstatus=this.comm.cstatus;
        }
        this.setStatus();
        return cstatus;
    }
    
    public boolean isOn() {
        return library.isOn();
    }
    
    public void play() {
        library.start();
    }
    
    public void sendCommand(final String s, final int n,final int vel) {
        byte[] array = null;
        int n2 = 0;
        int n3 = 0;
        
        if (s.equals(ROVStateX.front) || s.equals(ROVStateX.back) || s.equals(ROVStateX.left) || s.equals(ROVStateX.right)|| s.equals(ROVStateX.move4)|| s.equals(ROVStateX.move1)|| s.equals(ROVStateX.move2)|| s.equals(ROVStateX.move3)) {
                //final String nitro = ROVState.nitro;
                array = this.createMotionCommand(s, n,vel, this.id);
                n2 = this.id;
                ++this.id;
                n3 = 6;
        }
        if (s == ROVStateX.ping) {
            array = this.createMotionCommand(s, -1,vel, this.id);
            n2 = this.id;
            ++this.id;
            n3 = 6;
        }
        if (this.id == 65535) {
            this.id = 0;
        }
        
        //len
        if(comm!=null)
        {
        Log.e("Command ", String.valueOf(s) + ":" + array);
        this.comm.addWork(s, array, n3, n2);
        }
    }
    
    public void sendUpdates() {
    	
        this.sendCommand(ROVStateX.ping, -1,0);
    }
    
    public void setStatus() {
    	if(comm!=null)
        this.comm.setStatus();
    }
    
    public void startX()
    {
    	if (library != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (library.getState() == BluetoothLibrary.STATE_NONE) {
              // Start the Bluetooth com services
            	library.start();
            }
        }
    }
    
    private void sendMessage(){
        // Check that we're actually connected before trying anything
            if (library.getState() != BluetoothLibrary.STATE_CONNECTED) {
                //Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
                return;
            }
        //EditText et = (EditText)this.findViewById(R.id.edittext_out);
        //String msg = et.getText().toString();
        //msg += "\n";
        
        //btCom.write(msg);
        return;
      }
    
    
    Context c;
    
    public Context getApplicationContext()
    {
    	return c;
    }
    public Handler chandler;
    public String name;
    public void startCommunication(final Activity activity,String _name) throws UnknownHostException, IOException {
    	
    	chandler=new Handler();
    	c=activity;
    	library=new BluetoothLibrary(activity,mHandler);
        //library.comm = new CommunicationThread(activity,name);        
        //this.On();
    	name=_name;
    	library.connect(name);
    	
        this.mProgressChecker = new Runnable() {
            @Override
            public void run() {
                ROVStateX.this.sendUpdates();
                if (ROVStateX.this.mHandler != null) {
                    ROVStateX.this.mHandler.postDelayed(ROVStateX.this.mProgressChecker,1000L);
                }
            }
        };

        
        
    }
    public String mConnectedDeviceName;
    
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            
            case BluetoothLibrary.MESSAGE_STATE_CHANGE:
                Log.e(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothLibrary.STATE_CONNECTED:
                	comm_flag=true;
                    Log.e("GG","completed with connect");
                    //this.play();
                    
                    comm=library.mConnectedThread;
                    chandler.post(mProgressChecker);
                    //setStatus(R.string.connected);
                    break;
                case BluetoothLibrary.STATE_CONNECTING:
                	comm_flag=false;
                    //setStatus(R.string.connecting);
                    break;
                case BluetoothLibrary.STATE_LISTEN:
                case BluetoothLibrary.STATE_NONE:
                	comm_flag=false;
                    //setStatus(R.string.not_connected);
                    break;
                    
                case BluetoothLibrary.STATE_RECONNECT:
                	Log.e("ZZ","reconnecting ");
                	library.connect(name);
                	break;
                }
                break;
                
            case BluetoothLibrary.MESSAGE_WRITE:
                break;
            
            //case BluetoothInterfaceX.MESSAGE_READ:
            //    datBuf.add((String) msg.obj, true);
            //    break;
            
            case BluetoothLibrary.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(BluetoothLibrary.DEVICE_NAME);
                //Toast.makeText(getApplicationContext(), "Connected to "+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            
            case BluetoothLibrary.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(BluetoothLibrary.TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            
            //case DataBuffer.MESSAGE_NEW_VALUE:
            //  Double d = (Double)msg.obj;
             // UpdatePlot(d);
             // break;
              
            }
        }
    };
    
    public void stop() {
        //this.mHandler = null;
        this.library.stop();
               
    }
}
