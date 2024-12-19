package com.petistaan.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.petistaan.dto.DomesticPetDTO;
import com.petistaan.dto.PetDTO;
import com.petistaan.dto.WildPetDTO;
import com.petistaan.entity.DomesticPet;
import com.petistaan.entity.Pet;
import com.petistaan.entity.WildPet;

@Mapper(componentModel = "spring", uses = OwnerMapper.class)
public interface PetMapper {

	PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

	default PetDTO toDto(Pet pet) {
		return switch (pet) {
		case DomesticPet domesticPet -> toDomesticPetDTO(domesticPet);
		case WildPet wildPet -> toWildPetDTO(wildPet);
		default -> throw new IllegalArgumentException("Unsupported Pet type: " + pet.getClass());
		};
	}

	@Mapping(target = "ownerDTOList", source = "ownerList", qualifiedByName = "toDTOWithoutPets")
	DomesticPetDTO toDomesticPetDTO(DomesticPet domesticPet);

	@Mapping(target = "ownerDTOList", source = "ownerList", qualifiedByName = "toDTOWithoutPets")
	WildPetDTO toWildPetDTO(WildPet wildPet);

	@Named("toDTOWithoutOwner")
	default PetDTO toDTOWithoutOwner(Pet pet) {
		return switch (pet) {
		case DomesticPet domesticPet -> toDomesticPetDTOWithoutOwner(domesticPet);
		case WildPet wildPet -> toWildPetDTOWithoutOwner(wildPet);
		default -> throw new IllegalArgumentException("Unsupported Pet type: " + pet.getClass());
		};
	}

	@Mapping(target = "ownerDTOList", ignore = true)
	DomesticPetDTO toDomesticPetDTOWithoutOwner(DomesticPet pet);

	@Mapping(target = "ownerDTOList", ignore = true)
	WildPetDTO toWildPetDTOWithoutOwner(WildPet pet);

	default Pet toEntity(PetDTO petDTO) {
		return switch (petDTO) {
		case DomesticPetDTO domesticPetDTO -> toDomesticPet(domesticPetDTO);
		case WildPetDTO wildPetDTO -> toWildPet(wildPetDTO);
		default -> throw new IllegalArgumentException("Unsupported PetDTO type: " + petDTO.getClass());
		};
	}

	@Mapping(target = "ownerList", source = "ownerDTOList")
	DomesticPet toDomesticPet(DomesticPetDTO domesticPetDTO);

	@Mapping(target = "ownerList", source = "ownerDTOList")
	WildPet toWildPet(WildPetDTO wildPetDTO);

}
