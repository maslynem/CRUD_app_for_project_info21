package ru.s21school.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.s21school.dto.operationParametersDto.AddP2pCheckDto;
import ru.s21school.entity.CheckState;
import ru.s21school.service.PeerToPeerService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/operations")
public class OperationsController {
    private final PeerToPeerService peerService;

   @GetMapping
    String showOperationsPage(Model model) {
       log.info("GET /operations");
       model.addAttribute("addP2pCheck", new AddP2pCheckDto());
       model.addAttribute("states", CheckState.values());
        return "/operations/operations";
    }

    @PostMapping("/1")
    String executeAddP2pCheckProcedure(@Valid @ModelAttribute("addP2pCheck") AddP2pCheckDto addP2pCheckDto, BindingResult bindingResult) {
       log.info("POST /operations/1");
       if (bindingResult.hasErrors()) {
           log.warn("bindingResult has errors: {}", bindingResult.getAllErrors());
           return "/operations/operations";
       }
       log.info("ready to execute procedure addP2pCheck: {}", addP2pCheckDto);
       peerService.executeAddP2pCheck(addP2pCheckDto);
       log.info("procedure addP2pCheck was executed successfully");
       return "/operations/operations";
    }
}
