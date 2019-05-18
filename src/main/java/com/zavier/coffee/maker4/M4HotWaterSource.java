package com.zavier.coffee.maker4;

import com.zavier.coffee.base.HotWaterSource;
import com.zavier.coffee.maker4.base.BoilerState;
import com.zavier.coffee.maker4.base.BoilerStatus;
import com.zavier.coffee.maker4.base.CoffeeMakerApi;
import com.zavier.coffee.maker4.base.ReliefValueState;

public class M4HotWaterSource extends HotWaterSource implements Pollable {

    private CoffeeMakerApi coffeeMakerApi;

    public M4HotWaterSource(CoffeeMakerApi coffeeMakerApi) {
        this.coffeeMakerApi = coffeeMakerApi;
    }

    /**
     * 需要保证煮咖啡开始前有水
     * @return
     */
    @Override
    public boolean isReady() {
        BoilerStatus status = coffeeMakerApi.getBoilerStatus();
        return status == BoilerStatus.NOT_EMPTY;
    }

    /**
     * 开始生成热水时，关闭减压阀，打开加热开关
     */
    @Override
    public void startBrewing() {
        coffeeMakerApi.setReliefValueState(ReliefValueState.CLOSED);
        coffeeMakerApi.setBoilerState(BoilerState.ON);
    }

    @Override
    public void poll() {
        BoilerStatus boilerStatus = coffeeMakerApi.getBoilerStatus();
        if (isBrewing) {
            // 如果正在煮咖啡，且加热器中没有水是空的
            if (boilerStatus == BoilerStatus.EMPTY) {
                coffeeMakerApi.setBoilerState(BoilerState.OFF);
                coffeeMakerApi.setReliefValueState(ReliefValueState.CLOSED);
                declareDone();
            }
        }
    }

    /**
     * 暂停时，停止加热，打开减压阀
     */
    @Override
    public void pause() {
        coffeeMakerApi.setBoilerState(BoilerState.OFF);
        coffeeMakerApi.setReliefValueState(ReliefValueState.OPEN);
    }

    /**
     * 重新启动，打开加热开关，关闭减压阀
     */
    @Override
    public void resume() {
        coffeeMakerApi.setBoilerState(BoilerState.ON);
        coffeeMakerApi.setReliefValueState(ReliefValueState.CLOSED);
    }
}
