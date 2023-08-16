package ru.s21school.http.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.s21school.exceptions.NoSuchCheckException;
import ru.s21school.exceptions.NoSuchPeerException;
import ru.s21school.exceptions.NoSuchTaskException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException exception, Model model) {
        log.warn("handle exception: ResponseStatusException. Status: {}", exception.getStatus());
        switch (exception.getStatus()) {
            case NOT_FOUND: {
                return "errors/404";
            }
            case INTERNAL_SERVER_ERROR: {
                model.addAttribute("messageError", exception.getMessage());
                return "errors/500";
            }
            default:
                return "errors/404";
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFoundException() {
        log.warn("handle exception: NoHandlerFoundException.");
        return "errors/404";
    }

    @ExceptionHandler(NoSuchPeerException.class)
    public String handleNoSuchPeerException(NoSuchPeerException exception, Model model) {
        log.warn("handle exception: NoSuchPeerException. Message: {}", exception.getMessage());
        model.addAttribute("messageError", exception.getMessage());
        return "errors/500";
    }

    @ExceptionHandler(NoSuchTaskException.class)
    public String handleNoSuchTaskException(NoSuchTaskException exception, Model model) {
        log.warn("handle exception: NoSuchTaskException. Message: {}", exception.getMessage());
        model.addAttribute("messageError", exception.getMessage());
        return "errors/500";
    }

    @ExceptionHandler(NoSuchCheckException.class)
    public String handleNoSuchCheckException(NoSuchCheckException exception, Model model) {
        log.warn("handle exception: NoSuchCheckException. Message: {}", exception.getMessage());
        model.addAttribute("messageError", exception.getMessage());
        return "errors/500";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException exception, Model model) {
        String constraintName = exception.getConstraintName();
        log.warn("handle exception: ConstraintViolationException. Message: {} Constraint: {}", exception.getMessage(), constraintName);
        model.addAttribute("messageError", exception.getMessage());
        model.addAttribute("reasonError", getErrorReasonByConstraint(constraintName));
        return "errors/500";
    }

    private String getErrorReasonByConstraint(String constraint) {
        switch (constraint) {
            case "checks_peer_fkey":
                return "There is a record in Checks, that has a reference to this peer";
            case "checks_task_fkey":
                return "There is a record in Checks, that has a reference to this task";
            case "uk_p2p":
                return "In P2P table check id and state must be unique";
            case "p2p_check_id_fkey":
                return "There is a record in P2P, that has a reference to this check";
            case "p2p_checking_peer_fkey":
                return "There is a record in P2P, that has a reference to this peer";
            case "verter_check_id_fkey":
                return "There is a record in Verter, that has a reference to this check";
            case "xp_check_id_fkey":
                return "There is a record in XP, that has a reference to this check";
            case "transferred_points_checking_peer_fkey":
            case "transferred_points_checked_peer_fkey":
                return "There is a record in transferred_points, that has a reference to this peer";
            case "uk_friends":
            case "uk_recommendations":
                return "There is already exist a record with this nicknames";
            case "friends_peer1_fkey":
            case "friends_peer2_fkey":
                return "There is a record in Friends, that has a reference to this peer";
            case "recommendations_peer_fkey":
                return "There is a record in Recommendations, that has a reference to this peer";
            case "time_tracking_peer_fkey":
                return "There is a record in Time Tracking, that has a reference to this peer";
            default: return "Unknown constraint";
        }
    }
}