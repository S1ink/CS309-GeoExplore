package org.springframework.samples.petclinic.pet;

import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.owner.Owners;
import org.hibernate.annotations.*;
import jakarta.persistence.*;
import jakarta.persistence.Table;


@Entity
@Table(name = "pets")
public class Pet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	protected Integer id;

	@Column(name = "name")
	@NotFound(action = NotFoundAction.IGNORE)
	protected String name;

	@Column(name = "nickname")
	@NotFound(action = NotFoundAction.IGNORE)
	protected String nickname;

	@Column(name = "type")
	@NotFound(action = NotFoundAction.IGNORE)
	protected String type;

	@Column(name = "age")
	@NotFound(action = NotFoundAction.IGNORE)
	protected Integer age;

	@Column(name = "owner_id")
	@NotFound(action = NotFoundAction.IGNORE)
	protected Integer owner_id;


	public Pet() {}
	public Pet(int id, String name, String nickname, String type, int age, int owner_id) {
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.type = type;
		this.age = age;
		this.owner_id = owner_id;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("id", this.id)
			.append("name", this.name)
			.append("nickname", this.nickname)
			.append("type", this.type)
			.append("age", this.age)
			.append("ownerId", this.owner_id)
		.toString();
	}


}
