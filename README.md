# AptIntent

[![Build Status](https://travis-ci.org/zhl3391/AptIntent.svg?branch=master)](https://travis-ci.org/zhl3391/AptIntent)

AptIntent是一个Android平台基于annotation的库，其目的是简化创建带数据的Intent和获取Intent内的数据。

## 使用方法

1.添加依赖

注：依赖还有问题，不知为什么两个库都提交了，但是工作人员没有把compiler的库加到jcenter，如果有人知道如何解决麻烦告诉我，谢谢！

Gradle

在你的工程目录下的`build.gradle`添加`android-apt`插件：
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}
```
然后在你的模块下的`build.gradle`应用`android-apt`插件，添加AptIntent依赖：
```groovy
apply plugin: 'android-apt'

android {
  ...
}

dependencies {
	compile 'com.zhl:aptintent-api:1.0.0'
	apt 'com.zhl:aptintent-compiler:1.0.0'
}
```

2.定义接口

```java
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
```

定义一个`IntentCreator`接口，定义一个`mainActivityIntent`的方法,通过`@CreateIntent`注解声明, 通过`@Extra`注解声明参数。  
注：必须传入`Context`。

3.获取参数和跳转

```java
public class MainActivity extends AppCompatActivity {

    @ExtraField(KEY_STRING)
    String mStringTest;
    @ExtraField(KEY_INT)
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

        AptIntent.bind(this);
    }

    public void jump(View view) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("arrayList");
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE, "bundle");
        Intent intent = AptIntent.create(IntentCreator.class).mainActivityIntent(this,
                bundle, "haha", 888, true, new ObjectTest(), arrayList);
        startActivity(intent);
    }
}
```

通过`@ExtraField`注解声明成员变量，调用`AptIntent.bind(this)`获取`intent`内数据。  
调用`AptIntent.create(IntentCreator.class)`创建IntentCreator接口的实现，再调用`mianActivityIntent`创建`intent`。  
注：`@ExtraField`注解的成员变量不能用`private`，`static`修饰。

## License
```
Copyright 2016 zhl3391

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


