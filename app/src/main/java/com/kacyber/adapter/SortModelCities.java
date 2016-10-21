package com.kacyber.adapter;

import com.kacyber.model.JSONParceble;

import org.json.JSONException;
import org.json.JSONObject;

public class SortModelCities implements JSONParceble {

	public String name;
	public String sortLetters;
	public long cityID;
	public double lat;
	public double lng;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	@Override
	public boolean initWithJSONObject(JSONObject obj) {
		try {
			cityID = obj.getLong("id");
			name = obj.getString("name");
			lat = obj.getDouble("lat");
			lng = obj.getDouble("lng");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return true;
	}
}
