package com.petistaan.service;

import java.util.List;

import com.petistaan.dto.PetDTO;
import com.petistaan.enums.PetType;
import com.petistaan.exception.PetNotFoundException;

public interface PetService {

	PetDTO findByid(int petId, boolean loadOwners) throws PetNotFoundException;

	List<PetDTO> findAll(boolean loadOwners);

	void deleteById(int petId) throws PetNotFoundException;

	double calculatePetAverageAge();

	List<PetDTO> findAllSortedPets(String sortingParameter, boolean sortDescending, boolean loadOwners);

	List<PetDTO> findAllPaginatedPets(int pageNumber, int pageSize, boolean loadOwners);

	List<PetDTO> findAllPaginatedAndSortedPets(int pageNumber, int pageSize, String sortingParameter,
			boolean sortDescending, boolean loadOwners);

	List<PetDTO> findByType(PetType petType, boolean loadOwners);

	List<PetDTO> findByOwnerId(int ownerId, boolean loadOwners);

}
