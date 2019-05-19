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
     * 打开开关，但是没有接咖啡的杯子被拿走时，
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

    @Test
    public void startGoodStart() {
        normalStart();
        Assert.assertTrue(api.boilerOn);
        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    private void normalStart() {
        poll();
        api.boilerEmpty = false;
        api.buttonPressed = true;
        poll();
    }

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

    private void normalFill() {
        normalStart();
        api.potNotEmpty = true;
        poll();
    }

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

    @Test
    public void boilerEmptyPotNotEmpty() {
        normalBrew();
        Assert.assertFalse(api.boilerOn);
        Assert.assertTrue(api.lightOn);
        Assert.assertTrue(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }

    private void normalBrew() {
        normalFill();
        api.boilerEmpty = true;
        poll();
    }

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

    @Test
    public void emptyPotReturnedAfter() {
        normalBrew();
        api.potNotEmpty = false;
        poll();
        Assert.assertFalse(api.boilerOn);
//        Assert.assertFalse(api.lightOn);
        Assert.assertFalse(api.plateOn);
        Assert.assertTrue(api.valveClosed);
    }
}
