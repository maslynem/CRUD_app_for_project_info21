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
import ru.s21school.service.OperationsService;

import javax.validation.Valid;
import java.time.LocalDate;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/operations")
public class OperationsController {
    private final OperationsService operationsService;

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
        operationsService.executeAddP2pCheckProcedure(addP2PCheckParametersDto);
        model.addAttribute("addP2pCheckSuccess", true);
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
        operationsService.executeAddVerterCheckProcedure(addVerterCheckParametersDto);
        model.addAttribute("addVerterCheckSuccess", true);
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
        model.addAttribute("entities", operationsService.executeTransferredPointsHumanRead());
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
        model.addAttribute("entities", operationsService.executeSuccessfulChecksFunction());
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
        model.addAttribute("entities", operationsService.executePeersAllDayInCampusFunction(date));
        return "/operations/all_day_in_campus";
    }

    @GetMapping("/transferred-points-change-v1")
    String showTransferredPointsChangeV1() {
        log.info("GET /operations/transferred-points-change");
        return "/operations/transferred_points_change_v1";
    }

    @PostMapping("/transferred-points-change-v1")
    String executeFunctionTransferredPointsChangeV1(Model model) {
        log.info("POST /operations/transferred-points-change");
        model.addAttribute("entities", operationsService.executeTransferredPointsChangeFunctionV1());
        return "/operations/transferred_points_change_v1";
    }

    @GetMapping("/transferred-points-change-v2")
    String showTransferredPointsChangeV2() {
        log.info("GET /operations/transferred-points-change");
        return "/operations/transferred_points_change_v2";
    }

    @PostMapping("/transferred-points-change-v2")
    String executeFunctionTransferredPointsChangeV2(Model model) {
        log.info("POST /operations/transferred-points-change");
        model.addAttribute("entities", operationsService.executeTransferredPointsChangeFunctionV2());
        return "/operations/transferred_points_change_v2";
    }

    @GetMapping("/checked-tasks")
    String showCheckedTasks() {
        log.info("GET /operations/checked-tasks");
        return "/operations/checked_tasks";
    }

    @PostMapping("/checked-tasks")
    String executeFunctionCheckedTasks(Model model) {
        log.info("POST /operations/checked-tasks");
        model.addAttribute("entities", operationsService.executeCheckedTaskFunction());
        return "/operations/checked_tasks";
    }

    @GetMapping("/task-block")
    String showTaskBlockFunctionPage() {
        log.info("GET /operations/task-block");
        return "/operations/task_block";
    }

    @PostMapping("/task-block")
    String executeFunctionTaskBlock(@RequestParam String blockName, Model model) {
        log.info("POST /operations/task-block");
        model.addAttribute("entities", operationsService.executeTaskBlockFunction(blockName));
        return "/operations/task_block";
    }

    @GetMapping("/recommended-peer")
    String showRecommendedPeer() {
        log.info("GET /operations/recommended-peer");
        return "/operations/recommended_peer";
    }

    @PostMapping("/recommended-peer")
    String executeFunctionRecommendedPeer(Model model) {
        log.info("POST /operations/recommended-peer");
        model.addAttribute("entities", operationsService.executeRecommendedPeerFunction());
        return "/operations/recommended_peer";
    }

    @GetMapping("/blocks-comparing")
    String showBlocksComparing() {
        log.info("GET /operations/blocks-comparing");
        return "/operations/blocks_comparing";
    }

    @PostMapping("/blocks-comparing")
    String executeFunctionRecommendedPeer(@RequestParam String firstBlock, @RequestParam String secondBlock, Model model) {
        log.info("POST /operations/blocks-comparing");
        model.addAttribute("entities", operationsService.executeTwoBlockCompareFunction(firstBlock, secondBlock));
        return "/operations/blocks_comparing";
    }


}
