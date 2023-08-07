package ru.s21school.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.s21school.dto.PagePeerDto;
import ru.s21school.dto.PeerDto;
import ru.s21school.service.PeerService;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/peers")
public class PeerController {

    private final PeerService peerService;

    @GetMapping()
    public String peersPage(@RequestParam(required = false) Integer limit,
                            @RequestParam(required = false) Integer offset,
                            Model model) {
        if (limit == null) limit = 10;
        if (offset == null) offset = 0;
        PagePeerDto pagePeerDto = peerService.findAllPageable(PageRequest.of(offset, limit));
        model.addAttribute("peers", pagePeerDto.getPeersDto());
        model.addAttribute("currentPage", offset);
        model.addAttribute("totalPages", pagePeerDto.getTotalPages());
        model.addAttribute("totalItems", pagePeerDto.getTotalElements());
        model.addAttribute("limit", limit);
        return "peers/peers";
    }

    @GetMapping("/{nickname}")
    public String showPeer(@PathVariable String nickname, Model model) {
        PeerDto peer = peerService.findByNickname(nickname);
        model.addAttribute("peer", peer);
        return "peers/peer_page";
    }

    @GetMapping("/{nickname}/edit")
    public String edit(Model model, @PathVariable String nickname) {
        model.addAttribute("peer", peerService.findByNickname(nickname));
        return "peers/edit";
    }

    @PatchMapping("/{nickname}")
    public String update(@Valid @ModelAttribute("peer") PeerDto peer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "peers/edit";
        }
        peerService.update(peer);
        return "redirect:/peers";
    }


//
//    @GetMapping("/new")
//    public String newPeer(Model model) {
//        model.addAttribute("peer", new Peer());
//        return "peer/new";
//    }
//
//    @PostMapping()
//    public String create(@Valid @ModelAttribute("peer") Peer peer, BindingResult bindingResult) {
//        peerValidator.validate(peer,bindingResult);
//        if (bindingResult.hasErrors()) {
//            return "peer/new";
//        }
//        peerDao.save(peer);
//        return "redirect:/peers";
//    }
//

//

//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") int id) {
//        peerDao.delete(id);
//        return "redirect:/peers";
//    }

}
