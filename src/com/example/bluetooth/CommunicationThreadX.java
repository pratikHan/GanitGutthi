package com.example.bluetooth;



import java.util.concurrent.*;

import android.os.Handler;
import android.util.*;

import java.nio.*;
import java.io.*;

import android.bluetooth.*;

import java.util.*;

import com.example.kluggame2.MainActivity;





public class CommunicationThreadX extends Thread
{
    private static final String SHUTDOWN_REQ = "SHUTDOWN";
    private static final UUID SPP_UUID;
    private BlockingQueue<BMessage> Queue;
    private BlockingQueue<BMessage> ReplyQueue;
    public boolean cstatus;
    BMessage current;
    Boolean isOpen;
    Boolean isTerminated;
    //BluetoothLibrary library;
    public Boolean next_flag;
    BMessage reply;
    int scount;
    Boolean shuttingDown;
    
    static {
        SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }
    BluetoothSocket mSocket;
    String name="";
    public Handler mHandler;
    public CommunicationThreadX(BluetoothSocket _mSocket,Handler _mhandler)   {
        this.Queue = new ArrayBlockingQueue<BMessage>(500);
        this.ReplyQueue = new ArrayBlockingQueue<BMessage>(500);
        this.isTerminated = false;
        this.shuttingDown = false;
        this.isOpen = false;
        this.next_flag = false;
        this.cstatus = false;
        this.scount = 0;

        //.name=_name;
        mHandler=_mhandler;
        mSocket=_mSocket;
    }
    
    public void addWork(final String s, final byte[] array, final int n, final int n2) {
        if (this.shuttingDown || this.isTerminated) {
            //Log.w("XX", "thread has shutdown or terminated"); 
            return;
        }
        try {

            Log.e("TTTT","PPPPP"+s+":"+ROVStateX.move1+":"+s.equals(ROVStateX.front));
            if (s.equals(ROVStateX.front) || s.equals(ROVStateX.back) ||
                    s.equals(ROVStateX.left) || s.equals(ROVStateX.right) || s.equals(ROVStateX.nitro)
                    || s.equals(ROVStateX.move4) || s.equals(ROVStateX.move3) || s.equals(ROVStateX.move2)
                    || s.equals(ROVStateX.move1)) {
            	//array -length - id            	
                current = new BMessage(s, array, n, n2);
                Log.e("PPPPP", "current id is " + n2 + ":" + s);
            }
            else if(s.equals(ROVStateX.ping))
            {
                Log.w("GG1","commadn is ping");
            }
            else
            {
                Log.w("GG1","invalid command-"+s+":");
            }
            
            
            this.Queue.put(new BMessage(s, array, n, n2));
            //Log.i("XX", "added work-" + this.Queue.size());
            
        }
        catch (InterruptedException ex) {
            Log.w("EE", "error");
            Thread.currentThread().interrupt();
            throw new RuntimeException("Unexpected interruption");
        }
    }
    
    public Boolean parseResponse(final byte[] array, final BMessage message) {
        //Log.w("RR", " reveuved bytes are " + array[0] + ":" + array[1] + ":" + array[2] + ":" + array[3] + ":" + array[4]);
        final ByteBuffer allocate = ByteBuffer.allocate(array.length);
        //Log.i("parese", "length of messsage is " + array.length);
        allocate.put(array, 0, array.length);
        //Log.i("parese", "length of buffer is " + allocate.capacity() + ":" + allocate.position());
        allocate.position(0);
        //header
        final byte value = allocate.get();
        //Log.w("GG", "rsss " + value + ":" + -86 + ":" + 170);
        if (value != '<') {
        	this.reply=null;
            return false;
        }
        //header-1value2-
        
        //header --- length --- id
        this.reply = new BMessage("", null, allocate.getShort(), allocate.getShort());
        return true;
    }
    
   public void reset()
   {
	   mHandler.obtainMessage(BluetoothLibrary.MESSAGE_STATE_CHANGE, BluetoothLibrary.STATE_RECONNECT, -1).sendToTarget();
       
   }
        

