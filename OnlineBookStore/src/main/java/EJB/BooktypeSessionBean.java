/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Booktype;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author KHUSHI PC
 */
@Stateless
public class BooktypeSessionBean implements BooktypeSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    private EntityManager em;

    @Override
    public boolean create(Booktype booktype) {
        try {
            em.persist(booktype);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Booktype booktype) {
        try {
            em.merge(booktype);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try {
            Booktype b = em.find(Booktype.class, id);
            if (b != null) {
                em.remove(b);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Booktype find(Integer id) {
        return em.find(Booktype.class, id);
    }

    @Override
    public List<Booktype> findAll() {
        return em.createQuery("SELECT b FROM Booktype b", Booktype.class).getResultList();
    }
}
