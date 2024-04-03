package hb403.geoexplore.datatype.marker;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.datatype.MarkerTag;

import java.util.*;

import org.locationtech.jts.geom.*;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.*;
import lombok.*;


/** MarkerBase represents the base data used in all marker types */
@MappedSuperclass
@Getter
@Setter
public abstract class MarkerBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "marker_id")
	protected Long id = -1L;

	@Column()
	protected String title;
	@Column()
	protected String description;

	@Lob
	@JsonSerialize(using = GeometrySerializer.class)
	@JsonDeserialize(using = GeometryDeserializer.class)
	@JsonIgnore
	@Column()
	protected Point location;			// lat/long as stored in the tables -- not serialized to json (@JsonIgnore)

	@Transient
	protected Double io_latitude = 0.0;		// lat as serialized/deserialized -- not stored in the tables (@Transient)
	@Transient
	protected Double io_longitude = 0.0;		// long as serialize/deserialized -- not stored in the tables (@Transient)

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })	// caused an error - might have to implement per-entity
	@JoinColumn(name = "creator_user_id", referencedColumnName = "user_id")
	protected User creator;		// jsonignore (just send id?)

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column()
	protected Date time_created = new Date();	// default ts of whenever constructed
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column()
	protected Date time_updated = new Date();

	@Column()
	protected String meta;

	@ManyToMany(
		fetch = FetchType.EAGER,
		cascade = { CascadeType.PERSIST, CascadeType.MERGE }
	)
	@JoinTable(
		// name = "marker_linked_tags",		// the name of the intermediate table that links this entity and the target entity (NEW)
		joinColumns = {
			@JoinColumn(
				name = "marker_id_linked",		// the name of the column in the intermediate table that links to the primary key (NEW)
				referencedColumnName="marker_id"	// the name of the column in the owning entity table that this column links to (REFERENCED)
			)
		},
		inverseJoinColumns = {
			@JoinColumn(
				name = "tag_id_linked",		// the name of the column in the intermediate table that links to the non-owning key (NEW)
				referencedColumnName="tag_id"	// the name of the column in the non-owning entity table for which this column links to (REFERENCED)
			)
		}
	)
	protected Set<MarkerTag> tags;



	public void nullifyId() {
		this.id = -1L;
	}

	/** Synchronize the stored table location and IO lat/long values (copies from the IO variables */
	public void enforceLocationIO() {
		this.location = new Point(new Coordinate(this.io_latitude, this.io_longitude), new PrecisionModel(), 0);
	}
	/** Synchronize the stored table location and IO lat/long values (copies from the table entry) */
	public void enforceLocationTable() {
		if(this.location != null) {
			this.io_latitude = this.location.getX();
			this.io_longitude = this.location.getY();
		}
	}

	public void applyNewTimestamp() {
		this.time_created = new Date();
		this.applyUpdatedTimestamp();
	}
	public void applyUpdatedTimestamp() {
		this.time_updated = new Date();
	}



}
