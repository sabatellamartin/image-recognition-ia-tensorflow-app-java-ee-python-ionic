package com.cubit.celerity.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.cubit.celerity.data.idata.ITranslationRepository;
import com.cubit.celerity.model.translation.Lang;
import com.cubit.celerity.service.iservice.ITranslationService;
import com.cubit.celerity.util.yandex.YandexTranslator;
import com.cubit.celerity.util.yandex.params.Language;

@Stateless
public class TranslationService implements ITranslationService {
	
	@Inject
	YandexTranslator yandexTranslator;
	
	@Inject
	ITranslationRepository translationReposotory;
	
	@Override
	public String translate(String text, Language from, Language to) {
		String translated = this.translationReposotory.getTranslationIfExist(text, from, to);
		if (translated==null) {
			try {
				translated = yandexTranslator.translate(text, from, to);
				if (translated!=null) {
					this.translationReposotory.saveTranslationIfNotExist(text, translated, from, to);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return translated;	
	}
	
	@Override
	public List<Language> getLanguages() {
		List<Language> langs = new ArrayList<Language>();
		try {
			langs = this.yandexTranslator.getSupportedLanguages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return langs;
	}
	
	@Override
	public List<Lang> getLangs() {
		List<Lang> langs = new ArrayList<Lang>();
        for (Language language : Language.values()) {
            Lang lang = new Lang(language.ordinal(),language.name(),language.toString());
            if (language.toString().compareToIgnoreCase("")!=0) {
            	langs.add(lang);
            }
        }
		return langs;
	}
	
}
