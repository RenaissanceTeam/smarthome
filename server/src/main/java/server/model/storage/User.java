package server.model.storage;

import lombok.Data;
import server.model.Entity;

import javax.persistence.OneToOne;

@Data
@javax.persistence.Entity
public class User extends Entity {

    public String email;

    @OneToOne
    public RaspberryInfo raspberryInfo;

    public User () {}

    public User(String email, RaspberryInfo raspberryInfo) {
        this.email = email;
        this.raspberryInfo = raspberryInfo;
    }

    public User (User other) {
        this(other.email, other.raspberryInfo);
    }
}
