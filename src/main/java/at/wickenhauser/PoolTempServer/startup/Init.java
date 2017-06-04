/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.wickenhauser.PoolTempServer.startup;

import at.wickenhauser.PoolTempServer.Controller.TemperatureController;
import at.wickenhauser.PoolTempServer.entity.Temperature;
import at.wickenhauser.PoolTempServer.facades.TemperatureFacade;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author wicki
 */
@Singleton
@Startup
public class Init {
    
    @EJB
    TemperatureFacade facade;
    final long dayInMillis=86400000;
    
    @PostConstruct
    public void init(){
        /*facade.create(new Temperature(19.0, new Date(System.currentTimeMillis())));
        facade.create(new Temperature(23.0, new Date(System.currentTimeMillis()-dayInMillis+2000000)));
        facade.create(new Temperature(24.0, new Date(System.currentTimeMillis()-dayInMillis+4000000)));
        facade.create(new Temperature(18.0, new Date(System.currentTimeMillis()-dayInMillis+6000000)));
        facade.create(new Temperature(22.9, new Date(System.currentTimeMillis()-dayInMillis+8000000)));
        facade.create(new Temperature(23.0, new Date(System.currentTimeMillis()-dayInMillis+10000000)));
        facade.create(new Temperature(25.20, new Date(System.currentTimeMillis()-dayInMillis+12000000)));
        facade.create(new Temperature(15.0, new Date(System.currentTimeMillis()-dayInMillis+14000000)));
        */
        
        Runnable tempConsumer = new Runnable() {
            @Override
            public void run() {
                double temp;
                int i=0;
                TemperatureController.readTempEndless();
                while(true){
                    try {
                        temp=TemperatureController.readTemperature();
                        if(temp!=Double.MIN_VALUE)
                        facade.create(new Temperature(temp, new Date(System.currentTimeMillis()+0*dayInMillis)));
                        Thread.sleep(1800000);
                        i++;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        Thread t = new Thread(tempConsumer);
        t.start();
        //tempConsumer.run();
        
    }
    
}
