package aptintent.lib;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AptIntent {

    static final Map<Class<?>, Binder<Object>> BINDER = new LinkedHashMap<>();
    static final Map<Class<?>, Object> CREATOR = new LinkedHashMap<>();

    public static void bind(Object target) {
        Binder<Object> binder = BINDER.get(target.getClass());
        String clsName = target.getClass().getName();
        if (binder == null) {
            try {
                Class<?> bindClass = Class.forName(clsName + "_Binder");
                //noinspection unchecked
                binder = (Binder<Object>) bindClass.newInstance();
                BINDER.put(target.getClass(), binder);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to create binder for " + clsName, e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Unable to create binder for " + clsName, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to create binder for " + clsName, e);
            }
        }
        if (binder != null) {
            binder.bind(target);
        } else {
            throw new RuntimeException("Unable to create binder for " + clsName);
        }
    }

    public static <T> T create(Class<T> interfaceClass) {
        Object instance = CREATOR.get(interfaceClass);
        String clsName = interfaceClass.getName();

        if (instance != null) {
            //noinspection unchecked
            return (T) instance;
        }

        try {
            Class<?> clazz = Class.forName(clsName + "_Imp");
            instance = clazz.newInstance();
            CREATOR.put(interfaceClass, instance);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to create creator for " + clsName, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to create creator for " + clsName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to create creator for " + clsName, e);
        }
        //noinspection unchecked
        return (T) instance;
    }
}
