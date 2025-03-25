package com.bookNDrive.gateway.feign.models;



import java.time.LocalDateTime;

public class BaseEntity {


    private Long id;

    private String updatedBy;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}