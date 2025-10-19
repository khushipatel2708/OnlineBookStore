package EJB;

import Entity.Book;
import Entity.Booktype;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BookSessionBean implements BookSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;

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

}
