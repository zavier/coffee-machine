package com.zavier.coffee.maker4.base;

public interface CoffeeMakerApi {
    /**
     * 这个函数返回保温盘传感器的状态
     * 传感器检测有没有壶，壶中有没有咖啡
     * @return
     */
    WarmerPlatStatus getWarmerPlateStatus();

    /**
     * 这个函数返回加热器开关的状态
     * 加热器开关是浮动的，确保加热器中至少有半杯水
     * @return
     */
    BoilerStatus getBoilerStatus();

    /**
     * 这个函数返回冲煮按钮的状态
     * 冲煮按钮是一个瞬时开关，可以记住自己的状态
     * 每次调用这个函数都会返回记忆的状态
     * 然后加该状态重置为NOT_PUSHED
     *
     * 因此，即使以非常慢的速度查询这个函数，
     * 仍然可以在冲煮按钮被按下时检测到
     * @return
     */
    BrewButtonStatus getBrewButtonStatus();

    /**
     * 这个函数负责开、关加热器中的加热元件
     * @param s
     */
    void setBoilerState(BoilerState s);

    /**
     * 这个函数负责开、关保温盘加热器中的加热元件
     * @param s
     */
    void setWarmerState(WarmerState s);

    /**
     * 这个函数负责开、关指示灯
     * 冲煮完成时，灯亮；用户按下冲煮按钮时，灯灭
     * @param s
     */
    void setIndicatorState(IndicatorState s);

    /**
     * 这个函数负责开、关减压阀
     * 关闭减压阀时，加热器中的压力使热水喷洒在咖啡过滤器上
     * 打开减压阀时，加热器中的蒸汽排到外部
     * 加热器中的水不会再喷到过滤器上
     * @param s
     */
    void setReliefValveState(ReliefValueState s);
}
