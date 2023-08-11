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
import ru.s21school.dto.peerToPeerDto.PeerToPeerDto;
import ru.s21school.entity.State;
import ru.s21school.service.PeerToPeerService;
import ru.s21school.util.validator.peerToPeerValidator.PeerToPeerSaveValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("p2p")
public class PeerToPeerController {

    private final PeerToPeerService peerToPeerService;
    private final PeerToPeerSaveValidator saveValidator;

    @GetMapping()
    public String p2pPageDefault() {
        return "redirect:/p2p/page-0";
    }

    @GetMapping("/page-{page}")
    public String p2pPage(@PathVariable Integer page,
                          @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                          @RequestParam(required = false, defaultValue = "checkId") String sortField,
                          @RequestParam(required = false, defaultValue = "asc") String sortDir,
                          Model model) {
        Page<PeerToPeerDto> pagePeerToPeerDto = peerToPeerService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("entities", pagePeerToPeerDto.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagePeerToPeerDto.getTotalPages());
        model.addAttribute("totalItems", pagePeerToPeerDto.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        log.info("GET /p2p/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "/p2p/p2p";
    }

//    todo there is no view for p2p_page. May be add later.
//    @GetMapping("/{id}")
//    public String findById(@PathVariable Long id, Model model) {
//    }

    @GetMapping("/new")
    public String newPeerToPeer(Model model) {
        log.info("GET /p2p/new");
        model.addAttribute("peerToPeer", new PeerToPeerDto());
        model.addAttribute("states", State.values());
        return "p2p/new";
    }

    @PostMapping("/new")
    public String savePeerToPeer(@Valid @ModelAttribute("peerToPeer") PeerToPeerDto peerToPeerDto, BindingResult bindingResult, Model model) {
        saveValidator.validate(peerToPeerDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /p2p/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            model.addAttribute("states", State.values());
            return "p2p/new";
        }
        peerToPeerService.save(peerToPeerDto);
        log.info("POST /p2p/new CREATED NEW RECORD: {}", peerToPeerDto);
        return "redirect:/p2p/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return peerToPeerService.findById(id)
                .map(peerToPeerDto -> {
                    log.info("GET /p2p/{}/edit : {}", id, peerToPeerDto);
                    model.addAttribute("peerToPeer", peerToPeerDto);
                    model.addAttribute("states", State.values());
                    return "p2p/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /p2p/{}/edit RECORD WITH ID {} NOT FOUND", id, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("peerToPeer") PeerToPeerDto peerToPeerDto, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            log.warn("POST /p2p/{} FAIL UPDATE RECORD: {}", id, bindingResult.getAllErrors());
            return "/p2p/edit";
        }

        return peerToPeerService.update(id, peerToPeerDto)
                .map(it -> {
                    log.info("POST /p2p/{} WAS UPDATED: {}", id, it);
                    return "redirect:/p2p/";
                })
                .orElseThrow(() -> {
                            log.warn("POST /p2p/{} RECORD WITH ID {} NOT FOUND", id, id);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!peerToPeerService.delete(id)) {
            log.warn("DELETE /p2p/{} RECORD WITH ID {} NOT FOUND", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /p2p/{} RECORD WITH ID {} WAS DELETED", id, id);
        return "redirect:/p2p/";
    }
}
