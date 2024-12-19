package com.petistaan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "wild_pet_table")
@Getter
@Setter
public class WildPet extends Pet {

	@Column(name = "place_of_birth", nullable = false)
	private String birthPlace;

}
