package hb403.geoexplore.UserStorage.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "usergroups")
@Getter
@Setter
public class UserGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_id")
	private Long id;

	private String title;

	@ManyToMany(
		fetch = FetchType.EAGER,
		cascade = { CascadeType.PERSIST, CascadeType.MERGE }
	)
	@JoinTable(
		name = "group_members",		// the name of the intermediate table that links users and groups (NEW)
		joinColumns = {
			@JoinColumn(
				name = "group_id_linked",		// the name of the column in the intermediate table that links to the primary key (NEW)
				referencedColumnName="group_id"	// the name of the column in the owning entity table that this column links to (REFERENCED)
			)
		},
		inverseJoinColumns = {
			@JoinColumn(
				name = "member_id_linked",		// the name of the column in the intermediate table that links to the non-owning key (NEW)
				referencedColumnName="user_id"	// the name of the column in the non-owning entity table for which this column links to (REFERENCED)
			)
		}
	)
	private Set<User> members = new HashSet<>();

	// group owner? -- this will make the User class a little messy
	// tags for filtering?


	public UserGroup() {}

}

