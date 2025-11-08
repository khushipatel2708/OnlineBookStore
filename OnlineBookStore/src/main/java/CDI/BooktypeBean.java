/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package CDI;

import EJB.AdminSessionBeanLocal;
import Entity.Booktype;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author KHUSHI PC
 */
@Named(value = "booktypeBean")
@SessionScoped
public class BooktypeBean implements Serializable {

    @EJB
    private AdminSessionBeanLocal booktypeEJB;

    private Booktype booktype = new Booktype(); // For form binding
    private List<Booktype> booktypeList;

    // --- CREATE OR UPDATE ---
    public String save() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (booktype.getType() == null || booktype.getType().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type is required", null));
            return null;
        }

        boolean result;
        if (booktype.getId() == null) {
            result = booktypeEJB.create(booktype);
        } else {
            result = booktypeEJB.update(booktype);
        }

        if (result) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved successfully", null));
            booktype = new Booktype(); // Reset form
            return "bookList.xhtml?faces-redirect=true"; // Redirect to list page
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving", null));
            return null;
        }
    }

    // --- EDIT ---
    public String edit(Integer id) {
        booktype = booktypeEJB.find(id);
        return "BookTypeForm.xhtml?faces-redirect=true";
    }

    // --- DELETE ---
    public void delete(Integer id) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (booktypeEJB.delete(id)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted successfully", null));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error deleting", null));
        }
    }

    // --- GET LIST ---
    public List<Booktype> getBooktypeList() {
        booktypeList = booktypeEJB.findAll();
        return booktypeList;
    }

    // --- GETTERS & SETTERS ---
    public Booktype getBooktype() { return booktype; }
    public void setBooktype(Booktype booktype) { this.booktype = booktype; }
}
