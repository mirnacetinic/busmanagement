package com.mirna.busmanagement.controllers;

import com.mirna.busmanagement.models.Problem;
import com.mirna.busmanagement.security.CustomUserDetails;
import com.mirna.busmanagement.services.ProblemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/problem")
public class ProblemController {
    private final ProblemService problemService;


    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping("")
    public String getAllProblems(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                 Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Problem> problemPage = ((customUserDetails.getAuthorities().toString().contains("ROLE_ADMIN")) ?
                problemService.getAllProblemsPaginated(pageable) : problemService.getAllDriverProblemsPaginated(pageable, customUserDetails.getUserId()));
        model.addAttribute("page", problemPage);
        if (problemPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, problemPage.getTotalPages() - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }


        return "problems";
    }

    @GetMapping("/add")
    public String showProblemsForm(Model model) {
        model.addAttribute("problem", new Problem());
        return "new_problem";
    }


    @PostMapping("/add")
    public String addProblem(@AuthenticationPrincipal CustomUserDetails customUserDetails, @ModelAttribute @Valid Problem problem, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "new_problem";
        }

        try {
            problemService.saveProblem(problem, customUserDetails.getUserId());
            ;
            return "redirect:/problem";
        } catch (Exception e) {
            model.addAttribute("notValid", e.getMessage());
            return "new_problem";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProblem(@PathVariable String id) {
        problemService.deleteProblem(id);
        return "redirect:/problem";
    }

}
