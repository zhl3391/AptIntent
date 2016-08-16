package aptintent.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhl on 16/8/14.
 * used in createIntent method
 * the extra name will be inferred form the annotated element
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.PARAMETER)
public @interface Extra {
    String value();
}
