package com.petistaan.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "domestic_pet_table")
@Getter
@Setter
public class DomesticPet extends Pet {

	@Column(name = "date_of_birth", nullable = false)
	private LocalDate birthDate;

}
