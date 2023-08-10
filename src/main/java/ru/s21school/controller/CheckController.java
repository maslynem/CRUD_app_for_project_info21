package ru.s21school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.s21school.dto.checkDto.CheckDto;
import ru.s21school.service.CheckService;
import ru.s21school.util.validator.checkValidator.CheckSaveUpdateValidator;

import javax.validation.Valid;

@Repository
@RequiredArgsConstructor
@RequestMapping("/checks")
public class CheckController {
    private final CheckService checkService;
    private final CheckSaveUpdateValidator checkSaveUpdateValidator;

    @GetMapping()
    public String checksPageDefault() {
        return "redirect:/checks/page-0";
    }

    @GetMapping("/page-{page}")
    public String checksPage(@PathVariable Integer page,
                            @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                            @RequestParam(required = false, defaultValue = "id") String sortField,
                            @RequestParam(required = false, defaultValue = "asc") String sortDir,
                            Model model) {
        Page<CheckDto> pageCheckDto = checkService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("checks", pageCheckDto.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageCheckDto.getTotalPages());
        model.addAttribute("totalItems", pageCheckDto.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "/checks/checks";
    }

    @GetMapping("/{id}")
    public String findByTitle(@PathVariable Long id, Model model) {
        return checkService.findById(id)
                .map(check -> {
                    model.addAttribute("check", check);
                    return "checks/check_page";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/new")
    public String newCheck(Model model) {
        model.addAttribute("check", new CheckDto());
        return "checks/new";
    }

    @PostMapping("/new")
    public String saveCheck(@Valid @ModelAttribute("check") CheckDto checkDto, BindingResult bindingResult) {
        checkSaveUpdateValidator.validate(checkDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "checks/new";
        }
        checkService.save(checkDto);
        return "redirect:/checks/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return checkService.findById(id)
                .map(checkDto -> {
                    model.addAttribute("check", checkDto);
                    return "checks/edit";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("check") CheckDto checkDto, BindingResult bindingResult, @PathVariable Long id) {
        checkSaveUpdateValidator.validate(checkDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "checks/edit";
        }

        return checkService.update(id, checkDto)
                .map(it -> "redirect:/checks/{id}/")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!checkService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/checks/";
    }
}
