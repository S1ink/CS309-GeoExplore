package hb403.geoexplore.datatype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PointRepository extends JpaRepository<PointEntity, Long> {}
