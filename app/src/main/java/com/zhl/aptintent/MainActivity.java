package com.zhl.aptintent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import aptintent.annotation.Field;
import aptintent.lib.AptIntent;

public class MainActivity extends AppCompatActivity {

    @Field("sdfsdfdsf")
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AptIntent.bind(this);

        System.out.println(name);
    }
}
