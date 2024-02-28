package hb403.geoexplore.datatype;

import org.springframework.stereotype.Repository;


@Repository
public interface PointRepository extends LocatableRepository<PointEntity> {

	PointEntity findById(int id);
	void deleteById(int id);


}
