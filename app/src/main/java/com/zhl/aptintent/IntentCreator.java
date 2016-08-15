package com.zhl.aptintent;

import android.content.Context;
import android.content.Intent;

import aptintent.annotation.CreateIntent;
import aptintent.annotation.Extra;

/**
 * Created by zhl on 16/8/14.
 * IntentCreator
 */
public interface IntentCreator {

    String KEY_STRING   = "string";
    String KEY_INT      = "int";
    String KEY_BOOLEAN  = "boolean";
    String KEY_OBJECT   = "object";

    @CreateIntent(MainActivity.class)
    Intent mainActivityIntent(Context context,
                              @Extra(KEY_STRING)String stringTest,
                              @Extra(KEY_INT) int intTest,
                              @Extra(KEY_BOOLEAN) boolean booleanTest,
                              @Extra(KEY_OBJECT) ObjectTest objectTest);
}
