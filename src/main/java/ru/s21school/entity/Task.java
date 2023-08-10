package ru.s21school.entity;

import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
@ToString(exclude = "parentTask")
@Check(constraints = "parent_task != title")
public class Task  {
    @Id
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task")
    private Task parentTask;

    @Column(name = "max_xp")
    @NotNull
    private Long maxXp;
}
