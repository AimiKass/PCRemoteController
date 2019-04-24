package com.example.pcremcont.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pcremcont.R;
import com.example.pcremcont.adapters.ViewPagerAdapter;
import com.example.pcremcont.fragments.FirstTabFragment;
import com.example.pcremcont.fragments.SecondTabFragment;
import com.example.pcremcont.fragments.ThirdTabFragment;
import com.example.pcremcont.portCommunicator.SendToServer;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    private static final String KEY_FOR_PORT_NUMBER = "PORT_NUMBER";
    private static final String KEY_FOR_IP_ADDRESS = "IP_ADDRESS";
    private static final String KEY_FOR_SHARED_PREFERENCE = "MyPrefs";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        init();

    }

    @Override
    protected void onStart()
    {
        super.onStart();


        if(!pref.contains(KEY_FOR_PORT_NUMBER) || Objects.equals(pref.getString(KEY_FOR_PORT_NUMBER, ""), ""))
        {
            edit.putString(KEY_FOR_PORT_NUMBER, "7800");
            edit.apply();
        }

        // TODO: 4/24/2019 create new class for that
        if (!pref.contains(KEY_FOR_IP_ADDRESS) || Objects.equals(pref.getString(KEY_FOR_IP_ADDRESS, ""), ""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("You Need To Configure some Settings")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton("Go to settings", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            goToSettingsActivity();
                        }
                    });
            builder.show();
        }

    }

    private void init()
    {
        pref = getSharedPreferences(KEY_FOR_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        edit = pref.edit();


        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        viewPager = (ViewPager) findViewById(R.id.viewPagerId);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FirstTabFragment(),"sound");
        adapter.AddFragment(new SecondTabFragment(),"set_on/off");
        adapter.AddFragment(new ThirdTabFragment(),"(Under_const)");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            goToSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToSettingsActivity()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.joystick)
        {
            Intent intent = new Intent(this, JoystickActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_gallery)
        {

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg);
    }


}