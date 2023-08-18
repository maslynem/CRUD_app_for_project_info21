package ru.s21school.mapper.experienceMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.ExperienceDto;
import ru.s21school.mapper.Mapper;

@Component
public class CsvExperienceMapper implements Mapper<CSVRecord, ExperienceDto> {
    @Override
    public ExperienceDto map(CSVRecord object) {
        ExperienceDto experience = new ExperienceDto();
        experience.setId(Long.valueOf(object.get("id")));
        experience.setCheckId(Long.valueOf(object.get("check_id")));
        experience.setXpAmount(Long.valueOf(object.get("xp_amount")));
        return experience;
    }
}
