package finley.gmair.service.holder;

import finley.gmair.util.tmall.TmallDeviceTypeEnum;

import java.lang.annotation.*;

/**
 * 作用在类上，用来标识一个策略是作用于哪个类型的设备
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerOperationType {

    /**
     * 设备类型
     *
     * @return 设备类型
     */
    TmallDeviceTypeEnum value();

}
