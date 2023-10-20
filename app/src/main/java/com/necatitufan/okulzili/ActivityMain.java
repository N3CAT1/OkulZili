package com.necatitufan.okulzili;

import static android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class ActivityMain extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, AppBarLayout.OnOffsetChangedListener
{
    VTYardimcisi vtYard;
    ViewPager vpGunler;
    TabLayout tlGunler;
    AppBarLayout barLayout;
    TextView tvKalanSure;
    Handler handler = new Handler();
    Calendar zilCalmaZamani;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vtYard = new VTYardimcisi(this);
        yetkiAl(Manifest.permission.READ_EXTERNAL_STORAGE, 0);
        yetkiAl(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        yetkiAl(Manifest.permission.RECEIVE_BOOT_COMPLETED, 2);
        yetkiAl(Manifest.permission.WAKE_LOCK, 3);
        yetkiAl(Manifest.permission.USE_EXACT_ALARM, 4);
        yetkiAl(Manifest.permission.SCHEDULE_EXACT_ALARM, 5);
        alarmYetkisiAl();

        vpGunler = findViewById(R.id.vpGunler);
        tlGunler = findViewById(R.id.tlGunler);
        barLayout = findViewById(R.id.main_appbar);
        tvKalanSure = findViewById(R.id.tvKalanSure);

        barLayout.addOnOffsetChangedListener(this);

        viewpagerAyarla();

        tlGunler.addOnTabSelectedListener(this);
        tlGunler.setupWithViewPager(vpGunler);

        FloatingActionButton btn = findViewById(R.id.btnEkle);
        btn.setOnClickListener(this);

        FloatingActionButton btnZil = findViewById(R.id.btnZil);
        btnZil.setOnClickListener(this);

        Yardimci y = new Yardimci();
        zilCalmaZamani = y.alarmZamaniGetir(this);

        zileKalanSure();
    }

    private void yetkiAl(String izinAdi, int istekKodu)
    {
        int izinTamam = PackageManager.PERMISSION_GRANTED;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, izinAdi) != izinTamam)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, izinAdi))
            {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else
            {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{izinAdi}, istekKodu);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void alarmYetkisiAl()
    {
        AlarmManager almMngr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            if(!almMngr.canScheduleExactAlarms())
                startActivity(new Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM));
        }
    }

    private void viewpagerAyarla()
    {
        ViewPagerAdapter adp = new ViewPagerAdapter(getSupportFragmentManager());

        for (int i = 1; i <= 7; i++)
        {
            FragmentGun fg = new FragmentGun();
            Bundle b = new Bundle();
            b.putInt("GUN", i);
            b.putString("GUNADI", getResources().getStringArray(R.array.gunler)[i - 1]);
            fg.setArguments(b);
            adp.addFragment(fg);
        }
        vpGunler.setAdapter(adp);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
        vpGunler.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab)
    {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab)
    {

    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnEkle)
        {
            FragmentManager fm = getSupportFragmentManager();
            DialogFragmentZaman d = new DialogFragmentZaman();
            d.show(fm, "ZamanEkle");
        }
        else if (view.getId() == R.id.btnZil)
        {
            Intent i = new Intent(this, PreferenceActivityZil.class);
            startActivity(i);
        }
    }

    public void fragmentGuncelle()
    {
        ViewPagerAdapter adp = (ViewPagerAdapter) vpGunler.getAdapter();

        for (int i = 0; i < adp.getCount(); i++)
        {
            if (adp.getItem(i).isAdded())
                ((FragmentGun) adp.getItem(i)).gunZamanlariniGetir();
        }

        Log.d("_zil adp.count", adp.getCount() + "");

        if (adp.getCount() != 0)
        {
            Yardimci y = new Yardimci();
            y.alarmServiceBaslat(this);
            zilCalmaZamani = y.alarmZamaniGetir(this);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
    {
        int toplamH = this.getWindow().getDecorView().getHeight();
        int tabLayoutH = tlGunler.getMeasuredHeight();

        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarH = rectangle.top;
        int yeniH = toplamH - statusBarH - (appBarLayout.getMeasuredHeight() - Math.abs(verticalOffset)) - tabLayoutH;

        ViewGroup.LayoutParams params = vpGunler.getLayoutParams();
        params.height = yeniH;

        vpGunler.setLayoutParams(params);
    }

    private void zileKalanSure()
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                if (zilCalmaZamani != null)
                {
                    Calendar suan = Calendar.getInstance();
                    suan.setTimeInMillis(System.currentTimeMillis());

                    long kalanSure = zilCalmaZamani.getTimeInMillis() - suan.getTimeInMillis();

                    if (kalanSure >= 0L)
                    {
                        int toplamSaniye = (int) kalanSure / 1000;
                        int saat = toplamSaniye / (60 * 60);
                        int dakika = (toplamSaniye % 3600) / 60;
                        int saniye = (toplamSaniye - (saat * 3600) - (dakika * 60));

                        String strKalanSure = formatla(saat) + ":" + formatla(dakika) + ":" + formatla(saniye);
                        tvKalanSure.setText(strKalanSure);
                    }
                    else
                    {
                        Yardimci y = new Yardimci();
                        zilCalmaZamani = y.alarmZamaniGetir(getApplicationContext());
                    }
                }
                else
                {
                    tvKalanSure.setText("00:00:00");
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    private String formatla(int c)
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
