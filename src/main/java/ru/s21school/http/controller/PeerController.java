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
import ru.s21school.dto.PeerDto;
import ru.s21school.http.controllerUtil.ControllerUtil;
import ru.s21school.service.PeerService;
import ru.s21school.util.validator.peerValidator.PeerSaveValidator;
import ru.s21school.util.validator.peerValidator.PeerUpdateValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/peers")
public class PeerController {

    private final PeerService peerService;
    private final PeerSaveValidator peerSaveValidator;
    private final PeerUpdateValidator peerUpdateValidator;

    @GetMapping()
    public String peersPageDefault() {
        return "redirect:/peers/page-0";
    }

    @GetMapping("/page-{page}")
    public String peersPage(@PathVariable Integer page,
                            @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                            @RequestParam(required = false, defaultValue = "nickname") String sortField,
                            @RequestParam(required = false, defaultValue = "asc") String sortDir,
                            Model model) {
        Page<PeerDto> pagePeerDto = peerService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("peers", pagePeerDto.getContent());

        ControllerUtil.setModelPagination(model, pagePeerDto, page, pageSize, sortField, sortDir);
        log.info("GET /peers/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "peers/peers";
    }

    @GetMapping("/{nickname}")
    public String findByNickname(@PathVariable String nickname, Model model) {
        return peerService.findById(nickname)
                .map(peer -> {
                    log.info("GET /nickname/{} : {}", nickname, peer);
                    model.addAttribute("peer", peer);
                    return "peers/peer_page";
                }).orElseThrow(() -> {
                    log.warn("GET /nickname/{} RECORD WITH nickname {} NOT FOUND", nickname, nickname);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @GetMapping("/new")
    public String newPeer(Model model) {
        log.info("GET /peers/new");
        model.addAttribute("peer", new PeerDto());
        return "peers/new";
    }

    @PostMapping("/new")
    public String savePeer(@Valid @ModelAttribute("peer") PeerDto peerDto, BindingResult bindingResult) {
        peerSaveValidator.validate(peerDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /peers/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            return "peers/new";
        }
        peerService.save(peerDto);
        log.info("POST /peers/new CREATED NEW RECORD: {}", peerDto);
        return "redirect:/peers/";
    }

    @GetMapping("/{nickname}/edit")
    public String edit(Model model, @PathVariable String nickname) {
        return peerService.findById(nickname)
                .map(peerDto -> {
                    log.info("GET /peers/{}/edit : {}", nickname, peerDto);
                    model.addAttribute("peer", peerDto);
                    return "peers/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /peers/{}/edit RECORD WITH NICKNAME {} NOT FOUND", nickname, nickname);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("{nickname}")
    public String update(@Valid @ModelAttribute("peer") PeerDto peerDto, BindingResult bindingResult, @PathVariable String nickname) {
        peerUpdateValidator.validate(peerDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /peers/{} FAIL UPDATE RECORD: {}", nickname, bindingResult.getAllErrors());
            return "redirect:/peers/{nickname}/edit";
        }
        return peerService.update(nickname, peerDto)
                .map(it -> {
                    log.info("POST /peers/{} WAS UPDATED: {}", nickname, it);
                    return "redirect:/peers/{nickname}/";
                })
                .orElseThrow(() -> {
                    log.warn("POST /peers/{} RECORD WITH ID {} NOT FOUND", nickname, nickname);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @DeleteMapping("/{nickname}")
    public String delete(@PathVariable String nickname) {
        if (!peerService.delete(nickname)) {
            log.warn("DELETE /peers/{} RECORD WITH NICKNAME {} NOT FOUND", nickname, nickname);

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /tasks/{} RECORD WITH TITLE {} WAS DELETED", nickname, nickname);
        return "redirect:/peers/";
    }
}
