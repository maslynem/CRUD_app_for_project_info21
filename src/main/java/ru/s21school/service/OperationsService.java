package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.s21school.dao.function.*;
import ru.s21school.dao.procedure.AddP2pCheckProcedure;
import ru.s21school.dao.procedure.AddVerterProcedure;
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.dto.operationDto.AddP2pCheckParametersDto;
import ru.s21school.dto.operationDto.AddVerterCheckParametersDto;
import ru.s21school.functionResult.*;
import ru.s21school.mapper.transferredPointMapper.TransferedPointsHumanReadMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OperationsService {
    private final AddP2pCheckProcedure addP2pCheckProcedure;
    private final AddVerterProcedure addVerterProcedure;
    private final TransferredPointsHumanReadFunction transferredPointsHumanReadFunction;
    private final TransferedPointsHumanReadMapper transferedPointsHumanReadMapper;
    private final SuccessfulChecksFunction successfulChecksFunction;
    private final PeersAllDayInCampusFunction peersAllDayInCampusFunction;
    private final TransferredPointsChangeFunctionV1 transferredPointsChangeFunctionV1;
    private final TransferredPointsChangeFunctionV2 transferredPointsChangeFunctionV2;
    private final CheckedTaskFunction checkedTaskFunction;
    private final TaskBlockFunction taskBlockFunction;
    private final RecommendedPeerFunction recommendedPeerFunction;
    private final TwoBlockCompareFunction twoBlockCompareFunction;
    private final BirthdayCheckFunction birthdayCheckFunction;
    private final CompletedTwoTaskWithoutThirdFunction completedTwoTaskWithoutThirdFunction;
    private final TaskCountFunction taskCountFunction;
    private final LuckyDaysFunction luckyDaysFunction;
    private final TopPeerFunction topPeerFunction;
    private final CampusComingFunction campusComingFunction;
    private final LeavingFromCampusFunction leavingFromCampusFunction;
    private final EarlyComingFunction earlyComingFunction;
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

    public List<SuccessfulCheckResult> executeSuccessfulChecksFunction() {
        return successfulChecksFunction.execute();
    }

    public List<String> executePeersAllDayInCampusFunction(LocalDate day) {
        return peersAllDayInCampusFunction.execute(day);
    }

    public List<TransferredPointChangeResult> executeTransferredPointsChangeFunctionV1() {
        return transferredPointsChangeFunctionV1.execute();
    }

    public List<TransferredPointChangeResult> executeTransferredPointsChangeFunctionV2() {
        return transferredPointsChangeFunctionV2.execute();
    }

    public List<CheckedTaskResult> executeCheckedTaskFunction() {
        return checkedTaskFunction.execute();
    }

    public List<TaskBlockResult> executeTaskBlockFunction(String blockName) {
        return taskBlockFunction.execute(blockName);
    }

    public List<RecommendedPeerResult> executeRecommendedPeerFunction() {
        return recommendedPeerFunction.execute();
    }

    public List<TwoBlockCompareResult> executeTwoBlockCompareFunction(String firstBlock, String secondBlock) {
        return twoBlockCompareFunction.execute(firstBlock, secondBlock);
    }

    public List<BirthdayCheckResult> executeBirthdayCheckFunction() {
        return birthdayCheckFunction.execute();
    }

    public List<String> executeCompletedTwoTaskWithoutThirdFunction(String firstTask, String secondTask, String thirdTask) {
        return completedTwoTaskWithoutThirdFunction.execute(firstTask, secondTask, thirdTask);
    }
    public List<TaskCountResult> executeTaskCountFunction() {
        return taskCountFunction.execute();
    }
    public List<LocalDate> executeLuckyDaysFunction(Integer n) {
        return luckyDaysFunction.execute(n);
    }

    public TopPeerResult executeTopPeerFunction() {
        return topPeerFunction.execute();
    }

    public List<String> executeCampusComingFunction(LocalTime time, Integer n) {
        return campusComingFunction.execute(time, n);
    }

    public List<String> executeLeavingFromCampusFunction(Integer n, Integer m) {
        return leavingFromCampusFunction.execute(n, m);
    }
    public List<EarlyComingResult> executeEarlyComingFunction() {
        return earlyComingFunction.execute();
    }

}
