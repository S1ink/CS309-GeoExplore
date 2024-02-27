package hb403.geoexplore.UserStorage.repository;

import hb403.geoexplore.UserStorage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
