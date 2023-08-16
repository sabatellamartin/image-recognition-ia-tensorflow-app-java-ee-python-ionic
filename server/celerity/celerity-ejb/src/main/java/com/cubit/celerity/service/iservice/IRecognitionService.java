package com.cubit.celerity.service.iservice;

import java.util.List;

import com.cubit.celerity.model.User;
import com.cubit.celerity.model.recognition.Classify;
import com.cubit.celerity.model.recognition.Query;

public interface IRecognitionService {

	Classify getClassify(String traduction, String encodedImage, User user);

	void clean(Integer deactivatePeriod, String imageRepositoryPath);

	List<Query> getActiveQuerysByUser(User user);

	Classify getClassifyByFileName(String traduction, String fileName, User user);

	String getEncodedFileByName(String fileName, User user);

}
