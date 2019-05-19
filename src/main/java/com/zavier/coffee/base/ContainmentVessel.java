package com.zavier.coffee.base;

/**
 * 咖啡容器
 */
public abstract class ContainmentVessel {

    private UserInterface ui;
    private HotWaterSource hws;

    /**
     * 子类可以拿到状态位
     */
    protected  boolean isBrewing;
    protected boolean isComplete;

    public ContainmentVessel() {
        this.isBrewing = false;
        this.isComplete = true;
    }

    public void init(UserInterface ui, HotWaterSource hws) {
        this.ui = ui;
        this.hws = hws;
    }

    public void start() {
        isBrewing = true;
        isComplete = false;
    }

    public void done() {
        isBrewing = false;
    }

    /**
     * 声明制作咖啡完成
     */
    protected void declareComplete() {
        isComplete = true;
        ui.complete();
    }

    /**
     * 有容器时重新打开
     */
    protected void containerAvailable() {
        hws.resume();
    }

    /**
     * 没有容器时暂停
     */
    protected void containerUnAvailable() {
        hws.pause();
    }

    public abstract boolean isReady();

}
