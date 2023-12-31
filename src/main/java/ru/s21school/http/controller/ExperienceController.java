package ru.s21school.http.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.s21school.dto.ExperienceDto;
import ru.s21school.http.controllerUtil.ControllerUtil;
import ru.s21school.service.ExperienceService;
import ru.s21school.util.validator.experienceValidator.ExperienceSaveValidator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/experiences")
public class ExperienceController {
    private final ExperienceService experienceService;
    private final ExperienceSaveValidator experienceSaveValidator;

    @GetMapping()
    public String experiencesPageDefault() {
        return "redirect:/experiences/page-0";
    }

    @GetMapping("/page-{page}")
    public String experiencesPage(@PathVariable Integer page,
                             @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                             @RequestParam(required = false, defaultValue = "checkId") String sortField,
                             @RequestParam(required = false, defaultValue = "asc") String sortDir,
                             Model model) {
        Page<ExperienceDto> pageExperienceDto = experienceService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("experiences", pageExperienceDto.getContent());
        ControllerUtil.setModelPagination(model, pageExperienceDto, page, pageSize, sortField, sortDir);
        log.info("GET /experiences/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "/experiences/experiences";
    }

    @GetMapping("/new")
    public String newCheck(Model model) {
        log.info("GET /experiences/new");
        model.addAttribute("experience", new ExperienceDto());
        return "experiences/new";
    }

    @PostMapping("/new")
    public String saveCheck(@Valid @ModelAttribute("experience") ExperienceDto experienceDto, BindingResult bindingResult) {
        experienceSaveValidator.validate(experienceDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /experiences/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            return "experiences/new";
        }
        experienceService.save(experienceDto);
        log.info("POST /experiences/new CREATED NEW RECORD: {}", experienceDto);
        return "redirect:/experiences/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return experienceService.findById(id)
                .map(experienceDto -> {
                    log.info("GET /experiences/{}/edit : {}", id, experienceDto);
                    model.addAttribute("experience", experienceDto);
                    return "experiences/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /experiences/{}/edit RECORD WITH ID {} NOT FOUND", id, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("experience") ExperienceDto experienceDto, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            log.warn("POST /experiences/{} FAIL UPDATE RECORD: {}", id, bindingResult.getAllErrors());
            return "experiences/edit";
        }

        return experienceService.update(id, experienceDto)
                .map(it -> {
                    log.info("POST /experiences/{} WAS UPDATED: {}", id, it);
                    return "redirect:/experiences/";
                })
                .orElseThrow(() -> {
                            log.warn("POST /experiences/{} RECORD WITH ID {} NOT FOUND", id, id);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!experienceService.delete(id)) {
            log.warn("DELETE /experiences/{} RECORD WITH TITLE {} NOT FOUND", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /experiences/{} RECORD WITH TITLE {} WAS DELETED", id, id);
        return "redirect:/experiences/";
    }


    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"experiences.csv\"");
        experienceService.writeToCsv(servletResponse.getWriter());
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) throws IOException {
        experienceService.readFromCsv(file.getInputStream());
        return "redirect:/experiences/";
    }
}
