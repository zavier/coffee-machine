package com.zavier.coffee.base;

/**
 * 热水(咖啡)生成器
 */
public abstract class HotWaterSource {

    private UserInterface ui;
    private ContainmentVessel cv;

    /**
     * protected 让子类可以拿到状态
     */
    protected boolean isBrewing;

    public HotWaterSource() {
        this.isBrewing = false;
    }

    public void init(UserInterface ui, ContainmentVessel cv) {
        this.ui = ui;
        this.cv = cv;
    }

    public void start() {
        isBrewing = true;
        startBrewing();
    }

    public void done() {
        isBrewing = false;
    }

    /**
     * 通知其他系统，煮咖啡已经结束，重置标志位
     */
    public void declareDone() {
        ui.done();
        cv.done();
        isBrewing = false;
    }

    public abstract boolean isReady();
    public abstract void startBrewing();
    public abstract void pause();
    public abstract void resume();


}
