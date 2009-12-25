package org.flashmonkey.util.writers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.flashmonkey.neat.run.IEvolution;

public class FileWritingResultGenerator implements IResultGenerator {

	private static Logger logger = Logger.getLogger(FileWritingResultGenerator.class);
	
	private IFileNameGenerator filenameGenerator;
	
	@Override
	public Result getResult(IEvolution evolution) {
		
		File xmlOutputFile = new File(filenameGenerator.generateFileName(evolution));
		FileOutputStream fos;
		
		try {
			fos = new FileOutputStream(xmlOutputFile);
		} catch (FileNotFoundException e) {
			logger.error("Error occured: " + e.getMessage());
			return null;
		}
		
		return new StreamResult(fos);
	}

}
