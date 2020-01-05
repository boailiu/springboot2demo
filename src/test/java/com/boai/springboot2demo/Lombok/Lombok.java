package com.boai.springboot2demo.Lombok;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Lombok {
    private Integer id;
    private String name;
    private String field;
}
