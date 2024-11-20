package org.fintech2024.currencyexchangerateapi.repository;

import org.fintech2024.currencyexchangerateapi.model.PasswordResetToken;
import org.fintech2024.currencyexchangerateapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(User user);
}
