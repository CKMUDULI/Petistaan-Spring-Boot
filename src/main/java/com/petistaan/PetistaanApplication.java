package com.petistaan;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.petistaan.dto.OwnerDTO;
import com.petistaan.dto.PetDTO;
import com.petistaan.enums.PetType;
import com.petistaan.exception.OwnerNotFoundException;
import com.petistaan.exception.OwnerPetCombinationNotFoundException;
import com.petistaan.exception.PetNotFoundException;
import com.petistaan.service.OwnerService;
import com.petistaan.service.PetService;
import com.petistaan.util.InputUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:messages.properties")
@SpringBootApplication
public class PetistaanApplication implements CommandLineRunner {

	private final OwnerService ownerService;

	private final PetService petService;

	public static void main(String[] args) {
		SpringApplication.run(PetistaanApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try (Scanner scanner = new Scanner(System.in)) {
			do {
				try {
					System.out.println("Welcome to Petistaan");
					handleMenuOption(scanner);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			} while (InputUtil.wantToContinue(scanner));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void handleMenuOption(Scanner scanner)
			throws OwnerNotFoundException, PetNotFoundException, OwnerPetCombinationNotFoundException {
		int menuOption = InputUtil.acceptMenuOption(scanner);
		switch (menuOption) {
			case 0 -> System.exit(0);
			case 1 -> handleAddOwner(scanner);
			case 2 -> handleOwnerFetch(scanner);
			case 3 -> handleUpdatePetDetailsOfOwner(scanner);
			case 4 -> handleDeleteOwner(scanner);
			case 5 -> handleDeleteMultipleOwners(scanner);
			case 6 -> handlePetFetch(scanner);
			case 7 -> handleNewPetAdditionToExistingOwner(scanner);
			case 8 -> handlePetDelete(scanner);
			case 9 -> handleCoOwnerOperation(scanner);
			case 10 -> handleFetchAllOwners(scanner);
			case 11 -> handleFetchAllPets(scanner);
			case 12 -> handleFetchAllOwnerByInitialsOfFirstName(scanner);
			case 13 -> handlePetAverageAgeCalculation();
			case 14 -> handleFetchIdAndFirstNameAndLastNameAndPetIdAndPetNameAndPetTypeOfPaginatedOwners(scanner);
			case 15 -> handleOwnerSorting(scanner);
			case 16 -> handlePetSorting(scanner);
			case 17 -> handleOwnerPagination(scanner);
			case 18 -> handlePetPagination(scanner);
			case 19 -> handleOwnerSortingAndPagination(scanner);
			case 20 -> handlePetSortingAndPagination(scanner);
			case 21 -> handleFetchAllOwnersByState(scanner);
			case 22 -> handleFetchOwnersByPetNameContainingString(scanner);
			case 23 -> handleFetchOwnersByPetType(scanner);
			case 24 -> handleFetchPetsByType(scanner);
			case 25 -> handleFetchPetsByOwnerId(scanner);
			default -> System.out.println("Invalid option entered.");
		}
	}

	private <T> void printDetails(T dto) {
		if (Objects.isNull(dto)) {
			System.out.println("No item found.");
			return;
		}
		String type = dto.getClass().getSimpleName().replace("DTO", "");
		System.out.printf("%s details:%n", type);
		System.out.println(dto);
	}

	private <T> void printDetails(List<T> dtoList) {
		if (dtoList == null || dtoList.isEmpty()) {
			System.out.println("No items found.");
			return;
		}
		String type = dtoList.get(0).getClass().getSimpleName().replace("DTO", "").equalsIgnoreCase("owner") ? "Owner"
				: "Pet";
		System.out.printf("Total %d %s%s found.%n", dtoList.size(), type, dtoList.size() > 1 ? "s" : "");
		dtoList.forEach(System.out::println);
	}

	private void handleAddOwner(Scanner scanner) {
		OwnerDTO ownerDTO = InputUtil.acceptOwnerDetailsToSave(scanner);
		PetDTO petDTO = InputUtil.acceptPetDetailsToSave(scanner);
		ownerDTO.setPetDTOList(List.of(petDTO));
		OwnerDTO savedOwnerDTO = ownerService.save(ownerDTO);
		System.out.println("Owner saved successfully.");
		printDetails(savedOwnerDTO);
	}

	private void handleOwnerFetch(Scanner scanner) throws OwnerNotFoundException {
		int ownerId = InputUtil.acceptOwnerIdToOperate(scanner);
		boolean loadPets = InputUtil.wantToLoad(scanner, "Pet");
		OwnerDTO ownerDTO = ownerService.findById(ownerId, loadPets);
		System.out.println(loadPets ? "Owner along with pets fetched successfully." : "Owner fetched successfully.");
		printDetails(ownerDTO);
	}

	private void handleUpdatePetDetailsOfOwner(Scanner scanner)
			throws OwnerNotFoundException, OwnerPetCombinationNotFoundException {
		int ownerId = InputUtil.acceptOwnerIdToOperate(scanner);
		int petId = InputUtil.acceptPetIdToOperate(scanner);
		String petName = InputUtil.acceptPetDetailsToUpdate(scanner);
		OwnerDTO ownerDTO = ownerService.updatePetName(ownerId, petId, petName);
		System.out.println(String.format("Pet with ID %d of Owner with ID %d updated successfully.", petId, ownerId));
		printDetails(ownerDTO.getPetDTOList().stream().filter(p -> p.getId() == petId).findFirst().orElseThrow());
	}

	private void handleDeleteOwner(Scanner scanner) throws OwnerNotFoundException {
		int ownerId = InputUtil.acceptOwnerIdToOperate(scanner);
		ownerService.deleteById(ownerId);
		System.out.println(String.format("Owner with ID %d deleted successfully.", ownerId));
	}

	private void handleDeleteMultipleOwners(Scanner scanner) throws OwnerNotFoundException {
		List<Integer> ownerIds = InputUtil.acceptIdsToOperate(scanner, "Owner");
		ownerService.deleteByIds(ownerIds);
		System.out.println("Owners deleted successfully.");
	}

	private void handlePetFetch(Scanner scanner) throws PetNotFoundException {
		int petId = InputUtil.acceptPetIdToOperate(scanner);
		boolean loadOwners = InputUtil.wantToLoad(scanner, "Owner");
		PetDTO petDTO = petService.findByid(petId, loadOwners);
		System.out.println(loadOwners ? "Pet along with owners fetched successfully." : "Pet fetched successfully.");
		printDetails(petDTO);
	}

	private void handleNewPetAdditionToExistingOwner(Scanner scanner) throws OwnerNotFoundException {
		int ownerId = InputUtil.acceptOwnerIdToOperate(scanner);
		PetDTO petDTO = InputUtil.acceptPetDetailsToSave(scanner);
		ownerService.addNewPetToExistingOwner(ownerId, petDTO);
		System.out.println("Pet added to owner successfully.");
	}

	private void handlePetDelete(Scanner scanner) throws PetNotFoundException {
		int petId = InputUtil.acceptPetIdToOperate(scanner);
		petService.deleteById(petId);
		System.out.println(String.format("Pet with ID %d deleted successfully.", petId));
	}

	private void handleCoOwnerOperation(Scanner scanner) throws PetNotFoundException, OwnerNotFoundException {
		int petId = InputUtil.acceptPetIdToOperate(scanner);
		boolean coOwnerType = InputUtil.acceptCoOwnerType(scanner);
		if (coOwnerType) {
			OwnerDTO ownerDTO = InputUtil.acceptOwnerDetailsToSave(scanner);
			ownerService.addCoOwnerToPet(petId, ownerDTO);
			System.out.println("New co-owner added to pet successfully.");
		} else {
			int ownerId = InputUtil.acceptOwnerIdToOperate(scanner);
			ownerService.addCoOwnerToPet(petId, ownerId);
			System.out.println("Existing co-owner added to pet successfully.");
		}
	}

	private void handleFetchAllOwners(Scanner scanner) {
		boolean loadPets = InputUtil.wantToLoad(scanner, "Pet");
		List<OwnerDTO> ownerDTOList = ownerService.findAll(loadPets);
		printDetails(ownerDTOList);
	}

	private void handleFetchAllPets(Scanner scanner) {
		boolean loadOwners = InputUtil.wantToLoad(scanner, "Owner");
		List<PetDTO> petDTOList = petService.findAll(loadOwners);
		printDetails(petDTOList);
	}

	private void handleFetchAllOwnerByInitialsOfFirstName(Scanner scanner) {
		String initials = InputUtil.acceptInitials(scanner, "Owner first name");
		boolean loadPets = InputUtil.wantToLoad(scanner, "Pet");
		List<OwnerDTO> ownerDTOList = ownerService.findByInitialsOfFirstName(initials, loadPets);
		printDetails(ownerDTOList);
	}

	private void handlePetAverageAgeCalculation() {
		double averageAge = petService.calculatePetAverageAge();
		System.out.printf("Average age of all pets: %.2f%n", averageAge);
	}

	private void handleFetchIdAndFirstNameAndLastNameAndPetIdAndPetNameAndPetTypeOfPaginatedOwners(Scanner scanner) {
		int pageNumber = InputUtil.acceptPageNumber(scanner);
		int pageSize = InputUtil.acceptPageSize(scanner);
		List<Object[]> objects = ownerService
				.handleFetchIdAndFirstNameAndLastNameAndPetIdAndPetNameAndPetTypeOfPaginatedOwners(pageNumber - 1,
						pageSize);
		String separatorLine = "----------------------------------------------------------------------------------------------------";
		System.out.println(separatorLine);
		System.out.println(String.format("%-10s%-20s%-20s%-20s%-20s%-20s", "Id", "First name", "Last name", "Pet id",
				"Pet name", "Pet type"));
		System.out.println(separatorLine);
		objects.forEach(o -> System.out
				.println(String.format("%-10d%-20s%-20s%-20d%-20s%-20s", o[0], o[1], o[2], o[3], o[4], o[5])));
		System.out.println(separatorLine);
	}

	private void handleOwnerSorting(Scanner scanner) {
		String sortingParameter = InputUtil.acceptSortingParameterForOwner(scanner);
		boolean sortDescending = InputUtil.wantToSortByDescending(scanner);
		boolean loadPets = InputUtil.wantToLoad(scanner, "Pet");
		List<OwnerDTO> ownerDTOList = ownerService.findAllSortedOwners(sortingParameter, sortDescending, loadPets);
		printDetails(ownerDTOList);
	}

	private void handlePetSorting(Scanner scanner) {
		String sortingParameter = InputUtil.acceptSortingParameterForPet(scanner);
		boolean sortDescending = InputUtil.wantToSortByDescending(scanner);
		boolean loadOwners = InputUtil.wantToLoad(scanner, "Owner");
		List<PetDTO> petDTOList = petService.findAllSortedPets(sortingParameter, sortDescending, loadOwners);
		printDetails(petDTOList);
	}

	private void handleOwnerPagination(Scanner scanner) {
		int pageNumber = InputUtil.acceptPageNumber(scanner);
		int pageSize = InputUtil.acceptPageSize(scanner);
		boolean loadPets = InputUtil.wantToLoad(scanner, "Pet");
		List<OwnerDTO> ownerDTOList = ownerService.findAllPaginatedOwners(pageNumber - 1, pageSize, loadPets);
		printDetails(ownerDTOList);
	}

	private void handlePetPagination(Scanner scanner) {
		int pageNumber = InputUtil.acceptPageNumber(scanner);
		int pageSize = InputUtil.acceptPageSize(scanner);
		boolean loadOwners = InputUtil.wantToLoad(scanner, "Owner");
		List<PetDTO> petDTOList = petService.findAllPaginatedPets(pageNumber - 1, pageSize, loadOwners);
		printDetails(petDTOList);
	}

	private void handleOwnerSortingAndPagination(Scanner scanner) {
		String sortingParameter = InputUtil.acceptSortingParameterForOwner(scanner);
		boolean sortDescending = InputUtil.wantToSortByDescending(scanner);
		int pageNumber = InputUtil.acceptPageNumber(scanner);
		int pageSize = InputUtil.acceptPageSize(scanner);
		boolean loadPets = InputUtil.wantToLoad(scanner, "Pet");
		List<OwnerDTO> ownerDTOList = ownerService.findAllPaginatedAndSortedOwners(pageNumber - 1, pageSize,
				sortingParameter, sortDescending, loadPets);
		printDetails(ownerDTOList);
	}

	private void handlePetSortingAndPagination(Scanner scanner) {
		String sortingParameter = InputUtil.acceptSortingParameterForPet(scanner);
		boolean sortDescending = InputUtil.wantToSortByDescending(scanner);
		int pageNumber = InputUtil.acceptPageNumber(scanner);
		int pageSize = InputUtil.acceptPageSize(scanner);
		boolean loadOwners = InputUtil.wantToLoad(scanner, "Owner");
		List<PetDTO> petDTOList = petService.findAllPaginatedAndSortedPets(pageNumber - 1, pageSize, sortingParameter,
				sortDescending, loadOwners);
		printDetails(petDTOList);
	}

	private void handleFetchAllOwnersByState(Scanner scanner) {
		String state = InputUtil.acceptString(scanner, "Enter state:");
		boolean loadPets = InputUtil.wantToLoad(scanner, "Pet");
		List<OwnerDTO> ownerDTOList = ownerService.findByState(state, loadPets);
		printDetails(ownerDTOList);
	}

	private void handleFetchOwnersByPetNameContainingString(Scanner scanner) {
		String petName = InputUtil.acceptString(scanner, "Enter pet name to search:");
		boolean loadPets = InputUtil.wantToLoad(scanner, "Pet");
		List<OwnerDTO> ownerDTOList = ownerService.findByPetNameContainingString(petName, loadPets);
		printDetails(ownerDTOList);
	}

	private void handleFetchOwnersByPetType(Scanner scanner) {
		PetType petType = InputUtil.acceptPetTypeToOperate(scanner);
		boolean loadPets = InputUtil.wantToLoad(scanner, "Pet");
		List<OwnerDTO> ownerDTOList = ownerService.findByPetType(petType, loadPets);
		printDetails(ownerDTOList);
	}

	private void handleFetchPetsByType(Scanner scanner) {
		PetType petType = InputUtil.acceptPetTypeToOperate(scanner);
		boolean loadOwners = InputUtil.wantToLoad(scanner, "Owner");
		List<PetDTO> petDTOList = petService.findByType(petType, loadOwners);
		printDetails(petDTOList);
	}

	private void handleFetchPetsByOwnerId(Scanner scanner) {
		int ownerId = InputUtil.acceptOwnerIdToOperate(scanner);
		boolean loadOwners = InputUtil.wantToLoad(scanner, "Owner");
		List<PetDTO> petDTOList = petService.findByOwnerId(ownerId, loadOwners);
		printDetails(petDTOList);
	}

}
