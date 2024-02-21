package hb403.geoexplore.datatype;

import org.springframework.core.style.ToStringCreator;

import org.locationtech.jts.geom.Point;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.*;


/** Make sure to specify derived classes as [@Entity]! */
@MappedSuperclass
public abstract class LocationBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Lob
	@JsonSerialize(using = GeometrySerializer.class)
	@JsonDeserialize(using = GeometryDeserializer.class)
	protected Point point;



	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("id", this.id)
			.append("point", this.point.toString())
		.toString();
	}


}
