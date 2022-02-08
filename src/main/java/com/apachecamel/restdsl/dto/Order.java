package com.apachecamel.restdsl.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int id;
    private String name;
    private double price;
}
