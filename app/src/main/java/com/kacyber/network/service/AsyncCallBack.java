package com.kacyber.network.service;


import com.kacyber.base.BaseAction;

public interface AsyncCallBack {

	@SuppressWarnings("rawtypes")
	void onCallBack(final BaseAction action);

}
