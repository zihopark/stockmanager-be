package com.stock.service;

import org.springframework.stereotype.Service;

import com.stock.entity.SettingsEntity;
import com.stock.entity.repository.SettingsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SettingsServiceImpl implements SettingsService {

	private final SettingsRepository settingsRepository;
	
	@Override
	public void saveSetting(String key, String value) {
        SettingsEntity setting = new SettingsEntity();
        setting.setKey(key);
        setting.setValue(value);
        settingsRepository.save(setting);
    }

	@Override
    public String getSetting(String key) {
        return settingsRepository.findById(key)
                .map(SettingsEntity::getValue)
                .orElse(null);
    }
	
	@Override
	public String getDeliveredTime() {
		return settingsRepository.getDeliveredTime();
	}
}
