package server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.model.storage.RaspberryInfo;
import server.model.storage.User;
import server.repos.UserRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@CrossOrigin
@RestController
@RequestMapping(value = "/get")
public class GetController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    UserRepo userRepo;


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(@RequestParam(value="email") String email) {

        return userRepo.getUserByEmail(email);
    }

    @RequestMapping(value = "/user_by_id", method = RequestMethod.GET)
    public User getUserById (@RequestParam(value="id") long id) {

        return em.find(User.class, id);
    }

    @RequestMapping(value = "/get_raspberry_info_by_user_id", method = RequestMethod.GET)
    public RaspberryInfo getRaspberryInfo (@RequestParam(value="id") long id) {

        return em.find(User.class, id).raspberryInfo;
    }

}
