package com.necatitufan.okulzili;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Necati TUFAN on 21.02.2018.
 */

public class Yardimci
{
    public void alarmServiceBaslat(Context context)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int gun = c.get(Calendar.DAY_OF_WEEK);
        int haftaninGunu = -1;
        switch (gun)
        {
            case Calendar.MONDAY:
                haftaninGunu = 1;
                break;
            case Calendar.TUESDAY:
                haftaninGunu = 2;
                break;
            case Calendar.WEDNESDAY:
                haftaninGunu = 3;
                break;
            case Calendar.THURSDAY:
                haftaninGunu = 4;
                break;
            case Calendar.FRIDAY:
                haftaninGunu = 5;
                break;
            case Calendar.SATURDAY:
                haftaninGunu = 6;
                break;
            case Calendar.SUNDAY:
                haftaninGunu = 7;
                break;
        }

        String sql = "SELECT * FROM " + VTYardimcisi.TBL_HAFTALIK
                + " WHERE " + VTYardimcisi.COL_HAFTALIK_GUN + "={0}"
                + " ORDER BY " + VTYardimcisi.COL_HAFTALIK_ZAMAN + " ASC;";
        VTYardimcisi vtYardimcisi = new VTYardimcisi(context);
        SQLiteDatabase vt = vtYardimcisi.getWritableDatabase();

        Cursor sonuc = null;
        boolean alarmKuruldu = false;
        int i;

        for (i = haftaninGunu; i <= haftaninGunu + 7; i++)
        {
            sonuc = vt.rawQuery(sql.replace("{0}", String.valueOf((i % 7) == 0 ? 7 : (i % 7))), null);

            for (int j = 0; j < sonuc.getCount(); j++)
            {
                sonuc.moveToPosition(j);
                String zaman = sonuc.getString(VTYardimcisi.COL_ID_HAFTALIK_ZAMAN);
                String zilTur = sonuc.getString(VTYardimcisi.COL_ID_HAFTALIK_SES_TUR);
                Log.d("_zil tür", zilTur);

                Calendar zilZaman = Calendar.getInstance();
                zilZaman.setTimeInMillis(System.currentTimeMillis());
                zilZaman.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + (i - haftaninGunu));
                zilZaman.set(Calendar.HOUR_OF_DAY, Integer.valueOf(zaman.split(":")[0]));
                zilZaman.set(Calendar.MINUTE, Integer.valueOf(zaman.split(":")[1]));
                zilZaman.set(Calendar.SECOND, 0);
                zilZaman.set(Calendar.MILLISECOND, 0);

                Calendar suan = Calendar.getInstance();
                suan.setTimeInMillis(System.currentTimeMillis());
                suan.set(Calendar.SECOND, 0);
                suan.set(Calendar.MILLISECOND, 0);

                if (zilZaman.getTimeInMillis() - suan.getTimeInMillis() > 0L)
                {
                    Log.d("_zil_alarm_zamani", zilZaman.getTime().toString());
                    ZilAlarmReceiver zilAlarmReceiver = new ZilAlarmReceiver();
                    zilAlarmReceiver.cancelAlarm(context);
                    zilAlarmReceiver.alarmiKur(context, zilZaman, zilTur);
                    alarmKuruldu = true;
                    break;
                }
            }

            if (alarmKuruldu)
                break;
        }

        vt.close();
        vtYardimcisi.close();
    }

    public Calendar alarmZamaniGetir(Context context)
    {
        Calendar zilCalmaZamani = null;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int gun = c.get(Calendar.DAY_OF_WEEK);
        int haftaninGunu = -1;
        switch (gun)
        {
            case Calendar.MONDAY:
                haftaninGunu = 1;
                break;
            case Calendar.TUESDAY:
                haftaninGunu = 2;
                break;
            case Calendar.WEDNESDAY:
                haftaninGunu = 3;
                break;
            case Calendar.THURSDAY:
                haftaninGunu = 4;
                break;
            case Calendar.FRIDAY:
                haftaninGunu = 5;
                break;
            case Calendar.SATURDAY:
                haftaninGunu = 6;
                break;
            case Calendar.SUNDAY:
                haftaninGunu = 7;
                break;
        }

        String sql = "SELECT * FROM " + VTYardimcisi.TBL_HAFTALIK
                + " WHERE " + VTYardimcisi.COL_HAFTALIK_GUN + "={0}"
                + " ORDER BY " + VTYardimcisi.COL_HAFTALIK_ZAMAN + " ASC;";
        VTYardimcisi vtYardimcisi = new VTYardimcisi(context);
        SQLiteDatabase vt = vtYardimcisi.getWritableDatabase();

        Cursor sonuc = null;
        boolean alarmKuruldu = false;
        int i;

        for (i = haftaninGunu; i <= haftaninGunu + 7; i++)
        {
            sonuc = vt.rawQuery(sql.replace("{0}", String.valueOf((i % 7) == 0 ? 7 : (i % 7))), null);

            for (int j = 0; j < sonuc.getCount(); j++)
            {
                sonuc.moveToPosition(j);
                String zaman = sonuc.getString(VTYardimcisi.COL_ID_HAFTALIK_ZAMAN);
                String zilTur = sonuc.getString(VTYardimcisi.COL_ID_HAFTALIK_SES_TUR);
                Log.d("_zil_tür", zilTur);

                Calendar zilZaman = Calendar.getInstance();
                zilZaman.setTimeInMillis(System.currentTimeMillis());
                zilZaman.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + (i - haftaninGunu));
                zilZaman.set(Calendar.HOUR_OF_DAY, Integer.valueOf(zaman.split(":")[0]));
                zilZaman.set(Calendar.MINUTE, Integer.valueOf(zaman.split(":")[1]));
                zilZaman.set(Calendar.SECOND, 0);
                zilZaman.set(Calendar.MILLISECOND, 0);

                Calendar suan = Calendar.getInstance();
                suan.setTimeInMillis(System.currentTimeMillis());
                //suan.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + (i - haftaninGunu));
                suan.set(Calendar.SECOND, 0);
                suan.set(Calendar.MILLISECOND, 0);

                if (zilZaman.getTimeInMillis() - suan.getTimeInMillis() > 0L)
                {
                    Log.d("_zil_alarm_zamani", zilZaman.getTime().toString());
                    ZilAlarmReceiver zilAlarmReceiver = new ZilAlarmReceiver();
                    zilAlarmReceiver.cancelAlarm(context);
                    zilAlarmReceiver.alarmiKur(context, zilZaman, zilTur);
                    alarmKuruldu = true;

                    zilCalmaZamani = zilZaman;
                    break;
                }
            }

            if (alarmKuruldu)
                break;
        }

        vt.close();
        vtYardimcisi.close();

        return zilCalmaZamani;
    }
}
