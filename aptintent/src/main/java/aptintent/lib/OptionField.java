package aptintent.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标题：
 * 功能：可选传值，及时没有检索到这个值，也不会影响项目正常运行
 * 备注：
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/8/7 11:34
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface OptionField {
    String value();
}
