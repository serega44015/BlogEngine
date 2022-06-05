package main.model.repositories;

import main.model.CaptchaCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CaptchaCodesRepository extends JpaRepository<CaptchaCodes, Integer> {

    @Override
    List<CaptchaCodes> findAll();

    CaptchaCodes findByCode(String code);

}
