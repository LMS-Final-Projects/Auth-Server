package com.lms.authserver.global.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private KafkaAction kafkaAction;
}