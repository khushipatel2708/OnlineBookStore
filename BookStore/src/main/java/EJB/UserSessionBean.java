/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.City;
import Entity.Shipping;
import Entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;

/**
 *
 * @author KHUSHI PC
 */
@Stateless
public class UserSessionBean implements UserSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;

    // ✅ Get all shippings
    @Override
    public Collection<Shipping> getAllShippings() {
        return em.createNamedQuery("Shipping.findAll").getResultList();
    }

    // ✅ Get single shipping by id
    @Override
    public Shipping getShippingById(Integer id) {
        return em.find(Shipping.class, id);
    }

    // ✅ Add new shipping
    @Override
    public void addShipping(Integer userId, Integer cityId, String name, String phone, String address1, String address2, String landmark, String pincode) {
        User user = em.find(User.class, userId);
        City city = cityId != null ? em.find(City.class, cityId) : null;

        Shipping s = new Shipping();
        s.setUserid(user);
        s.setCityid(city);
        s.setName(name);
        s.setPhone(phone);
        s.setAddress1(address1);
        s.setAddress2(address2);
        s.setLandmark(landmark);
        s.setPincode(pincode);

        em.persist(s);
    }

    // ✅ Update existing shipping
    @Override
    public void updateShipping(Integer id, Integer userId, Integer cityId, String name, String phone, String address1, String address2, String landmark, String pincode) {
        Shipping s = em.find(Shipping.class, id);
        if (s != null) {
            User user = em.find(User.class, userId);
            City city = cityId != null ? em.find(City.class, cityId) : null;

            s.setUserid(user);
            s.setCityid(city);
            s.setName(name);
            s.setPhone(phone);
            s.setAddress1(address1);
            s.setAddress2(address2);
            s.setLandmark(landmark);
            s.setPincode(pincode);

            em.merge(s);
        }
    }

    // ✅ Delete shipping
    @Override
    public void removeShipping(Integer id) {
        Shipping s = em.find(Shipping.class, id);
        if (s != null) {
            em.remove(s);
        }
    }

    // ✅ Get shippings by City
    @Override
    public Collection<Shipping> getShippingByCity(Integer cityId) {
        return em.createQuery("SELECT s FROM Shipping s WHERE s.cityid.id = :cityId", Shipping.class)
                .setParameter("cityId", cityId)
                .getResultList();
    }

    // ✅ Get shippings by Pincode
    @Override
    public Collection<Shipping> getShippingByPincode(String pincode) {
        return em.createQuery("SELECT s FROM Shipping s WHERE s.pincode = :pincode", Shipping.class)
                .setParameter("pincode", pincode)
                .getResultList();
    }

    // ✅ Get shippings by User
    @Override
    public Collection<Shipping> getShippingByUser(Integer userId) {
        return em.createQuery("SELECT s FROM Shipping s WHERE s.userid.id = :userId", Shipping.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}