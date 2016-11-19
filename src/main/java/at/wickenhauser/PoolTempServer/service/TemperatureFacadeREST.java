/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.wickenhauser.PoolTempServer.service;

import at.wickenhauser.PoolTempServer.Controller.TemperatureController;
import at.wickenhauser.PoolTempServer.entity.Temperature;
import at.wickenhauser.PoolTempServer.facades.TemperatureFacade;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Expression;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author wicki
 */
@Stateless
@Path("TemperatureREST")
public class TemperatureFacadeREST extends AbstractFacade<Temperature> {

    @PersistenceContext(unitName = "PoolTempPU")
    private EntityManager em;
    
    @EJB
    TemperatureFacade tempFacade;

    public TemperatureFacadeREST() {
        super(Temperature.class);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Temperature> findAll() {
        List<Temperature> templist = super.findAll();
        templist.sort(new Comparator<Temperature>() {
            @Override
            public int compare(Temperature o1, Temperature o2) {
                if (o1.getTime().getTime() > o2.getTime().getTime()) {
                    return 1;
                } else if (o1.getTime().getTime() < o2.getTime().getTime()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return templist;
    }

    @GET
    @Path("{from}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Temperature> findRange(@PathParam("from") Long from) {
        List<Temperature> result;
        System.out.println("" + new Date(from));
        Date s = new Date(from);
        result = getEntityManager().createNamedQuery("getAllTempsSince").setParameter("time", new Date(from)).getResultList();
        result.sort(new Comparator<Temperature>() {
            @Override
            public int compare(Temperature o1, Temperature o2) {
                if (o1.getTime().getTime() > o2.getTime().getTime()) {
                    return 1;
                } else if (o1.getTime().getTime() < o2.getTime().getTime()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return result;
        //q.setMaxResults(range[1] - range[0] + 1);
        //q.setFirstResult(range[0]);

    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("forceTemperature")
    @Produces(MediaType.APPLICATION_JSON)
    public Temperature forceNewTemperature() {

        try {
            double temp = TemperatureController.readTemperature();
            Temperature t=new Temperature(temp, new Date());
            tempFacade.create(t);
            return t;
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(TemperatureFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
