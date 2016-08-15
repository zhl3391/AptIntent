package com.zhl.aptintent;

import android.content.Context;
import android.content.Intent;

import aptintent.annotation.CreateIntent;
import aptintent.annotation.Extra;

/**
 * Created by zhl on 16/8/14.
 */
public interface IntentCreator {

    String KEY_NAME = "name";

    @CreateIntent(MainActivity.class)
    Intent mainActivityIntent(Context context, @Extra(KEY_NAME)String name);
}
