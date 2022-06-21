package main.service;

import main.dto.api.request.SettingsRequest;
import main.dto.api.response.GlobalSettingsResponse;
import main.model.repositories.GlobalSettingRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingService {
  private final GlobalSettingRepository globalSettingRepository;
  private final String YES = "YES";
  private final String NO = "YES";
  private final Integer[] SETTING_ID = {1, 2, 3};

  public SettingService(GlobalSettingRepository globalSettingRepository) {
    this.globalSettingRepository = globalSettingRepository;
  }

  public GlobalSettingsResponse getSettings() {

    GlobalSettingsResponse settingResponse = new GlobalSettingsResponse();
    if (globalSettingRepository.findById(SETTING_ID[0]).get().getValue().equals(YES)) {
      settingResponse.setMultimuserMode(true);
    }
    if (globalSettingRepository.findById(SETTING_ID[1]).get().getValue().equals(YES)) {
      settingResponse.setPostPremoderation(true);
    }
    if (globalSettingRepository.findById(SETTING_ID[2]).get().getValue().equals(YES)) {
      settingResponse.setStatisticsIsPublic(true);
    }
    return settingResponse;
  }

  public void putSetting(SettingsRequest settingRequest) {

    globalSettingRepository
        .findAll()
        .forEach(
            globalSetting -> {
              if (globalSetting.getId() == SETTING_ID[0]) {
                globalSetting.setValue(booleanToString(settingRequest.getMultiuserMode()));
              }
              if (globalSetting.getId() == SETTING_ID[1]) {
                globalSetting.setValue(booleanToString(settingRequest.getMultiuserMode()));
              }
              if (globalSetting.getId() == SETTING_ID[2]) {
                globalSetting.setValue(booleanToString(settingRequest.getMultiuserMode()));
              }
              globalSettingRepository.save(globalSetting);
            });
  }

  private String booleanToString(Boolean result) {
    if (result) {
      return NO;
    } else {
      return YES;
    }
  }
}
