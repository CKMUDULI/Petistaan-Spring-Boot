package com.petistaan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.petistaan.entity.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer>, CustomizedOwnerRepository {

	@Query("SELECT o FROM Owner o LEFT JOIN FETCH o.petList WHERE o.id = :ownerId")
	Optional<Owner> findByIdWithPets(@Param("ownerId") int ownerId);

	@Query("SELECT o FROM Owner o LEFT JOIN FETCH o.petList")
	List<Owner> findAllWithPets();

	List<Owner> findByFirstNameStartsWith(String initials);

	@Query("SELECT o.id, o.firstName, o.lastName, p.id, p.name, p.type FROM Owner o LEFT JOIN o.petList p")
	List<Object[]> fetchIdAndFirstNameAndLastNameAndPetIdAndPetNameAndPetType(Pageable pageable);

	List<Owner> findByState(String state);

}
