package se.hem.spliteven.repository;

import se.hem.spliteven.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
