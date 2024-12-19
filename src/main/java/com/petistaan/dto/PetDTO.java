package com.petistaan.dto;

import java.util.List;

import com.petistaan.enums.Gender;
import com.petistaan.enums.PetType;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class PetDTO {

	@EqualsAndHashCode.Include
	private int id;

	private String name;

	private Gender gender;

	private PetType type;

	@ToString.Include
	private int numberOfOwners() {
		return ownerDTOList != null ? ownerDTOList.size() : 0;
	}

	private List<OwnerDTO> ownerDTOList;

	@Override
	public String toString() {
		if (ownerDTOList != null) {
			return "PetDTO (id=" + id + ", name=" + name + ", gender=" + gender + ", type=" + type + ", numberOfOwners="
					+ numberOfOwners() + ", ownerDTOList=" + ownerDTOList + ")";
		} else {
			return "PetDTO (id=" + id + ", name=" + name + ", gender=" + gender + ", type=" + type + ")";
		}
	}

}
