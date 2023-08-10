package ru.s21school.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.s21school.dto.peerDto.PeerDto;
import ru.s21school.service.PeerService;
import ru.s21school.util.validators.peerValidators.PeerSaveValidator;
import ru.s21school.util.validators.peerValidators.PeerUpdateValidator;

import javax.validation.Valid;

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
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagePeerDto.getTotalPages());
        model.addAttribute("totalItems", pagePeerDto.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "peers/peers";
    }

    @GetMapping("/{nickname}")
    public String findByNickname(@PathVariable String nickname, Model model) {
        return peerService.findById(nickname)
                .map(peer -> {
                    model.addAttribute("peer", peer);
                    return "peers/peer_page";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/new")
    public String newPeer(Model model) {
        model.addAttribute("peer", new PeerDto());
        return "peers/new";
    }

    @PostMapping("/new")
    public String savePeer(@Valid @ModelAttribute("peer") PeerDto peerDto, BindingResult bindingResult) {
        peerSaveValidator.validate(peerDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "peers/new";
        }
        peerService.save(peerDto);
        return "redirect:/peers/";
    }

    @GetMapping("/{nickname}/edit")
    public String edit(Model model, @PathVariable String nickname) {
        return peerService.findById(nickname)
                .map(peerDto -> {
                    model.addAttribute("peer", peerDto);
                    return "peers/edit";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("{nickname}")
    public String update(@Valid @ModelAttribute("peer") PeerDto peerDto, BindingResult bindingResult, @PathVariable String nickname) {
        peerUpdateValidator.validate(peerDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/peers/{nickname}/edit";
        }
        return peerService.update(nickname, peerDto)
                .map(it -> "redirect:/peers/{nickname}/")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{nickname}")
    public String delete(@PathVariable String nickname) {
        if (!peerService.delete(nickname)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/peers/";
    }
}
