package hb403.geoexplore.datatype.map.items;

import hb403.geoexplore.datatype.*;
import org.springframework.stereotype.Repository;


@Repository
public interface EventRepository extends LocatableRepository<EventEntity> {
	
	EventEntity findById(int id);
	void deleteById(int id);


}
