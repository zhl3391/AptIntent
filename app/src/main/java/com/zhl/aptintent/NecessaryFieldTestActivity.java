package com.zhl.aptintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import aptintent.lib.AptIntent;
import aptintent.lib.NecessaryField;

import static com.zhl.aptintent.IntentCreator.KEY_STRING;

/**
 * 标题：必要值传递测试页
 * 功能：测试bundle传值的时候，如果接收页包含NecessaryField，无论是否bundle==null,都给出toast提示。
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/8/24 10:49
 */
public class NecessaryFieldTestActivity extends AppCompatActivity {

    TextView mTvContent;

    @NecessaryField(KEY_STRING)
    String mNecessaryField;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, NecessaryFieldTestActivity.class);
        return intent;
    }

    public static Intent createIntent2(Context context) {
        Intent intent = new Intent(context, NecessaryFieldTestActivity.class);
        intent.putExtra(KEY_STRING, "test string");
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AptIntent.bind(this);
        setContentView(R.layout.activity_necessary_field_test);
        mTvContent = (TextView) findViewById(R.id.mTvContent);
        mTvContent.setText(mNecessaryField);
    }

}
