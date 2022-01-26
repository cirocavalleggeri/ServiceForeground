package org.altervista.ultimaprovaprimadi.ciromelody.serviceforeground;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    Intent intentservizio;
    public boolean isServizioAgganciato;

    public MyService istanzamyService;
    //servira' per agganciare il servizio
    public MyService getIstanzamyService() {
        return istanzamyService;
    }
    public Intent getIntentservizio() {
        return intentservizio;
    }


    public void setIntentservizio(Intent intent) {
        this.intentservizio = intent;
    }
    ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            String name = className.getClassName();
            if (name.endsWith("MyService")) {
              istanzamyService = ((MyService.PrivateClassBinder) service).getService();



            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d("MYS","disconnesso"+className.getClassName());
            if (className.getClassName().endsWith("MyService")) {
               istanzamyService = null;



            }
        }
    };
}
