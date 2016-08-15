package com.zhl.aptintent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import aptintent.annotation.Field;
import aptintent.lib.AptIntent;

public class MainActivity extends AppCompatActivity {

    @Field(IntentCreator.KEY_NAME)
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AptIntent.bind(this);

        System.out.println(name);
    }

    public void jump(View view) {
        AptIntent.create(IntentCreator.class).mainActivityIntent(this, "haha");
    }
}
