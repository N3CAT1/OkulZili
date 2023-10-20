package com.necatitufan.okulzili;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;

import java.util.Calendar;

/**
 * Created by Necati TUFAN on 21.02.2018.
 */

public class ZilAlarmReceiver extends WakefulBroadcastReceiver
{
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmPendingIntent;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent intentService = new Intent(context, ZilIntentService.class);
        intentService.setAction(ZilIntentService.ACTION_ZIL_CAL);
        String zilTuru = intent.getExtras().getString("zil_turu");
        Bundle b = new Bundle();
        b.putString(ZilIntentService.EXTRA_PARAM_ZIL_TUR, zilTuru);
        intentService.putExtras(b);
        startWakefulService(context, intentService);
    }

    public void alarmiKur(Context context, Calendar calendar, String zilTur)
    {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ZilAlarmReceiver.class);
        intent.putExtra("zil_turu", zilTur);
        alarmPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);
        }
        else
        {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);
        }

        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, ZilBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Log.d("_zil", "alarm kuruldu");
        Log.d("_zil_zamani", calendar.getTime().toString());
    }

    public void cancelAlarm(Context context)
    {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentGiris = new Intent(context, ZilAlarmReceiver.class);
        intentGiris.putExtra("zil_turu", "G");

        Intent intentCikis = new Intent(context, ZilAlarmReceiver.class);
        intentCikis.putExtra("zil_turu", "C");

        PendingIntent piGiris = PendingIntent.getBroadcast(context, 0, intentGiris, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        PendingIntent piCikis = PendingIntent.getBroadcast(context, 0, intentCikis, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        am.cancel(piGiris);
        am.cancel(piCikis);

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, ZilBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
