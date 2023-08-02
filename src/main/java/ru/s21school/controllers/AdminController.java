package ru.s21school.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.s21school.dao.PeerDao;
import ru.s21school.peerDto.Peer;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PeerDao peerDao;

    @Autowired
    public AdminController(PeerDao peerDao) {
        this.peerDao = peerDao;
    }

    @GetMapping
    public String adminPage(Model model, @ModelAttribute("peer") Peer peer) {
        model.addAttribute("peers", peerDao.getAllPeer());
        return "adminPage";
    }

    @PostMapping("/add")
    public String setAdmin(@ModelAttribute("peer") Peer peer) {
        System.out.println(peer.getId());
        return "redirect:/peers";
    }
}
