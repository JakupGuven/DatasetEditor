package de;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClassListSetter {
	private String pathToNames = "/home/jakup/Documents/DatasetFiles/";;
	private String namesFile = "MyEsteeme.NAMES";
	private String pathToConfigFile = "/home/jakup/Documents/DatasetFiles/newNames.NAMES";
	
	private ArrayList<Class> classList;
	
	
	public static void main(String[] args) {
		ClassListSetter program = new ClassListSetter();
		program.makeNewOrder();
	}
	
	
	public void makeNewOrder() {
		buildClassList();
		getNewClassIndices();
		printClassList();
	}
	
	public void buildClassList() {
		try(BufferedReader in = new BufferedReader(new FileReader(new File(pathToNames + namesFile)))){
			classList = new ArrayList();
			String input;
			//int index = 0;
			while((input = in.readLine()) != null) {
				Class detectionClass = new Class();
				detectionClass.setClassName(input);
				classList.add(detectionClass);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getNewClassIndices() {
		try(BufferedReader in = new BufferedReader(new FileReader(new File(pathToConfigFile)))){
			String input;
			int index = 0;
			while((input = in.readLine()) != null) {
				for(int i = 0; i < classList.size(); i++) {
					if(classList.get(i).getClassName().equals(input)) {
						classList.get(i).setNewClassIndex(index);
						break;
					}else if(i == classList.size()-1) {
						throw new Exception("No such class in dataset: " + input);
					}
				}
				index++;
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

	}
	
	public void printClassList() {
		for(int i = 0; i < classList.size(); i++) {
			Class detectionClass = classList.get(i);
			System.out.println(detectionClass.getClassName() + " old index: " + i + " . New index: " + detectionClass.getNewClassIndex());
		}
	}
	
	public ArrayList<Class> getClassList(){
		return classList;
	}
	

}
