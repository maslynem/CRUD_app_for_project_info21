package ru.s21school.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.s21school.dto.TaskDto;
import ru.s21school.service.TaskService;
import ru.s21school.util.validator.taskValidator.TaskSaveValidator;
import ru.s21school.util.validator.taskValidator.TaskUpdateValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskSaveValidator taskSaveValidator;
    private final TaskUpdateValidator taskUpdateValidator;

    @GetMapping()
    public String tasksPageDefault() {
        return "redirect:/tasks/page-0";
    }

    @GetMapping("/page-{page}")
    public String tasksPage(@PathVariable Integer page,
                            @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                            @RequestParam(required = false, defaultValue = "title") String sortField,
                            @RequestParam(required = false, defaultValue = "asc") String sortDir,
                            Model model) {
        Page<TaskDto> pageTaskDto = taskService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("tasks", pageTaskDto.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageTaskDto.getTotalPages());
        model.addAttribute("totalItems", pageTaskDto.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        log.info("GET /tasks/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "/tasks/tasks";
    }

    @GetMapping("/{title}")
    public String findByTitle(@PathVariable String title, Model model) {
        return taskService.findById(title)
                .map(task -> {
                    log.info("GET /tasks/{} : {}", title, task);
                    model.addAttribute("task", task);
                    return "tasks/task_page";
                }).orElseThrow(() -> {
                    log.warn("GET /tasks/{} RECORD WITH TITLE {} NOT FOUND", title, title);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @GetMapping("/new")
    public String newTask(Model model) {
        log.info("GET /tasks/new");
        model.addAttribute("task", new TaskDto());
        return "tasks/new";
    }

    @PostMapping("/new")
    public String saveTask(@Valid @ModelAttribute("task") TaskDto taskDto, BindingResult bindingResult) {
        taskSaveValidator.validate(taskDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /tasks/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            return "tasks/new";
        }
        taskService.save(taskDto);
        log.info("POST /tasks/new CREATED NEW RECORD: {}", taskDto);
        return "redirect:/tasks/";
    }

    @GetMapping("/{title}/edit")
    public String edit(Model model, @PathVariable String title) {
        return taskService.findById(title)
                .map(taskDto -> {
                    log.info("GET /tasks/{}/edit : {}", title, taskDto);
                    model.addAttribute("task", taskDto);
                    return "tasks/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /tasks/{}/edit RECORD WITH TITLE {} NOT FOUND", title, title);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("/{title}")
    public String update(@Valid @ModelAttribute("task") TaskDto taskDto, BindingResult bindingResult, @PathVariable String title) {
        taskUpdateValidator.validate(taskDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /tasks/{} FAIL UPDATE RECORD: {}", title, bindingResult.getAllErrors());
            return "tasks/edit";
        }

        return taskService.update(title, taskDto)
                .map(it -> {
                    log.info("POST /tasks/{} WAS UPDATED: {}", title, it);
                    return "redirect:/tasks/{title}/";
                })
                .orElseThrow(() -> {
                            log.warn("POST /tasks/{} RECORD WITH ID {} NOT FOUND", title, title);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{title}")
    public String delete(@PathVariable String title) {
        if (!taskService.delete(title)) {
            log.warn("DELETE /tasks/{} RECORD WITH TITLE {} NOT FOUND", title, title);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /tasks/{} RECORD WITH TITLE {} WAS DELETED", title, title);
        return "redirect:/tasks/";
    }
}
