package ru.s21school.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PeerValidator implements Validator {
//    private final PeerDao peerDao;

//    @Autowired
//    public PeerValidator(PeerDao peerDao) {
//        this.peerDao = peerDao;
//    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
//        return Peers.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
//        Peers peer = (Peers) target;
//        Optional<Peers> byEmail = peerDao.getByEmail(peer.getEmail());
//        if (byEmail.isPresent() && !byEmail.get().getEmail().equals(peer.getEmail())) {
//            errors.rejectValue("email", "", "This email is already taken");
//        }
    }
}
