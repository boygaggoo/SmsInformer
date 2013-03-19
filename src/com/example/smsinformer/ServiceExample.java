package com.example.smsinformer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class ServiceExample extends IntentService {

	private static final String PREFS_NAME = "SmsInformer";
	private static final boolean REMOUNT = true;

	public ServiceExample(String name) {
		super(name);
		Log.v("Service create", "creating service");
	}

	public ServiceExample() {
		super("com.example.smsinformer.ServiceExample");
		Log.v("Service create", "creating service");
	}

	CentreonMessages messages = new CentreonMessages();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
		/*
		 * Log.e("Service run", "Stopping service (harakiri)"); AlarmManager
		 * alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		 * Intent intent = new Intent(this, ServiceExample.class); PendingIntent
		 * pintent = PendingIntent .getService(this, 0, intent, 0);
		 * alarm.cancel(pintent); }
		 */
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
			Log.v("Thread service", "Running service thread");
			boolean s = ReadMessages();
			int time = GetLastTime();
			Log.v("Thread service", "Searching for messages modified after "
					+ time);
			CentreonMessage m;
			if (!s)
				Log.v("Thread service", "ReadMessages call failed, aborting");
			else
				while ((m = messages.getNew(time)) != null) {
					SmsManager smsManager = SmsManager.getDefault();
					Log.v("Thread service", "Sending sms to " + m.phone);
					String message = m.getSmsText();
					Log.v("Thread service", "SMS text:  " + message);

					/*
					 * boolean bBigSMS = message.length() > 160;
					 * 
					 * if (bBigSMS) { msgTexts =
					 * smsManager.divideMessage(message);
					 * 
					 * listOfIntents = new ArrayList<PendingIntent>(); for (int
					 * k = 0; k < msgTexts.size(); k++) {
					 * 
					 * Intent sentIntent = new Intent(SENT);
					 * 
					 * PendingIntent pi = PendingIntent.getBroadcast( null, 0,
					 * sentIntent, 0); listOfIntents.add(pi); } }
					 */
					String phone = m.phone;
					// smsManager.sendMultipartTextMessage(phone, null,
					// msgTexts, listOfIntents, null);
					smsManager
							.sendTextMessage(phone, null, message, null, null);
					SetLastTime(m.time);
					time = m.time;
					Log.v("Thread service",
							"Setting last modification time to " + m.time);
				}
		}

	}

	boolean ReadMessages() {

		final Runtime runtime = Runtime.getRuntime();
		if (REMOUNT) {
			Log.v("ReadMessages", "Remounting SD card to update contents");

			String[] str = { "su", "-c", "umount /mnt/sdcard" };
			String[] str2 = { "su", "-c",
					"mount -r -t vfat /dev/block/vold/179:1 /mnt/sdcard" };
			try {
				runtime.exec(str);
				runtime.exec(str2);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		Log.v("ReadMessages", "Copying messages log to local app folder");
		String from = Environment.getExternalStorageDirectory()
				+ "/SmsInformer/messages.txt";
		PackageManager m = getPackageManager();
		String s = getPackageName();
		PackageInfo p = null;
		try {
			p = m.getPackageInfo(s, 0);
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		s = p.applicationInfo.dataDir;

		String to = s + "/messages.txt";
		String command = "cp " + from + " " + to;
		Log.v("ReadMessages", command);

		String[] str3 = { "su", "-c", command };
		try {
			runtime.exec(str3);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Log.v("ReadMessages", "Reading messages file");
		BufferedReader rd;
		StringBuilder sb = new StringBuilder();
		try {
			rd = new BufferedReader(new FileReader(to));
			String line = null;
			// read all the lines till the end
			while ((line = rd.readLine()) != null) {
				sb.append(line + "\n");
			}
			rd.close(); // close reader
			if (sb.length() > 5) {
				messages.set(sb.toString());
				return true;
			} else {
				Log.v("ReadMessages",
						"Too little messages length, won't parse.");
				return false;
			}
		} catch (Exception e) {
			// Log.e("Service run", e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	int GetLastTime() {
		SharedPreferences settings = getApplicationContext()
				.getSharedPreferences(PREFS_NAME, 0);
		return settings.getInt("LastTime", 0);
	}

	void SetLastTime(int d) {
		SharedPreferences settings = getApplicationContext()
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("LastTime", d);
		editor.commit();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v("Thread service", "handling intent");
	}

}