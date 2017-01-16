package at.wickenhauser.PoolTempServer.startup;

import at.wickenhauser.PoolTempServer.Controller.TemperatureController;
import at.wickenhauser.PoolTempServer.entity.Temperature;
import at.wickenhauser.PoolTempServer.facades.TemperatureFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
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
    final long dayInMillis = 86400000;

    @PostConstruct
    public void init() {
        /*
        facade.create(new Temperature(19.0, new Date(System.currentTimeMillis())));
        facade.create(new Temperature(23.0, new Date(System.currentTimeMillis() - dayInMillis + 2000000)));
        facade.create(new Temperature(24.0, new Date(System.currentTimeMillis() - dayInMillis + 4000000)));
        facade.create(new Temperature(18.0, new Date(System.currentTimeMillis() - dayInMillis + 6000000)));
        facade.create(new Temperature(22.9, new Date(System.currentTimeMillis() - dayInMillis + 8000000)));
        facade.create(new Temperature(23.0, new Date(System.currentTimeMillis() - dayInMillis + 10000000)));
        facade.create(new Temperature(25.20, new Date(System.currentTimeMillis() - dayInMillis + 12000000)));
        facade.create(new Temperature(15.0, new Date(System.currentTimeMillis() - dayInMillis + 14000000)));
*/

        //Production mode
        Runnable tempConsumer = new Runnable() {
            @Override
            public void run() {
                double temp;
                while (true) {
                    try {
                        temp = TemperatureController.readTemperature();
                        if (temp != Double.MIN_VALUE) {
                            facade.create(new Temperature(temp, new Date(System.currentTimeMillis() + 0 * dayInMillis)));
                        }
                        Thread.sleep(1800000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        Thread t = new Thread(tempConsumer);
        t.start();

        //Test mode
        Runnable testTempConsumer = new Runnable() {
            @Override
            public void run() {
                
                double actTemp = 20;
                int numberOfMeasurements = 0;
                
                while (true){
                    
                    try{
                        actTemp += ThreadLocalRandom.current().nextDouble(-1, 1);
                        actTemp = round(actTemp, 2);
                        facade.create(new Temperature(actTemp, new Date(System.currentTimeMillis() + numberOfMeasurements * 1000 * 60 * 30)));
                        numberOfMeasurements++;
                        Thread.sleep(1000);
                    } catch (InterruptedException ex){
                        Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        };
        Thread testDataThread = new Thread(testTempConsumer);
        testDataThread.start();
        
    }
    
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
