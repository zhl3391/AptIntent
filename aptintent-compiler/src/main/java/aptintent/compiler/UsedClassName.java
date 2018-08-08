package aptintent.compiler;

import com.squareup.javapoet.ClassName;

import aptintent.lib.Binder;

public class UsedClassName {

    public static final ClassName BUNDLE = ClassName.get("android.os", "Bundle");
    public static final ClassName INTENT = ClassName.get("android.content", "Intent");
    public static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName TOAST = ClassName.get("android.widget", "Toast");
    public static final ClassName BINDER = ClassName.get(Binder.class.getPackage().getName(), Binder.class.getSimpleName());

}
