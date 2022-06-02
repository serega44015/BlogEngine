package main.service;


import main.dto.api.request.SettingsRequest;
import main.dto.api.response.GlobalSettingsResponse;
import main.model.repositories.GlobalSettingRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingService {
    private final GlobalSettingRepository globalSettingRepository;


    public SettingService(GlobalSettingRepository globalSettingRepository) {
        this.globalSettingRepository = globalSettingRepository;
    }

    public GlobalSettingsResponse getSettings() {

        GlobalSettingsResponse settingResponse = new GlobalSettingsResponse();
        if (globalSettingRepository.findById(1).get().getValue().equals("YES")) {
            settingResponse.setMultimuserMode(true);
        }
        if (globalSettingRepository.findById(2).get().getValue().equals("YES")) {
            settingResponse.setPostPremoderation(true);
        }
        if (globalSettingRepository.findById(3).get().getValue().equals("YES")) {
            settingResponse.setStatisticsIsPublic(true);
        }
        return settingResponse;
    }

    public void putSetting(SettingsRequest settingRequest) {

        globalSettingRepository.findAll().forEach(globalSetting -> {
            if (globalSetting.getId() == 1) {
                globalSetting.setValue(booleanToString(settingRequest.isMultiuserMode()));
            }
            if (globalSetting.getId() == 2) {
                globalSetting.setValue(booleanToString(settingRequest.isPostPremoderation()));
            }
            if (globalSetting.getId() == 3) {
                globalSetting.setValue(booleanToString(settingRequest.isStatisticsIsPublic()));
            }
            globalSettingRepository.save(globalSetting);
        });

    }

    private String booleanToString(boolean result) {
        if (result) {
            return "NO";
        } else return "YES";
    }


}
