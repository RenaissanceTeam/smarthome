package server.model.storage;

import lombok.Data;
import server.model.Entity;

@Data
@javax.persistence.Entity
public class RaspberryInfo extends Entity {

    public String ip;

    public String port;

}
