package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import server.model.storage.RaspberryInfo;
import server.model.storage.User;
import server.repos.UserRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


@CrossOrigin
@Transactional
@RestController
@RequestMapping(value = "/post")
public class PostController {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager em;

    @Autowired
    ErrorAttributes errorAttributes;

    @Autowired
    UserRepo userRepo;


    /**
     * Full user's data update method<br>
     * For raspberry info update the {@code updateRaspberryInfo} method should be used
     * @param user
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/update_user", method = RequestMethod.POST)
    public void updateUser(@RequestBody User user) {

        User instance = em.find(User.class, user.GUID);

        User newInstance = new User(user);

        if (instance != null) {
            em.merge(newInstance.raspberryInfo);
            em.merge(newInstance);

        } else
            em.persist(newInstance.raspberryInfo);
        em.persist(newInstance);

        em.flush();
    }

    /**
     * Create user method<br>
     * Use this method after user's first authorization in the client app
     * @param user
     * @return user's guid
     * @see User
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/create_user", method = RequestMethod.POST)
    public Long createUser(@RequestBody User user) {

        User existing = userRepo.getUserByEmail(user.email);

        if (existing != null)
            return existing.GUID;

        User instance = new User(user);

        if(instance.email == null)
            throw new IllegalArgumentException("Can't create user without email");

        if(instance.email.isEmpty())
            throw new IllegalArgumentException("Can't create user without email");

        em.persist(instance.raspberryInfo);
        em.persist(instance);

        em.flush();

        return instance.GUID;
    }

    /**
     * Update raspberry info method
     * @param id user's id
     * @param info new raspberry info
     * @see RaspberryInfo
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/update_raspberry_info", method = RequestMethod.POST)
    public void updateRaspberryInfo(@RequestParam(value = "id") long id,
                           @RequestBody RaspberryInfo info) {

        User user = em.find(User.class, id);

        if(user == null)
            throw new IllegalArgumentException("Can't find user with provided id");

        user.raspberryInfo = info;

    }

}
