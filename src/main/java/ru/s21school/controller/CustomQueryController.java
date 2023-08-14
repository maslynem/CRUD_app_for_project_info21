package ru.s21school.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.s21school.service.CustomQueryService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/query")
public class CustomQueryController {
    private final CustomQueryService customQueryService;

    @GetMapping
    String showQueryPage() {
        return "/customQuery/query";
    }

    @PostMapping
    String executeQuery(@RequestParam("query") String query) {
        log.info("POST /query QUERY is {}", query);
        customQueryService.execute(query);
        return "/customQuery/query";
    }
}
