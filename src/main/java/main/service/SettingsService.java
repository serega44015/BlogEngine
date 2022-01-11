package main.service;

import main.api.response.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    public SettingsResponse getInitGlobalSettins(){
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setPostPremoderation(true);
        settingsResponse.setStatisticsIsPublic(true);
        return settingsResponse;
    }
}
