package com.aledoux.bluspace;

import java.util.ArrayList;
import java.util.UUID;
import java.io.IOException;

import android.bluetooth.*;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.widget.Toast;
/**
 *
 * @author Shane del Solar
 */
public class Bluetooth
{
    public static BluetoothAdapter adapter;
    public static BluetoothDevice device;
    
    public static BluetoothSocket socket;
    public static BluetoothServerSocket serverSocket;
    
    public static UUID uuid = new UUID(13666, 13666);
    public static String bluetoothName = "UFO";
    
    private static ArrayList<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
    
    public static final BroadcastReceiver broadcastListener = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	Log.i("client","found a bluetooth!!");
	        	Toast.makeText(context, "found a device!", Toast.LENGTH_LONG).show();
	        	
	        	//simplified version
	        	device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        	
	        	/**
	            // Get the BluetoothDevice object from the Intent
	            //deviceList.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            deviceList.add(device);
	            // Add the name and address to an array adapter to show in a ListView
	            //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	             **/
	        }
    	}
    }; 
    
    
    public static void init()
    {
       
    }
    
    public static void makeServer()
    {   
    	new AcceptThread();
       /**
       try
       {
           serverSocket = adapter.listenUsingRfcommWithServiceRecord(bluetoothName, uuid);
           socket = serverSocket.accept();
           serverSocket.close();
       }
       catch(IOException ioe)
       {
           
       }
       **/
    }
    
    //thread for creating a server and listening for connections to it
    private static class AcceptThread implements Runnable{
    	Thread thread;
    	
    	public AcceptThread(){
    		try{
        		Log.i("server", "thread test success");
    			serverSocket = adapter.listenUsingRfcommWithServiceRecord(bluetoothName, uuid);
        		Log.i("server", "thread test success 2");
        		thread = new Thread(this);
        		thread.start();
    		}
    		catch (IOException e){
        		Log.i("server", "thread test fail");}
    	}
    	
    	public void run(){
    		Log.i("server", "thread test success 3");
    		socket = null;
    		
    		while(true){
        		Log.i("server", "thread test success 4");
    			try{
            		Log.i("server", "thread test success 5");
    				socket = serverSocket.accept();
            		Log.i("server", "thread test success 6");
    				
    				if (socket != null){
        				serverSocket.close();
        				Log.i("server","connection accepted!");
        				break;
        			}
    			}
    			catch(IOException e){
    				Log.i("server","whoops");
    				break;
    			}
    		}
    	}
    	
    	public void cancel(){
    		try{
    			serverSocket.close();
    		}
    		catch (IOException e){}
    	}
    }
    
    public static void makeClient()
    {
    	new ConnectThread();
    	/**
        adapter.startDiscovery();
        
        
        
        //Block until all devices are found
        while(adapter.isDiscovering()){}
        
        try
        {
            socket = device.createRfcommSocketToServiceRecord(uuid);
        }
        catch(IOException ioe)
        {
            
        }
        **/
    }
    
    //class that creates a thread which tries to connect to a server
    private static class ConnectThread implements Runnable{
    	Thread thread;
    	
    	public ConnectThread(){
    		adapter.cancelDiscovery();
    		adapter.startDiscovery();
    		
    		thread = new Thread(this);
    		thread.start();
    		
    		/**
    		try{
    			socket = device.createRfcommSocketToServiceRecord(uuid);
    			thread = new Thread(this);
    			thread.start();
    		}
    		catch (IOException e){}
    		**/
    	}

		public void run() {
			Log.i("client","thread start");
			
			while(true){
				Log.i("client","thread running");
				if (device != null){
					Log.i("client", "device found 2");
					adapter.cancelDiscovery();
					
					try{
						Log.i("client","trying to connect with the socket");
						socket.connect();
					}
					catch(IOException connectException){
						try{
							socket.close();
						}
						catch(IOException closeException){}
						return;
					}
				}
			}
			
			/**
			adapter.cancelDiscovery();
			
			try{
				socket.connect();
			}
			catch(IOException connectException){
				try{
					socket.close();
				}
				catch(IOException closeException){}
				return;
			}
			**/
		}
    	
		public void cancel(){
			try{
				socket.close();
			} catch (IOException e){}
		}
    }
        
    public static void uninit()
    {
    	try{
    		socket.close();
    	}
    	catch (Exception e){}
    }
}
