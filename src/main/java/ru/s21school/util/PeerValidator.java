package ru.s21school.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dao.PeerDao;
import ru.s21school.peerDto.Peer;

import java.util.Optional;

@Component
public class PeerValidator implements Validator {
    private final PeerDao peerDao;

    @Autowired
    public PeerValidator(PeerDao peerDao) {
        this.peerDao = peerDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Peer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Peer peer = (Peer) target;
        Optional<Peer> byEmail = peerDao.getByEmail(peer.getEmail());
        if (byEmail.isPresent() && !byEmail.get().getEmail().equals(peer.getEmail())) {
            errors.rejectValue("email", "", "This email is already taken");
        }
    }
}
