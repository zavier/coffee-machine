package com.zavier.coffee.maker4;

import com.zavier.coffee.base.UserInterface;
import com.zavier.coffee.maker4.base.BrewButtonStatus;
import com.zavier.coffee.maker4.base.CoffeeMakerApi;
import com.zavier.coffee.maker4.base.IndicatorState;

public class M4UserInterface extends UserInterface implements Pollable {

    private CoffeeMakerApi coffeeMakerApi;

    public M4UserInterface(CoffeeMakerApi coffeeMakerApi) {
        this.coffeeMakerApi = coffeeMakerApi;
    }

    @Override
    public void poll() {
        // 如果按下了开始按钮，则开始煮咖啡
        BrewButtonStatus status = coffeeMakerApi.getBrewButtonStatus();
        if (status == BrewButtonStatus.PUSHED) {
            startBrewing();
        }
    }

    /**
     * 结束后打开指示灯
     */
    @Override
    public void done() {
        coffeeMakerApi.setIndicatorState(IndicatorState.ON);
    }

    @Override
    public void completeCycle() {
        coffeeMakerApi.setIndicatorState(IndicatorState.OFF);
    }
}
