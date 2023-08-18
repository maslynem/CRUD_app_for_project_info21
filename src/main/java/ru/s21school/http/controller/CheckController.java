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
import ru.s21school.dto.CheckDto;
import ru.s21school.http.controllerUtil.ControllerUtil;
import ru.s21school.service.CheckService;
import ru.s21school.util.validator.checkValidator.CheckSaveUpdateValidator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
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
                             @RequestParam(required = false, defaultValue = "peerNickname") String sortField,
                             @RequestParam(required = false, defaultValue = "asc") String sortDir,
                             Model model) {
        Page<CheckDto> pageCheckDto = checkService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("checks", pageCheckDto.getContent());
        ControllerUtil.setModelPagination(model, pageCheckDto, page, pageSize, sortField, sortDir);
        log.info("GET /checks/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "/checks/checks";
    }

    @GetMapping("/new")
    public String newCheck(Model model) {
        log.info("GET /checks/new");
        model.addAttribute("check", new CheckDto());
        return "checks/new";
    }

    @PostMapping("/new")
    public String saveCheck(@Valid @ModelAttribute("check") CheckDto checkDto, BindingResult bindingResult) {
        checkSaveUpdateValidator.validate(checkDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /checks/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            return "checks/new";
        }
        checkService.save(checkDto);
        log.info("POST /checks/new CREATED NEW RECORD: {}", checkDto);
        return "redirect:/checks/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return checkService.findById(id)
                .map(checkDto -> {
                    log.info("GET /checks/{}/edit : {}", id, checkDto);
                    model.addAttribute("check", checkDto);
                    return "checks/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /checks/{}/edit RECORD WITH ID {} NOT FOUND", id, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("check") CheckDto checkDto, BindingResult bindingResult, @PathVariable Long id) {
        checkSaveUpdateValidator.validate(checkDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /checks/{} FAIL UPDATE RECORD: {}", id, bindingResult.getAllErrors());
            return "checks/edit";
        }

        return checkService.update(id, checkDto)
                .map(it -> {
                    log.info("POST /checks/{} WAS UPDATED: {}", id, it);
                    return "redirect:/checks/{id}/";
                })
                .orElseThrow(() -> {
                            log.warn("POST /checks/{} RECORD WITH ID {} NOT FOUND", id, id);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!checkService.delete(id)) {
            log.warn("DELETE /checks/{} RECORD WITH TITLE {} NOT FOUND", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /checks/{} RECORD WITH TITLE {} WAS DELETED", id, id);
        return "redirect:/checks/";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"checks.csv\"");
        checkService.writeToCsv(servletResponse.getWriter());
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) throws IOException {
        checkService.readFromCsv(file.getInputStream());
        return "redirect:/checks/";
    }
}
