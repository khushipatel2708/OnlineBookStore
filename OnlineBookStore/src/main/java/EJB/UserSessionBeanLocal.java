/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entities.User;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author KHUSHI PC
 */
@Local
public interface UserSessionBeanLocal {
    void registerUser(User user);
    User login(String username, String password);
    List<User> getAllUsers();
}
