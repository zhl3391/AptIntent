package aptintent.lib;

public class AptIntent {

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
}
