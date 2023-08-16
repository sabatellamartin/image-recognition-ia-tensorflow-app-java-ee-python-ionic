package com.cubit.celerity.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.cubit.celerity.data.idata.IRecognitionRepository;
import com.cubit.celerity.model.User;
import com.cubit.celerity.model.recognition.Classify;
import com.cubit.celerity.model.recognition.Prediction;
import com.cubit.celerity.model.recognition.Query;
import com.cubit.celerity.service.iservice.IRecognitionService;
import com.cubit.celerity.service.iservice.ITranslationService;
import com.cubit.celerity.util.Constants;
import com.cubit.celerity.util.yandex.params.Language;

@Stateless
public class RecognitionService implements IRecognitionService {

	@Inject
	private IRecognitionRepository recognitionRepository;	

	@Inject
	private ITranslationService translationService;
    
	public Classify getClassify(String traduction, String encodedImage, User user) {
		String outputFileName = this.recognitionRepository.saveImage(encodedImage);
		//this.recognitionRepository.saveMiniImage(outputFileName); // Guardo la miniatura
		Classify classify = this.recognitionRepository.classifyFile(outputFileName);
		List<Prediction> translatePredictions = new ArrayList<Prediction>();
		for (Prediction p : classify.getPredictions()) {
			String translated = this.translationService.translate(p.getDescription(), Language.ENGLISH, Language.byCode(traduction));
			translatePredictions.add(new Prediction(translated, p.getScore()));	
		}
		//classify.getPredictions().addAll(translatePredictions);
		classify.setTranslated(translatePredictions);
		this.recognitionRepository.saveQuery(outputFileName, user, Language.ENGLISH, Language.byCode(traduction));
		this.controlClean(user);
		return classify;
	}

	private void controlClean(User user) {
		List<Query> querys = this.recognitionRepository.getActiveQuerysByUser(user);
		if (querys.size() >= Constants.RECOGNITION_MAX_IMAGES) {
			long lastest = System.currentTimeMillis() / 1000L;
			Query toRemove = new Query();
			for(Query query : querys) {
				if (query.getTimestamp() < lastest) {
					toRemove = query;
				}
			}
			this.removeQuery(toRemove);
		}
	}

	private void removeQuery(Query query) {
		this.recognitionRepository.removeImage(query.getOutputFile());
		this.recognitionRepository.deactivateQuery(query);
	}

	@Override
	public List<Query> getActiveQuerysByUser(User user) {
		List<Query> querys = this.recognitionRepository.getActiveQuerysByUser(user);
		for(Query query : querys) {
			String encodedFile = this.recognitionRepository.getFileEncoded(query.getOutputFile());
			query.setEncodedFile(encodedFile);
		}
		return querys;
	}

	@Override
	public void clean(Integer deactivatePeriod, String imageRepositoryPath) {
		this.recognitionRepository.removeOldImages(deactivatePeriod, imageRepositoryPath);
		deactivatePeriod -= 10; // Resto 10 segundos de error por diff. de tiempo de la consulta
		this.recognitionRepository.deactivateOldQuerys(deactivatePeriod);
	}

	@Override
	public Classify getClassifyByFileName(String traduction, String fileName, User user) {
		Classify classify = this.recognitionRepository.classifyFile(fileName);
		List<Prediction> translatePredictions = new ArrayList<Prediction>();
		for (Prediction p : classify.getPredictions()) {
			String translated = this.translationService.translate(p.getDescription(), Language.ENGLISH, Language.byCode(traduction));
			translatePredictions.add(new Prediction(translated, p.getScore()));	
		}
		classify.setTranslated(translatePredictions);
		this.recognitionRepository.saveReQuery(fileName, user, Language.ENGLISH, Language.byCode(traduction));
		return classify;
	}

	@Override
	public String getEncodedFileByName(String fileName, User user) {
		String encodedFile = null;
		List<Query> querys = this.recognitionRepository.getActiveQuerysByUser(user);
		for(Query query : querys) {
			if (query.getOutputFile().compareToIgnoreCase(fileName)==0 ) {
				encodedFile = this.recognitionRepository.getFileEncoded(fileName);
			}
		}
		return encodedFile;
	}
	
}
