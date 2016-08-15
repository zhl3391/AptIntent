package aptintent.lib;

public final class AptIntent {

    public static void bind(Object target) {
        String clsName = target.getClass().getName();
        try {
            Class<?> bindClass = Class.forName(clsName + "_Binder");
            //noinspection unchecked
            Binder<Object> binder = (Binder<Object>) bindClass.newInstance();
            binder.bind(target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> T create(Class<T> interfaceClass) {
        String clsName = interfaceClass.getName();

        T instance = null;

        try {
            Class<?> clazz = Class.forName(clsName + "_Imp");
            //noinspection unchecked
            instance =  (T) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return instance;
    }
}
