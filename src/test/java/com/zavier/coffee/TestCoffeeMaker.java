package com.zavier.coffee;

import com.zavier.coffee.maker4.M4ContainmentVessel;
import com.zavier.coffee.maker4.M4HotWaterSource;
import com.zavier.coffee.maker4.M4UserInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestCoffeeMaker {
    private M4UserInterface ui;
    private M4HotWaterSource hws;
    private M4ContainmentVessel cv;
    private MockCoffeeMaker api;

    @Before
    public void setUp() {
        api = new MockCoffeeMaker();
        ui = new M4UserInterface(api);
        hws = new M4HotWaterSource(api);
        cv = new M4ContainmentVessel(api);
        ui.init(hws, cv);
        hws.init(ui, cv);
        cv.init(ui, hws);
    }

    private void poll() {
        ui.poll();
        hws.poll();
        cv.poll();
    }

    /**
     * 正常状态，开关未打开时，默认状态：
     * 加热器关闭，灯关闭，保温加热器关闭，减压阀关闭
     */
    @Test
    public void initialConditions() {
        poll();
        Assert.assertFalse(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    /**
     * 打开开关，但是接咖啡的杯子被拿走时，
     * 加热开关关闭，指示灯关闭，保温加热器关闭，减压阀打开
     */
    @Test
    public void startNoPot() {
        poll();
        api.buttonPressed = true;
        api.potPresent = false;
        poll();
        Assert.assertFalse(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertFalse(api.valveClosed);
    }

    /**
     * 打开开关，但是加热器中水没有时，
     * 加热器关闭，灯关闭，保温加热器关闭，减压阀关闭
     */
    @Test
    public void startNoWater() {
        poll();
        api.buttonPressed = true;
        api.boilerEmpty = true;
        poll();
        Assert.assertFalse(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    /**
     * 添水后，打开冲煮按钮时，加热器打开，灯关闭状态，保温加热器关闭（因为其中无咖啡），减压阀关闭
     */
    @Test
    public void startGoodStart() {
        normalStart();
        Assert.assertTrue(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    /**
     * 加热器中添水，开关按钮打开
     */
    private void normalStart() {
        poll();
        api.boilerEmpty = false;
        api.buttonPressed = true;
        poll();
    }

    /**
     * 加热器中添加打开开关后，保温盘杯子中的有生成的咖啡了时，
     * 加热器打开状态，灯关闭状态，保温盘加热器打开状态，减压阀关闭
     */
    @Test
    public void startedPotNotEmpty() {
        normalStart();
        api.potNotEmpty = true;
        poll();
        Assert.assertTrue(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertTrue(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    /**
     * 正常启动后，拿走接咖啡的杯子时，加热器关闭，灯关闭，保温盘加热器关闭，减压阀打开；
     * 之后放回杯子，加热器关闭，灯关闭，保温盘加热器关闭（因为默认其中无咖啡），减压阀关闭
     */
    @Test
    public void potRemovedAndReplaceWhileEmpty() {
        normalStart();
        api.potPresent = false;
        poll();
        Assert.assertFalse(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertFalse(api.valveClosed);

        api.potPresent = true;
        poll();
        Assert.assertTrue(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    /**
     * 正常启动生成咖啡后，拿走接咖啡的杯子时：加热器关闭，灯关闭，保温盘加热器关闭，减压阀打开；
     * 后续放回接咖啡的杯子（其中无咖啡）时：加热器打开，灯关闭，保温盘加热器关闭，减压阀关闭
     */
    @Test
    public void potRemovedWhileNotEmptyAndReplaceEmpty() {
        normalFill();
        api.potPresent = false;
        poll();
        Assert.assertFalse(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertFalse(api.valveClosed);

        api.potPresent = true;
        api.potNotEmpty = false;
        poll();
        Assert.assertTrue(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    /**
     * 正常启动，并且已经生成了咖啡
     */
    private void normalFill() {
        normalStart();
        api.potNotEmpty = true;
        poll();
    }

    /**
     * 正常启动，并且产生了咖啡，这时拿走接咖啡的杯子，之后再放回时：
     * 加热器打开，灯关闭，保温盘加热器打开（因为其中有咖啡），减压阀关闭
     */
    @Test
    public void potRemovedWhileNotEmptyAndReplaceNotEmpty() {
        normalFill();
        api.potPresent = false;
        poll();
        api.potPresent = true;
        poll();
        Assert.assertTrue(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertTrue(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    /**
     * 正常启动，生成咖啡后，水源用光时（冲煮咖啡结束）：
     * 加热器关闭，灯打开，保温加热器打开（因为其中仍有咖啡），减压阀关闭
     */
    @Test
    public void boilerEmptyPotNotEmpty() {
        normalBrew();
        Assert.assertFalse(api.boilerOn);
        Assert.assertTrue(api.lightOn);
        Assert.assertTrue(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    /**
     * 正常启动，生成咖啡后，水源用光
     */
    private void normalBrew() {
        normalFill();
        api.boilerEmpty = true;
        poll();
    }

    /**
     * 正常启动，生成咖啡后拿走接咖啡的杯子，并且水源用光时：加热器关闭，灯打开，保温加热器关闭，减压阀关闭；
     * 这时放回煮咖啡的杯子：加热器关闭（无水了），灯打开，保温加热器打开（此时其中仍有咖啡），减压阀关闭
     */
    @Test
    public void boilerEmptiesWhilePotRemoved() {
        normalFill();
        api.potPresent = false;
        poll();
        api.boilerEmpty = true;
        poll();
        Assert.assertFalse(api.boilerOn);
        Assert.assertTrue(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertTrue(api.valveClosed);

        api.potPresent = true;
        poll();
        Assert.assertFalse(api.boilerOn);
        Assert.assertTrue(api.lightOn);
        Assert.assertTrue(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    /**
     * 正常启动，生成咖啡后，水源用光，这时喝光咖啡杯中的咖啡：
     * 加热器关闭，灯打开，保温加热器关闭，减压阀打开
     */
    @Test
    public void emptyPotReturnedAfter() {
        normalBrew();
        api.potNotEmpty = false;
        poll();
        Assert.assertFalse(api.boilerOn);
        Assert.assertTrue(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }
}
