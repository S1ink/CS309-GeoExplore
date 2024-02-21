package hb403.geoexplore.datatype.map.items;

import hb403.geoexplore.datatype.*;
import jakarta.persistence.*;


@Entity
@Table(name = "geomap_events")
public class ObservationEntity extends LocationBase {
	
	public static class JsonFormat {

		public long
			id;
		public double
			latitude,
			longitude;
		public String
			title;

	}


}
