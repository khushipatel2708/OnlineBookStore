package Entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "group_master")
public class GroupMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Group_id")
    private int groupId;

    @Column(name = "Group_name")
    private String groupName;

    @Column(name = "Username")
    private String username;

    @OneToMany(mappedBy = "groupMaster")
    private List<User> users;

    // Getters & Setters
    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}