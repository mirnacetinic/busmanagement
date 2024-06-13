package com.mirna.busmanagement.repositories;

import com.mirna.busmanagement.models.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, String> {
    Page<Problem> getProblemsByUserId(String user_id, Pageable pageable);
}
