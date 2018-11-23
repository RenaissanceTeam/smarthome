package server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import server.model.storage.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


@CrossOrigin
@Transactional
@RestController
@RequestMapping(value = "/delete")
public class DeleteController {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager em;

    /**
     * Delete all user's info method
     * @param id user's id
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    public void material (@RequestParam(value="id") long id) {

        User instance = em.find(User.class, id);

        if(instance != null) {
            em.remove(instance.raspberryInfo);
            em.remove(instance);
        }

        em.flush();
    }

}
