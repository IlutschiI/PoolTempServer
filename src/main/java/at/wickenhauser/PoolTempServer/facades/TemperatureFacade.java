package at.wickenhauser.PoolTempServer.facades;

import at.wickenhauser.PoolTempServer.entity.Temperature;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wicki
 */
@Stateless
public class TemperatureFacade extends AbstractFacade<Temperature> {

    @PersistenceContext(unitName = "PoolTempPU")
    private EntityManager em;

    public TemperatureFacade() {
        super(Temperature.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Temperature getActualTemp() {
        Temperature t = (Temperature) em.createNamedQuery("getActualTemp").setMaxResults(1).getSingleResult();
        return t;
    }

    public Temperature getHighestTemp() {
        Temperature t = (Temperature) em.createNamedQuery("getHighestTemp").setMaxResults(1).getSingleResult();
        return t;
    }

    public Temperature getLowestTemp() {
        Temperature t = (Temperature) em.createNamedQuery("getLowestTemp").setMaxResults(1).getSingleResult();
        return t;
    }

    public double getYesterdayTemp() {
         Calendar calendar=GregorianCalendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Date startDate=calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);

        Date endDate=calendar.getTime();
        
        List<Temperature> list =  em.createNamedQuery("getTempBetween").setParameter("minDate", startDate).setParameter("maxDate", endDate).getResultList();
                
        double result=0;
        
        for (Temperature temperature : list) {
            result+=temperature.getTemperature();
        }
        if(list.size()!=0){
        result/=list.size();
        result=round(result, 2);
        
        }
        else
            result=Double.MIN_VALUE;
        return result;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
}
