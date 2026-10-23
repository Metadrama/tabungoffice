package io.github.metadrama.tabungoffice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring writes the class that talks to the contributions table
public interface ContributionRepository extends JpaRepository<Contribution, Long> {

	List<Contribution> findByTabungId(Long tabungId);
}