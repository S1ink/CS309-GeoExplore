package hb403.geoexplore.UserStorage.repository;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.datatype.map.items.ObservationEntity;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id = :id")
    User getUserByid(@Param("id")Long id);
}
