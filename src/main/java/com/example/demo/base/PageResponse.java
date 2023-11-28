package com.example.demo.base;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private Integer page;
    private Integer size;
    private Integer totalElements;
    private Integer totalPages;
    private List<T> data;
}