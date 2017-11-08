package edu.buffalo.dm;

import java.util.Set;

public class QueryTemplate {
	int queryTempType;
	String TempRulePart;
	String numItemsAllowed1;
	Set<String> itemset1;
	Set<String> itemset2;
	String Temp3RulePart1;
	String Temp3RulePart2;
	String numItemsAllowed2;
	
	public int getQueryTempType() {
		return queryTempType;
	}
	public void setQueryTempType(int queryTempType) {
		this.queryTempType = queryTempType;
	}
	public String getTempRulePart() {
	return TempRulePart;
	}
	public void setTempRulePart(String temp1RulePart) {
		TempRulePart = temp1RulePart;
	}
	public String getNumItemsAllowed1() {
		return numItemsAllowed1;
	}
	public void setNumItemsAllowed1(String numItemsAllowed1) {
		this.numItemsAllowed1 = numItemsAllowed1;
	}
	public Set<String> getItemset1() {
		return itemset1;
	}
	public void setItemset1(Set<String> itemset1) {
		this.itemset1 = itemset1;
	}
	public Set<String> getItemset2() {
		return itemset2;
	}
	public void setItemset2(Set<String> itemset2) {
		this.itemset2 = itemset2;
	}
	public String getTemp3RulePart1() {
		return Temp3RulePart1;
	}
	public void setTemp3RulePart1(String temp3RulePart1) {
		Temp3RulePart1 = temp3RulePart1;
	}
	public String getTemp3RulePart2() {
		return Temp3RulePart2;
	}
	public void setTemp3RulePart2(String temp3RulePart2) {
		Temp3RulePart2 = temp3RulePart2;
	}
	public String getNumItemsAllowed2() {
		return numItemsAllowed2;
	}
	public void setNumItemsAllowed2(String numItemsAllowed2) {
		this.numItemsAllowed2 = numItemsAllowed2;
	}

}
