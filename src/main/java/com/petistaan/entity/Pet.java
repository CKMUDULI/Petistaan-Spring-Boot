package com.petistaan.entity;

import java.util.HashSet;
import java.util.Set;

import com.petistaan.enums.Gender;
import com.petistaan.enums.PetType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pet_table")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Pet extends Base {

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "gender", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Gender gender;

	@Column(name = "type", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private PetType type;

	@ManyToMany(mappedBy = "petList")
	private Set<Owner> ownerList = new HashSet<>();

}
