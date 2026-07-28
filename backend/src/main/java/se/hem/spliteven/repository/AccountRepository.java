package se.hem.spliteven.repository;

import se.hem.spliteven.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}