package hb403.geoexplore.datatype.map.items;

import hb403.geoexplore.datatype.*;

import org.springframework.core.style.ToStringCreator;

import org.locationtech.jts.geom.*;

import jakarta.persistence.*;


@Entity
@Table(name = "geomap_reports")
public class ReportEntity extends LocationBase {

	// from LocationBase: id (Long), point (Point)

	protected String title;


	/** Data format for sending/recieving from REST controllers */
	public static class JsonFormat {

		public long
			id;
		public double
			latitude,
			longitude;
		public String
			title;

		@Override
		public String toString() {
			return new ToStringCreator(this)
				.append("id", this.id)
				.append("lat", this.latitude)
				.append("long", this.longitude)
				.append("title", this.title)
			.toString();
		}

	}


	public static JsonFormat formatJson(ReportEntity entity)
		{ return formatJson(entity, null); }
	public static JsonFormat formatJson(ReportEntity entity, JsonFormat buff) {

		if(entity == null) return null;
		final JsonFormat fmt = (buff == null) ? new JsonFormat() : buff;
		fmt.id = entity.id;
		fmt.latitude = entity.point.getX();
		fmt.longitude = entity.point.getY();
		fmt.title = entity.title;
		return fmt;

	}
	public static ReportEntity fromJson(JsonFormat fmt)
		{ return fromJson(fmt, null); }
	public static ReportEntity fromJson(JsonFormat fmt, ReportEntity buff) {

		if(fmt == null) return null;
		final ReportEntity entity = (buff == null) ? new ReportEntity() : buff;
		entity.id = fmt.id;
		entity.point = new Point(new Coordinate(fmt.latitude, fmt.longitude), new PrecisionModel(), 0);
		entity.title = fmt.title;
		return entity;

	}


}
