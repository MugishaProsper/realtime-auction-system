package com.webapp.realtimeauctionbackend.repositories;

import com.webapp.realtimeauctionbackend.models.Person;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE p.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT p FROM Person p WHERE p.email = :email")
    Optional<Person> findByEmail(@Param("email") String email);

    @Query("SELECT p FROM Person p WHERE p.id = :id")
    Optional<Person> findById(@Param("id") Long id);

    @Query("SELECT p FROM Person p WHERE p.role = :role")
    Optional<Person> findByRole(@Param("role") String role);
}