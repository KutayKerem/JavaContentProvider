package com.kutaykerem.javacontentprovider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.kutaykerem.javacontentprovider.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ListView listView = findViewById(R.id.listView);



        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }


        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {



                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
                    ContentResolver contentResolver = getContentResolver();
                    String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
                    Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,projection,null,null,ContactsContract.Contacts.DISPLAY_NAME);


                    if(cursor!= null){

                        ArrayList<String> contractsList = new ArrayList<>();
                        String columnIX = ContactsContract.Contacts.DISPLAY_NAME;

                        while (cursor.moveToNext()){
                            contractsList.add(cursor.getString(cursor.getColumnIndex(columnIX)));
                        }
                        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,contractsList);
                        listView.setAdapter(listAdapter);

                        cursor.close();

                    }
                }else{
                    Snackbar.make(view,"İzin Verilmeli",Snackbar.LENGTH_LONG).setAction("İzin Ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_CONTACTS)){
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                            }else{
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                                intent.setData(uri);
                                startActivity(intent);

                            }
                        }
                    }).show();
                }



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}