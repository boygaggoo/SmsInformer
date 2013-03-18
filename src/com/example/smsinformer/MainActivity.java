package com.example.smsinformer;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void SendSMS(View view) {
		// test sending SMS

		String phone=((EditText)findViewById(R.id.phone)).getText().toString();
		String msg=((EditText)findViewById(R.id.msg)).getText().toString();
		SmsManager smsManager = SmsManager.getDefault();
		//phone = "+79161234567";
		//msg="непереводимая игра слов";
		
		smsManager.sendTextMessage(phone, null, msg,
				null, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		// Add the buttons
		builder.setMessage("Сообщение отослано").setTitle("Ок");
		builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				// MainActivity.this.finish();
				// User clicked OK button
			}
		});
		builder.show();
	}

	public void ToggleService(View view) {

		boolean on = ((ToggleButton) view).isChecked();
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, ServiceExample.class);
	   // Intent intent=new Intent("com.example.smsinformer.ServiceExample");
		PendingIntent pintent = PendingIntent.getService(this, 0,
				intent, 0);

		if (on) {
			// Enable vibrate
			//startService(new Intent(this, ServiceExample.class));
		    //this.startService(intent);
			// inetnt=new Intent(this,ServiceExample.class);
			Log.v("SmsInformer", "Starting service");
			// startService(inetnt);
			Calendar cal = Calendar.getInstance();
			// Start every 30 seconds
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					40 * 1000, pintent);

		} else {
			// Disable vibrate
			// inetnt=new Intent(this,ServiceExample.class);
			Log.v("SmsInformer", "Stopping service");
			// stopService(inetnt);
			alarm.cancel(pintent);
		}

	}
}
