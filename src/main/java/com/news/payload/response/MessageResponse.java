package com.news.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MessageResponse {

    private String message;
    private String status;

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String status , String message) {
        this.message = message;
        this.status = status;
    }

}
