package com.zavier.coffee;

import com.zavier.coffee.maker4.M4ContainmentVessel;
import com.zavier.coffee.maker4.M4HotWaterSource;
import com.zavier.coffee.maker4.M4UserInterface;
import com.zavier.coffee.maker4.base.CoffeeMakerApi;

public class M4CoffeeMaker {
    public static void main(String[] args) {
        CoffeeMakerApi api = null; // new M4CoffeeMakerApi();
        M4UserInterface ui = new M4UserInterface(api);
        M4HotWaterSource hws = new M4HotWaterSource(api);
        M4ContainmentVessel cv = new M4ContainmentVessel(api);

        ui.init(hws, cv);
        hws.init(ui, cv);
        cv.init(ui, hws);

        while (true) {
            ui.poll();
            hws.poll();
            cv.poll();
        }

    }
}
