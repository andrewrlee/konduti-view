package uk.co.optimisticpanda.base;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FileWriter {

	private Logger log = Logger.getLogger(FileWriter.class);
	private File baseDir;

	public FileWriter(String baseDir) {
		this.baseDir = new File(baseDir);
	}

	public void writeOut(String childDir, String fileName, String line) {
		File childDirectory = getOrCreateChildDir(childDir);
		File file = new File(childDirectory, fileName);
		this.writeOut(file, line);
	}

	public void writeOut(String fileName, String line) {
		File file = new File(baseDir, fileName);
		this.writeOut(file, line);
	}

	private void writeOut(File file, String line) {
		try {
			if (file.exists()) {
				if (!file.delete()) {
					throw new RuntimeException("Could not delete file:" + file);
				}
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException("Could not create file:" + file, e);
			}
			Files.append(line + "\n", file, Charsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Could not append:" + line + " to " + file, e);
		}
	}

	private File getOrCreateChildDir(String childDir) {
		File directory = new File(baseDir, childDir);
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				throw new RuntimeException("File :" + directory.getAbsolutePath() + " exists but is not a directory");
			}
			return directory;
		}
		if (!directory.mkdir()) {
			throw new RuntimeException("Could not create directory:" + directory.getAbsolutePath());
		}
		return directory;
	}

	public String copy(String childDir, File imageFile) {
		try {
			File dir = getOrCreateChildDir(childDir);
			File output = new File(dir, imageFile.getName());
			log.info("copying from :" + imageFile.getAbsolutePath());
			log.info("copying to:" + output.getAbsolutePath());
			FileUtils.copyFile(imageFile, output);
			return childDir + "/" + output.getName();
		} catch (IOException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1);
		}
	}

}
