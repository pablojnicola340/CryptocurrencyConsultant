package com.exercise.criptocurrency.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "cryptocurrency_names_from_service")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoName {

    @Id
    private Long id;

    @Column()
    private String name;
}
