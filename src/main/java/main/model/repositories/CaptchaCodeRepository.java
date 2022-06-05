package main.model.repositories;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaptchaCodeRepository extends JpaRepository<CaptchaCode, Integer> {

  @Override
  List<CaptchaCode> findAll();

  CaptchaCode findByCode(String code);
}
