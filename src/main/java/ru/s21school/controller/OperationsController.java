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
import ru.s21school.dto.operationParametersDto.AddVerterCheckDto;
import ru.s21school.service.ProcedureService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/operations")
public class OperationsController {
    private final ProcedureService procedureService;

    @GetMapping
    String showOperationsPage() {
        log.info("GET /operations");
        return "/operations/operations";
    }

    @GetMapping("/add-p2p-check")
    String showAddP2pCheckPage(Model model) {
        log.info("GET /operations/add-p2p-check");
        model.addAttribute("addP2pCheck", new AddP2pCheckDto());
        return "/operations/add_p2p_check";
    }

    @PostMapping("/add-p2p-check")
    String executeAddP2pCheckProcedure(@Valid @ModelAttribute("addP2pCheck") AddP2pCheckDto addP2pCheckDto, BindingResult bindingResult, Model model) {
        log.info("POST /operations/add-p2p-check");
        if (bindingResult.hasErrors()) {
            log.warn("bindingResult has errors: {}", bindingResult.getAllErrors());
            return "/operations/add_p2p_check";
        }
        log.info("ready to execute procedure add_p2p_check: {}", addP2pCheckDto);
        procedureService.executeAddP2pCheckProcedure(addP2pCheckDto);
        model.addAttribute("addP2pCheckSuccess", true);
        log.info("procedure add_p2p_check was executed successfully");
        return "/operations/add_p2p_check";
    }

    @GetMapping("/add-verter-check")
    String showAddVerterCheckPage(Model model) {
        log.info("GET /operations/add-verter-check");
        model.addAttribute("addVerterCheck", new AddVerterCheckDto());
        return "/operations/add_verter_check";
    }

    @PostMapping("/add-verter-check")
    String executeAddVerterCheck(@Valid @ModelAttribute("AddVerterCheck") AddVerterCheckDto addVerterCheckDto, BindingResult bindingResult, Model model) {
        log.info("POST /operations/add-verter-check");
        if (bindingResult.hasErrors()) {
            log.warn("bindingResult has errors: {}", bindingResult.getAllErrors());
            return "/operations/add_verter_check";
        }
        log.info("ready to execute procedure add_verter_check: {}", addVerterCheckDto);
        procedureService.executeAddVerterCheckProcedure(addVerterCheckDto);
        model.addAttribute("addVerterCheckSuccess", true);
        log.info("procedure add_verter_check was executed successfully");
        return "/operations/add_verter_check";
    }

    @GetMapping("/transferred-points-human-read")
    String showTransferredPointsHumanRead() {
        log.info("GET /operations/transferred-points-human-read");
        return "/operations/transferred_points_human_read";
    }

    @PostMapping("/transferred-points-human-read")
    String executeFunctionTransferredPointsHumanRead(Model model) {
        log.info("POST /operations/transferred-points-human-read");
        model.addAttribute("entities", procedureService.executeTransferredPointsHumanRead());
        return "/operations/transferred_points_human_read";
    }
}
