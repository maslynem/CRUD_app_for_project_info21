package ru.s21school.http.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.s21school.dto.TimeTrackingDto;
import ru.s21school.http.controllerUtil.ControllerUtil;
import ru.s21school.service.TimeTrackingService;
import ru.s21school.util.validator.timeTrackinkValidator.TimeTrackingSaveEditValidator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/time-tracking")
public class TimeTrackingController {


    private final TimeTrackingService timeTrackingService;
    private final TimeTrackingSaveEditValidator saveEditValidator;

    @GetMapping()
    public String timeTrackingPageDefault() {
        return "redirect:/time-tracking/page-0";
    }

    @GetMapping("/page-{page}")
    public String timeTrackingPage(@PathVariable Integer page,
                                        @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                                        @RequestParam(required = false, defaultValue = "peer") String sortField,
                                        @RequestParam(required = false, defaultValue = "asc") String sortDir,
                                        Model model) {
        Page<TimeTrackingDto> pageTimeTrackingDto = timeTrackingService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("entities", pageTimeTrackingDto.getContent());

        ControllerUtil.setModelPagination(model, pageTimeTrackingDto, page, pageSize, sortField, sortDir);

        log.info("GET /time-tracking/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "/timeTracking/time-tracking";
    }

    @GetMapping("/new")
    public String newTimeTracking(Model model) {
        log.info("GET /time-tracking/new");
        model.addAttribute("entity", new TimeTrackingDto());
        model.addAttribute("states", TimeTrackingDto.State.values());
        return "/timeTracking/new";
    }

    @PostMapping("/new")
    public String saveTimeTracking(@Valid @ModelAttribute("entity") TimeTrackingDto timeTrackingDto, BindingResult bindingResult) {
        saveEditValidator.validate(timeTrackingDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /time-tracking/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            return "/timeTracking/new";
        }
        timeTrackingService.save(timeTrackingDto);
        log.info("POST /time-tracking/new CREATED NEW RECORD: {}", timeTrackingDto);
        return "redirect:/time-tracking/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return timeTrackingService.findById(id)
                .map(timeTrackingDto -> {
                    log.info("GET /time-tracking/{}/edit : {}", id, timeTrackingDto);
                    model.addAttribute("entity", timeTrackingDto);
                    model.addAttribute("states", TimeTrackingDto.State.values());
                    return "/timeTracking/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /time-tracking/{}/edit RECORD WITH ID {} NOT FOUND", id, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("entity") TimeTrackingDto timeTrackingDto, BindingResult bindingResult, @PathVariable Long id) {
       saveEditValidator.validate(timeTrackingDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /time-tracking/{} FAIL UPDATE RECORD: {}", id, bindingResult.getAllErrors());
            return "/timeTracking/edit";
        }

        return timeTrackingService.update(id, timeTrackingDto)
                .map(it -> {
                    log.info("POST /time-tracking/{} WAS UPDATED: {}", id, it);
                    return "redirect:/time-tracking/";
                })
                .orElseThrow(() -> {
                            log.warn("POST /time-tracking/{} RECORD WITH ID {} NOT FOUND", id, id);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!timeTrackingService.delete(id)) {
            log.warn("DELETE /time-tracking/{} RECORD WITH ID {} NOT FOUND", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /time-tracking/{} RECORD WITH ID {} WAS DELETED", id, id);
        return "redirect:/time-tracking/";
    }


    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"time_tracking.csv\"");
        timeTrackingService.writeToCsv(servletResponse.getWriter());
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) throws IOException {
        timeTrackingService.readFromCsv(file.getInputStream());
        return "redirect:/time-tracking/";
    }

}
