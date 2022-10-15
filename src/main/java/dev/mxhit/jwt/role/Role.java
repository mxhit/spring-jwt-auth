package dev.mxhit.jwt.role;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Role")
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = AUTO)
	Long id;
	
	String name;

	/**
	 * 
	 */
	public Role() {
	}

	/**
	 * @param id
	 * @param name
	 */
	public Role(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
