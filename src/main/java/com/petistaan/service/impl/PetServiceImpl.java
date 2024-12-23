package com.petistaan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.petistaan.dto.PetDTO;
import com.petistaan.entity.Pet;
import com.petistaan.enums.PetType;
import com.petistaan.exception.PetNotFoundException;
import com.petistaan.repository.PetRepository;
import com.petistaan.service.PetService;
import com.petistaan.util.PetMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

	@Value("${pet.not.found}")
	private String petNotFound;

	private final PetRepository petRepository;

	private final PetMapper petMapper;

	@Autowired
	private PetServiceImpl petService;

	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public List<PetDTO> mapPetsToDTOs(List<Pet> pets, boolean loadOwners) {
		return pets.stream().map(pet -> {
			if (loadOwners) {
				pet.getOwnerList();
				return petMapper.toDto(pet);
			} else {
				return petMapper.toDTOWithoutOwner(pet);
			}
		}).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public PetDTO findByid(int petId, boolean loadOwners) throws PetNotFoundException {
		Pet pet = petRepository.findById(petId)
				.orElseThrow(() -> new PetNotFoundException(String.format(petNotFound, petId)));
		return loadOwners ? petMapper.toDto(pet) : petMapper.toDTOWithoutOwner(pet);
	}

	@Override
	public List<PetDTO> findAll(boolean loadOwners) {
		return loadOwners ? petRepository.findAllWithOwners().stream().map(petMapper::toDto).toList()
				: petRepository.findAll().stream().map(petMapper::toDTOWithoutOwner).toList();
	}

	@Override
	@Transactional
	public void deleteById(int petId) throws PetNotFoundException {
		Pet pet = petRepository.findById(petId)
				.orElseThrow(() -> new PetNotFoundException(String.format(petNotFound, petId)));
		petRepository.deleteOwnerPetRelationsByPetId(petId); // Remove associated owner-pet relationships first
		petRepository.delete(pet);
	}

	@Override
	public double calculatePetAverageAge() {
		return petRepository.calculatePetAverageAge();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PetDTO> findAllSortedPets(String sortingParameter, boolean sortDescending, boolean loadOwners) {
		Direction direction = sortDescending ? Direction.DESC : Direction.ASC;
		Sort sort = Sort.by(direction, sortingParameter);
		List<Pet> pets = petRepository.findAll(sort);
		return petService.mapPetsToDTOs(pets, loadOwners);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PetDTO> findAllPaginatedPets(int pageNumber, int pageSize, boolean loadOwners) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		List<Pet> pets = petRepository.findAll(pageable).getContent();
		return petService.mapPetsToDTOs(pets, loadOwners);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PetDTO> findAllPaginatedAndSortedPets(int pageNumber, int pageSize, String sortingParameter,
			boolean sortDescending, boolean loadOwners) {
		Direction direction = sortDescending ? Direction.DESC : Direction.ASC;
		Sort sort = Sort.by(direction, sortingParameter);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		List<Pet> pets = petRepository.findAll(pageable).getContent();
		return petService.mapPetsToDTOs(pets, loadOwners);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PetDTO> findByType(PetType petType, boolean loadOwners) {
		List<Pet> pets = petRepository.findByType(petType);
		return petService.mapPetsToDTOs(pets, loadOwners);
	}

	@Override
	public List<PetDTO> findByOwnerId(int ownerId, boolean loadOwners) {
		List<Pet> pets = petRepository.findByOwnerId(ownerId);
		return petService.mapPetsToDTOs(pets, loadOwners);
	}

}
