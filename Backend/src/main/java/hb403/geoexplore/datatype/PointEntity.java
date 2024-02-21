package hb403.geoexplore.datatype;

import org.locationtech.jts.geom.Point;
import jakarta.persistence.*;


@Entity
@Table(name = "points")
public class PointEntity extends LocationBase {

	public PointEntity() {}
	public PointEntity(Point pt) {
		this.point = pt;
	}
	public PointEntity(Long id, Point pt) {
		this.id = id;
		this.point = pt;
	}

	public Point getPoint() { return this.point; }
	public void updatePoint(Point pt) { if(pt != null) this.point = pt; }


}
