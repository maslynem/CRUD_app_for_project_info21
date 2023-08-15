package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.s21school.dao.function.SuccessfulChecksFunction;
import ru.s21school.dao.function.TransferredPointsHumanReadFunction;
import ru.s21school.dao.procedure.AddP2pCheckProcedure;
import ru.s21school.dao.procedure.AddVerterProcedure;
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.dto.operationDto.AddP2pCheckParametersDto;
import ru.s21school.dto.operationDto.AddVerterCheckParametersDto;
import ru.s21school.dto.operationDto.SuccessfulCheckReadDto;
import ru.s21school.mapper.transferredPointMapper.TransferedPointsHumanReadMapper;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcedureService {
    private final AddP2pCheckProcedure addP2pCheckProcedure;
    private final AddVerterProcedure addVerterProcedure;
    private final TransferredPointsHumanReadFunction transferredPointsHumanReadFunction;
    private final TransferedPointsHumanReadMapper transferedPointsHumanReadMapper;
    private final SuccessfulChecksFunction successfulChecksFunction;

    public void executeAddP2pCheckProcedure(AddP2pCheckParametersDto dto) {
        addP2pCheckProcedure.execute(dto.getCheckingPeer(), dto.getCheckedPeer(), dto.getTaskTitle(), dto.getState(), dto.getCheckTime());
    }

    public void executeAddVerterCheckProcedure(AddVerterCheckParametersDto dto) {
        addVerterProcedure.execute(dto.getCheckedPeer(), dto.getTaskTitle(), dto.getState(), dto.getCheckTime());
    }

    public List<TransferredPointDto> executeTransferredPointsHumanRead() {
        List<Map<String, Object>> execute = transferredPointsHumanReadFunction.execute();
        return transferedPointsHumanReadMapper.map(execute);
    }

    public List<SuccessfulCheckReadDto> executeSuccessfulChecksFunction() {
        return successfulChecksFunction.execute();
    }



}
