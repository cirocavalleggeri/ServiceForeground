package org.altervista.ultimaprovaprimadi.ciromelody.serviceforeground;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyService extends Service  {
    final String TAG="MYS";
    int canale=1;
    int numero_foto=0;

    public boolean isRunningService() {
        return isRunningService;
    }

    public void setRunningService(boolean runningService) {
        isRunningService = runningService;
    }

    boolean isRunningService;
    private NotificationManager mNotificationManager;
    NotificationChannel notificationChannel = null;

    PrivateClassBinder binder =new PrivateClassBinder();
    private ScheduledExecutorService sched;
    Long tempo_iniziale=0L;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public void onCreate()
    {
        Log.i(TAG, "onCreate");

        if(mNotificationManager==null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {


        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(55366678, getNotification());
        }
        canale++;
        sendNotification("MyService","servizio creato",canale);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand: "+startId);
        if (intent.getAction().equals("ACTION.STARTFOREGROUND_ACTION")) {
            Log.i(TAG, "Received Start Foreground Intent ");
            // your start service code
        }
        else if (intent.getAction().equals("ACTION.STOPFOREGROUND_ACTION")) {
            Log.i(TAG, "Received Stop Foreground Intent");
            //your end servce code
            stopForeground(true);
            stopSelfResult(startId);
            sendNotification("Servizio MyService","stopMyService..con Id:"+startId,canale);
            stopMyService();
        }
        canale++;
        sendNotification("MyService:", "onStartCommand di MyService partito con id:"+startId,canale);
        if(intent!=null){
          String azione_da_compiere=  intent.getAction();
          Log.d(TAG,"azione_da_compiere"+azione_da_compiere);
            sendNotification("MyService:", "onStartCommand di MyService con azione da compiere:"+azione_da_compiere,canale);
        }
        return START_NOT_STICKY;
    }
    public void startMyService(String messaggio) {
        canale++;
        sendNotification("MYService","startMyService.."+messaggio,canale);
        if (sched==null){
            //esegue ogni 15 secondi il metodo run
            sched= Executors.newSingleThreadScheduledExecutor();
            sched.scheduleAtFixedRate(beeper, 0, 15, TimeUnit.SECONDS);
            Log.d(TAG,"sched.scheduleAtFixedRate");
        }else{if(sched.isShutdown()||sched.isTerminated()) {
            Log.d(TAG,"sched.isShutdown() or sched.isTerminated()");


        }
        }

    }
    public void stopMyService() {
        canale=canale+1;
        sendNotification("Servizio carposition","stopMyService..",canale);
        this.onDestroy();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(sched!=null){
            sched.shutdownNow();
            Log.d(TAG,"scheduler shutdown");
            sched=null;
        }



        Log.i(TAG, "onDestroy(): ");
        stopForeground(true);
        stopSelf();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification() {

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("channel_02", "My Channel MyService", NotificationManager.IMPORTANCE_DEFAULT);
        }

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification.Builder builder = new Notification.Builder(getApplicationContext(), "channel_02").setAutoCancel(true);
        return builder.build();
    }
    public void sendNotification(String title, String message, int canale) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent);

        notificationManager.notify(canale, notification.build());
    }
    private void showNotification(String message){


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"default")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Notifica")
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);




        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("com.MyService");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "com.MyService",
                    "Esercizio myServiceForeground",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        notificationManager.notify( canale,builder.build());
    }
    private void muletto(){
        Log.d(TAG,"durata tempo:");
    }
    final Runnable beeper = new Runnable() {
        public void run() {
            numero_foto++;
            Log.d("TAG", "New Picture Taken");
           Long differenzaTempo=System.currentTimeMillis()-tempo_iniziale;
            tempo_iniziale=System.currentTimeMillis();
            Log.d(TAG,"durata tempo:"+differenzaTempo);
            showNotification(numero_foto+" New Picture Taken:"+differenzaTempo+" millisecondi");
            setRunningService(true);
            sendBroadcast(new Intent("sendbroadcast.REFRESH_DATA_INTENT").putExtra("numerofoto", numero_foto));

        }
    };


    public class PrivateClassBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
}
