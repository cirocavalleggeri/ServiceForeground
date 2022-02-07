package org.altervista.ultimaprovaprimadi.ciromelody.serviceforeground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
   public int numerofoto;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("sendbroadcast.REFRESH_DATA_INTENT")) {
            numerofoto=intent.getIntExtra("numerofoto",0);
            Toast.makeText(context,"numero foto:"+numerofoto,Toast.LENGTH_SHORT).show();

        }
    }
}
