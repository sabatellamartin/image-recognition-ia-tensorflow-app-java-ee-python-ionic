package com.cubit.celerity.data.idata;

import com.cubit.celerity.util.yandex.params.Language;

public interface ITranslationRepository {

	public String getTranslationIfExist(String input, Language from, Language to);

	public void saveTranslationIfNotExist(String input, String output, Language from, Language to);
		
}
