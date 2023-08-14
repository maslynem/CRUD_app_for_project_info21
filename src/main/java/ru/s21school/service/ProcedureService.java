package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.s21school.dao.procedure.AddP2pCheckProcedure;
import ru.s21school.dao.procedure.AddVerterProcedure;
import ru.s21school.dto.operationParametersDto.AddP2pCheckDto;
import ru.s21school.dto.operationParametersDto.AddVerterCheckDto;

@Service
@RequiredArgsConstructor
public class ProcedureService {
    private final AddP2pCheckProcedure addP2pCheckProcedure;
    private final AddVerterProcedure addVerterProcedure;

    public void executeAddP2pCheckProcedure(AddP2pCheckDto dto) {
        addP2pCheckProcedure.execute(dto.getCheckingPeer(), dto.getCheckedPeer(), dto.getTaskTitle(), dto.getState(), dto.getCheckTime());
    }

    public void executeAddVerterCheckProcedure(AddVerterCheckDto dto) {
        addVerterProcedure.execute(dto.getCheckedPeer(), dto.getTaskTitle(), dto.getState(), dto.getCheckTime());
    }
}
