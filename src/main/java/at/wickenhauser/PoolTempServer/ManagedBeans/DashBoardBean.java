package at.wickenhauser.PoolTempServer.ManagedBeans;

import at.wickenhauser.PoolTempServer.entity.Temperature;
import at.wickenhauser.PoolTempServer.facades.TemperatureFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
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
  
}
