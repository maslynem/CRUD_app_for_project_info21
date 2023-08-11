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
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.service.TransferredPointService;
import ru.s21school.util.validator.transferedPointValidator.TransferredPointSaveValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/transferred-points")
public class TransferredPointController {

    private final TransferredPointService transferredPointService;
    private final TransferredPointSaveValidator saveValidator;

    @GetMapping()
    public String transferredPointsPageDefault() {
        return "redirect:/transferred-points/page-0";
    }

    @GetMapping("/page-{page}")
    public String transferredPointsPage(@PathVariable Integer page,
                              @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                              @RequestParam(required = false, defaultValue = "checkingPeerNickname") String sortField,
                              @RequestParam(required = false, defaultValue = "asc") String sortDir,
                              Model model) {
        Page<TransferredPointDto> pageTransferredPointDto = transferredPointService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("entities", pageTransferredPointDto.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageTransferredPointDto.getTotalPages());
        model.addAttribute("totalItems", pageTransferredPointDto.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        log.info("GET /transferred-points/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "/transferredPoints/transferred-points";
    }

//    todo there is no view for transferredPoint_page. May be add later.
//    @GetMapping("/{id}")
//    public String findById(@PathVariable Long id, Model model) {
//    }

    @GetMapping("/new")
    public String newTransferredPoint(Model model) {
        log.info("GET /transferred-points/new");
        model.addAttribute("entity", new TransferredPointDto());
        return "/transferredPoints/new";
    }

    @PostMapping("/new")
    public String saveTransferredPoint(@Valid @ModelAttribute("entity") TransferredPointDto transferredPointDto, BindingResult bindingResult) {
        saveValidator.validate(transferredPointDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /transferred-points/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            return "/transferredPoints/new";
        }
        transferredPointService.save(transferredPointDto);
        log.info("POST /transferred-points/new CREATED NEW RECORD: {}", transferredPointDto);
        return "redirect:/transferred-points/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return transferredPointService.findById(id)
                .map(transferredPointDto -> {
                    log.info("GET /transferred-points/{}/edit : {}", id, transferredPointDto);
                    model.addAttribute("entity", transferredPointDto);
                    return "/transferredPoints/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /transferred-points/{}/edit RECORD WITH ID {} NOT FOUND", id, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("entity") TransferredPointDto transferredPointDto, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            log.warn("POST /transferred-points/{} FAIL UPDATE RECORD: {}", id, bindingResult.getAllErrors());
            return "/transferredPoints/edit";
        }

        return transferredPointService.update(id, transferredPointDto)
                .map(it -> {
                    log.info("POST /transferred-points/{} WAS UPDATED: {}", id, it);
                    return "redirect:/transferred-points/";
                })
                .orElseThrow(() -> {
                            log.warn("POST /transferred-points/{} RECORD WITH ID {} NOT FOUND", id, id);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!transferredPointService.delete(id)) {
            log.warn("DELETE /transferred-points/{} RECORD WITH ID {} NOT FOUND", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /transferred-points/{} RECORD WITH ID {} WAS DELETED", id, id);
        return "redirect:/transferred-points/";
    }
}
