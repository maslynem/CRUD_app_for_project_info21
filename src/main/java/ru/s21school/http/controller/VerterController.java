package ru.s21school.http.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.s21school.dto.VerterDto;
import ru.s21school.entity.CheckState;
import ru.s21school.http.controllerUtil.ControllerUtil;
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

        ControllerUtil.setModelPagination(model, pageVerterDto, page, pageSize, sortField, sortDir);

        log.info("GET /verters/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "/verters/verters";
    }

    @GetMapping("/new")
    public String newVerter(Model model) {
        log.info("GET /verters/new");
        model.addAttribute("verter", new VerterDto());
        model.addAttribute("states", CheckState.values());
        return "verters/new";
    }

    @PostMapping("/new")
    public String saveVerter(@Valid @ModelAttribute("verter") VerterDto verterDto, BindingResult bindingResult, Model model) {
        saveValidator.validate(verterDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /verters/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            model.addAttribute("states", CheckState.values());
            return "verters/new";
        }
        verterService.save(verterDto);
        log.info("POST /verters/new CREATED NEW RECORD: {}", verterDto);
        return "redirect:/verters/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return verterService.findById(id)
                .map(verterDto -> {
                    log.info("GET /verters/{}/edit : {}", id, verterDto);
                    model.addAttribute("verter", verterDto);
                    model.addAttribute("states", CheckState.values());
                    return "verters/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /verters/{}/edit RECORD WITH ID {} NOT FOUND", id, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("verter") VerterDto verterDto, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            log.warn("POST /verters/{} FAIL UPDATE RECORD: {}", id, bindingResult.getAllErrors());
            return "/verters/edit";
        }

        return verterService.update(id, verterDto)
                .map(it -> {
                    log.info("POST /verters/{} WAS UPDATED: {}", id, it);
                    return "redirect:/verters/";
                })
                .orElseThrow(() -> {
                            log.warn("POST /verters/{} RECORD WITH ID {} NOT FOUND", id, id);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!verterService.delete(id)) {
            log.warn("DELETE /verters/{} RECORD WITH ID {} NOT FOUND", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /verters/{} RECORD WITH ID {} WAS DELETED", id, id);
        return "redirect:/verters/";
    }
}
