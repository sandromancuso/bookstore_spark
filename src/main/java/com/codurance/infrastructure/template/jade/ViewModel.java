package com.codurance.infrastructure.template.jade;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ViewModel {

	private Map<String, Object> model = new HashMap<>();

	public void put(String key, Object value) {
		model.put(key, value);
	}

	public Object get(String key) {
		return model.get(key);
	}

	public Map<String, Object> toMap() {
		return Collections.unmodifiableMap(model);
	}

}
