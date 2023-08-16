package com.cubit.celerity.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.cubit.celerity.model.Setting;
import com.cubit.celerity.model.User;
import com.cubit.celerity.model.translation.Lang;
import com.cubit.celerity.service.iservice.ISettingService;
import com.cubit.celerity.service.iservice.ITranslationService;
import com.cubit.celerity.service.iservice.IUserService;

@Stateless
public class SettingService implements ISettingService {

	@Inject
	private IUserService userService;
	
	@Inject
	private ITranslationService translationService;

	@Override
	public Setting getCurrent(User auth) {
		Setting setting = null;
		User user = this.userService.getByEmail(auth.getEmail());
		if (user!=null) {
			setting = user.getSetting();
		}
		return setting;
	}

	@Override
	public Setting setLanguage(User auth, String language) {
		Setting setting = new Setting();
		User user = this.userService.getByEmail(auth.getEmail());
		if (user!=null) {
			setting = user.getSetting();
			setting.setLanguage(language);
			user.setSetting(setting);
			this.userService.update(user);
			setting = this.getCurrent(user);
		}
		return setting;
	}

	@Override
	public Setting setTraduction(User auth, String traduction) {
		Setting setting = new Setting();
		User user = this.userService.getByEmail(auth.getEmail());
		if (user!=null) {
			setting = user.getSetting();
			setting.setTraduction(traduction);
			user.setSetting(setting);
			this.userService.update(user);
			setting = this.getCurrent(user);
		}
		return setting;
	}

	@Override
	public List<Lang> getLanguages() {
		return this.translationService.getLangs();
	}

}
