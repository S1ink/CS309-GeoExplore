package hb403.geoexplore.data;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class PointEntity {

	@Id
	@GeneratedValue
	private Long id;

	private Point point;


}
