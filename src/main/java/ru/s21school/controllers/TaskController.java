package ru.s21school.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.s21school.dto.taskDto.TaskDto;
import ru.s21school.service.TaskService;
import ru.s21school.util.validators.taskValidators.TaskSaveValidator;
import ru.s21school.util.validators.taskValidators.TaskUpdateValidator;

import javax.validation.Valid;

@Repository
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
        return "tasks/tasks";
    }

    @GetMapping("/{title}")
    public String findByTitle(@PathVariable String title, Model model) {
        return taskService.findById(title)
                .map(task -> {
                    model.addAttribute("task", task);
                    return "tasks/task_page";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

//    @GetMapping("/new")
//    public String newTask(Model model) {
//        model.addAttribute("task", new TaskDto());
//        return "tasks/new";
//    }
//
//    @PostMapping("/new")
//    public String saveTask(@Valid @ModelAttribute("task") TaskDto taskDto, BindingResult bindingResult) {
//        taskSaveValidator.validate(taskDto, bindingResult);
//        if (bindingResult.hasErrors()) {
//            return "tasks/new";
//        }
//        taskService.save(taskDto);
//        return "redirect:/tasks/";
//    }
//
//    @GetMapping("/{title}/edit")
//    public String edit(Model model, @PathVariable String title) {
//        return taskService.findById(title)
//                .map(taskDto -> {
//                    model.addAttribute("task", taskDto);
//                    return "tasks/edit";
//                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//    }
//
//    @PatchMapping("{title}")
//    public String update(@Valid @ModelAttribute("task") TaskDto taskDto, BindingResult bindingResult, @PathVariable String title) {
//        taskUpdateValidator.validate(taskDto, bindingResult);
//        if (bindingResult.hasErrors()) {
//            return "redirect:/tasks/{title}/edit";
//        }
//        return taskService.update(title, taskDto)
//                .map(it -> "redirect:/tasks/{title}/")
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//    }
//
//    @DeleteMapping("/{title}")
//    public String delete(@PathVariable String title) {
//        if (!taskService.delete(title)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        return "redirect:/tasks/";
//    }
}
