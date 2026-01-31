package com.qudus.postra.model;

public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Object error;

    public ApiResponse(String status, String message, T data, Object error) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }
    
    public void setError(Object error) {
        this.error = error;
    }

    
}
