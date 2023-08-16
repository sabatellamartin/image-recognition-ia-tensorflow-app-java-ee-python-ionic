package com.cubit.celerity.service.iservice;

import java.util.List;

import com.cubit.celerity.model.Setting;
import com.cubit.celerity.model.User;
import com.cubit.celerity.model.translation.Lang;

public interface ISettingService {

	public Setting getCurrent(User authenticatedUser);

	public Setting setLanguage(User authenticatedUser, String language);

	public Setting setTraduction(User authenticatedUser, String traduction);

	public List<Lang> getLanguages();

}
