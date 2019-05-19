package com.zavier.coffee;

import com.zavier.coffee.maker4.base.*;

public class MockCoffeeMaker implements CoffeeMakerApi {
    public boolean buttonPressed;
    public boolean lightOn;
    public boolean boilerOn;
    public boolean valveClosed;
    public boolean plateOn;
    public boolean boilerEmpty;
    public boolean potPresent;
    public boolean potNotEmpty;

    /**
     * 默认状态： 开关按钮关闭，指示灯关闭，加热器关闭，减压阀关闭，加热器中无水，加热盘所存在杯子，加热盘中杯子为空
     */
    public MockCoffeeMaker() {
        this.buttonPressed = false;
        this.lightOn = false;
        this.boilerOn = false;
        this.valveClosed = true;
        this.plateOn = false;
        this.boilerEmpty = true;
        this.potPresent = true;
        this.potNotEmpty = false;
    }

    @Override
    public WarmerPlatStatus getWarmerPlateStatus() {
        if (!potPresent) {
            return WarmerPlatStatus.WARMER_EMPTY;
        } else if (potNotEmpty) {
            return WarmerPlatStatus.POT_NOT_EMPTY;
        } else {
            return WarmerPlatStatus.POT_EMPTY;
        }
    }

    @Override
    public BoilerStatus getBoilerStatus() {
        return boilerEmpty ? BoilerStatus.EMPTY : BoilerStatus.NOT_EMPTY;
    }

    @Override
    public BrewButtonStatus getBrewButtonStatus() {
        if (buttonPressed) {
            return BrewButtonStatus.PUSHED;
        } else {
            return BrewButtonStatus.NOT_PUSHED;
        }
    }

    @Override
    public void setBoilerState(BoilerState boilerState) {
        boilerOn = boilerState == BoilerState.ON;
    }

    @Override
    public void setWarmerState(WarmerState warmerState) {
        plateOn = warmerState == WarmerState.ON;
    }

    @Override
    public void setIndicatorState(IndicatorState indicatorState) {
        lightOn = indicatorState == IndicatorState.ON;
    }

    @Override
    public void setReliefValveState(ReliefValueState relieValveState) {
        valveClosed = relieValveState == ReliefValueState.CLOSED;
    }

}
