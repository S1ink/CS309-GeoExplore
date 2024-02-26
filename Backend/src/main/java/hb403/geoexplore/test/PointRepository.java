package hb403.geoexplore.test;

import org.springframework.stereotype.Repository;

import hb403.geoexplore.datatype.LocatableRepository;


@Repository
public interface PointRepository extends LocatableRepository<PointEntity> {

	PointEntity findById(int id);
	void deleteById(int id);


}
