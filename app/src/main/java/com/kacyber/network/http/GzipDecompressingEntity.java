package com.kacyber.network.http;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;


class GzipDecompressingEntity extends HttpEntityWrapper {

	public GzipDecompressingEntity(final HttpEntity entity) {
		super(entity);
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {

		InputStream wrappedin = wrappedEntity.getContent();
		return new GZIPInputStream(wrappedin);
	}

	@Override
	public long getContentLength() {
		return -1;
	}

}
