package hb403.geoexplore.test;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hb403.geoexplore.datatype.LocatableRepository;


// @Repository
public interface PointRepository /*extends LocatableRepository<PointEntity>*/ {

	// PointEntity findById(int id);
	// void deleteById(int id);

	// @Query(value = "SELECT p from PointEntity p WHERE within(p.point, :bounds) = true")
	// public List<PointEntity> findWithin(@Param("bounds") Geometry bounds);


}