    @Override
    public void run() {
    	//Log.e("ZZ","string mConnectedThread");
        final boolean booleanValue = this.isOpen;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int n = 0;
        
        if (booleanValue==false) {
            this.isOpen = true;
        }
        
        while (true) {
        	if (shuttingDown==true) {
        		break;
        	}
            //while (library.BA.enable()==true) {
            	//if (shuttingDown==true) {
            		//break;
            	//}
                                    	
            	BMessage message = null;
                try {
            	message = this.Queue.take();
            	
                if (message.type == "SHUTDOWN") {
                    break;
                }
                
                if (mSocket == null) {
                    Log.e("GG", "linbrary socket is null");
                    break;
                }
                    //if(!message.type.equals("ping"))

                if (message.bdata == null) {
                	//Log.w("GG", "skipping Writing data " + message.type + ":");
                    continue;
                }
                int repeat=1;
                    boolean b = false;

                    while(repeat>0) {
                        if (outputStream == null) {
                            inputStream = mSocket.getInputStream();
                            outputStream = mSocket.getOutputStream();
                        }
                        outputStream.write(message.bdata);
                        outputStream.flush();
                        Log.e("PPPPP", "Writing data " + message.type + ":");

                        int timeout = 3;
                        final byte[] array = new byte[message.len];
                        int i;
                        int read = 0;
                        
                        
                        
                        for (i = 0; i < message.len; i += read) {
                            if (inputStream.available() <= 0) {
                                timeout = timeout - 1;
                                if (timeout > 0) {
                                    Thread.sleep(10);
                                    continue;
                                } else {
                                    Log.e("ZZ", "timeout has occured");
                                    break;
                                }
                            }

                            read = inputStream.read(array, i, message.len - i);
                            if (read == -1) {
                                break;
                            }
                            if(array[0]!='<' && read>0)
                            {
                            	i=0;
                            	read=0;
                            	Log.e("ZZ","Reading again,since repy was invalid");
                            	continue;
                            }
                        }
                        //if(!message.type.equals("ping"))
                        Log.e("PPPPP", "completed reading ");

                        if (message.type.equals(ROVStateX.ping)) {
                        	 Log.e("PPPPP", "parsing reading "+array[0]+":"+array[1]+":"+array[2]+":"+array[3]+":"+array[4]+":"+array[5]);
                            b = this.parseResponse(array, message);
                            repeat=0;
                        }


                       /* if (message.type.equals(ROVStateX.front) || message.type.equals(ROVStateX.back) || message.type.equals(ROVStateX.left) || message.type.equals(ROVStateX.right
                        )||message.type.equals(ROVStateX.move1) || message.type.equals(ROVStateX.nitro) ) {
                            b = this.parseResponse(array, message);
//                            Log.e("ZZ",":"+message.type+":"+message.id+":"+this.reply.id);
                            if (message.id == this.reply.id) {
                                Log.e("rrrrrr", "valid reply");
                                repeat=1;
                            } else {
                                Log.e("rrrrrr", "invalid reply");
                                repeat=0;
                            }
                            b = false;
                        }*/
                        repeat=0;
                    }

                //Log.e("ZZ", "parse respobse is " + b);
                if (b) {

                    this.cstatus = true;
                    if (current != null) {
                        Log.w("GG1", "current received reply :" + this.current.type + ":" + this.current.id+":"+next_flag);
                    }
                    if (reply != null) {
                        Log.w("GG1", "reply received reply :" + this.reply.type + ":" + this.reply.id + ":" + this.reply.len + ":" + next_flag);
                    }
                    else {
                        //L//og.w("GG", "reply is null");
                    }

                    if (this.reply != null && current != null && current.id == reply.len && reply.len>=20) {
                        current = null;
                        next_flag = true;
                        Log.w("rrrrrr", "completed motion command---------------------------------------------------------------");
                    }
                    else
                    {
                        Log.e("rrrrrr","reply is "+reply.len+":"+reply.id);
                        if(!(reply.len>=20) && reply.len!=-1 && reply.id>0)
                        {
                            Log.e("ZZ","before event callback");
                            eventCallback(reply.len);
                            Log.e("ZZ", "after event callback");
                        }

                    }
                    //event_flag=false;
                    this.scount = 0;                    ;
                }
                else
                {
                	Log.w("ZZ", "invalid response ");
                }
                
                }
                catch (Exception ex) {
                    Log.w("GG", "error in sending socket data" + ex.getLocalizedMessage() + ":");
                    ex.printStackTrace();
                    reset();
                    break;
                }
            }
            //}
            this.Queue.clear();
            //Log.w("XX", "completed thread execution");
            return;

        }


    public void eventCallback(int code)
    {
        //if(act!=null)
        //act.callEvent(code);

    }

    MainActivity act;
    public void setStatus() {
        this.cstatus = false;
    }

    
    public void cancel() 
    {
    	try {
			shutDown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void shutDown() throws InterruptedException {
        this.shuttingDown = true;
        while (true) {
            try {
                mSocket.close();
                this.Queue.put(new BMessage("SHUTDOWN", null, 0, -1));
            }
            catch (IOException ex) {
                ex.printStackTrace();
                continue;
            }
            break;
        }
    }
    
    public String validateCommand(final int n) {
        String front;
        if (n == 1) {
            front = ROVStateX.front;
        }
        else {
            if (n == 2) {
                return ROVStateX.back;
            }
            if (n == 3) {
                return ROVStateX.left;
            }
            if (n == 4) {
                return ROVStateX.right;
            }
            front = null;
            if (n == 0) {
                return ROVStateX.ping;
            }
        }
        return front;
    }
    
    
    
    
}

