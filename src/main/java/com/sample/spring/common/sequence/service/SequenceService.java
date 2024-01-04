package com.sample.spring.common.sequence.service;

import com.sample.spring.common.sequence.SequenceType;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Year;

public interface SequenceService {

    String injectFieldWithSequenceValue(Object entity, String fieldName) throws NoSuchFieldException;

    Long getCurrentSequence(String sequenceName, SequenceType sequenceType, boolean recycle);

    @SneakyThrows
    default String generate(int numOfDigit, String sequenceName, String sequencePrefix, SequenceType sequenceType, boolean recycle) {
        Long current = this.getCurrentSequence(sequenceName, sequenceType, recycle);
        String year = String.valueOf(Year.now()).substring(2);
        String dayOfYear = String.format("%03d", LocalDate.now().getDayOfYear());
        String sequence = String.format(String.format("%%0%dd", numOfDigit), current);
        String randomValue = String.format("%03d", SecureRandom.getInstanceStrong().nextInt(999));
        if (ObjectUtils.isEmpty(sequencePrefix))
            return String.format("%s%s%s%s", year, dayOfYear, sequence, randomValue);
        return String.format("%s%s%s%s%s", sequencePrefix, year, dayOfYear, sequence, randomValue);
    }
}
