package com.petistaan.repository.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.petistaan.entity.Owner;
import com.petistaan.entity.Pet;
import com.petistaan.enums.PetType;
import com.petistaan.repository.CustomizedOwnerRepository;

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
@Repository
public class CustomizedOwnerRepositoryImpl implements CustomizedOwnerRepository {

	@PersistenceUnit
	private final EntityManagerFactory entityManagerFactory;

	@Override
	public List<Owner> findByPetNameContainingString(String petName) {
		try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Owner> query = cb.createQuery(Owner.class);
			Root<Owner> owner = query.from(Owner.class);
			Join<Owner, Pet> petJoin = owner.join("petList", JoinType.INNER);
			Predicate namePredicate = cb.like(cb.lower(petJoin.get("name")), cb.lower(cb.literal("%" + petName + "%")));
			query.select(owner).where(namePredicate).distinct(true);
			List<Owner> owners = entityManager.createQuery(query).getResultList();
			owners.forEach(o -> Hibernate.initialize(o.getPetList()));
			return owners;
		}
	}

	@Override
	public List<Owner> findByPetType(PetType petType) {
		try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Owner> query = cb.createQuery(Owner.class);
			Root<Owner> owner = query.from(Owner.class);
			Join<Owner, Pet> petJoin = owner.join("petList", JoinType.INNER);
			Predicate typePredicate = cb.equal(petJoin.get("type"), petType);
			query.select(owner).where(typePredicate).distinct(true);
			List<Owner> owners = entityManager.createQuery(query).getResultList();
			owners.forEach(o -> Hibernate.initialize(o.getPetList()));
			return owners;
		}
	}

}
