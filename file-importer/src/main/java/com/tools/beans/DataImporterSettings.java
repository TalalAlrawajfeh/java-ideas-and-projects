package com.tools.beans;

import java.io.Serializable;
import java.nio.file.Path;

public class DataImporterSettings implements Serializable {
	private static final long serialVersionUID = 1L;

	private Path sourcePath;
	private Path successPath;
	private Path errorPath;

	public Path getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(Path sourcePath) {
		this.sourcePath = sourcePath;
	}

	public Path getSuccessPath() {
		return successPath;
	}

	public void setSuccessPath(Path successPath) {
		this.successPath = successPath;
	}

	public Path getErrorPath() {
		return errorPath;
	}

	public void setErrorPath(Path errorPath) {
		this.errorPath = errorPath;
	}
}
