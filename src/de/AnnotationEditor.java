package de;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class AnnotationEditor {
	private String pathToDataset = "/home/jakup/Documents/DatasetFiles/";
	private String pathToNewDataset = "/home/jakup/Documents/Dataset2/";

	public ArrayList<String> imagesToBeMoved = new ArrayList();
	public ArrayList<Class> classList;

	public static void main(String[] args) {
		ClassListSetter cls = new ClassListSetter();
		AnnotationEditor ae = new AnnotationEditor();
		cls.makeNewOrder();
		
		ae.classList = cls.getClassList();
		
		ae.createNewDataset();
		
	}

	public void createNewDataset() {
		File[] datasetFiles = new File(pathToDataset).listFiles();

		for (int i = 0; i < datasetFiles.length; i++) {
			File currentFile = datasetFiles[i];
			
			if (currentFile.getName().substring(currentFile.getName().length() - 4, currentFile.getName().length()).equals(".txt")) {
				
				try (BufferedReader in = new BufferedReader(new FileReader(currentFile))) {
					ArrayList<String> lines = new ArrayList();
					
					
					String input;
					
					while ((input = in.readLine()) != null) {
						
						String[] classIndexString = input.split("\\s+");
						
						int classIndex = classList.get(Integer.parseInt(classIndexString[0])).getNewClassIndex();
						int numberOfNumbers = classIndexString[0].length();
						
						if (classIndex == -1) {
							throw new Exception("Invalid classindex");
						}
						//if iza bug iza here
						input = classIndex + input.substring(numberOfNumbers, input.length());
						lines.add(input);
					}

					try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(pathToNewDataset + currentFile.getName()), "utf-8"))) {
						for(int j = 0; j < lines.size(); j++) {
							out.write(lines.get(j));
							out.newLine();
						}
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				imagesToBeMoved.add(currentFile.getName().substring(0, currentFile.getName().length()-4) + ".jpg");
			}
		}
		
		copyAllImages();
	}
	
	public void copyAllImages() {
		for(int i = 0; i < imagesToBeMoved.size(); i++) {
			try {
				copyFile(new File(pathToDataset + imagesToBeMoved.get(i)), new File(pathToNewDataset + imagesToBeMoved.get(i)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;
	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();

	        // previous code: destination.transferFrom(source, 0, source.size());
	        // to avoid infinite loops, should be:
	        long count = 0;
	        long size = source.size();              
	        while((count += destination.transferFrom(source, count, size-count))<size);
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}

	
}
