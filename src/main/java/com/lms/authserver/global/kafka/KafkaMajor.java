package com.lms.authserver.global.kafka;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMajor {

    private String id;
    private String memberId;
    private String checkMajor;
    private String majorName;
    private KafkaAction kafkaAction;
    private String role;;
}

