package com.cubit.celerity.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.cubit.celerity.data.idata.ITranslationRepository;
import com.cubit.celerity.util.MongoThread;
import com.cubit.celerity.util.yandex.params.Language;
import com.mongodb.client.MongoCollection;

@Stateless
public class TranslationRepository implements ITranslationRepository {

	@Inject
	private MongoThread mongoThread;
	
	private String translationCollection =  "translations";
	
	public TranslationRepository() {}

	@Override
	public String getTranslationIfExist(String text, Language from, Language to) {
		String output = null;
		if (text.compareToIgnoreCase("")!=0) {
			MongoCollection<Document> collection = mongoThread.getDB().getCollection(this.translationCollection);
			Document doc = collection.find(and(
						eq("input", text), 
						eq("from", from.toString()),
						eq("to", to.toString())
					)).first();
			if (doc!=null) {
				output = (String)doc.get("output").toString();
			}
		}
		return output;
	}

	@Override
	public void saveTranslationIfNotExist(String input, String output, Language from, Language to) {
		if (!this.existTranslation(input, from, to)) {
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long unixTime = System.currentTimeMillis() / 1000L;
				MongoCollection<Document> collection = mongoThread.getDB().getCollection(this.translationCollection);
				Document doc = new Document("from", from.toString())
							.append("to", to.toString())
							.append("input", input)
							.append("output", output)
							.append("date", dateFormat.format(new Date()))
							.append("timestamp", unixTime);
				collection.insertOne(doc);	
			} catch(Exception e) {
				System.out.println("MongoDB save translation exception");
			}
		}
	}
	
	private Boolean existTranslation(String input, Language from, Language to) {
		Boolean result = false;
		if (input.compareToIgnoreCase("")!=0) {
			MongoCollection<Document> collection = mongoThread.getDB().getCollection(this.translationCollection);
			Document doc = collection.find(and(
						eq("input", input), 
						eq("from", from.toString()),
						eq("to", to.toString())
					)).first();
			if (doc!=null) {
				result = true;
			}
		}
		return result;
	}


}
