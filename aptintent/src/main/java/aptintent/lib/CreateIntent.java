package aptintent.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by zhl on 16/8/14.
 * create a intent with extra
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CreateIntent {

    Class<?> value();
}
