package com.zavier.coffee.maker4;

import com.zavier.coffee.base.ContainmentVessel;
import com.zavier.coffee.maker4.base.CoffeeMakerApi;
import com.zavier.coffee.maker4.base.WarmerPlatStatus;
import com.zavier.coffee.maker4.base.WarmerState;

public class M4ContainmentVessel extends ContainmentVessel implements Pollable {
    private CoffeeMakerApi coffeeMakerApi;
    /**
     * 保温盘的最后状态
     */
    private WarmerPlatStatus lastPotStatus;

    public M4ContainmentVessel(CoffeeMakerApi coffeeMakerApi) {
        this.coffeeMakerApi = coffeeMakerApi;
        this.lastPotStatus = WarmerPlatStatus.POT_EMPTY;
    }

    /**
     * 开始前保温盘中有空杯子
     * @return
     */
    @Override
    public boolean isReady() {
        WarmerPlatStatus status = coffeeMakerApi.getWarmerPlateStatus();
        return status == WarmerPlatStatus.POT_EMPTY;
    }

    /**
     * 开始时记录状态
     */
    @Override
    public void start() {
        isBrewing = true;
    }

    @Override
    public void poll() {
        WarmerPlatStatus potStatus = coffeeMakerApi.getWarmerPlateStatus();
        // 保温盘状态有变更
        if (potStatus != lastPotStatus) {
            handleBrewEvent(potStatus);
        } else if (!isComplete) {
            handleIncompleteEvent(potStatus);
        }
        lastPotStatus = potStatus;
    }

    private void handleBrewEvent(WarmerPlatStatus potStatus) {
        if (potStatus == WarmerPlatStatus.POT_NOT_EMPTY) {
            containerAvailable();
            coffeeMakerApi.setWarmerState(WarmerState.ON);
        } else if (potStatus == WarmerPlatStatus.WARMER_EMPTY) {
            // 保温盘是空的，杯子被拿走，此时关闭加热器等
            containerUnAvailable();
            coffeeMakerApi.setWarmerState(WarmerState.OFF);
        } else if (potStatus == WarmerPlatStatus.POT_EMPTY){
            containerAvailable();
            coffeeMakerApi.setWarmerState(WarmerState.OFF);
        }
    }

    private void handleIncompleteEvent(WarmerPlatStatus potStatus) {
        if (potStatus == WarmerPlatStatus.POT_NOT_EMPTY) {
            coffeeMakerApi.setWarmerState(WarmerState.ON);
        } else if (potStatus == WarmerPlatStatus.WARMER_EMPTY) {
            coffeeMakerApi.setWarmerState(WarmerState.OFF);
        } else if (potStatus == WarmerPlatStatus.POT_EMPTY) {
            coffeeMakerApi.setWarmerState(WarmerState.OFF);
            declareComplete();
        }
    }
}
