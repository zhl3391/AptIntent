package com.zhl.aptintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import aptintent.lib.CreateIntent;
import aptintent.lib.Extra;

/**
 * Created by zhl on 16/8/14.
 * 生成intent的接口
 */
public interface IntentCreator {

    String KEY_STRING       = "string";
    String KEY_INT          = "int";
    String KEY_BOOLEAN      = "boolean";
    String KEY_OBJECT       = "object";
    String KEY_ARRAY_LIST   = "array_list";
    String KEY_BUNDLE       = "bundle";

    @CreateIntent(MainActivity.class)
    Intent mainActivityIntent(Context context,
                              Bundle bundle,
                              @Extra(KEY_STRING)String stringTest,
                              @Extra(KEY_INT) int intTest,
                              @Extra(KEY_BOOLEAN) boolean booleanTest,
                              @Extra(KEY_OBJECT) ObjectTest objectTest,
                              @Extra(KEY_ARRAY_LIST) ArrayList<String> arrayListTest);
}
