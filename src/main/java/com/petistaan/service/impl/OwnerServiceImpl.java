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

import com.petistaan.dto.OwnerDTO;
import com.petistaan.dto.PetDTO;
import com.petistaan.entity.Owner;
import com.petistaan.entity.Pet;
import com.petistaan.enums.PetType;
import com.petistaan.exception.OwnerNotFoundException;
import com.petistaan.exception.OwnerPetCombinationNotFoundException;
import com.petistaan.exception.PetNotFoundException;
import com.petistaan.repository.OwnerRepository;
import com.petistaan.repository.PetRepository;
import com.petistaan.service.OwnerService;
import com.petistaan.util.OwnerMapper;
import com.petistaan.util.PetMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OwnerServiceImpl implements OwnerService {

	@Value("${owner.not.found}")
	private String ownerNotFound;

	@Value("${pet.not.found}")
	private String petNotFound;

	@Value("${pet.not.belong.to.owner}")
	private String petNotBelongToOwner;

	private final OwnerRepository ownerRepository;

	private final PetRepository petRepository;

	private final OwnerMapper ownerMapper;

	private final PetMapper petMapper;
	
	@Autowired
	private OwnerServiceImpl ownerService;

	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public List<OwnerDTO> mapOwnersToDTOs(List<Owner> owners, boolean loadPets) {
		return owners.stream().map(owner -> {
			if (loadPets) {
				owner.getPetList();
				return ownerMapper.toDTO(owner);
			} else {
				return ownerMapper.toDTOWithoutPets(owner);
			}
		}).toList();
	}

	@Override
	public OwnerDTO save(OwnerDTO ownerDTO) {
		return ownerMapper.toDTO(ownerRepository.save(ownerMapper.toEntity(ownerDTO)));
	}

	@Override
	public OwnerDTO findById(int ownerId, boolean loadPets) throws OwnerNotFoundException {
		Owner owner = loadPets
				? ownerRepository.findByIdWithPets(ownerId)
						.orElseThrow(() -> new OwnerNotFoundException(String.format(ownerNotFound, ownerId)))
				: ownerRepository.findById(ownerId)
						.orElseThrow(() -> new OwnerNotFoundException(String.format(ownerNotFound, ownerId)));
		return loadPets ? ownerMapper.toDTO(owner) : ownerMapper.toDTOWithoutPets(owner);
	}

	@Override
	@Transactional
	public OwnerDTO updatePetName(int ownerId, int petId, String petName)
			throws OwnerNotFoundException, OwnerPetCombinationNotFoundException {
		Owner owner = ownerRepository.findByIdWithPets(ownerId)
				.orElseThrow(() -> new OwnerNotFoundException(String.format(ownerNotFound, ownerId)));
		Pet targetPet = owner.getPetList().stream().filter(pet -> pet.getId() == petId).findFirst().orElseThrow(
				() -> new OwnerPetCombinationNotFoundException(String.format(petNotBelongToOwner, petId, ownerId)));
		targetPet.setName(petName);
		return ownerMapper.toDTO(ownerRepository.save(owner));
	}

	@Override
	@Transactional
	public void deleteById(int ownerId) throws OwnerNotFoundException {
		Owner owner = ownerRepository.findById(ownerId)
				.orElseThrow(() -> new OwnerNotFoundException(String.format(ownerNotFound, ownerId)));
		owner.getPetList().stream().filter(pet -> pet.getOwnerList().size() == 1)
				.forEach(petRepository::delete);
		ownerRepository.deleteById(ownerId);
	}

	@Override
	public OwnerDTO addNewPetToExistingOwner(int ownerId, PetDTO petDTO) throws OwnerNotFoundException {
		Owner owner = ownerRepository.findById(ownerId)
				.orElseThrow(() -> new OwnerNotFoundException(String.format(ownerNotFound, ownerId)));
		Pet pet = petMapper.toEntity(petDTO);
		owner.getPetList().add(pet);
		return ownerMapper.toDTO(ownerRepository.save(owner));
	}

	@Override
	public List<OwnerDTO> findAll(boolean loadPets) {
		return loadPets ? ownerRepository.findAllWithPets().stream().map(ownerMapper::toDTO).toList()
				: ownerRepository.findAll().stream().map(ownerMapper::toDTOWithoutPets).toList();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteByIds(List<Integer> ownerIds) throws OwnerNotFoundException {
		for (Integer ownerId : ownerIds) {
			ownerService.deleteById(ownerId);
		}
	}

	@Override
	@Transactional
	public OwnerDTO addCoOwnerToPet(int petId, OwnerDTO ownerDTO) throws PetNotFoundException {
		Pet pet = petRepository.findById(petId)
				.orElseThrow(() -> new PetNotFoundException(String.format(petNotFound, petId)));
		Owner owner = ownerMapper.toEntity(ownerDTO);
		owner.getPetList().add(pet);
		return ownerMapper.toDTO(ownerRepository.save(owner));
	}

	@Override
	@Transactional
	public OwnerDTO addCoOwnerToPet(int petId, int ownerId) throws PetNotFoundException, OwnerNotFoundException {
		Pet pet = petRepository.findById(petId)
				.orElseThrow(() -> new PetNotFoundException(String.format(petNotFound, petId)));
		Owner owner = ownerRepository.findById(ownerId)
				.orElseThrow(() -> new OwnerNotFoundException(String.format(ownerNotFound, ownerId)));
		owner.getPetList().add(pet);
		return ownerMapper.toDTO(ownerRepository.save(owner));
	}

	@Override
	@Transactional(readOnly = true)
	public List<OwnerDTO> findByInitialsOfFirstName(String initials, boolean loadPets) {
		List<Owner> owners = ownerRepository.findByFirstNameStartsWith(initials);
		return ownerService.mapOwnersToDTOs(owners, loadPets);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OwnerDTO> findAllSortedOwners(String sortingParameter, boolean sortDescending, boolean loadPets) {
		Direction direction = sortDescending ? Direction.DESC : Direction.ASC;
		Sort sort = Sort.by(direction, sortingParameter);
		List<Owner> owners = ownerRepository.findAll(sort);
		return ownerService.mapOwnersToDTOs(owners, loadPets);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OwnerDTO> findAllPaginatedOwners(int pageNumber, int pageSize, boolean loadPets) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		List<Owner> owners = ownerRepository.findAll(pageable).getContent();
		return ownerService.mapOwnersToDTOs(owners, loadPets);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OwnerDTO> findAllPaginatedAndSortedOwners(int pageNumber, int pageSize, String sortingParameter,
			boolean sortDescending, boolean loadPets) {
		Direction direction = sortDescending ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(pageNumber, pageSize, direction, sortingParameter);
		List<Owner> owners = ownerRepository.findAll(pageable).getContent();
		return ownerService.mapOwnersToDTOs(owners, loadPets);
	}

	@Override
	public List<Object[]> handleFetchIdAndFirstNameAndLastNameAndPetIdAndPetNameAndPetTypeOfPaginatedOwners(
			int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return ownerRepository.fetchIdAndFirstNameAndLastNameAndPetIdAndPetNameAndPetType(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OwnerDTO> findByState(String state, boolean loadPets) {
		List<Owner> owners = ownerRepository.findByState(state);
		return ownerService.mapOwnersToDTOs(owners, loadPets);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OwnerDTO> findByPetNameContainingString(String petName, boolean loadPets) {
		List<Owner> owners = ownerRepository.findByPetNameContainingString(petName);
		return ownerService.mapOwnersToDTOs(owners, loadPets);
	}

	@Override
	public List<OwnerDTO> findByPetType(PetType petType, boolean loadPets) {
		List<Owner> owners = ownerRepository.findByPetType(petType);
		return ownerService.mapOwnersToDTOs(owners, loadPets);
	}

}
