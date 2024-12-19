package com.petistaan.repository;

import java.util.List;

import com.petistaan.entity.Pet;

public interface CustomizedPetRepository {

	List<Pet> findByOwnerId(int ownerId);

}
