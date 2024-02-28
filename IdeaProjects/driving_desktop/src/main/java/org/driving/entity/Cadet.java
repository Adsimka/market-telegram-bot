package org.driving.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cadet extends User {

    @ManyToOne
    private Instructor instructor;

    private DrivingCategory category;
}
