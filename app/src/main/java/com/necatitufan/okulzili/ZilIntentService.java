package com.necatitufan.okulzili;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ZilIntentService extends IntentService
{
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_ZIL_CAL = "com.necatitufan.okulzili.action.ZIL_CAL";
    //
    // TODO: Rename parameters
    public static final String EXTRA_PARAM_ZIL_TUR = "com.necatitufan.okulzili.extra.ZIL_TUR";

    MediaPlayer mediaPlayer;
    Handler durdurHandler = new Handler();

    public ZilIntentService()
    {
        super("ZilIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d("_zil_intent", "intent service çalıştı: " + intent.getAction());

        if (intent != null)
        {
            final String action = intent.getAction();
            if (ACTION_ZIL_CAL.equals(action))
            {
                String zilTur = intent.getExtras().getString(EXTRA_PARAM_ZIL_TUR);
                zilCal(zilTur);
            }
        }
    }


    private void zilCal(String zilTur)
    {
        Log.d("_zil_cal", "zil çaldı. zil tür: " + zilTur);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String strRingtonePreference = "content://settings/system/ringtone";

        switch (zilTur)
        {
            case "G":
                strRingtonePreference = prefs.getString("zil_sesi_giris", "content://settings/system/ringtone");
                break;
            case "C":
                strRingtonePreference = prefs.getString("zil_sesi_cikis", "content://settings/system/ringtone");
                break;
            case "O":
                strRingtonePreference = prefs.getString("zil_sesi_ogretmen", "content://settings/system/ringtone");
                break;
        }

        long zilSure = Long.valueOf(prefs.getString("zil_suresi", "10"));
        zilSure = zilSure * 1000;
        Uri defaultSoundUri = Uri.parse(strRingtonePreference);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), defaultSoundUri);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

        durdurHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (mediaPlayer != null)
                {
                    Log.d("_zil_durdur", "ok");
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }
            }
        }, zilSure);

        Yardimci y = new Yardimci();
        y.alarmServiceBaslat(getApplicationContext());
    }
}
