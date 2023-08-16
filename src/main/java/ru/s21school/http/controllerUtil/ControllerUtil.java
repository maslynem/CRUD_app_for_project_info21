package ru.s21school.http.controllerUtil;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

@UtilityClass
public class ControllerUtil {
    public static void setModelPagination(Model model, Page<?> page, int currentPage,
                                          Integer pageSize, String sortField,
                                          String sortDir) {
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
    }
}
