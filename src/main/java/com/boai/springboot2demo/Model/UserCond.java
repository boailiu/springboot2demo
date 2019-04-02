package com.boai.springboot2demo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserCond extends User {
    private LocalDate startDate;
    private LocalDateTime startTime;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
