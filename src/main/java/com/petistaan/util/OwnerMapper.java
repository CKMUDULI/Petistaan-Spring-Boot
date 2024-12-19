package com.petistaan.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.petistaan.dto.OwnerDTO;
import com.petistaan.entity.Owner;

@Mapper(componentModel = "spring", uses = PetMapper.class)
public interface OwnerMapper {

	OwnerMapper INSTANCE = Mappers.getMapper(OwnerMapper.class);

	@Mapping(target = "petDTOList", source = "petList", qualifiedByName = "toDTOWithoutOwner")
	OwnerDTO toDTO(Owner owner);

	@Mapping(target = "petDTOList", ignore = true)
	@Named("toDTOWithoutPets")
	OwnerDTO toDTOWithoutPets(Owner owner);

	@Mapping(target = "petList", source = "petDTOList")
	Owner toEntity(OwnerDTO ownerDTO);

}
