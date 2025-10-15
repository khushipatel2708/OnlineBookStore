/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author 91931
 */
@Entity
@Table(name = "group_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupMaster.findAll", query = "SELECT g FROM GroupMaster g"),
    @NamedQuery(name = "GroupMaster.findByGroupid", query = "SELECT g FROM GroupMaster g WHERE g.groupid = :groupid"),
    @NamedQuery(name = "GroupMaster.findByGroupname", query = "SELECT g FROM GroupMaster g WHERE g.groupname = :groupname"),
    @NamedQuery(name = "GroupMaster.findByUsername", query = "SELECT g FROM GroupMaster g WHERE g.username = :username")})
public class GroupMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Group_id")
    private Integer groupid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Group_name")
    private String groupname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Username")
    private String username;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupid")
    private Collection<User> userCollection;

    public GroupMaster() {
    }

    public GroupMaster(Integer groupid) {
        this.groupid = groupid;
    }

    public GroupMaster(Integer groupid, String groupname, String username) {
        this.groupid = groupid;
        this.groupname = groupname;
        this.username = username;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupid != null ? groupid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupMaster)) {
            return false;
        }
        GroupMaster other = (GroupMaster) object;
        if ((this.groupid == null && other.groupid != null) || (this.groupid != null && !this.groupid.equals(other.groupid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.GroupMaster[ groupid=" + groupid + " ]";
    }
    
}
