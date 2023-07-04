package com.bbva.wallet.entities;

import com.bbva.wallet.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RoleName name;

    String description;

    @CreationTimestamp
    LocalDateTime creationDate;

    @UpdateTimestamp
    LocalDateTime updatedDate;
}
