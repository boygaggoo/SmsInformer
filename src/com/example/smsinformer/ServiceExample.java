package com.example.smsinformer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServiceExample extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Service Created",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service Destroy",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(this,"Service LowMemory",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this,"Service start",Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v("Service","Starting");
        Toast.makeText(this,"task perform in service",Toast.LENGTH_LONG).show();
        ThreadDemo td=new ThreadDemo();
        td.start();
        return super.onStartCommand(intent, flags, startId);
    }
    
    private class ThreadDemo extends Thread{
        @Override
        public void run() {
            super.run();
            try{
            	//while(true)
            	//{
		            Log.v("Thread service","all ok");
		            //handler.sendEmptyMessage(0);
            	//}
            }catch(Exception e){
                e.getMessage();
            }
        }
    }
    /*
   private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        //showAppNotification();
    }
   };*/
}