/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.wickenhauser.PoolTempServer.Controller;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wicki
 */
public class Pi4JTemperatureController {

    double actualTemperature = 0;

    public void startReading() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try{
                     actualTemperature=readTemperature();   
                    }
                    catch(Exception e)
                    {
                        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
                    }
                    
                }
            }
        });
        t.start();
    }

    public double readTemperature() {
        W1Master master = new W1Master();

        for (W1Device device : master.getDevices(TmpDS18B20DeviceType.FAMILY_CODE)) {
            return ((TemperatureSensor) device).getTemperature();
        }

        return 0;
    }

}
