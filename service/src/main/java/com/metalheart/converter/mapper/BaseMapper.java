package com.metalheart.converter.mapper;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public abstract class BaseMapper {

    protected OffsetDateTime map(ZonedDateTime dateTime) {
        return dateTime != null ? OffsetDateTime.from(dateTime) : null;
    }

    protected ZonedDateTime map(OffsetDateTime dateTime) {
        return dateTime != null ? ZonedDateTime.from(dateTime) : null;
    }
}
