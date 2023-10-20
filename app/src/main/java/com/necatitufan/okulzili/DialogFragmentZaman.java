package com.necatitufan.okulzili;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Necati TUFAN on 19.02.2018.
 */

public class DialogFragmentZaman extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener
{
    TimePicker timePicker;
    Button btnKaydet;
    Button btnIptal;
    RadioGroup rgSesTur;
    ToggleButton togglePt;
    ToggleButton toggleSl;
    ToggleButton toggleCr;
    ToggleButton togglePr;
    ToggleButton toggleCm;
    ToggleButton toggleCt;
    ToggleButton togglePz;

    String sesTur = "B"; // B = BOŞ

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.dialogfragment_zaman, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        timePicker = view.findViewById(R.id.timePicker);
        btnKaydet = view.findViewById(R.id.btnKaydet);
        btnIptal = view.findViewById(R.id.btnIptal);
        rgSesTur = view.findViewById(R.id.rgSesTur);
        togglePt = view.findViewById(R.id.togglePt);
        toggleSl = view.findViewById(R.id.toggleSl);
        toggleCr = view.findViewById(R.id.toggleCr);
        togglePr = view.findViewById(R.id.togglePr);
        toggleCm = view.findViewById(R.id.toggleCm);
        toggleCt = view.findViewById(R.id.toggleCt);
        togglePz = view.findViewById(R.id.togglePz);

        timePicker.setIs24HourView(true);
        btnKaydet.setOnClickListener(this);
        btnIptal.setOnClickListener(this);

        rgSesTur.setOnCheckedChangeListener(this);
        ((RadioButton) view.findViewById(R.id.radioGiris)).setChecked(true);
        sesTur = "G";
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == R.id.btnIptal)
            this.dismiss();
        else if (id == R.id.btnKaydet)
            zamanKaydet();
    }

    private void zamanKaydet()
    {
        List<Integer> gunler = new ArrayList<>();

        if (togglePt.isChecked()) gunler.add(1);
        if (toggleSl.isChecked()) gunler.add(2);
        if (toggleCr.isChecked()) gunler.add(3);
        if (togglePr.isChecked()) gunler.add(4);
        if (toggleCm.isChecked()) gunler.add(5);
        if (toggleCt.isChecked()) gunler.add(6);
        if (togglePz.isChecked()) gunler.add(7);

        if (gunler.size() == 0)
        {
            Toast.makeText(getActivity(), "Gün seçilmedi!", Toast.LENGTH_SHORT).show();
            return;
        }

        int saat = timePicker.getCurrentHour();
        int dakika = timePicker.getCurrentMinute();
        String zaman = formatla(saat) + ":" + formatla(dakika);

        String sql = "INSERT OR REPLACE INTO " + VTYardimcisi.TBL_HAFTALIK + "("
                + VTYardimcisi.COL_HAFTALIK_ID + ", "
                + VTYardimcisi.COL_HAFTALIK_GUN + ", "
                + VTYardimcisi.COL_HAFTALIK_ZAMAN + ", "
                + VTYardimcisi.COL_HAFTALIK_SES_TUR + ") VALUES ("
                + "(SELECT " + VTYardimcisi.COL_HAFTALIK_ID + " FROM "
                + VTYardimcisi.TBL_HAFTALIK + " WHERE " + VTYardimcisi.COL_HAFTALIK_GUN + "={0} AND "
                + VTYardimcisi.COL_HAFTALIK_ZAMAN + "='" + zaman + "'), "
                + "{1}, "
                + "'" + zaman + "', "
                + "'{2}');";

        VTYardimcisi vtYard = new VTYardimcisi(getActivity());
        SQLiteDatabase vt = vtYard.getWritableDatabase();
        String sqlQ;

        for (int i = 0; i < gunler.size(); i++)
        {
            sqlQ = sql.replace("{0}", gunler.get(i).toString());
            sqlQ = sqlQ.replace("{1}", gunler.get(i).toString());
            sqlQ = sqlQ.replace("{2}", sesTur);

            vt.execSQL(sqlQ);
        }

        vt.close();
        vtYard.close();

        ((ActivityMain) getActivity()).fragmentGuncelle();

        this.dismiss();
    }

    private String formatla(int c)
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedRadioId)
    {
        if (checkedRadioId == R.id.radioGiris)
            sesTur = "G";
        else if (checkedRadioId == R.id.radioCikis)
            sesTur = "C";
        else if (checkedRadioId == R.id.radioOgretmen)
            sesTur = "O";
    }
}
