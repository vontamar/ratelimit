package com.ratelimit.example.ratelimit.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String name;
    private String  category;
    private Date expireDate;
}
