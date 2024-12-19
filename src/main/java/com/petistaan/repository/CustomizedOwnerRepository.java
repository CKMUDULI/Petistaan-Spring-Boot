package com.petistaan.repository;

import java.util.List;

import com.petistaan.entity.Owner;
import com.petistaan.enums.PetType;

public interface CustomizedOwnerRepository {

	List<Owner> findByPetNameContainingString(String petName);

	List<Owner> findByPetType(PetType petType);

}
