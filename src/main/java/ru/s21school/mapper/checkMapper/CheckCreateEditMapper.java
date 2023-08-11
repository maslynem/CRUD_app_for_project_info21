package ru.s21school.mapper.checkMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.checkDto.CheckDto;
import ru.s21school.entity.Check;
import ru.s21school.entity.Peer;
import ru.s21school.entity.Task;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.PeerRepository;
import ru.s21school.repository.TaskRepository;

@Component
@RequiredArgsConstructor
public class CheckCreateEditMapper implements Mapper<CheckDto, Check> {
    private final TaskRepository taskRepository;
    private final PeerRepository peerRepository;

    @Override
    public Check map(CheckDto object) {
        Peer peer = peerRepository
                .findById(object.getPeerNickname())
                .orElseThrow(() -> new RuntimeException("No peer with nickname " + object.getPeerNickname()));
        Task task = taskRepository
                .findById(object.getTaskTitle())
                .orElseThrow(() -> new RuntimeException("No task with title " + object.getTaskTitle()));
        return new Check(
                object.getId(),
                peer,
                task,
                object.getDate()
        );
    }

    @Override
    public Check map(CheckDto fromObject, Check toObject) {
        Peer peer = peerRepository
                .findById(fromObject.getPeerNickname())
                .orElseThrow(() -> new RuntimeException("No peer with nickname " + fromObject.getPeerNickname()));
        Task task = taskRepository
                .findById(fromObject.getTaskTitle())
                .orElseThrow(() -> new RuntimeException("No task with title " + fromObject.getTaskTitle()));

        toObject.setPeer(peer);
        toObject.setTask(task);
        toObject.setDate(fromObject.getDate());
        return toObject;
    }
}
