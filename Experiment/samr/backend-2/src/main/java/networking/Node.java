package networking;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.style.ToStringCreator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "nodes")
public class Node {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected Integer id;

	// @OneToMany(fetch = FetchType.LAZY,
	// 		cascade = {
	// 			CascadeType.PERSIST,
	// 			CascadeType.MERGE
	// 		},
	// 		mappedBy = "adjacent")
	// protected Set<Node> adjacent = new HashSet<>();

	public Node() {
		this.id = -1;
	}
	public Node(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.id).toString();
	}

}
