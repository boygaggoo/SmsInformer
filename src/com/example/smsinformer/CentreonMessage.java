package com.example.smsinformer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CentreonMessage {

	String type, service_description, host, host_alias, address, state, phone;
	int time;

	String getSmsText() {
		String format = "dd/MM HH:mm";
		Date dt = new java.util.Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
		String dt2 = sdf.format(dt);
		String message = type + ": " + service_description + " on "
				+ host_alias + "(" + address + ") is " + state + " on " + dt2;
		if (message.length() > 160)
			message = message.substring(0, 157) + "...";// cut message
		return message;
	}
}
