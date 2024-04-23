package hb403.geoexplore.datatype;

import hb403.geoexplore.util.GeometryUtil;

import org.locationtech.jts.geom.Polygon;


public class LocationRange {
	
	public Double
		min_latitude,
		min_longitude,
		max_latitude,
		max_longitude;


	public boolean isValid() {
		return (
			this.min_latitude != null &&
			this.min_longitude != null &&
			this.max_latitude != null &&
			this.max_longitude != null
		);
	}
	public boolean isInvalid() {
		return (
			this.min_latitude == null ||
			this.min_longitude == null ||
			this.max_latitude == null ||
			this.max_longitude == null
		);
	}
	public Polygon getRect() {
		if(this.isInvalid()) return null;
		return GeometryUtil.makeRectangle(
			this.min_latitude,
			this.max_latitude,
			this.min_longitude,
			this.max_longitude
		);
	}

}
