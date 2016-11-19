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
    
    public static double readTemperature() throws InterruptedException{
        
        File file=new File("/sys/bus/w1/devices/28-80000026d871/w1_slave");
        String line;
        
        try {
            BufferedReader br=new BufferedReader(new FileReader(file));
            br.close();
            br=new BufferedReader(new FileReader(file));
            
                br.readLine();
                line=br.readLine();
                Thread.sleep(3000);
                br.close();
                System.out.println(line);
                String num=line.substring(29);
                return (double)Integer.parseInt(num)/1000;

            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TemperatureController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TemperatureController.class.getName()).log(Level.SEVERE, null, ex);
        }
                

        
        return Double.MIN_VALUE;
    }
    
}
