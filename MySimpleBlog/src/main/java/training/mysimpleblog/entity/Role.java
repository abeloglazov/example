package training.mysimpleblog.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * Entity class, that represents role of user in applications. Contains list of
 * {@link User} of given role.
 * 
 * @author Aleksejs Beloglazovs
 */
@Entity
@Table(name = "\"ROLE\"")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private List<User> users;

    public Role() {
    }

    @Id
    @SequenceGenerator(name = "ROLE_ID_GENERATOR", 
                        sequenceName = "ROLE_ID_SEQ", 
                        initialValue = 1, 
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ID_GENERATOR")
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // bi-directional many-to-one association to User
    @OneToMany(mappedBy = "role")
    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (id != other.id)
            return false;
        return true;
    }

}