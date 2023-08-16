package com.cubit.celerity.util;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordTool {

	public String randomPassword() {
		Random random = new SecureRandom();
		int length = 8;
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";
		String pw = "";
		for (int i=0; i<length; i++) {
			int index = (int)(random.nextDouble()*letters.length());
			pw += letters.substring(index, index+1);
		}
		return pw;
	}
	
	public String hashPassword(String clear) {
		return DigestUtils.sha512Hex(clear);//Hex.encodeHex(DigestUtils.sha512Hex(clear));
	}
}
