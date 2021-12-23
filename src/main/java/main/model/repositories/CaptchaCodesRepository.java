package main.model.repositories;

import main.model.CaptchaCodes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptchaCodesRepository extends JpaRepository<CaptchaCodes, Integer> {
}
