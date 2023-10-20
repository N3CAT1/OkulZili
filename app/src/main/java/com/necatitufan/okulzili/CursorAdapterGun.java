package com.necatitufan.okulzili;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

/**
 * Created by Necati TUFAN on 18.02.2018.
 */

public class CursorAdapterGun extends CursorAdapter implements View.OnClickListener
{
    private ActivityMain activityMain;

    public CursorAdapterGun(Context context, Cursor c, boolean autoRequery)
    {
        super(context, c, autoRequery);
        activityMain = (ActivityMain) context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View satirView = inflater.inflate(R.layout.listview_gun, viewGroup, false);

        return satirView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView tvZaman = view.findViewById(R.id.tvZaman);
        ImageButton ibSil = view.findViewById(R.id.ibSil);
        ImageButton ibDuzenle = view.findViewById(R.id.ibDuzenle);
        TextView tvTip = view.findViewById(R.id.tvTip);

        tvZaman.setText(cursor.getString(VTYardimcisi.COL_ID_HAFTALIK_ZAMAN));

        String tur = cursor.getString(VTYardimcisi.COL_ID_HAFTALIK_SES_TUR);
        int colorSesTur;
        String sesTurAdi;

        if(tur.equals("G"))
        {
            colorSesTur = Color.rgb(102, 153, 0);
            sesTurAdi = "Giriş";
        }
        else if(tur.equals("C"))
        {
            colorSesTur = Color.rgb(204, 0, 0);
            sesTurAdi = "Çıkış";
        }
        else
        {
            colorSesTur = Color.rgb(255, 140, 0);
            sesTurAdi = "Öğretmen";
        }

        tvTip.setText(sesTurAdi);
        tvTip.setTextColor(colorSesTur);
        ibSil.setOnClickListener(this);
        ibDuzenle.setOnClickListener(this);

        ibSil.setTag(cursor.getInt(VTYardimcisi.COL_ID_HAFTALIK_ID));
        ibDuzenle.setTag(cursor.getInt(VTYardimcisi.COL_ID_HAFTALIK_ID));
    }

    @Override
    public void onClick(View view)
    {
        int id = (int) view.getTag();

        if (view.getId() == R.id.ibSil)
            zamanSil(id);
        else if (view.getId() == R.id.ibDuzenle)
            zamanDuzenle(id);
    }

    private void zamanSil(int id)
    {
        String sql = "DELETE FROM " + VTYardimcisi.TBL_HAFTALIK
                + " WHERE " + VTYardimcisi.COL_HAFTALIK_ID + "=" + String.valueOf(id) + ";";

        VTYardimcisi vtYardimcisi = new VTYardimcisi(activityMain);
        SQLiteDatabase vt = vtYardimcisi.getWritableDatabase();

        vt.execSQL(sql);
        vt.close();
        vtYardimcisi.close();
        activityMain.fragmentGuncelle();
    }

    private void zamanDuzenle(int id)
    {
        FragmentManager fm = activityMain.getSupportFragmentManager();
        DialogFragmentZamanGuncelle d = new DialogFragmentZamanGuncelle();
        Bundle b = new Bundle();
        b.putInt("ZAMAN_ID", id);
        d.setArguments(b);
        d.show(fm, "ZamanGuncelle");
    }
}
