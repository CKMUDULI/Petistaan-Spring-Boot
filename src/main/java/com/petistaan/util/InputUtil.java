package com.petistaan.util;

import static com.petistaan.util.InputValidator.getValidatedInput;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.petistaan.dto.DomesticPetDTO;
import com.petistaan.dto.OwnerDTO;
import com.petistaan.dto.PetDTO;
import com.petistaan.dto.WildPetDTO;
import com.petistaan.enums.Gender;
import com.petistaan.enums.PetType;
import com.petistaan.exception.InternalServiceException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InputUtil {

	public static boolean wantToContinue(Scanner scanner) {
		char choice = getValidatedInput(scanner, "Press Y to continue and N to exit.",
				input -> ValidationUtil.validateCharacterWithOptions(input, 'Y', 'N'));
		return choice == 'Y';
	}

	public static int acceptMenuOption(Scanner scanner) {
		String[] menuOptions = { "Press 1 to add new owner.", "Press 2 to fetch owner details.",
				"Press 3 to update pet details of owner.", "Press 4 to delete owner details.",
				"Press 5 to delete multiple owners.", "Press 6 to fetch pet details.",
				"Press 7 to add new pet to existing owner.", "Press 8 to remove pet details.",
				"Press 9 to add co-owner.", "Press 10 to fetch all owners", "press 11 to fetch all pets",
				"Press 12 to fetch owner by initials of first name of owner.", "Press 13 to find average age of pet.",
				"Press 14 to find specific details using pagination.", "Press 15 to sort owners by specific parameter.",
				"Press 16 to sort pets by specific parameter.", "Press 17 to fetch owner by pagination.",
				"Press 18 to fetch pet by pagination.", "Press 19 to fetch owner by sorting and pagination.",
				"Press 20 to fetch pet by sorting and pagination.", "Press 21 to fetch all owners by state.",
				"Press 22 to fetch all owners by pet name containing specific string.",
				"Press 23 to fetch all owners by pet type.", "Press 24 to fetch all pets by type.",
				"Press 25 to fetch all pets by owner id.", "Press 0 to exit." };
		return getValidatedInput(scanner, String.join("\n", menuOptions),
				input -> ValidationUtil.validateIntegerWithinRange(input, 0, menuOptions.length - 1));
	}

	public static OwnerDTO acceptOwnerDetailsToSave(Scanner scanner) {
		String firstName = getValidatedInput(scanner, "Enter first name of owner:", ValidationUtil::validateString);
		String lastName = getValidatedInput(scanner, "Enter last name of owner:", ValidationUtil::validateString);
		Gender gender = getValidatedInput(scanner,
				"Enter gender of owner (options: " + Arrays.asList(Gender.values()) + "):",
				input -> ValidationUtil.validateEnumValue(input, Gender.class));
		String city = getValidatedInput(scanner, "Enter city of owner:", ValidationUtil::validateString);
		String state = getValidatedInput(scanner, "Enter state of owner:", ValidationUtil::validateString);
		String mobileNumber = getValidatedInput(scanner, "Enter mobile number of owner:",
				ValidationUtil::validateMobileNumber);
		String emailId = getValidatedInput(scanner, "Enter email id of owner:", ValidationUtil::validateEmail);
		return OwnerDTO.builder().firstName(firstName).lastName(lastName).gender(gender).city(city).state(state)
				.mobileNumber(mobileNumber).emailId(emailId).build();
	}

	public static PetDTO acceptPetDetailsToSave(Scanner scanner) {
		String name = getValidatedInput(scanner, "Enter name of pet:", ValidationUtil::validateString);
		char choice = acceptPetCategoryToOperate(scanner);
		LocalDate birthDate = null;
		String birthPlace = null;
		if (choice == 'D') {
			birthDate = getValidatedInput(scanner, "Enter date of birth of pet (dd-MM-yyyy):",
					ValidationUtil::validateAndParseDate);
		} else if (choice == 'W') {
			birthPlace = getValidatedInput(scanner, "Enter place of birth of pet:", ValidationUtil::validateString);
		}
		Gender gender = getValidatedInput(scanner,
				"Enter gender of pet (options: " + Arrays.asList(Gender.values()) + "):",
				input -> ValidationUtil.validateEnumValue(input, Gender.class));
		PetType type = getValidatedInput(scanner,
				"Enter type of pet (options: " + Arrays.asList(PetType.values()) + "):",
				input -> ValidationUtil.validateEnumValue(input, PetType.class));
		if (choice == 'D') {
			return DomesticPetDTO.builder().name(name).birthDate(birthDate).gender(gender).type(type).build();
		} else if (choice == 'W') {
			return WildPetDTO.builder().name(name).birthPlace(birthPlace).gender(gender).type(type).build();
		} else {
			throw new InternalServiceException("Unsupported pet category: " + choice);
		}
	}

	public static char acceptPetCategoryToOperate(Scanner scanner) {
		return getValidatedInput(scanner, "Press D for domestic pet and W for wild pet:",
				input -> ValidationUtil.validateCharacterWithOptions(input, 'D', 'W'));
	}

	public static int acceptOwnerIdToOperate(Scanner scanner) {
		return getValidatedInput(scanner, "Enter id of owner:", ValidationUtil::validatePositiveInteger);
	}

	public static List<Integer> acceptIdsToOperate(Scanner scanner, String entityType) {
		List<Integer> ids = new ArrayList<>();
		do {
			int id = getValidatedInput(scanner, "Enter id of " + entityType + ":",
					ValidationUtil::validatePositiveInteger);
			ids.add(id);
		} while (wantToContinue(scanner));
		if (ids.isEmpty()) {
			throw new IllegalArgumentException("At least one " + entityType + " id is required.");
		}
		return ids;
	}

	public static String acceptPetDetailsToUpdate(Scanner scanner) {
		return getValidatedInput(scanner, "Enter updated name of pet:", ValidationUtil::validateString);
	}

	public static int acceptPetIdToOperate(Scanner scanner) {
		return getValidatedInput(scanner, "Enter id of pet:", ValidationUtil::validatePositiveInteger);
	}

	public static String acceptSortingParameterForOwner(Scanner scanner) {
		Set<String> ownerParameters = ValidationUtil.getValidOwnerParameters();
		return getValidatedInput(scanner, "Enter sorting parameter for Owner (options: " + ownerParameters + "):",
				input -> ValidationUtil.validateSortingParameter(input, ownerParameters));
	}

	public static boolean wantToSortByDescending(Scanner scanner) {
		char choice = getValidatedInput(scanner,
				"Press D to sort by descending order and A to sort by ascending order.",
				input -> ValidationUtil.validateCharacterWithOptions(input, 'D', 'A'));
		return choice == 'D';
	}

	public static int acceptPageNumber(Scanner scanner) {
		return getValidatedInput(scanner, "Enter page number:", ValidationUtil::validatePositiveInteger);
	}

	public static int acceptPageSize(Scanner scanner) {
		return getValidatedInput(scanner, "Enter page size:", ValidationUtil::validatePositiveInteger);
	}

	public static String acceptString(Scanner scanner, String prompt) {
		return getValidatedInput(scanner, prompt, ValidationUtil::validateString);
	}

	public static PetType acceptPetTypeToOperate(Scanner scanner) {
		return InputValidator.getValidatedInput(scanner,
				"Enter type of pet (options: " + Arrays.asList(PetType.values()) + "):",
				input -> ValidationUtil.validateEnumValue(input, PetType.class));
	}

	public static LocalDate acceptPetBirthDateToOperate(Scanner scanner, String prompt) {
		return InputValidator.getValidatedInput(scanner, prompt + " (dd-MM-yyyy):",
				ValidationUtil::validateAndParseDate);
	}

	public static boolean acceptCoOwnerType(Scanner scanner) {
		return getValidatedInput(scanner, "Press 1 for new co-owner and 2 for existing co-owner:",
				input -> ValidationUtil.validateIntegerWithinRange(input, 1, 2)) == 1;
	}

	public static int acceptPetType(Scanner scanner) {
		return getValidatedInput(scanner, "Press 1 for new pet and 2 for existing pet:",
				input -> ValidationUtil.validateIntegerWithinRange(input, 1, 2));
	}

	public static boolean acceptLazyLoadingType(Scanner scanner, String entity) {
		return getValidatedInput(scanner, "Press 1 to fetch with " + entity + " and 2 to fetch without " + entity + ":",
				input -> ValidationUtil.validateIntegerWithinRange(input, 1, 2)) == 1;
	}

	public static String acceptInitials(Scanner scanner, String type) {
		return getValidatedInput(scanner, "Enter initials of " + type + ":", ValidationUtil::validateString);
	}

	public static boolean wantToLoad(Scanner scanner, String type) {
		char choice = getValidatedInput(scanner,
				String.format("Press Y to load with %1$ss and N to load without %1$ss.", type),
				input -> ValidationUtil.validateCharacterWithOptions(input, 'Y', 'N'));
		return choice == 'Y';
	}

	public static String acceptSortingParameterForPet(Scanner scanner) {
		Set<String> parameters = ValidationUtil.getValidPetParameters();
		return getValidatedInput(scanner, "Enter sorting parameter for Pet (options: " + parameters + "):",
				input -> ValidationUtil.validateSortingParameter(input, parameters));
	}

}
