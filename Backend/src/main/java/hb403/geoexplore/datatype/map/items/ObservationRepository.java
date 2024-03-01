package hb403.geoexplore.datatype.map.items;

import hb403.geoexplore.datatype.*;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ObservationRepository extends LocatableRepository<ObservationEntity> {
	
	ObservationEntity findById(int id);
	void deleteById(int id);

	@Query(value = "SELECT p from ObservationEntity p WHERE within(p.point, :bounds) = true")
	public List<ObservationEntity> findWithin(@Param("bounds") Geometry bounds);


}
