package com.lms.authserver.global.kafka;

import com.lms.authserver.major.entity.Major;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMember {
    private String id;
    private String email;
    private String name;
    private String phNumber;
    private Integer year;
    private Integer studentNumber;
    private String status;
    private String role;
    private String majorList;
    private KafkaAction kafkaAction;
}