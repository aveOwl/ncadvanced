package com.overseer.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.overseer.util.LocalDateTimeDeserializer;
import com.overseer.util.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The <code>Request</code> class represents requests of users {@link User}.
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString(of = {"title", "description", "dateOfCreation", "estimateTimeInDays"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY)
@SuppressWarnings("PMD.UnusedPrivateField")
public class Request extends AbstractEntity {
    private static final int MIN_TITLE_LENGTH = 5;
    private static final int MAX_TITLE_LENGTH = 45;
    private static final int MIN_DESCRIPTION_LENGTH = 10;
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    @NonNull
    @NotNull
    @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH)
    private String title;

    @NonNull
    @NotNull
    @Size(min = MIN_DESCRIPTION_LENGTH, max = MAX_DESCRIPTION_LENGTH)
    private String description;

    @NonNull
    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateOfCreation;

    private PriorityStatus priorityStatus;

    private ProgressStatus progressStatus;

    @NonNull
    @NotNull
    private User reporter;

    private User assignee;

    private Long parentId;

    private Integer estimateTimeInDays;
}
