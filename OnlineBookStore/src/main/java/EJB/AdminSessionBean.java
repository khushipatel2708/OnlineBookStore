package EJB;

import Entity.Book;
import Entity.Booktype;
import Entity.City;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AdminSessionBean implements AdminSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;

    //BookType Logic
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
    
    //Book Logic
    @Override
    public void addBook(Book book) {
        em.persist(book);
    }

    @Override
    public void updateBook(Book book) {
        em.merge(book);
    }

    @Override
    public void deleteBook(int bookId) {
        Book book = em.find(Book.class, bookId);
        if(book != null) {
            em.remove(book);
        }
    }

    @Override
    public Book getBookById(int bookId) {
        return em.find(Book.class, bookId);
    }

    @Override
    public List<Book> getAllBooks() {
        return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    @Override
    public List<Booktype> getAllBookTypes() {
        return em.createQuery("SELECT bt FROM Booktype bt", Booktype.class).getResultList();
    }
    @Override
    public List<Book> getBooksByType(int typeId) {
    return em.createQuery("SELECT b FROM Book b WHERE b.booktype.id = :typeId", Book.class)
             .setParameter("typeId", typeId)
             .getResultList();
    }
    
    
    //City Logic
    @Override
    public void addCity(City city) {
        em.persist(city);
    }

    @Override
    public void updateCity(City city) {
        em.merge(city);
    }

    @Override
    public void deleteCity(int cityid) {
        City c = em.find(City.class, cityid);
        if (c != null) {
            em.remove(c);
        }
    }

    @Override
    public City getCityById(int cityid) {
        return em.find(City.class, cityid);
    }

    @Override
    public List<City> getAllCities() {
        return em.createQuery("SELECT c FROM City c", City.class).getResultList();
    }
}
