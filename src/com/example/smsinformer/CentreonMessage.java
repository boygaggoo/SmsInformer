package com.example.smsinformer;
public class CentreonMessage {

	String type,service_description,host,host_alias,address,state,phone;
	int time;
	String getSmsText()
	{
		return type+": "+service_description+" on "+host_alias+"("+address+") is "+state;
	}
}
