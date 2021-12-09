package testorder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import testorder.dto.CsvFile;

public class InsuranceFiles {	
	
	private static final String FILE_INPUT_PATH = "insurance_data.csv";	
	private static final String FILE_OUTPUT_PATH = "src\\main\\resources\\";
	
	private static Scanner scanner;
	
	public static void main(String[] args) {
        
		InsuranceFiles insuranceFiles = new InsuranceFiles();								
		insuranceFiles.createSeparateCsvFiles(insuranceFiles.csvListData());
	}
	
	private List<CsvFile> csvListData() {
		
		List<CsvFile> csvList = new ArrayList<>();
		
		try {
		   String fullPath = getClass().getClassLoader().getResource(FILE_INPUT_PATH).getPath();
		   scanner = new Scanner(new File(fullPath));
		   scanner.useDelimiter("[\n]");
		
   		   while (scanner.hasNext()) {
   			  String scannerRow = scanner.next();			  
			  String [] row = scannerRow.replace("\\n", "").split(",");
			  CsvFile csvFile = convertArrayIntoCsvFile(row);
			  csvList.add(csvFile);
		   }
		
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		
		return sortCsvFileList(csvList);		
	}
	
	private CsvFile convertArrayIntoCsvFile(String[] row) {				
		
		CsvFile csvFile = new CsvFile();
		csvFile.setUserId(row[0]);
		csvFile.setFirstName(row[1]);
		csvFile.setLastName(row[2]);
		csvFile.setVersion(new Integer(row[3]));
		csvFile.setInsuranceCompany(row[4]);
		return csvFile;		
	}
	
	private List<CsvFile> sortCsvFileList(List<CsvFile> csvList) {				
		
		Comparator<CsvFile> comparator = Comparator
				.comparing(CsvFile :: getInsuranceCompany)
				.thenComparing(CsvFile :: getLastName)
				.thenComparing(CsvFile :: getFirstName)				
				.thenComparing(CsvFile :: getVersion, Comparator.reverseOrder());
		
		return csvList.stream()
				.sorted(comparator)					
				.filter(distinctByKey(csv -> csv.getUserId()))
				.collect(Collectors.toList());	        				
	}
	
	private List<String> insuranceCompanies (List<CsvFile> csvList) {		
		
		return csvList.stream()
		  .filter(distinctByKey(csv -> csv.getInsuranceCompany()))
		  .map(csv -> csv.getInsuranceCompany())		  
		  .collect(Collectors.toList());				
	}
		
	private void createSeparateCsvFiles(List<CsvFile> csvList) {
		
		List<String> insuranceCompanies = insuranceCompanies(csvList);
		insuranceCompanies.forEach(ic -> {
			System.out.println(ic);
			List<CsvFile> csvPerInsuranceCompany = csvList.stream()
			  .filter(csv -> ic.equals(csv.getInsuranceCompany()))
			  .collect(Collectors.toList());
			
			csvPerInsuranceCompany.forEach(c -> System.out.println("c = " + c));
			System.out.println("\n\n\n");
			
			createCsvFileForInsuranceCompany(
					ic.replace("'", "").replaceAll(" ", ""), 
					csvPerInsuranceCompany
			);
		});		
	}
	
	private void createCsvFileForInsuranceCompany(String fileName, List<CsvFile> csvPerInsuranceCompany) {
					
		String fileNameIC = fileName.replaceAll("\"", "").trim();
		Path filePath = Paths.get(FILE_OUTPUT_PATH + fileNameIC + ".csv");
				
		try {									
			BufferedWriter fileWriter = Files.newBufferedWriter(filePath);
			
			for (CsvFile ic : csvPerInsuranceCompany) {			
			  String fileRow = ic.getUserId() + "," + ic.getFirstName() + "," + ic.getLastName() + "," + ic.getVersion() + "\\n";			  
			  System.out.println(fileRow);
			  fileWriter.append(fileRow);				
			  fileWriter.newLine();
			}
			
			fileWriter.flush();
			fileWriter.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private <T> Predicate<T> distinctByKey(Function<? super T, ?> attributeExtractor) {
		
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(attributeExtractor.apply(t), Boolean.TRUE) == null;		
	}
}
