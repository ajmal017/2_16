package com.algoTrader.starter;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Interpolation;

public class SimulationStarter {

    public static void main(String[] args) {

        start();
    }

    public static void start() {

        ServiceLocator.instance().getSimulationService().init();

        ServiceLocator.instance().getRuleService().activateAll();
        ServiceLocator.instance().getSimulationService().run();

        Interpolation interpolation = ServiceLocator.instance().getSimulationService().getInterpolation();

        System.out.print("a=" + interpolation.getA());
        System.out.print(" b=" + interpolation.getB());
        System.out.println(" r=" + interpolation.getR());
    }
}
