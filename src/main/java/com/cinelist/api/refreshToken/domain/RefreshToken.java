package com.cinelist.api.refreshToken.domain;

import com.cinelist.api.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(generator = "uuid_v7_gen")
    @GenericGenerator(name = "uuid_v7_gen", strategy = "com.cinelist.api.config.generator.UuidV7Generator")
    @Schema(hidden = true)
    private UUID id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false, name = "expires_at")
    private Instant expiresAt;

    private boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
