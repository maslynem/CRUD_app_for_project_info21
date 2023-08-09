package ru.s21school.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.s21school.dto.PagePeerDto;
import ru.s21school.dto.PeerDto;
import ru.s21school.service.PeerService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/peers")
public class PeerController {

    private final PeerService peerService;

    @GetMapping("/page-{page}")
    public String peersPage(@PathVariable Integer page,
                            @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                            @RequestParam(required = false, defaultValue = "nickname") String sortField,
                            @RequestParam(required = false, defaultValue = "asc") String sortDir,
                            Model model) {
        PagePeerDto pagePeerDto = peerService.findPeersWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("peers", pagePeerDto.getPeersDto());
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
