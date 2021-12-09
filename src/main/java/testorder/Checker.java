package testorder;

import org.apache.commons.lang3.StringUtils;

public class Checker {
	private static final String PARENTHESES_ONLY_PATTERN = "[^\\(.\\)]";
	
	public static void main(String[] args) {		
	
	    Checker checker = new Checker();
		
		String text = "(cons 1 (cons 2 nil))";
		//String text = "((cons 1 (cons 2 nil))"; // returns false
		
		System.out.println("text = " + text);
		
		String str = checker.onlyParentheses(text);
		System.out.println("str = " + str);
		
		boolean valid = checker.isValid(str);
		System.out.println("valid = " + valid);
	}
	
	private String onlyParentheses(String str) {
		
		if (StringUtils.isEmpty(str)) {
		  return str;
		}
		
		return str.replaceAll(PARENTHESES_ONLY_PATTERN, "");
	}
	
	private boolean isValid(String str) {
		
		if (StringUtils.isEmpty(str)) {
		  return false;
		}
		
		long numberOfOpeningPar = countOfChar(str, "(");
		long numberOfClosingPar = countOfChar(str, ")");
		
		if (numberOfOpeningPar != numberOfClosingPar) {
		  return false;
		}
		
		System.out.println("str before change = " + str);
		
		while (str.contains("()")) {
		  str = str.replaceAll("\\(\\)", "");
		}
		
		System.out.println("str after change = " + str);
		return str.length() == 0;		
	}
	
	private long countOfChar(String str, String searchChar) {
		
		return str.chars()
				.filter(ch -> searchChar.equals(ch))
				.count();
	}
}
