package aptintent.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标题：
 * 功能：必选传值，如果不传，可能导致数据异常，遗漏或为null时会有Toast提示
 * 备注：
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/8/7 11:34
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface NecessaryField {
    String value();
}
