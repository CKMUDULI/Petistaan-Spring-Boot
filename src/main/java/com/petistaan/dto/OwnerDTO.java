package com.petistaan.dto;

import java.util.List;

import com.petistaan.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OwnerDTO {

	@EqualsAndHashCode.Include
	private int id;

	private String firstName;

	private String lastName;

	private Gender gender;

	private String city;

	private String state;

	@EqualsAndHashCode.Include
	private String mobileNumber;

	@EqualsAndHashCode.Include
	private String emailId;

	@ToString.Include
	public int numberOfPets() {
		return petDTOList != null ? petDTOList.size() : 0;
	}

	private List<PetDTO> petDTOList;

	@Override
	public String toString() {
		if (petDTOList != null) {
			return "OwnerDTO (id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
					+ ", city=" + city + ", state=" + state + ", mobileNumber=" + mobileNumber + ", emailId=" + emailId
					+ ", numberOfPets=" + petDTOList.size() + ", petDTOList=" + petDTOList + ")";
		} else {
			return "OwnerDTO (id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
					+ ", city=" + city + ", state=" + state + ", mobileNumber=" + mobileNumber + ", emailId=" + emailId
					+ ")";
		}
	}

}
