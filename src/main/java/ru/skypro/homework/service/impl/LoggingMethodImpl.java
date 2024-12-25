package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.service.LoggingMethod;

@Service
public class LoggingMethodImpl implements LoggingMethod {

    @Override
    public String getMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread()
                .getStackTrace();
        return stackTrace[2].getMethodName();
    }

}
