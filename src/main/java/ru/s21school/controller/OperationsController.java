package ru.s21school.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.s21school.dto.operationDto.AddP2pCheckParametersDto;
import ru.s21school.dto.operationDto.AddVerterCheckParametersDto;
import ru.s21school.service.ProcedureService;

import javax.validation.Valid;
import java.time.LocalDate;

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
        model.addAttribute("addP2pCheck", new AddP2pCheckParametersDto());
        return "/operations/add_p2p_check";
    }

    @PostMapping("/add-p2p-check")
    String executeAddP2pCheckProcedure(@Valid @ModelAttribute("addP2pCheck") AddP2pCheckParametersDto addP2PCheckParametersDto, BindingResult bindingResult, Model model) {
        log.info("POST /operations/add-p2p-check");
        if (bindingResult.hasErrors()) {
            log.warn("bindingResult has errors: {}", bindingResult.getAllErrors());
            return "/operations/add_p2p_check";
        }
        log.info("ready to execute procedure add_p2p_check: {}", addP2PCheckParametersDto);
        procedureService.executeAddP2pCheckProcedure(addP2PCheckParametersDto);
        model.addAttribute("addP2pCheckSuccess", true);
        log.info("procedure add_p2p_check was executed successfully");
        return "/operations/add_p2p_check";
    }

    @GetMapping("/add-verter-check")
    String showAddVerterCheckPage(Model model) {
        log.info("GET /operations/add-verter-check");
        model.addAttribute("addVerterCheck", new AddVerterCheckParametersDto());
        return "/operations/add_verter_check";
    }

    @PostMapping("/add-verter-check")
    String executeAddVerterCheck(@Valid @ModelAttribute("AddVerterCheck") AddVerterCheckParametersDto addVerterCheckParametersDto, BindingResult bindingResult, Model model) {
        log.info("POST /operations/add-verter-check");
        if (bindingResult.hasErrors()) {
            log.warn("bindingResult has errors: {}", bindingResult.getAllErrors());
            return "/operations/add_verter_check";
        }
        log.info("ready to execute procedure add_verter_check: {}", addVerterCheckParametersDto);
        procedureService.executeAddVerterCheckProcedure(addVerterCheckParametersDto);
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

    @GetMapping("/successful-checks")
    String showSuccessfulChecks() {
        log.info("GET /operations/successful-checks");
        return "/operations/successful_checks";
    }

    @PostMapping("/successful-checks")
    String executeFunctionSuccessfulChecks(Model model) {
        log.info("POST /operations/successful-checks");
        model.addAttribute("entities", procedureService.executeSuccessfulChecksFunction());
        return "/operations/successful_checks";
    }

    @GetMapping("/all-day-in-campus")
    String showAllDayInCampus() {
        log.info("GET /operations/all-day-in-campus");
        return "/operations/all_day_in_campus";
    }

    @PostMapping("/all-day-in-campus")
    String executeFunctionAllDayInCampus(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        log.info("POST /operations/all-day-in-campus");
        model.addAttribute("entities", procedureService.executePeersAllDayInCampusFunction(date));
        return "/operations/all_day_in_campus";
    }
}
