package org.driving.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Instructor extends User {

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Cadet> cadetSet = new HashSet<>();

    private int drivingExperience;

}
