package com.overseer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

/**
 * HistoryDetail entity.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("PMD.UnusedPrivateField")
public class History extends AbstractEntity {
    private static final int MAX_COLUMN_NAME_LENGTH = 20;

    @NotNull(message = "History has to have a column name")
    private String columnName;

    private String oldValue;

    private String newValue;

    @NotNull(message = "History has to have a change date")
    private LocalDateTime dateOfChange;

    @NotNull(message = "History has to have a changer")
    private User changer;

    @NotNull(message = "History has to belongs to something")
    private Long recordId;
}
