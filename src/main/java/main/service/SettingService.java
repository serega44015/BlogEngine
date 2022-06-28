package main.service;

import main.dto.api.request.SettingsRequest;
import main.dto.api.response.GlobalSettingsResponse;
import main.model.repositories.GlobalSettingRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import static main.mappers.converter.ResultValue.NO;
import static main.mappers.converter.ResultValue.YES;

@Service
public class SettingService {
  private final GlobalSettingRepository globalSettingRepository;
  private final Integer[] SETTING_ID = {1, 2, 3};

  public SettingService(GlobalSettingRepository globalSettingRepository) {
    this.globalSettingRepository = globalSettingRepository;
  }

  public GlobalSettingsResponse getSettings() {
    GlobalSettingsResponse settingResponse = new GlobalSettingsResponse();
    String multiuserMode = globalSettingRepository.findById(SETTING_ID[0]).get().getValue();
    String postPremoderation = globalSettingRepository.findById(SETTING_ID[1]).get().getValue();
    String statisticsIsPublic = globalSettingRepository.findById(SETTING_ID[2]).get().getValue();

    if (Objects.isNull(multiuserMode) || multiuserMode.equals(NO)) {
      settingResponse.setMultiuserMode(false);
    } else {
      settingResponse.setMultiuserMode(true);
    }

    if (Objects.isNull(postPremoderation) || postPremoderation.equals(NO)) {
      settingResponse.setPostPremoderation(false);
    } else {
      settingResponse.setPostPremoderation(true);
    }

    if (Objects.isNull(statisticsIsPublic) || statisticsIsPublic.equals(NO)) {
      settingResponse.setStatisticsIsPublic(false);
    } else {
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
                globalSetting.setValue(booleanToString(settingRequest.getPostPremoderation()));
              }
              if (globalSetting.getId() == SETTING_ID[2]) {
                globalSetting.setValue(booleanToString(settingRequest.getStatisticsIsPublic()));
              }
              globalSettingRepository.save(globalSetting);
            });
  }

  private String booleanToString(Boolean result) {
    if (Objects.isNull(result)) {
      return null;
    } else if (result) {
      return YES;
    } else {
      return NO;
    }
  }
}
