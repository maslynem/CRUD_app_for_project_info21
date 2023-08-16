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
import ru.s21school.dto.FriendDto;
import ru.s21school.entity.CheckState;
import ru.s21school.service.FriendService;
import ru.s21school.util.validator.friendValidator.FriendSaveEditValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;
    private final FriendSaveEditValidator saveEditValidator;

    @GetMapping()
    public String friendsPageDefault() {
        return "redirect:/friends/page-0";
    }

    @GetMapping("/page-{page}")
    public String friendsPage(@PathVariable Integer page,
                              @RequestParam(required = false, defaultValue = "30") Integer pageSize,
                              @RequestParam(required = false, defaultValue = "peer1") String sortField,
                              @RequestParam(required = false, defaultValue = "asc") String sortDir,
                              Model model) {
        Page<FriendDto> pageFriendDto = friendService.findAllWithPaginationAndSorting(page, pageSize, sortField, sortDir);
        model.addAttribute("friends", pageFriendDto.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageFriendDto.getTotalPages());
        model.addAttribute("totalItems", pageFriendDto.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        log.info("GET /friends/page-{}?pageSize={}&sortField={}&sortDir={}", page, pageSize, sortField, sortDir);
        return "/friends/friends";
    }

//    todo there is no view for friend_page. May be add later.
//    @GetMapping("/{id}")
//    public String findById(@PathVariable Long id, Model model) {
//    }

    @GetMapping("/new")
    public String newFriend(Model model) {
        log.info("GET /friends/new");
        model.addAttribute("friend", new FriendDto());
        model.addAttribute("states", CheckState.values());
        return "friends/new";
    }

    @PostMapping("/new")
    public String saveFriend(@Valid @ModelAttribute("friend") FriendDto friendDto, BindingResult bindingResult, Model model) {
        saveEditValidator.validate(friendDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /friends/new FAIL CREATE NEW RECORD: {}", bindingResult.getAllErrors());
            model.addAttribute("states", CheckState.values());
            return "friends/new";
        }
        friendService.save(friendDto);
        log.info("POST /friends/new CREATED NEW RECORD: {}", friendDto);
        return "redirect:/friends/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        return friendService.findById(id)
                .map(friendDto -> {
                    log.info("GET /friends/{}/edit : {}", id, friendDto);
                    model.addAttribute("friend", friendDto);
                    model.addAttribute("states", CheckState.values());
                    return "friends/edit";
                }).orElseThrow(() -> {
                    log.warn("GET /friends/{}/edit RECORD WITH ID {} NOT FOUND", id, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("friend") FriendDto friendDto, BindingResult bindingResult, @PathVariable Long id) {
        saveEditValidator.validate(friendDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("POST /friends/{} FAIL UPDATE RECORD: {}", id, bindingResult.getAllErrors());
            return "/friends/edit";
        }

        return friendService.update(id, friendDto)
                .map(it -> {
                    log.info("POST /friends/{} WAS UPDATED: {}", id, it);
                    return "redirect:/friends/";
                })
                .orElseThrow(() -> {
                            log.warn("POST /friends/{} RECORD WITH ID {} NOT FOUND", id, id);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (!friendService.delete(id)) {
            log.warn("DELETE /friends/{} RECORD WITH ID {} NOT FOUND", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("DELETE /friends/{} RECORD WITH ID {} WAS DELETED", id, id);
        return "redirect:/friends/";
    }
}
