package aptintent.compiler;

import com.squareup.javapoet.ClassName;

/**
 * Created by zhl on 16/8/14.
 * UsedClassName
 */
public class UsedClassName {
    public static final ClassName BUNDLE = ClassName.get("android.os", "Bundle");
    public static final ClassName INTENT = ClassName.get("android.content", "Intent");
    public static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName BINDER = ClassName.get("aptintent.lib", "Binder");
}
