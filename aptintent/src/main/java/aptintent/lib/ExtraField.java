package aptintent.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhl on 16/8/14.
 * bind a field to a extra
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface ExtraField {
    String value();
}
