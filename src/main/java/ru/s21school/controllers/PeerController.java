package ru.s21school.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.s21school.dao.PeerDao;
import ru.s21school.peerDto.Peer;
import ru.s21school.util.PeerValidator;

import javax.validation.Valid;


@Controller
@RequestMapping("/peers")
public class PeerController {

    private final PeerDao peerDao;
    private final PeerValidator peerValidator;

    @Autowired
    public PeerController(PeerDao peerDao, PeerValidator peerValidator) {
        this.peerDao = peerDao;
        this.peerValidator = peerValidator;
    }

    @GetMapping()
    public String showAllPeers(Model model) {
        model.addAttribute("peers", peerDao.getAllPeer());
        return "peer/all";
    }

    @GetMapping("/{id}")
    public String showPeer(@PathVariable("id") int id, Model model) {
        model.addAttribute("peer", peerDao.getById(id).orElse(null));
        return "peer/show";
    }

    @GetMapping("/new")
    public String newPeer(Model model) {
        model.addAttribute("peer", new Peer());
        return "peer/new";
    }

    @PostMapping()
    public String create(@Valid @ModelAttribute("peer") Peer peer, BindingResult bindingResult) {
        peerValidator.validate(peer,bindingResult);
        if (bindingResult.hasErrors()) {
            return "peer/new";
        }
        peerDao.save(peer);
        return "redirect:/peers";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("peer", peerDao.getById(id).orElse(null));
        return "peer/edit";
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("peer") Peer peer, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        peerValidator.validate(peer,bindingResult);
        if (bindingResult.hasErrors()) {
            return "peer/edit";
        }
        peerDao.update(id, peer);
        return "redirect:/peers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peerDao.delete(id);
        return "redirect:/peers";
    }
}
