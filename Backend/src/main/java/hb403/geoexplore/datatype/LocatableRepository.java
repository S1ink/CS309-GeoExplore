package hb403.geoexplore.datatype;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import org.locationtech.jts.geom.Geometry;


@NoRepositoryBean
public interface LocatableRepository<Entity extends LocationBase> extends JpaRepository<Entity, Long> {

	// this is now worthless
	// @Query(value = "SELECT p from Entity p WHERE within(p.point, :bounds) = true")
	// public List<Entity> findWithin(@Param("bounds") Geometry bounds);


}
