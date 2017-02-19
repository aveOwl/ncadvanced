package com.overseer.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

/**
 * The <code>ProgressStatus</code> class represents progress status of request {@link Request}.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY)
public class ProgressStatus extends AbstractEntity {
    @NonNull
    private String name;
}