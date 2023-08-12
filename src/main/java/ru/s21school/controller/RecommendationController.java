package ru.s21school.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.s21school.dto.RecommendationDto;
import ru.s21school.entity.CheckState;
import ru.s21school.service.RecommendationService;
import ru.s21school.util.validator.recommendationValidator.RecommendationSaveEditValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final RecommendationSaveEditValidator saveEditValidator;

    @GetMapping()
    public String recommendationsPageDefault() {
        return "redirect:/recommendations/page-0";
    }

    @GetMapping("/page-{page}")
    public String recommendationsPage(@PathVariable Integer page,
                                      @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                                      @RequestParam(required = false, defaultValue = "peer") String sortField,
                                      @RequestParam(required = false, defaultValue = "asc") String sortDir,
                                      Model model) {
        Page<RecommendationDto> pageRecommendationDto = recommendationService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("recommendations", pageRecommendationDto.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageRecommendationDto.getTotalPages());
        model.addAttribute("totalItems", pageRecommendationDto.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        log.info("GET /recommendations/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "/recommendations/recommendations";
    }

//    todo there is no view for recommendation_page. May be add later.
//    @GetMapping("/{id}")
//    public String findById(@PathVariable Long id, Model model) {
//    }

    @GetMapping("/new")
    public String newRecommendation(Model model) {
        log.info("GET /recommendations/new");
        model.addAttribute("recommendation", new RecommendationDto());
        return "recommendations/new";
    }

    @PostMapping("/new")
    public String saveRecommendation(@Valid @ModelAttribute("recommendation") RecommendationDto recommendationDto, BindingResult bindingResult, Model model) {
        saveEditValidator.validate(recommendationDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /recommendations/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            model.addAttribute("states", CheckState.values());
            return "recommendations/new";
        }
        recommendationService.save(recommendationDto);
        log.info("POST /recommendations/new CREATED NEW RECORD: {}", recommendationDto);
        return "redirect:/recommendations/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return recommendationService.findById(id)
                .map(recommendationDto -> {
                    log.info("GET /recommendations/{}/edit : {}", id, recommendationDto);
                    model.addAttribute("recommendation", recommendationDto);
                    return "recommendations/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /recommendations/{}/edit RECORD WITH ID {} NOT FOUND", id, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("recommendation") RecommendationDto recommendationDto, BindingResult bindingResult, @PathVariable Long id) {
        saveEditValidator.validate(recommendationDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /recommendations/{} FAIL UPDATE RECORD: {}", id, bindingResult.getAllErrors());
            return "/recommendations/edit";
        }

        return recommendationService.update(id, recommendationDto)
                .map(it -> {
                    log.info("POST /recommendations/{} WAS UPDATED: {}", id, it);
                    return "redirect:/recommendations/";
                })
                .orElseThrow(() -> {
                            log.warn("POST /recommendations/{} RECORD WITH ID {} NOT FOUND", id, id);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!recommendationService.delete(id)) {
            log.warn("DELETE /recommendations/{} RECORD WITH ID {} NOT FOUND", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /recommendations/{} RECORD WITH ID {} WAS DELETED", id, id);
        return "redirect:/recommendations/";
    }
}
