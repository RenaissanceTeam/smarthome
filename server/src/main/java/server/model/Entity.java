package server.model;

import lombok.Data;
import server.utils.GuidGenerator;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@Data
@MappedSuperclass
public class Entity {

    @Id
    public long GUID;

    public Entity(){
        GUID = new GuidGenerator().issuenewguid();
    }

}
