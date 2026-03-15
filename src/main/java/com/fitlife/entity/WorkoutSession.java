package com.fitlife.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "workout_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dayOfWeek; // Monday, Tuesday...
    private String focusArea; // Push, Triceps...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore // FIX: Ngắt lặp ngược về Plan
    private WorkoutPlan workoutPlan;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    // FIX: Xóa @JsonManagedReference đi
    private Set<WorkoutDetail> details = new LinkedHashSet<>();
}