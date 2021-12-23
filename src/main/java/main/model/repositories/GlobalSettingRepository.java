package main.model.repositories;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalSettingRepository extends JpaRepository<GlobalSetting, Integer> {

}
