package com.example.smsinformer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class ServiceExample extends IntentService {
	public ServiceExample(String name) {
		super(name);
		Log.e("Service create", "creating service");
		// TODO Auto-generated constructor stub
	}

	public ServiceExample() {
		super("com.example.smsinformer.ServiceExample");
		Log.e("Service create", "creating service");
		// TODO Auto-generated constructor stub
	}

	ArrayList<String> receivers = new ArrayList<String>();
	ArrayList<String> messages = new ArrayList<String>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

		Log.v("Thread service", "Creating service");
		Log.v("Thread service", "Reading receivers file");
		String path = Environment.getExternalStorageDirectory()
				+ "/SmsInformer/receivers.txt";

		BufferedReader rd;
		// final StringBuffer storedString = new StringBuffer();

		try {
			rd = new BufferedReader(new FileReader(path));
			String line = null;
			// read all the lines till the end
			while ((line = rd.readLine()) != null) {
				receivers.add(line);
			}
			rd.close(); // close reader

			rd.close(); // close reader

		} catch (Exception e) {
			Log.e("Service run error", e.getMessage());
			Log.e("Service run", "Stopping service (harakiri)");
			AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(this, ServiceExample.class);
			PendingIntent pintent = PendingIntent
					.getService(this, 0, intent, 0);
			alarm.cancel(pintent);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service Destroy", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Toast.makeText(this, "Service LowMemory", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Toast.makeText(this, "Service start", Toast.LENGTH_LONG).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.v("Service", "Starting");
		Toast.makeText(this, "task perform in service", Toast.LENGTH_LONG)
				.show();
		ThreadDemo td = new ThreadDemo();
		td.start();
		return super.onStartCommand(intent, flags, startId);
	}

	private class ThreadDemo extends Thread {
		@Override
		public void run() {
			super.run();
			// while(true)
			// {
			Log.v("Thread service", "Running service thread");
			boolean s = ReadMessages();
			if (!s)
				Log.v("Thread service", "ReadMessages call failed, aborting");
			else if (messages.size() == 0)
				Log.v("Thread service", "No messages, abotring");
			else if (receivers.size() == 0)
				Log.v("Thread service", "No receivers, abotring");
			else {
				SmsManager smsManager = SmsManager.getDefault();
				//ArrayList<String> msgTexts = null;
				//ArrayList<PendingIntent> listOfIntents = null;
				//String SENT = "SMS_SENT";
				for (int i = 0; i < messages.size(); i++)
					for (int j = 0; j < receivers.size(); j++) {
						Log.v("Thread service",
								"Sending sms to " + receivers.get(j));
						String message = messages.get(i);

						/*boolean bBigSMS = message.length() > 160;

						if (bBigSMS) {
							msgTexts = smsManager.divideMessage(message);

							listOfIntents = new ArrayList<PendingIntent>();
							for (int k = 0; k < msgTexts.size(); k++) {

								Intent sentIntent = new Intent(SENT);

								PendingIntent pi = PendingIntent.getBroadcast(
										null, 0, sentIntent, 0);
								listOfIntents.add(pi);
							}
						}*/
						String phone = receivers.get(j);
						//smsManager.sendMultipartTextMessage(phone, null,
						//		msgTexts, listOfIntents, null);
						 smsManager.sendTextMessage(phone,
						 null,message
						 , null, null);
					}
			}
			messages.clear();
			// handler.sendEmptyMessage(0);
			// }

		}

		boolean ReadMessages() {

			messages.clear();
			String path = Environment.getExternalStorageDirectory()
					+ "/SmsInformer/messages.txt";
			BufferedReader rd;
			// final StringBuffer storedString = new StringBuffer();

			try {
				rd = new BufferedReader(new FileReader(path));
				String line = null;
				// read all the lines till the end
				while ((line = rd.readLine()) != null) {
					messages.add(line);
				}
				rd.close(); // close reader

				rd.close(); // close reader

				File file = new File(path);
				file.delete();
				return true;
			} catch (Exception e) {
				Log.e("Service run", e.getMessage());
				messages.clear();
				return false;
			}
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v("Thread service", "handling intent");
	}

	/*
	 * private Handler handler=new Handler(){
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * super.handleMessage(msg); //showAppNotification(); } };
	 */
}