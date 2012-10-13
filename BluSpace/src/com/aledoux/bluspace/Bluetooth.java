package com.aledoux.bluspace;

import java.util.ArrayList;
import java.util.UUID;
import java.io.IOException;

import android.bluetooth.*;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
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
            // Get the BluetoothDevice object from the Intent
            //deviceList.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            deviceList.add(device);
            // Add the name and address to an array adapter to show in a ListView
            //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
        }
    }
}; 
    
    
    public static void init()
    {
       
    }
    
    public static void makeServer()
    {        
       try
       {
           serverSocket = adapter.listenUsingRfcommWithServiceRecord(bluetoothName, uuid);
           socket = serverSocket.accept();
           serverSocket.close();
       }
       catch(IOException ioe)
       {
           
       }
       
       
    }
    
    public static void makeClient()
    {
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
    }
        
    public static void uninit()
    {
        
       
    }
}
