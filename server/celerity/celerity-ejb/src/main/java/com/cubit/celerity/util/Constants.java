package com.cubit.celerity.util;

public class Constants {

	public static final String LOCALHOST = "127.0.0.1";
	public static final String APPLICATION_URL = "celerity.localhost";
	
	// Authentication & Authorization
	public static final String SALT = "yoursecuritysalt";
    public static final String TOKEN_KEY = "token_key";
    public static final String FACEBOOK_APP_ID = "face_app_id";
    
    // ROLES
    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String OPERADOR = "OPERADOR";
    public static final String CLIENT = "CLIENT";
    
	// PERSISTANCE UNIT
	public static final String PERSISTENCE_UNIT_NAME = "CelerityDS";
    
    // CONEXION MongoDB
    public static final String MONGODBHOST = "172.27.1.3";
    public static final String MONGODBPORT = "27017";
    public static final String MONGODBNAME = "celerity";
    
    // ENVIO DE MAIL
    public static final String ACCOUNT_SEND_EMAIL = "user@gmail.com";
    public static final String ACCOUNT_SEND_PASSWORD = "pass";
    public static final String SMTP_MAIL_PORT = "587";
    public static final String SMTP_MAIL_HOST = "smtp.gmail.com"; //  (Gmail)
    public static final String SMTP_MAIL_AUTH = "false"; // true (Gmail) : Without session
    public static final String SMTP_MAIL_TLS = "true";
    
    // API KEY Yandex Translate
    public static final String YANDEX_API_KEY = "apk_key";
    
    // RECOGNITION PARAMETERS
    public static final String RECOGNITION_IMAGE_REPOSITORY = "/opt/shared/";
    public static final String RECOGNITION_API_URI = "http://172.27.1.6:5000";
    public static final Integer RECOGNITION_EXECUTE_CLEAN = 1000*60*60; // Miliseconds
    public static final Integer RECOGNITION_DEACTIVATE_PERIOD = 60*60*24; // Seconds
    public static final Integer RECOGNITION_MAX_IMAGES = 10; // Max number of images per user
    
}