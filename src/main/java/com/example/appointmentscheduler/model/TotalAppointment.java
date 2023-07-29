package com.example.appointmentscheduler.model;

import java.time.LocalDateTime;

public class TotalAppointment {
    private int total;
    private String type;
    private LocalDateTime month;

    public TotalAppointment() {
    }

    public TotalAppointment(int total, String type, LocalDateTime month) {
        this.total = total;
        this.type = type;
        this.month = month;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getMonth() {
        return month;
    }

    public void setMonth(LocalDateTime month) {
        this.month = month;
    }


}
