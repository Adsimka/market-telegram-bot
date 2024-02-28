package org.driving.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

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
    @OneToMany()
    private Set<Cadet> cadetSet = new HashSet<>();

}
