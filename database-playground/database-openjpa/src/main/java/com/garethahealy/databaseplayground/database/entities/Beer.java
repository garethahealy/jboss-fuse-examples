package com.garethahealy.databaseplayground.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@Table(name = "Beer")
@NamedNativeQueries({
	@NamedNativeQuery(name = "1", query = "{call Somet(?,?,?,?)}")
})
public class Beer {

	private Integer id;
	
	public Beer() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

}