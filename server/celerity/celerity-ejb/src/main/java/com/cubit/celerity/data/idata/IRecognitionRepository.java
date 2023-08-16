package com.cubit.celerity.data.idata;

import java.util.List;

import com.cubit.celerity.model.User;
import com.cubit.celerity.model.recognition.Classify;
import com.cubit.celerity.model.recognition.Query;
import com.cubit.celerity.util.yandex.params.Language;

public interface IRecognitionRepository {

	public String saveImage(String encodedImage);

	public Classify classifyFile(String outputFileName);

	public void saveQuery(String outputFileName, User authenticatedUser, Language form, Language to);
	
	public List<Query> getActiveQuerysByUser(User authenticatedUser);

	public void removeOldImages(Integer deactivatePeriod, String imageRepositoryPath);

	public void deactivateOldQuerys(Integer deactivatePeriod);
	
	public String getFileEncoded(String fileName);

	public void saveReQuery(String outputFileName, User user, Language from, Language to);

	public void removeImage(String outputFile);

	public void deactivateQuery(Query query);
	
	public void saveMiniImage(String fileName);
	
}
