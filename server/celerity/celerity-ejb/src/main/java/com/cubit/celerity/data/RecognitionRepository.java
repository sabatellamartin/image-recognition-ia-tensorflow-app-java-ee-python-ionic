package com.cubit.celerity.data;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.cubit.celerity.data.idata.IRecognitionRepository;
import com.cubit.celerity.model.User;
import com.cubit.celerity.model.recognition.Classify;
import com.cubit.celerity.model.recognition.Query;
import com.cubit.celerity.util.Constants;
import com.cubit.celerity.util.JerseyClient;
import com.cubit.celerity.util.MongoThread;
import com.cubit.celerity.util.yandex.params.Language;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;


@Stateless
public class RecognitionRepository implements IRecognitionRepository {

	@Inject
	private MongoThread mongoThread;
	
	@Inject
	private JerseyClient jerseyClient;
	
	private String queryCollection = "querys";
	
	private String requeryCollection = "requerys";
	
	public RecognitionRepository() {}

	public String saveImage(String encodedImage) {
		/* 
		 * Dependency, migrate to ejb pom file if instance FileUtils class
		 * https://mvnrepository.com/artifact/commons-io/commons-io 
		 * Reference
		 * 
		 * http://www.baeldung.com/java-base64-image-string
		 * */
		// Filename to save
		long unixTime = System.currentTimeMillis() / 1000L;
		String outputFileName = String.valueOf(unixTime).toString() + ".jpg";
		String outputPath = Constants.RECOGNITION_IMAGE_REPOSITORY;
	
		String encodedString = encodedImage.split(",")[1];
		
		/*Okay, I found out. The original String is encoded on an Android device using 
		 * android.util.Base64 by Base64.encodeToString(json.getBytes("UTF-8"), 
		 * Base64.DEFAULT);. It uses android.util.Base64.DEFAULT encoding scheme.
		 * Then on the server side when using java.util.Base64 this has to be 
		 * decoded with Base64.getMimeDecoder().decode(payload) 
		 * not with Base64.getDecoder().decode(payload)
		 * */
		//byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		byte[] decodedBytes = Base64.getMimeDecoder().decode(encodedString);
		
		try {
			FileUtils.writeByteArrayToFile(new File(outputPath, outputFileName), decodedBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputFileName;
	}

	@Override
	public Classify classifyFile(String outputFileName) {
	    Client client = this.jerseyClient.getJerseyClient();
		String url = Constants.RECOGNITION_API_URI +"/classify/image/" + outputFileName + "/5";
		WebResource webResource = client.resource(url);
		Classify classify = webResource.accept(MediaType.APPLICATION_JSON).get(Classify.class);
		return classify;
	}

	@Override
	public void saveQuery(String outputFileName, User authenticatedUser, Language from, Language to) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long unixTime = System.currentTimeMillis() / 1000L;
			MongoCollection<Document> collection = mongoThread.getDB().getCollection(this.queryCollection);
			Document doc = new Document("output_file", outputFileName)
						.append("user_id", authenticatedUser.getId().toString())
						.append("user_email", authenticatedUser.getEmail().toString())
						.append("from", from.toString())
						.append("to", to.toString())
						.append("active", true)
						.append("date", dateFormat.format(new Date()))
						.append("timestamp", unixTime);
			collection.insertOne(doc);
		} catch(Exception e) {
			System.out.println("MongoDB save recognition query exception");
		}
	}
	
	@Override
	public List<Query> getActiveQuerysByUser(User user) {
		MongoCollection<Document> collection = mongoThread.getDB().getCollection(this.queryCollection);
		FindIterable<Document> docs = collection.find(
				and(eq("active", true),
					eq("user_id", user.getId().toString()),
					eq("user_email", user.getEmail())))
				.sort(descending("timestamp"));
		return this.findIterableToQueryList(docs);
	}
	
	private List<Query> findIterableToQueryList(FindIterable<Document> docs) {
		List<Query> querys = new ArrayList<Query>();
		if (docs!=null) {
			for(Document doc : docs) {
				querys.add(documentToQuery(doc));
			}
		}
		return querys;
	}
	
