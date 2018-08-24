package com.zhl.aptintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import aptintent.lib.AptIntent;
import aptintent.lib.ExtraField;
import aptintent.lib.NecessaryField;
import aptintent.lib.OptionField;

import static com.zhl.aptintent.IntentCreator.KEY_ARRAY_LIST;
import static com.zhl.aptintent.IntentCreator.KEY_BOOLEAN;
import static com.zhl.aptintent.IntentCreator.KEY_BUNDLE;
import static com.zhl.aptintent.IntentCreator.KEY_INT;
import static com.zhl.aptintent.IntentCreator.KEY_OBJECT;
import static com.zhl.aptintent.IntentCreator.KEY_STRING;

public class MainActivity extends AppCompatActivity {

    @NecessaryField(KEY_STRING)
    String mStringTest;
    @OptionField(KEY_INT)
    int mIntTest;
    @ExtraField(KEY_BOOLEAN)
    boolean mBooleanTest;
    @ExtraField(KEY_OBJECT)
    ObjectTest mObjectTest;
    @ExtraField(KEY_ARRAY_LIST)
    ArrayList<String> mArrayListTest;
    @ExtraField(KEY_BUNDLE)
    String mBundleTest;

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
        if (mArrayListTest != null) {
            for (String str : mArrayListTest) {
                tvExtra.append(KEY_ARRAY_LIST + " : " + str);
            }
        }
        tvExtra.append(KEY_BUNDLE + " : " + mBundleTest + "\n");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                System.out.println(key + " : " + bundle.get(key));
            }
        }

    }

    public void jump(View view) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("arrayList");
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE, "bundle");
//        Intent intent = AptIntent.create(IntentCreator.class).mainActivityIntent(this,
//                bundle, null, 888, true, new ObjectTest(), arrayList);
        Intent intent = AptIntent.create(IntentCreator.class).mainActivityIntent(this,
                bundle, "德玛西亚", true, new ObjectTest(), arrayList);
        startActivity(intent);
    }

    public void jump2(View view) {
//        startActivity(NecessaryFieldTestActivity.createIntent(this));
        startActivity(NecessaryFieldTestActivity.createIntent2(this));
    }

}
