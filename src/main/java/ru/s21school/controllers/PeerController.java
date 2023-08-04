package ru.s21school.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.s21school.dto.PeerDto;
import ru.s21school.service.PeerService;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
@RequestMapping("/peers")
public class PeerController {

    private final PeerService peerService;

    @GetMapping()
    public String peersPage(Model model) {
        model.addAttribute("peers", peerService.findAll());
        return "peers/peers";
    }
//
//    @GetMapping("/{nickname}")
//    public String showPeer(@PathVariable String nickname, Model model) {
//        model.addAttribute("peer", peerService.findByNickname(nickname));
//        return "peers/peer_page";
//    }
//
//    @GetMapping("/{nickname}/edit")
//    public String edit(Model model, @PathVariable String nickname) {
//        model.addAttribute("peer", peerService.findByNickname(nickname));
//        return "peers/edit";
//    }
//
//    @PatchMapping("/{nickname}")
//    public String update(@Valid @ModelAttribute("peer") PeerDto peer, BindingResult bindingResult,
//                         @PathVariable String nickname) {
//        if (bindingResult.hasErrors()) {
//            return "peer/edit";
//        }
//        peerService.update(peer);
//        return "redirect:/peers";
//    }


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
