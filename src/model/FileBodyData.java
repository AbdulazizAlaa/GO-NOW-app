package model;

import org.apache.http.entity.mime.content.FileBody;

public class FileBodyData {

	String key;
	private FileBody fileBody;

	public FileBodyData(String key, FileBody fileBody) {
		super();
		this.key = key;
		this.fileBody = fileBody;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public FileBody getFileBody() {
		return fileBody;
	}

	public void setFileBody(FileBody fileBody) {
		this.fileBody = fileBody;
	}

}
