package com.petistaan.service;

import java.util.List;

import com.petistaan.dto.OwnerDTO;
import com.petistaan.dto.PetDTO;
import com.petistaan.enums.PetType;
import com.petistaan.exception.OwnerNotFoundException;
import com.petistaan.exception.OwnerPetCombinationNotFoundException;
import com.petistaan.exception.PetNotFoundException;

public interface OwnerService {

	OwnerDTO save(OwnerDTO ownerDTO);

	OwnerDTO findById(int ownerId, boolean loadPets) throws OwnerNotFoundException;

	OwnerDTO updatePetName(int ownerId, int petId, String petName)
			throws OwnerNotFoundException, OwnerPetCombinationNotFoundException;

	void deleteById(int ownerId) throws OwnerNotFoundException;

	void deleteByIds(List<Integer> ownerIds) throws OwnerNotFoundException;

	OwnerDTO addNewPetToExistingOwner(int ownerId, PetDTO petDTO) throws OwnerNotFoundException;

	List<OwnerDTO> findAll(boolean loadPets);

	OwnerDTO addCoOwnerToPet(int petId, OwnerDTO ownerDTO) throws PetNotFoundException;

	OwnerDTO addCoOwnerToPet(int petId, int ownerId) throws PetNotFoundException, OwnerNotFoundException;

	List<OwnerDTO> findByInitialsOfFirstName(String initials, boolean loadPets);

	List<OwnerDTO> findAllSortedOwners(String sortingParameter, boolean sortDescending, boolean loadPets);

	List<OwnerDTO> findAllPaginatedOwners(int pageNumber, int pageSize, boolean loadPets);

	List<OwnerDTO> findAllPaginatedAndSortedOwners(int pageNumber, int pageSize, String sortingParameter,
			boolean sortDescending, boolean loadPets);

	List<Object[]> handleFetchIdAndFirstNameAndLastNameAndPetIdAndPetNameAndPetTypeOfPaginatedOwners(int pageNumber,
			int pageSize);

	List<OwnerDTO> findByState(String state, boolean loadPets);

	List<OwnerDTO> findByPetNameContainingString(String petName, boolean loadPets);

	List<OwnerDTO> findByPetType(PetType petType, boolean loadPets);

}
