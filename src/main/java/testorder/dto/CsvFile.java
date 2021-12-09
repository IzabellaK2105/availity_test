package testorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CsvFile {
		
	private String userId;			
	private String firstName;	
	private String lastName;
	private Integer version;
	private String insuranceCompany;
}
