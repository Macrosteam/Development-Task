package com.omar.SpringTask.report;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class ReportConfig {

    @Value("${spring.report.hour}")
    private int hour;

    @Value("${spring.report.minute}")
    private int minute;

    @Value("${spring.report.recipient}")
    private String recipient;

    @Value("${spring.report.subject}")
    private String subject;

    public int getHour()
    {
        return hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public String getRecipient()
    {
        return recipient;
    }

    public  String getSubject()
    {
        return subject;
    }

}
