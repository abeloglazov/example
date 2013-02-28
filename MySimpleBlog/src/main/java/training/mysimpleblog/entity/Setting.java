package training.mysimpleblog.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity class, that represents a single setting of application.
 * 
 * @author Aleksejs Beloglazovs
 */
@Entity
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String value;

    public Setting() {
    }

    @Id
    @SequenceGenerator(name = "SETTING_ID_GENERATOR", 
                        sequenceName = "SETTING_ID_SEQ", 
                        initialValue = 1, 
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SETTING_ID_GENERATOR")
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

    @Column(name = "\"VALUE\"")
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
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
        Setting other = (Setting) obj;
        if (id != other.id)
            return false;
        return true;
    }

}