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

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Created by Necati TUFAN on 19.02.2018.
 */

public class DialogFragmentZamanGuncelle extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener
{
    TimePicker timePicker;
    Button btnKaydet;
    Button btnIptal;
    RadioGroup rgSesTur;
    String sesTur = "B";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.dialogfragment_zaman_guncelle, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        timePicker = view.findViewById(R.id.timePicker);
        btnKaydet = view.findViewById(R.id.btnKaydet);
        btnIptal = view.findViewById(R.id.btnIptal);
        rgSesTur = view.findViewById(R.id.rgSesTur);

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
            zamanGuncelle();
    }

    private void zamanGuncelle()
    {
        int saat = timePicker.getCurrentHour();
        int dakika = timePicker.getCurrentMinute();
        String zaman = formatla(saat) + ":" + formatla(dakika);
        int id = getArguments().getInt("ZAMAN_ID", -1);

        String sql = "UPDATE " + VTYardimcisi.TBL_HAFTALIK + " SET "
                + VTYardimcisi.COL_HAFTALIK_ZAMAN + "='" + zaman + "', "
                + VTYardimcisi.COL_HAFTALIK_SES_TUR + "='" + sesTur + "' "
                + "WHERE " + VTYardimcisi.COL_HAFTALIK_ID + "=" + String.valueOf(id) + ";";

        VTYardimcisi vtYard = new VTYardimcisi(getActivity());
        SQLiteDatabase vt = vtYard.getWritableDatabase();

        vt.execSQL(sql);
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
