package hb403.geoexplore.datatype.map.items;

import hb403.geoexplore.datatype.*;
import jakarta.persistence.*;
import org.springframework.core.style.ToStringCreator;
import org.locationtech.jts.geom.*;

@Entity
@Table(name = "geomap_observations")
public class ObservationEntity extends LocationBase {

	protected String title, description;


	public static class JsonFormat {

		public long
			id;
		public double
			latitude,
			longitude;
		public String
			title;

		public String description;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		@Override
		public String toString(){
			return new ToStringCreator(this)
					.append("id", this.id)
					.append("lat", this.latitude)
					.append("long",this.longitude)
					.append("title", this.title)
					.append("description",this.description)
					.toString();
		}

	}

	public static JsonFormat formatJson(ObservationEntity entity)
	{ return formatJson(entity, null); }
	public static JsonFormat formatJson(ObservationEntity entity, JsonFormat buff) {

		if(entity == null) return null;
		final JsonFormat fmt = (buff == null) ? new JsonFormat() : buff;
		fmt.id = entity.id;
		fmt.latitude = entity.point.getX();
		fmt.longitude = entity.point.getY();
		fmt.title = entity.title;
		fmt.description = entity.description;
		return fmt;

	}
	public static ObservationEntity fromJson(JsonFormat fmt)
	{ return fromJson(fmt, null); }
	public static ObservationEntity fromJson(JsonFormat fmt, ObservationEntity buff) {

		if(fmt == null) return null;
		final ObservationEntity entity = (buff == null) ? new ObservationEntity() : buff;
		entity.id = fmt.id;
		entity.point = new Point(new Coordinate(fmt.latitude, fmt.longitude), new PrecisionModel(), 0);
		entity.title = fmt.title;
		entity.description = fmt.description;
		return entity;

	}

}
