package com.mirna.busmanagement.services;

import com.mirna.busmanagement.models.Problem;
import com.mirna.busmanagement.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final UserService userService;

    @Autowired
    public ProblemService(ProblemRepository problemRepository, UserService userService) {
        this.problemRepository = problemRepository;
        this.userService = userService;
    }

    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    public Problem getProblemById(String id) {
        return problemRepository.findById(id).orElse(null);
    }

    public void saveProblem(Problem problem, String userId) {
        problem.setUser(userService.getUserById(userId));
        problemRepository.save(problem);
    }

    public void deleteProblem(String id) {
        problemRepository.deleteById(id);
    }

    public Page<Problem> getAllProblemsPaginated(Pageable pageable) {
        return problemRepository.findAll(pageable);
    }

    public Page<Problem> getAllDriverProblemsPaginated(Pageable pageable, String userId) {
        return problemRepository.getProblemsByUserId(userId, pageable);
    }
}