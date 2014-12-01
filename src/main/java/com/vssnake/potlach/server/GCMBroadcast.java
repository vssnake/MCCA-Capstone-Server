package com.vssnake.potlach.server;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class GCMBroadcast {
	private static final long serialVersionUID = 1L;
	
	// The SENDER_ID here is the "Browser Key" that was generated when I
	// created the API keys for my Google APIs project.
	private static final String SENDER_ID = "AIzaSyBtdio6AsjxW-D1HpQPEUIzbx7QYhDtIs4";
	
	// This array will hold all the registration ids used to broadcast a message.

	private List<String> androidTargets = new ArrayList<String>();
	
	private static final String MESSAGE ="Gift changes";
	private static final String KEY = "ChangeKey";
	
	
	public GCMBroadcast(){
		
	}

	// Add a android device to list for broadcast info
	public void addAndroidTarget(String androidID){
		androidTargets.add(androidID);
	}
	//Remove android device for broadcast
	public void removeAndroidTarget(String androidID){
		androidTargets.remove(androidID);
	}
	
	//I send a notification to all android devices connected to the web server. 
	//In this moment the only purpose of this is tell the client when a user upload or delete a gift
	public void doBradcast(){
		// Instance of com.android.gcm.server.Sender, that does the
		// transmission of a Message to the Google Cloud Messaging service.
		Sender sender = new Sender(SENDER_ID);

		// This Message object will hold the data that is being transmitted
		// to the Android client devices.
		Message message = new Message.Builder()
		
	
		
		// This Message object will hold the data that is being transmitted
		// to the Android client devices.
		.collapseKey(KEY)
		.timeToLive(30)
		.delayWhileIdle(true)
		.addData("message", MESSAGE)
		.build();
				
		
		try {
			// use this for multicast messages.  The second parameter
			// of sender.send() will need to be an array of register ids.
			MulticastResult result = sender.send(message, androidTargets, 1);
			
			if (result.getResults() != null) {
				int canonicalRegId = result.getCanonicalIds();
				if (canonicalRegId != 0) {
					
				}
			} else {
				int error = result.getFailure();
				System.out.println("Broadcast failure: " + error);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
}
