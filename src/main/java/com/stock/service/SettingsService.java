package com.stock.service;

public interface SettingsService {

	public void saveSetting(String key, String value);
	
	public String getSetting(String key);
	
	public String getDeliveredTime();
}
