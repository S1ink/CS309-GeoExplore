package hb403.geoexplore.datatype.map.items;

import hb403.geoexplore.datatype.*;

import org.springframework.stereotype.Repository;


@Repository
public interface ReportRepository extends LocatableRepository<ReportEntity> {
	
	ReportEntity findById(int id);
	void deleteById(int id);


}
