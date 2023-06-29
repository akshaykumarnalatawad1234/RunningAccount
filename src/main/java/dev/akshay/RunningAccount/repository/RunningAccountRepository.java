package dev.akshay.RunningAccount.repository;

import dev.akshay.RunningAccount.domain.RunningAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunningAccountRepository extends JpaRepository<RunningAccount, Integer> {
    public RunningAccount findById(int id);
}
