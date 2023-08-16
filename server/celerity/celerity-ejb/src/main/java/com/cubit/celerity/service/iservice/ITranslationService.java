package com.cubit.celerity.service.iservice;

import java.util.List;

import com.cubit.celerity.model.translation.Lang;
import com.cubit.celerity.util.yandex.params.Language;

public interface ITranslationService {

	public String translate(String text, Language from, Language to);

	public List<Language> getLanguages();

	List<Lang> getLangs();
	
}
