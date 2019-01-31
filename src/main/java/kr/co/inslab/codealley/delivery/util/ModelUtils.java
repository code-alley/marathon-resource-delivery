package kr.co.inslab.codealley.delivery.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Instance를 json 형태로 표시하는 유틸리티 클래스
 */
public class ModelUtils {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
			.disableHtmlEscaping().create();

	public static String toString(Object o) {
		return GSON.toJson(o);
	}
}
