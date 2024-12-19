package com.petistaan.entity;

import java.util.HashSet;
import java.util.Set;

import com.petistaan.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "owner_table")
@Getter
@Setter
public class Owner extends Base {

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "gender", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Gender gender;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "state", nullable = false)
	private String state;

	@Column(name = "mobile_number", nullable = false, unique = true, length = 10)
	private String mobileNumber;

	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "owner_pet_table", joinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "pet_id", referencedColumnName = "id", nullable = false))
	private Set<Pet> petList = new HashSet<>();

}
