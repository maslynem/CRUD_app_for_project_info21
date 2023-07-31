package ru.s21school.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/peer")
public class PeerController {

    @GetMapping()
    public String showAllPeers(Model model) {

        return "hello_world";
    }

    @GetMapping("/{id}")
    public String showPeer(@PathVariable("id") int id, Model model) {

        return null;
    }


}
