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
import ru.s21school.dto.verterDto.VerterDto;
import ru.s21school.service.VerterService;
import ru.s21school.util.validator.verterValidator.VerterSaveValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/verters")
public class VerterController {
    private final VerterService verterService;
    private final VerterSaveValidator saveValidator;
    
    @GetMapping()
    public String vertersPageDefault() {
        return "redirect:/verters/page-0";
    }

    @GetMapping("/page-{page}")
    public String vertersPage(@PathVariable Integer page,
                             @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                             @RequestParam(required = false, defaultValue = "checkId") String sortField,
                             @RequestParam(required = false, defaultValue = "asc") String sortDir,
                             Model model) {
        Page<VerterDto> pageVerterDto = verterService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("verters", pageVerterDto.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageVerterDto.getTotalPages());
        model.addAttribute("totalItems", pageVerterDto.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "/verters/verters";
    }

    // todo there is no view for verter_page. May be add later.
//    @GetMapping("/{id}")
//    public String findByTitle(@PathVariable Long id, Model model) {
//        return verterService.findById(id)
//                .map(verter -> {
//                    model.addAttribute("verter", verter);
//                    return "verters/verter_page";
//                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//    }

    @GetMapping("/new")
    public String newVerter(Model model) {
        model.addAttribute("verter", new VerterDto());
        return "verters/new";
    }

    @PostMapping("/new")
    public String saveVerter(@Valid @ModelAttribute("verter") VerterDto verterDto, BindingResult bindingResult) {
        saveValidator.validate(verterDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "verters/new";
        }
        verterService.save(verterDto);
        return "redirect:/verters/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return verterService.findById(id)
                .map(verterDto -> {
                    model.addAttribute("verter", verterDto);
                    return "verters/edit";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("verter") VerterDto verterDto, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            log.warn(bindingResult.getAllErrors().toString());
            return "/verters/edit";
        }

        return verterService.update(id, verterDto)
                .map(it -> "redirect:/verters/")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!verterService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/verters/";
    }
}
