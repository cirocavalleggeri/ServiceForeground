package org.altervista.ultimaprovaprimadi.ciromelody.serviceforeground;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.function.ToDoubleBiFunction;

public class MainActivity extends AppCompatActivity {
Button bn_start_service,bn_sgancia_service,bn_send_service,bn_ferma_service,bn_aggancia_service;
TextView tx_status;
MyReceiver myReceiver;





    MainViewModel viewModel;



    final String TAG="MYS";

    @Override
    protected void onResume() {
        super.onResume();
        controlla_bottoni();
        registra_receiver();
        if(viewModel.istanzamyService==null){
           //aggancia_il_servizio();
            Log.d(TAG,"onREsume instanzaService null");


        }

        //aggancia_il_servizio();
    }
    private void registra_receiver() {
        if (myReceiver== null) myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("sendbroadcast.REFRESH_DATA_INTENT");
        registerReceiver(myReceiver, intentFilter);
    }
   @Override
  protected void  onStop() {

       super.onStop();
       cancella_receiver();
   }
    private void cancella_receiver() {
        if(myReceiver!=null){unregisterReceiver(myReceiver);}
    }


    private void controlla_bottoni() {
        if(myReceiver!=null){tx_status.setText("numerofoto:"+myReceiver.numerofoto);}
       if((viewModel.getIntentservizio()!=null)){
            bn_start_service.setEnabled(false);
            bn_ferma_service.setEnabled(true);
            if(viewModel.isServizioAgganciato){
                bn_send_service.setEnabled(true);
                bn_sgancia_service.setEnabled(true);
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
        viewModel=new ViewModelProvider(this).get(MainViewModel.class);

        tx_status=findViewById(R.id.textView);
        bn_start_service=findViewById(R.id.id_start_service);
        bn_sgancia_service=findViewById(R.id.id_stop_service);
        bn_send_service=findViewById(R.id.id_send_service);
        bn_ferma_service=findViewById(R.id.id_ferma_service);
        bn_aggancia_service=findViewById(R.id.id_aggancia_service);
        if(viewModel.istanzamyService==null){
            //aggancia_il_servizio();
            Log.d(TAG,"onCreate instanzaService null");

        }

        controlla_bottoni();
        bn_send_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.istanzamyService!=null){
                    viewModel.istanzamyService.startMyService("un saluto da MainActivity");

                }
                controlla_bottoni();
            }
        });
        bn_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.istanzamyService==null){
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
                sgancia_il_servizio();

                controlla_bottoni();
            }
        });
      bn_ferma_service.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             // sgancia_il_servizio();
              fermaIlServizio();
              //istanzamyService.stopMyService();
              sgancia_il_servizio();

              controlla_bottoni();

          }
      });
    }
    private void aggancia_il_servizio() {
        if(viewModel.getIntentservizio()!=null){
            viewModel.isServizioAgganciato =this.getApplication().bindService(viewModel.getIntentservizio(), viewModel.serviceConnection, Context.BIND_AUTO_CREATE);
                }
        if(viewModel.isServizioAgganciato){  tx_status.setText("myService agganciato");}
    }

    private void sgancia_il_servizio() {

        if (viewModel.isServizioAgganciato&&(viewModel.serviceConnection!=null)) {getApplicationContext().unbindService(viewModel.serviceConnection);}


        if(viewModel.istanzamyService!=null){
            //istanzamyService.stopMyService();
            viewModel.istanzamyService = null;


        }
        viewModel.isServizioAgganciato=false;
    }

    private void fermaIlServizio() {
        sgancia_il_servizio();
        viewModel.getIntentservizio().setAction("ACTION.STOPFOREGROUND_ACTION");
        startService( viewModel.getIntentservizio());

        viewModel.setIntentservizio(null);


        //tx_status.setText("myServizio fermato!");
    }

    private void  faiPartireIlServizio() {
        //start

      viewModel.intentservizio = new Intent(this.getApplication(), MyService.class);
        viewModel.intentservizio.setAction("ACTION.STARTFOREGROUND_ACTION");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.getApplication().startForegroundService(viewModel.intentservizio);

        }else {this.getApplication().startService(viewModel.intentservizio);
        }
       if(viewModel.intentservizio!=null){viewModel.setIntentservizio(viewModel.intentservizio);}
    }

    public boolean checkServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }


}