	private Query documentToQuery(Document doc) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Query query = new Query();
		if (doc!=null) {
			query.set_id((String)doc.get("_id").toString());
			query.setOutputFile((String)doc.get("output_file").toString());
			query.setUserId((Long)Long.parseLong((String)doc.get("user_id").toString()));
			query.setUserEmail((String)doc.get("user_email").toString());
			query.setFrom(Language.byCode((String)doc.get("from").toString()));
			query.setTo(Language.byCode((String)doc.get("to").toString()));
			query.setLangcode((String)doc.get("to").toString());
			query.setActive((Boolean)doc.get("active"));
			query.setTimestamp((long)doc.get("timestamp"));
			try {
				query.setDate((Date)dateFormat.parse((String)doc.get("date").toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return query;
	}

	@Override
	public void removeOldImages(Integer deactivatePeriod, String imageRepositoryPath) {
		long unixTime = System.currentTimeMillis() / 1000L;
		long timeLimit = unixTime - (long)Long.valueOf(deactivatePeriod.longValue());
		File dir = new File(imageRepositoryPath);
		for (File file: dir.listFiles()) {
			if (!file.isDirectory()) {
				String name = file.getName().split("\\.")[0];
				long fileTime =(long)Long.parseLong(name);
				if (fileTime <= timeLimit) {
					file.delete();
					System.out.println("Delete image "+ name + " from DATE "+new Date(fileTime*1000));
				}
			}
		}
	}

	@Override
	public void deactivateOldQuerys(Integer deactivatePeriod) {
		long unixTime = System.currentTimeMillis() / 1000L;
		long timeLimit = unixTime - (long)Long.valueOf(deactivatePeriod.longValue());
		MongoCollection<Document> collection = mongoThread.getDB().getCollection(this.queryCollection);
		try {
			collection.updateMany(and(lte("timestamp", timeLimit), eq("active", true)), combine(set("active", false)));
		} catch(Exception e) {
			System.out.println("Mongo deactivate many exception");
		}
	}

	@Override
	public String getFileEncoded(String fileName) {
		String encodedMime = null;
		byte[] bytes = loadFileAsBytesArray(Constants.RECOGNITION_IMAGE_REPOSITORY+fileName);
		encodedMime = Base64.getMimeEncoder().encodeToString(bytes);
		encodedMime = encodedMime.replace("\n", "").replace("\r", "");
		return encodedMime;
	}
	
    /**
     * This method loads a file from file system and returns the byte array of the content.
     */
    private byte[] loadFileAsBytesArray(String fileName) {
        File file = new File(fileName);
        int length = (int) file.length();
        BufferedInputStream reader = null;
		try {
			reader = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        byte[] bytes = new byte[length];
        try {
			reader.read(bytes, 0, length);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return bytes;
    }

	@Override
	public void saveReQuery(String outputFileName, User user, Language from, Language to) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long unixTime = System.currentTimeMillis() / 1000L;
			MongoCollection<Document> collection = mongoThread.getDB().getCollection(this.requeryCollection);
			Document doc = new Document("output_file", outputFileName)
						.append("user_id", user.getId().toString())
						.append("user_email", user.getEmail().toString())
						.append("from", from.toString())
						.append("to", to.toString())
						.append("active", true)
						.append("date", dateFormat.format(new Date()))
						.append("timestamp", unixTime);
			collection.insertOne(doc);
		} catch(Exception e) {
			System.out.println("MongoDB save new recognition query exception");
		}
	}

	@Override
	public void removeImage(String fileName) {
		String outputPath = Constants.RECOGNITION_IMAGE_REPOSITORY;
		File dir = new File(outputPath);
		for (File file: dir.listFiles()) {
			if (!file.isDirectory()) {
				if (file.getName().compareToIgnoreCase(fileName)==0) {
					file.delete();
					System.out.println("Delete image " + fileName);
				}
			}
		}
	}

	@Override
	public void deactivateQuery(Query query) {
		MongoCollection<Document> collection = mongoThread.getDB().getCollection(this.queryCollection);
		try {
			collection.updateMany(and(eq("output_file", query.getOutputFile()), eq("active", true)), combine(set("active", false)));
		} catch(Exception e) {
			System.out.println("Mongo deactivate many exception");
		}
	}
	
	@Override
	public void saveMiniImage(String fileName) {
	    try {
	        BufferedImage originalImage = ImageIO.read(new File(Constants.RECOGNITION_IMAGE_REPOSITORY+"/"+fileName));//change path to where file is located
	        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	        BufferedImage resizeImageJpg = resizeImage(originalImage, type, 60, 60);
	        ImageIO.write(resizeImageJpg, "jpg", new File(Constants.RECOGNITION_IMAGE_REPOSITORY+"/mini-"+fileName)); //change path where you want it saved
	    } catch (IOException e) {
	        System.out.println(e.getMessage());
	    }
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT) {
	    BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	    g.dispose();
	    return resizedImage;
	}
	
}
