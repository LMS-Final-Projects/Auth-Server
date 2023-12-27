package com.lms.authserver.major.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean checkMajor;

    @Column(unique = true, nullable = false)
    private String majorName;

    @Override
    public String toString() {
        return "Major{" +
                "id=" + id +
                ", checkMajor=" + checkMajor +
                ", majorName='" + majorName + '\'' +
                '}';
    }
}

