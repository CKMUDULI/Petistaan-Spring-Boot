package com.petistaan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.petistaan.entity.Pet;
import com.petistaan.enums.PetType;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer>, CustomizedPetRepository {

	@Query("SELECT p FROM Pet p LEFT JOIN FETCH p.ownerList")
	List<Pet> findAllWithOwners();

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(value = "DELETE FROM owner_pet_table WHERE pet_id = :petId", nativeQuery = true)
	void deleteOwnerPetRelationsByPetId(@Param("petId") int petId);

	@Query("SELECT AVG(YEAR(CURRENT_DATE) - YEAR(birthDate)) FROM Pet p")
	double calculatePetAverageAge();

	List<Pet> findByNameContainsIgnoreCase(String name);

	List<Pet> findByType(PetType petType);

}
