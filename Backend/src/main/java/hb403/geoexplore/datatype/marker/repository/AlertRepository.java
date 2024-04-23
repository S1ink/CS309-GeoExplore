package hb403.geoexplore.datatype.marker.repository;

import hb403.geoexplore.datatype.marker.AlertMarker;

import java.util.Set;

import org.locationtech.jts.geom.Geometry;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;


@Repository
public interface AlertRepository extends JpaRepository<AlertMarker, Long> {

	@Query(value = "SELECT m from AlertMarker m WHERE within(m.location, :bounds) = true")
	public Set<AlertMarker> findWithin(@Param("bounds") Geometry bounds);


}
