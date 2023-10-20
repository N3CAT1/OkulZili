package com.necatitufan.okulzili;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;


public class FragmentGun extends ListFragment
{
    public FragmentGun()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        gunZamanlariniGetir();

    }

    public void gunZamanlariniGetir()
    {
        int gunNo = getArguments().getInt("GUN", 1);

        VTYardimcisi vtYard = new VTYardimcisi(getActivity());
        SQLiteDatabase vt = vtYard.getWritableDatabase();

        String sql = "Select * From " + VTYardimcisi.TBL_HAFTALIK
                + " Where " + VTYardimcisi.COL_HAFTALIK_GUN + "=" + gunNo
                + " Order By " + VTYardimcisi.COL_HAFTALIK_ZAMAN + " ASC;";

        Cursor cursor = vt.rawQuery(sql, null);

        CursorAdapterGun adp = new CursorAdapterGun(getActivity(), cursor, false);
        setListAdapter(adp);
        //vt.close();
        //vtYard.close();
        //cursor.close();
    }
}
