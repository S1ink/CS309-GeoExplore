package hb403.geoexplore.datatype;

import java.util.Date;

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

	@Temporal(TemporalType.TIMESTAMP)	// the make more sense in a "base marker" class
	private Date add_tstamp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_tstamp;

	@Lob
	@JsonSerialize(using = GeometrySerializer.class)
	@JsonDeserialize(using = GeometryDeserializer.class)
	protected Point point;



	public long getId() { return this.id; }
	public void nullId() { this.id = (long)-1; }
	public void setId(long id) { this.id = id; }


	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("id", this.id)
			.append("point", this.point.toString())
		.toString();
	}


}
