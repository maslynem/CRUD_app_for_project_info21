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

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CheckCreateEditMapper implements Mapper<CheckDto, Check> {
    private final TaskRepository taskRepository;
    private final PeerRepository peerRepository;

    @Override
    public Check map(CheckDto object) {
        Optional<Peer> peer = peerRepository.findById(object.getPeerNickname());
        Optional<Task> task = taskRepository.findById(object.getTaskTitle());
        return new Check(
                object.getId(),
                peer.get(),
                task.get(),
                object.getDate()
        );
    }

    @Override
    public Check map(CheckDto fromObject, Check toObject) {
        Optional<Peer> peer = peerRepository.findById(fromObject.getPeerNickname());
        Optional<Task> task = taskRepository.findById(fromObject.getTaskTitle());
        toObject.setPeer(peer.get());
        toObject.setTask(task.get());
        toObject.setDate(fromObject.getDate());
        return toObject;
    }
}
