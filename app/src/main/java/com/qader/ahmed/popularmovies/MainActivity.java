package com.qader.ahmed.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MainActivityFragment fragment = new MainActivityFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment1,fragment).commit();

    }

}
