package hb403.geoexplore.datatype;

// import hb403.geoexplore.UserStorage.entity.User;

import jakarta.persistence.*;
import lombok.*;


/** Tag represents a tag that can be applied to any marker type (used for filtering). TODO: finish this when we have all *revamped* marker types */
@Entity
@Table(name = "tags")
@Getter
@Setter
public class Tag {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private Long id = -1L;

	@Column()
	private String name;


}
