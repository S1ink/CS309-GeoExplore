package hb403.geoexplore.datatype.marker;

import hb403.geoexplore.UserStorage.entity.User;
// import hb403.geoexplore.datatype.MarkerTag;
import hb403.geoexplore.comments.Entity.CommentEntity;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "geomap_observations")
@Getter
@Setter
public class ObservationMarker extends MarkerBase {

	@ManyToMany(
		fetch = FetchType.EAGER,
		cascade = { CascadeType.PERSIST, CascadeType.MERGE }
	)
	@JoinTable(
		name = "observation_confirmations",		// the name of the intermediate table that links this entity and the target entity (NEW)
		joinColumns = {
			@JoinColumn(
				name = "observation_id_linked",		// the name of the column in the intermediate table that links to the primary key (NEW)
				referencedColumnName="marker_id"	// the name of the column in the owning entity table that this column links to (REFERENCED)
			)
		},
		inverseJoinColumns = {
			@JoinColumn(
				name = "user_id_linked",		// the name of the column in the intermediate table that links to the non-owning key (NEW)
				referencedColumnName="user_id"	// the name of the column in the non-owning entity table for which this column links to (REFERENCED)
			)
		}
	)
	private Set<User> confirmed_by = new HashSet<>();



	@Getter
	@Setter
	@OneToMany(mappedBy = "pertainsObservationMarker", fetch = FetchType.EAGER)
	private List<CommentEntity> Comments;


}
