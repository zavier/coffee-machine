package com.zavier.coffee.maker4.base;

/**
 * 保温盘状态
 */
public enum WarmerPlatStatus {
    /**
     * 保温盘是空的(上面没有壶)
     */
    WARMER_EMPTY,

    /**
     * 保温盘壶中没有咖啡
     */
    POT_EMPTY,

    /**
     * 保温盘壶中有咖啡
     */
    POT_NOT_EMPTY
}
