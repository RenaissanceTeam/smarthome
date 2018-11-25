package server.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.model.storage.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User getUserByEmail(String email);
}
