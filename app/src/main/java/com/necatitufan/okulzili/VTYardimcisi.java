package com.necatitufan.okulzili;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VTYardimcisi extends SQLiteOpenHelper
{
    private static final String VT_ADI = "okulzili.db";
    private static final int VT_SURUM = 1;

    public static final String TBL_HAFTALIK = "HaftalikProgram";

    public static final String COL_HAFTALIK_ID = "_id";
    public static final String COL_HAFTALIK_GUN = "Gun";
    public static final String COL_HAFTALIK_ZAMAN = "Zaman";
    public static final String COL_HAFTALIK_SES_TUR = "SesTur";

    public static final int COL_ID_HAFTALIK_ID = 0;
    public static final int COL_ID_HAFTALIK_GUN = 1;
    public static final int COL_ID_HAFTALIK_ZAMAN = 2;
    public static final int COL_ID_HAFTALIK_SES_TUR = 3;

    public static final String[] COLUMNS_HAFTALIK = new String[]
            {
                    COL_HAFTALIK_ID,
                    COL_HAFTALIK_GUN,
                    COL_HAFTALIK_ZAMAN,
                    COL_HAFTALIK_SES_TUR
            };

    private static final String TABLE_CREATE_HAFTALIK = "Create Table " + TBL_HAFTALIK + " ("
            + COL_HAFTALIK_ID + " integer primary key autoincrement, "
            + COL_HAFTALIK_GUN + " integer not null, "
            + COL_HAFTALIK_ZAMAN + " text not null, "
            + COL_HAFTALIK_SES_TUR + " text not null);";

    public VTYardimcisi(Context context)
    {
        super(context, VT_ADI, null, VT_SURUM);
    }

    @Override
    public void onCreate(SQLiteDatabase veritabani)
    {
        veritabani.execSQL(TABLE_CREATE_HAFTALIK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        if (newVersion == 2 && oldVersion == 1)
            yeniVersiyonaGecir(database);
    }

    private void yeniVersiyonaGecir(SQLiteDatabase vt)
    {
//        String sql = "ALTER TABLE " + TBL_SES + " ADD COLUMN " + COL_SES_URL + " text;";
//        database.execSQL(sql);
//
//        sql = "ALTER TABLE " + TBL_SES + " ADD COLUMN " + COL_SES_THUMBNAIL + " text;";
//        database.execSQL(sql);
//
//        sql = "ALTER TABLE " + TBL_SES + " ADD COLUMN " + COL_SES_ITEMCOUNT + " integer default 0;";
//        database.execSQL(sql);
//
//        sql = "ALTER TABLE " + TBL_SES + " ADD COLUMN " + COL_SES_UPDATETIME + " text;";
//        database.execSQL(sql);
    }
}
