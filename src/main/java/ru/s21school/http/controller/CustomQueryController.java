package ru.s21school.http.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.s21school.service.CustomQueryService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/operations/query")
public class CustomQueryController {
    private final CustomQueryService customQueryService;

    @ExceptionHandler(SQLGrammarException.class)
    public String handleSQLGrammarException(SQLGrammarException exception, Model model) {
        log.warn("handle exception: SQLGrammarException. Message: {}", exception.getMessage());
        model.addAttribute("errorMessage", "SQL sent to the database server was invalid (syntax error, invalid object references, etc).");
        return "customQuery/query";
    }

    @GetMapping
    String showQueryPage(Model model) {
        model.addAttribute("query", "");
        return "/customQuery/query";
    }

    @PostMapping
    String executeQuery(@RequestParam("query") String query, Model model) {
        log.info("POST /query QUERY is [{}]", query);
        List<String> list = new ArrayList<>();
        int updateResult;
        if (query.toLowerCase().contains("select")) {
            list = customQueryService.executeSelect(query);
            updateResult = list.size();
        } else {
            updateResult = customQueryService.executeUpdate(query);
        }
        model.addAttribute("selectResults", list);
        model.addAttribute("updateResults", updateResult);
        model.addAttribute("query", query);
        log.info("QUERY [{}] WAS EXECUTED", query);
        return "/customQuery/query";
    }
}
