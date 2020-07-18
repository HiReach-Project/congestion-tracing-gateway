package com.hireach.congestiontracinggateway.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Node {

    @Id
    @GeneratedValue(generator = "node_history_gen_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "node_history_gen_seq", sequenceName = "node_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String url;

}
