package kr.co.infob.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtil {

	public static String parseToString(String json, String attr) {
		ObjectMapper mapper = new ObjectMapper();
		try {

			JsonNode node = mapper.readTree(json);
			if( node.has(attr) ) {
				return node.get(attr).asText();
			} else {
				return null;
			}

		} catch (JsonProcessingException e) {
			return null;
		}
	}

}
