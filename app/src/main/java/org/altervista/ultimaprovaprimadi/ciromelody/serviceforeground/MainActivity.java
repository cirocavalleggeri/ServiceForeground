package org.altervista.ultimaprovaprimadi.ciromelody.serviceforeground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.function.ToDoubleBiFunction;

public class MainActivity extends AppCompatActivity {
Button bn_start_service,bn_sgancia_service,bn_send_service,bn_ferma_service,bn_aggancia_service;
TextView tx_status;

    @Override
    public Intent getIntent() {
        return intent;
    }

    @Override
    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    Intent intent;
    public MyService istanzamyService;

    //servira' per agganciare il servizio
    public MyService getIstanzamyService() {
        return istanzamyService;
    }
    boolean isServizioAgganciato=false;
    final String TAG="MYS";

    @Override
    protected void onResume() {
        super.onResume();
        controlla_bottoni();
        if(istanzamyService==null){
           //aggancia_il_servizio();
            Log.d(TAG,"onREsume instanzaService null");


        }
        //aggancia_il_servizio();
    }
   @Override
  protected void  onStop() {
      sgancia_il_servizio();
       super.onStop();
   }
    private void controlla_bottoni() {

       if(getIntent()!=null){
            bn_start_service.setEnabled(false);
            bn_ferma_service.setEnabled(true);
            if(isServizioAgganciato){
                bn_send_service.setEnabled(true);
                bn_sgancia_service.setEnabled(false);
                bn_aggancia_service.setEnabled(false);
            }else {

                bn_send_service.setEnabled(false);
                bn_sgancia_service.setEnabled(false);
                bn_aggancia_service.setEnabled(true);
            }
        }else {
            bn_start_service.setEnabled(true);
            bn_ferma_service.setEnabled(false);
            bn_send_service.setEnabled(false);
           bn_sgancia_service.setEnabled(false);
           bn_aggancia_service.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tx_status=findViewById(R.id.textView);
        bn_start_service=findViewById(R.id.id_start_service);
        bn_sgancia_service=findViewById(R.id.id_stop_service);
        bn_send_service=findViewById(R.id.id_send_service);
        bn_ferma_service=findViewById(R.id.id_ferma_service);
        bn_aggancia_service=findViewById(R.id.id_aggancia_service);
        if(istanzamyService==null){
            //aggancia_il_servizio();
            Log.d(TAG,"onCreate instanzaService null");

        }

        controlla_bottoni();
        bn_send_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(istanzamyService!=null){
                    istanzamyService.startMyService("un saluto da MainActivity");

                }
                controlla_bottoni();
            }
        });
        bn_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(istanzamyService==null){
                   faiPartireIlServizio();
                   //aggancia_il_servizio();
                }
                controlla_bottoni();

            }

        });
        bn_aggancia_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggancia_il_servizio();
                controlla_bottoni();
            }
        });
        bn_sgancia_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //sgancia il servizio
                //TODO;
                //sgancia_il_servizio();
                controlla_bottoni();
            }
        });
      bn_ferma_service.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             // sgancia_il_servizio();
              fermaIlServizio();
              //istanzamyService.stopMyService();
              controlla_bottoni();

          }
      });
    }
    private void aggancia_il_servizio() {
        if(getIntent()!=null){
            isServizioAgganciato =this.getApplication().bindService(getIntent(), serviceConnection, Context.BIND_AUTO_CREATE);
                }
        if(isServizioAgganciato){  tx_status.setText("myService agganciato");}
    }

    private void sgancia_il_servizio() {

        if (isServizioAgganciato) {getApplicationContext().unbindService(serviceConnection);

        }
        if(istanzamyService!=null){
            //istanzamyService.stopMyService();
            istanzamyService = null;


        }

    }

    private void fermaIlServizio() {
        Intent stopIntent = new Intent(MainActivity.this, MyService.class);
        stopIntent.setAction("ACTION.STOPFOREGROUND_ACTION");
        startService(stopIntent);
        setIntent(null);

        //tx_status.setText("myServizio fermato!");
    }

    private void  faiPartireIlServizio() {
        //start

        final Intent intent = new Intent(this.getApplication(), MyService.class);
        intent.setAction("ACTION.STARTFOREGROUND_ACTION");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.getApplication().startForegroundService(intent);

        }else {this.getApplication().startService(intent);
        }
       if(intent!=null){setIntent(intent);}
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            String name = className.getClassName();
            if (name.endsWith("MyService")) {
                istanzamyService = ((MyService.PrivateClassBinder) service).getService();
                controlla_bottoni();


            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG,"disconnesso"+className.getClassName());
            if (className.getClassName().endsWith("MyService")) {
                istanzamyService = null;
                tx_status.setText("myServizio sganciato");
                controlla_bottoni();


            }
        }
    };

}