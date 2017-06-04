/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.wickenhauser.PoolTempServer.Controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wicki
 */
public class TemperatureController {

    private static double actualTemperature = 0;
    private static final File file = new File("/sys/bus/w1/devices/28-80000026d871/w1_slave");

    public static void readTempEndless() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        actualTemperature = readTemperatureFromFile(file);
                        Logger.getGlobal().log(Level.SEVERE, "aktuelle Temperatur: "+actualTemperature);
                        Thread.sleep(3000);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(TemperatureController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TemperatureController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NumberFormatException ex) {
                    Logger.getLogger(TemperatureController.class.getName()).log(Level.SEVERE, null, ex);
                }catch(Exception ex){
                    Logger.getLogger(TemperatureController.class.getName()).log(Level.SEVERE, null, ex);
                }
                run();
            }
        });
        t.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TemperatureController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static double readTemperature() throws InterruptedException {
            return actualTemperature;
    }

    private static double readTemperatureFromFile(File file) throws InterruptedException, FileNotFoundException, IOException, NumberFormatException {
        BufferedReader br;
        String line;
        br = new BufferedReader(new FileReader(file));
        br.readLine();
        line = br.readLine();
        System.out.println(line);
        br.close();
        System.out.println(line);
        String num = line.substring(29);
        if((double) Integer.parseInt(num) / 1000==0)
            return actualTemperature;
        return (double) Integer.parseInt(num) / 1000;
    }

}
