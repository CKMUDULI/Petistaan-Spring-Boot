package com.petistaan.dto;

import java.time.LocalDate;
import java.util.List;

import com.petistaan.enums.Gender;
import com.petistaan.enums.PetType;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DomesticPetDTO extends PetDTO {

	private LocalDate birthDate;

	@Builder
	public DomesticPetDTO(int id, String name, Gender gender, PetType type, List<OwnerDTO> ownerDTOList,
			LocalDate birthDate) {
		super(id, name, gender, type, ownerDTOList);
		this.birthDate = birthDate;
	}

}
