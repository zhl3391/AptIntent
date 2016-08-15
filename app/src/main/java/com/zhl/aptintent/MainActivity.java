package com.zhl.aptintent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import aptintent.annotation.ExtraField;
import aptintent.lib.AptIntent;

import static com.zhl.aptintent.IntentCreator.*;

public class MainActivity extends AppCompatActivity {

    @ExtraField(KEY_STRING)
    String mStringTest;
    @ExtraField(KEY_INT)
    int mIntTest;
    @ExtraField(KEY_BOOLEAN)
    boolean mBooleanTest;
    @ExtraField(KEY_OBJECT)
    ObjectTest mObjectTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvExtra = (TextView) findViewById(R.id.tv_extras);

        AptIntent.bind(this);

        tvExtra.append("------extra------\n");
        tvExtra.append(KEY_STRING + " : " + mStringTest + "\n");
        tvExtra.append(KEY_INT + " : " + mIntTest + "\n");
        tvExtra.append(KEY_BOOLEAN + " : " + mBooleanTest + "\n");
        tvExtra.append(KEY_OBJECT + " : " + (mObjectTest == null ? "null" : mObjectTest.name) + "\n");

    }

    public void jump(View view) {
        startActivity(AptIntent.create(IntentCreator.class).mainActivityIntent(this, "haha", 888, true, new ObjectTest()));
    }
}
