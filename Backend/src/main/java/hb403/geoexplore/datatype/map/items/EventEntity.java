package hb403.geoexplore.datatype.map.items;

import hb403.geoexplore.datatype.*;

import org.springframework.core.style.ToStringCreator;

import org.locationtech.jts.geom.*;

import jakarta.persistence.*;


@Entity
@Table(name = "geomap_events")
public class EventEntity extends LocationBase {

	// from LocationBase: id (Long), point (Point)

	protected String
		title,
		city_department;
	
	public static class JsonFormat {

		public long
			id;
		public double
			latitude,
			longitude;
		public String
			title,
			city_department;

		@Override
		public String toString() {
			return new ToStringCreator(this)
				.append("id", this.id)
				.append("lat", this.latitude)
				.append("long", this.longitude)
				.append("title", this.title)
				.append("department", this.city_department)
			.toString();
		}

	}


	public static JsonFormat formatJson(EventEntity entity)
		{ return formatJson(entity, null); }
	public static JsonFormat formatJson(EventEntity entity, JsonFormat buff) {

		if(entity == null) return null;
		final JsonFormat fmt = (buff == null) ? new JsonFormat() : buff;
		fmt.id = entity.id;
		fmt.latitude = entity.point.getX();
		fmt.longitude = entity.point.getY();
		fmt.title = entity.title;
		fmt.city_department = entity.city_department;
		return fmt;

	}
	public static EventEntity fromJson(JsonFormat fmt)
		{ return fromJson(fmt, null); }
	public static EventEntity fromJson(JsonFormat fmt, EventEntity buff) {

		if(fmt == null) return null;
		final EventEntity entity = (buff == null) ? new EventEntity() : buff;
		entity.id = fmt.id;
		entity.point = new Point(new Coordinate(fmt.latitude, fmt.longitude), new PrecisionModel(), 0);
		entity.title = fmt.title;
		entity.city_department = fmt.city_department;
		return entity;

	}


}
