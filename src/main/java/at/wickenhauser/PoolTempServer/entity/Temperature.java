package at.wickenhauser.PoolTempServer.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wicki
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "getAllTemps", query = "Select t from Temperature t "),
    @NamedQuery(name = "getAllTempsSince", query = "Select t from Temperature t where t.time> :time"),
    @NamedQuery(name="getActualTemp",query = "Select t from Temperature t order by t.time DESC"),
    @NamedQuery(name="getHighestTemp",query = "Select t from Temperature t where t.temperature = (Select MAX(t2.temperature) from Temperature t2)"),
    @NamedQuery(name="getLowestTemp",query = "Select t from Temperature t where t.temperature = (Select MIN(t2.temperature) from Temperature t2)"),
    @NamedQuery(name="getTempBetween",query = "Select t from Temperature t WHERE t.time >= :minDate and t.time <= :maxDate ")
})
@XmlRootElement
public class Temperature implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Double temperature;
    private Date time;

    public Temperature(Double temperature, Date time) {
        this.temperature = temperature;
        this.time = time;
        
    }

    public Temperature() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
    
    
}
