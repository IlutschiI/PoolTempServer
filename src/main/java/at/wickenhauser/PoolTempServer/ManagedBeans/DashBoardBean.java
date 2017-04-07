/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.wickenhauser.PoolTempServer.ManagedBeans;

import at.wickenhauser.PoolTempServer.entity.Temperature;
import at.wickenhauser.PoolTempServer.facades.TemperatureFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author wicki
 */
@Named("DashBoardBean")
@RequestScoped
public class DashBoardBean implements Serializable{
    
    @Inject
    TemperatureFacade tempFacade;
    
    double accTemp;
    double highestTemp;
    double lowestTemp;
    double yesterdayTemp;
    List<Temperature> tempList;
    LineChartModel chartModel;
    
    
    @PostConstruct
    public void init(){
        accTemp=tempFacade.getActualTemp().getTemperature();
        highestTemp=tempFacade.getHighestTemp().getTemperature();
        lowestTemp=tempFacade.getLowestTemp().getTemperature();
        yesterdayTemp=tempFacade.getYesterdayTemp();
        tempList=tempFacade.findAll();
        chartModel=initLinearChartModel();
    }

    public LineChartModel getChartModel() {
        return chartModel;
    }

    public void setChartModel(LineChartModel chartModel) {
        this.chartModel = chartModel;
    }

    
    
    public double getAccTemp() {
        return accTemp;
    }

    public void setAccTemp(double accTemp) {
        this.accTemp = accTemp;
    }

    public double getHighestTemp() {
        return highestTemp;
    }

    public void setHighestTemp(double highestTemp) {
        this.highestTemp = highestTemp;
    }

    public double getLowestTemp() {
        return lowestTemp;
    }

    public void setLowestTemp(double lowestTemp) {
        this.lowestTemp = lowestTemp;
    }

    public double getYesterdayTemp() {
        return yesterdayTemp;
    }

    public void setYesterdayTemp(double yesterdayTemp) {
        this.yesterdayTemp = yesterdayTemp;
    }
    
    private LineChartModel initLinearChartModel() {
        LineChartModel model = new LineChartModel();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

 
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Temperatur");
        int i=0;
        tempList=minimizeList(tempList,100);
        for (Temperature temperature : tempList) {
            series1.set(sdf.format(temperature.getTime()),temperature.getTemperature());
        }
       
        DateAxis axis = new DateAxis("Datum");
        axis.setTickAngle(-50);
        axis.setTickFormat("%#d.%m.%Y");
        axis.setTickInterval("86400000");
        axis.setTickCount(30);
        model.getAxes().put(AxisType.X, axis);
        
 
        model.getAxis(AxisType.Y).setMax(30);
        model.getAxis(AxisType.Y).setMin(00);
        
        model.getAxis(AxisType.X).setLabel("Temperature");

        
        //model.setShowPointLabels(true);
        model.addSeries(series1);
        model.setZoom(true);
        
        //model.getAxis(AxisType.Y).setMax(50);
        //model.getAxis(AxisType.Y).setMax(00);
         
        return model;
    }
    
 private List<Temperature> minimizeList(List<Temperature> fullList, int maxSize) {
        Temperature temp1;
        Temperature temp2;
        List<Temperature> minimizedList = new LinkedList<>();
        
        while (fullList.size() >= maxSize) {
            minimizedList = new LinkedList<>();
            for (int i = 0; i < fullList.size(); i = i + 2) {
                if (i + 1 < fullList.size()) {
                    temp1 = fullList.get(i);
                    temp2 = fullList.get(i + 1);
                    temp1.setTemperature((temp1.getTemperature()+ temp2.getTemperature()) / 2);
                    minimizedList.add(temp1);
                }
            }
            fullList = minimizedList;
/*
            temp1=result.get(0);
            temp2=result.get(1);
            result.remove(0);
            result.remove(0);
            temp1.setTemp((temp1.getTemp()+temp2.getTemp())/2);
            minimizedList.add(temp1);
            */
        }
        if(minimizedList.size()==0)
            return fullList;
        return minimizedList;
    }
    
}
