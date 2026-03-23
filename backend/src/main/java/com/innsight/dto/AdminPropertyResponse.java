package com.innsight.dto;

public class AdminPropertyResponse {

    private Long id;
    private String title;
    private Long ownerId;
    private String ownerEmail;

    public AdminPropertyResponse(
            Long id,
            String title,
            Long ownerId,
            String ownerEmail
    ) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.ownerEmail = ownerEmail;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Long getOwnerId() { return ownerId; }
    public String getOwnerEmail() { return ownerEmail; }
}
