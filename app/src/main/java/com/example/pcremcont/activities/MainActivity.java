package com.example.pcremcont.activities;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.pcremcont.R;
import com.example.pcremcont.adapters.ViewPagerAdapter;
import com.example.pcremcont.database.Database;
import com.example.pcremcont.fragments.FirstTabFragment;
import com.example.pcremcont.fragments.SecondTabFragment;
import com.example.pcremcont.fragments.ThirdTabFragment;
import com.example.pcremcont.portCommunicator.GetServersVerification;
import com.example.pcremcont.portCommunicator.SendToServer;
import com.example.pcremcont.checkPermissions.Permission;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    Database database;


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


        Permission permission = new Permission();
        permission.forRecordAudio(this);



        if(!database.portExist() || Objects.equals(database.getPort(), ""))
        {
            database.setPort("7800");
        }


        if (!database.ipExist() || Objects.equals(database.getIP(), ""))
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
        database = new Database(getApplicationContext());

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        viewPager = (ViewPager) findViewById(R.id.viewPagerId);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FirstTabFragment(),"sound");
        adapter.AddFragment(new SecondTabFragment(),"set_on/off");
        adapter.AddFragment(new ThirdTabFragment(),"Sen Voice");

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
        else if (id == R.id.connectionTest)
        {
            GetServersVerification getServersVer = new GetServersVerification();
            Toast toast ;
            String toastMsg;
            String verMsg;

            try {

                verMsg = getServersVer.execute("19",database.getPort(),database.getIP()).get();

                if (verMsg == null)
                    toastMsg = "Connection failed";
                else if (verMsg.equals("697"))
                    toastMsg = "You are connected!";
                else
                    toastMsg ="Something went wrong";

            } catch (ExecutionException | InterruptedException e)
            {
                toastMsg = "Something went wrong";
            }

            toast = Toast.makeText(this,toastMsg,Toast.LENGTH_LONG);
            toast.show();

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
        messageSender.execute(msg,database.getPort(),database.getIP());
    }

}