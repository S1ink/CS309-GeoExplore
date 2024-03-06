package hb403.geoexplore.UserStorage.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "user-groups")
@Data
public class UserGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "user_groups", joinColumns = { @JoinColumn(name = "usergroup_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private Set<User> members = new HashSet<>();


	public UserGroup() {}

}
