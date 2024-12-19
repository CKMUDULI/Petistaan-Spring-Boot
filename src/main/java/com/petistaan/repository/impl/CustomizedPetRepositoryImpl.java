package com.petistaan.repository.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import com.petistaan.entity.Owner;
import com.petistaan.entity.Pet;
import com.petistaan.repository.CustomizedPetRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomizedPetRepositoryImpl implements CustomizedPetRepository {

	@PersistenceUnit
	private final EntityManagerFactory entityManagerFactory;

	@Override
	@Transactional(readOnly = true)
	public List<Pet> findByOwnerId(int ownerId) {
		try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Pet> query = cb.createQuery(Pet.class);
			Root<Pet> pet = query.from(Pet.class);
			Join<Pet, Owner> ownerJoin = pet.join("ownerList", JoinType.INNER);
			Predicate ownerIdPredicate = cb.equal(ownerJoin.get("id"), ownerId);
			query.select(pet).where(ownerIdPredicate).distinct(true);
			List<Pet> pets = entityManager.createQuery(query).getResultList();
			pets.forEach(p -> Hibernate.initialize(p.getOwnerList()));
			return pets;
		}
	}

}
