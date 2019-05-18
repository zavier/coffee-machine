package com.zavier.coffee.maker4.base;

public enum WarmerPlatStatus {
    /**
     * 保温盘是空的
     */
    WARMER_EMPTY,

    /**
     * 保温盘壶中没有咖啡
     */
    POT_EMPTY,

    /**
     * 保温盘壶中有咖啡（已经制作完喝完了，制作结束）
     */
    POT_NOT_EMPTY
}
