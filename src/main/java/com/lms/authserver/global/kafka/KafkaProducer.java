package com.lms.authserver.global.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMember signup(String topic, KafkaMember kafkaMember) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(kafkaMember);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        return kafkaMember;
    }

    public List<KafkaMajor> saveMajor(String topic, List<KafkaMajor> kafkaMajorList) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> jsonInStrings = new ArrayList<>();

        try {
            for (KafkaMajor kafkaMajor : kafkaMajorList) {
                String jsonInString = mapper.writeValueAsString(kafkaMajor);
                jsonInStrings.add(jsonInString);
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        for (String jsonInString : jsonInStrings) {
            kafkaTemplate.send(topic, jsonInString);
        }

        return kafkaMajorList;
    }
}

