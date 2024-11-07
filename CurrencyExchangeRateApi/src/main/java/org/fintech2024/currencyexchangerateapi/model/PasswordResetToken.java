package org.fintech2024.currencyexchangerateapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "passwordreset")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private User user;

    private Date expiryDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000L);

    public PasswordResetToken(final String token, final User user) {
        this.token = token;
        this.user = user;
    }
}
