package finley.gmair.annotation;


import finley.gmair.model.minivoice.OperateType;
import finley.gmair.model.minivoice.PlatformType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateDataDetail {

    /**
     * 操作类型(enum):主要是 select,insert,update,delete
     */
    OperateType operateType() default OperateType.DEVICE;

    /**
     * 被操作的对象(此处使用 enum):可以是任何对象，如表名(user)，或者是工具(redis)
     */
    PlatformType platformType() default PlatformType.COMMON;

}
