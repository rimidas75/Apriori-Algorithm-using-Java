package edu.buffalo.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

public class GenerateRule {
	static Map<Set<String>, Integer> finalFrequentItemSetMapSmple = new HashMap<Set<String>, Integer>();
	double minConfidence;
	String ruleString = "";
	static Set<AssociationRule> ruleList = new HashSet<AssociationRule>();
	static Set<AssociationRule> finalRuleList = new HashSet<AssociationRule>();
	int itemsetSupportCount;
	int bodySupportCount;
	double confidenceBody;

	public GenerateRule(String ruleString, double minConfidence) {
		this.ruleString = ruleString;
		this.minConfidence = minConfidence;
	}

	public void parseQueryTemplate ()  throws Exception
	{
		String[] brokenRule = ruleString.split(" ");
		QueryTemplate qTemp = new QueryTemplate();
		qTemp.setQueryTempType(getTemplateType(brokenRule[0]));

		switch (qTemp.getQueryTempType()) {
		case 1: {
			qTemp.setTempRulePart(brokenRule[0]);
			qTemp.setNumItemsAllowed1(brokenRule[1]);
			qTemp.setItemset1(fetchItemSetFromString(brokenRule[2]));
			filterRuleSets(qTemp, 1);
		}
			break;
		case 2: {
			qTemp.setTempRulePart(brokenRule[1]);
			qTemp.setNumItemsAllowed1(brokenRule[2]);
			filterRuleSets(qTemp, 2);

		}
			break;
		case 3: {
			qTemp.setTempRulePart(brokenRule[0]);
			if(brokenRule[0].equalsIgnoreCase("1or1") || brokenRule[0].equalsIgnoreCase("1and1")) 
			 {
				qTemp.setTemp3RulePart1(brokenRule[1]);
				qTemp.setNumItemsAllowed1(brokenRule[2]);
				qTemp.setItemset1(fetchItemSetFromString(brokenRule[3]));
				qTemp.setTemp3RulePart2(brokenRule[4]);
				qTemp.setNumItemsAllowed2(brokenRule[5]);
				qTemp.setItemset2(fetchItemSetFromString(brokenRule[6]));
				filterRuleSets(qTemp, 3);

			}
			 else if(brokenRule[0].equalsIgnoreCase("1or2") || brokenRule[0].equalsIgnoreCase("1and2")) 
				 {
					qTemp.setTemp3RulePart1(brokenRule[1]);
					qTemp.setNumItemsAllowed1(brokenRule[2]);
					qTemp.setItemset1(fetchItemSetFromString(brokenRule[3]));
					qTemp.setTemp3RulePart2(brokenRule[4]);
					qTemp.setNumItemsAllowed2(brokenRule[5]);
					filterRuleSets(qTemp, 4);
					

				}
				 else if(brokenRule[0].equalsIgnoreCase("2or1") || brokenRule[0].equalsIgnoreCase("2and1")) 
					 {
						qTemp.setTemp3RulePart1(brokenRule[1]);
						qTemp.setNumItemsAllowed1(brokenRule[2]);
						
						qTemp.setTemp3RulePart2(brokenRule[3]);
						qTemp.setNumItemsAllowed2(brokenRule[4]);
						qTemp.setItemset2(fetchItemSetFromString(brokenRule[5]));
						filterRuleSets(qTemp, 5);
						

					}	
					 else if(brokenRule[0].equalsIgnoreCase("2or2") || brokenRule[0].equalsIgnoreCase("2and2")) 
						 {
							qTemp.setTemp3RulePart1(brokenRule[1]);
							qTemp.setNumItemsAllowed1(brokenRule[2]);
							
							qTemp.setTemp3RulePart2(brokenRule[3]);
							qTemp.setNumItemsAllowed2(brokenRule[4]);
							filterRuleSets(qTemp, 6);
							
						}

		}
		
		}
		
	}

	private void filterRuleSets(QueryTemplate qTemp, int i) {
		int count;
		switch (i) {
		case 1: {
			//template 1 
			for (AssociationRule ar : ruleList) {
				List<String> arItems = new ArrayList<String>();
				count = 0;
				
					if (qTemp.getTempRulePart().equalsIgnoreCase("BODY")) {
						arItems.addAll(ar.getBody());

					} else if (qTemp.getTempRulePart().equalsIgnoreCase("HEAD")) {
						arItems.addAll(ar.getHead());
					} else if (qTemp.getTempRulePart().equalsIgnoreCase("RULE")) {
						arItems.addAll(ar.getBody());
						arItems.addAll(ar.getHead());
					}
					for (String allowedItems : qTemp.getItemset1()) {
						if (arItems.contains(allowedItems))
							count++;
					}

					if (qTemp.getNumItemsAllowed1().equalsIgnoreCase("NONE") && count == 0) {
						finalRuleList.add(ar);
					} else if (qTemp.getNumItemsAllowed1().equals("1") && count == 1) {
						finalRuleList.add(ar);
					}
					else if (qTemp.getNumItemsAllowed1().equalsIgnoreCase("ANY") && count >= 1) {
						finalRuleList.add(ar);
					}
				

			}

		}
			break;
		case 2: {
			//template 2 
			for (AssociationRule ar : ruleList) {
				if (qTemp.getTempRulePart().equalsIgnoreCase("BODY")) {
					if (ar.getBody().size() >= Integer.parseInt(qTemp.getNumItemsAllowed1())) {
						finalRuleList.add(ar);
					}

				}

				else if (qTemp.getTempRulePart().equalsIgnoreCase("HEAD")) {
					if (ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed1())) {
						finalRuleList.add(ar);
					}
				} else if (qTemp.getTempRulePart().equalsIgnoreCase("RULE")) {
					if ((ar.getBody().size() + ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed1()))) {
						finalRuleList.add(ar);
					}
				}
			}

		}
			break;
		case 3: {
			//template 3  1or1 1and1
			int count1 = 0;
			int count2 = 0;
			boolean toAdd1 = false;
			boolean toAdd2 = false;
			for (AssociationRule ar : ruleList) {
				List<String> arItems = new ArrayList<String>();
				count1 = 0;
				count2 = 0;
				toAdd1 = false;
				toAdd2 = false;
				
					if (qTemp.getTemp3RulePart1().equalsIgnoreCase("BODY")) {
						arItems.addAll(ar.getBody());

					} else if (qTemp.getTemp3RulePart1().equalsIgnoreCase("HEAD")) {
						arItems.addAll(ar.getHead());
					} else if (qTemp.getTemp3RulePart1().equalsIgnoreCase("RULE")) {
						arItems.addAll(ar.getBody());
						arItems.addAll(ar.getHead());
					}
					for (String allowedItems : qTemp.getItemset1()) {
						if (arItems.contains(allowedItems))
							count1++;
					}

				arItems.clear();
				
					if (qTemp.getTemp3RulePart2().equalsIgnoreCase("BODY")) {
						arItems.addAll(ar.getBody());

					} else if (qTemp.getTemp3RulePart2().equalsIgnoreCase("HEAD")) {
						arItems.addAll(ar.getHead());
					} else if (qTemp.getTemp3RulePart2().equalsIgnoreCase("RULE")) {
						arItems.addAll(ar.getBody());
						arItems.addAll(ar.getHead());
					}
					for (String allowedItems : qTemp.getItemset2()) {
						if (arItems.contains(allowedItems))
							count2++;
					}
					
				
				if(!toAdd1)	
				{
				if (qTemp.getNumItemsAllowed1().equalsIgnoreCase("NONE") && count1 == 0) {
					
					toAdd1 = true;
					} else if (qTemp.getNumItemsAllowed1().equals("1") && count1 == 1) {
						
						toAdd1 = true;
					}	
					else if (qTemp.getNumItemsAllowed1().equalsIgnoreCase("ANY") && count1 >= 1) {
						
						toAdd1 = true;
					}	
					

				}	
				if(!toAdd2)	
				{
				if (qTemp.getNumItemsAllowed2().equalsIgnoreCase("NONE") && count2 == 0) {
						
					toAdd2 = true;
					} else if (qTemp.getNumItemsAllowed2().equals("1") && count2 == 1) {
						
						toAdd2 = true;
					}	
					else if (qTemp.getNumItemsAllowed2().equalsIgnoreCase("ANY") && count2 >= 1) {
						
						toAdd2 = true;
					}	

				}	
				if(qTemp.getTempRulePart().equalsIgnoreCase("1or1") && (toAdd1 || toAdd2))
				{
						finalRuleList.add(ar);
						
				}
				else if(qTemp.getTempRulePart().equalsIgnoreCase("1and1") && (toAdd1 && toAdd2))
				{
						finalRuleList.add(ar);
						
				}
				
			}

		}
			break;
			
		case 4 :{
			//template 3  1or2 1and2
			int count1 = 0;
			boolean toAdd1 = false;
			boolean toAdd2 = false;

			for (AssociationRule ar : ruleList) {
				List<String> arItems = new ArrayList<String>();
				count1 = 0;
				toAdd1 = false;
				toAdd2 = false;
				
					if (qTemp.getTemp3RulePart1().equalsIgnoreCase("BODY")) {
						arItems.addAll(ar.getBody());

					} else if (qTemp.getTemp3RulePart1().equalsIgnoreCase("HEAD")) {
						arItems.addAll(ar.getHead());
					} else if (qTemp.getTemp3RulePart1().equalsIgnoreCase("RULE")) {
						arItems.addAll(ar.getBody());
						arItems.addAll(ar.getHead());
					}
					for (String allowedItems : qTemp.getItemset1()) {
						if (arItems.contains(allowedItems))
							count1++;
					}
					
				
				
				arItems.clear();
				if (qTemp.getTemp3RulePart2().equalsIgnoreCase("BODY")) {
					if (ar.getBody().size() >= Integer.parseInt(qTemp.getNumItemsAllowed2())) {
					
						toAdd2 = true;
					}

				}

				else if (qTemp.getTemp3RulePart2().equalsIgnoreCase("HEAD")) {
					if (ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed2())) {
						
						toAdd2 = true;
					}
				} else if (qTemp.getTemp3RulePart2().equalsIgnoreCase("RULE")) {
					if ((ar.getBody().size() + ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed2()))) {
						
						toAdd2 = true;
					}
				}
				if(!toAdd1)	
				{
				if (qTemp.getNumItemsAllowed1().equalsIgnoreCase("NONE") && count1 == 0) {
						
					toAdd1 = true;
					} else if (qTemp.getNumItemsAllowed1().equals("1") && count1 == 1) {
						
						toAdd1 = true;
					}	
					else if (qTemp.getNumItemsAllowed1().equalsIgnoreCase("ANY") && count1 >= 1) {
						
						toAdd1 = true;
					}

				}	
				
				if(qTemp.getTempRulePart().equalsIgnoreCase("1or2") && (toAdd1 || toAdd2))
				{
						finalRuleList.add(ar);
						
				}
				else if(qTemp.getTempRulePart().equalsIgnoreCase("1and2") && (toAdd1 && toAdd2))
				{
						finalRuleList.add(ar);
						
				}
				
			}

		
		}
		break;
		case 5 : 
			//template 3  2or1 2and1
		{
			
			int count2 = 0;
			boolean toAdd1 = false;
			boolean toAdd2 = false;
			for (AssociationRule ar : ruleList) {
				List<String> arItems = new ArrayList<String>();
				count2 = 0;
				toAdd1 = false;
				toAdd2 = false;
				
					if (qTemp.getTemp3RulePart2().equalsIgnoreCase("BODY")) {
						arItems.addAll(ar.getBody());

					} else if (qTemp.getTemp3RulePart2().equalsIgnoreCase("HEAD")) {
						arItems.addAll(ar.getHead());
					} else if (qTemp.getTemp3RulePart2().equalsIgnoreCase("RULE")) {
						arItems.addAll(ar.getBody());
						arItems.addAll(ar.getHead());
					}
					for (String allowedItems : qTemp.getItemset2()) {
						if (arItems.contains(allowedItems))
							count2++;
					}
					
				
				
				arItems.clear();
				if (qTemp.getTemp3RulePart1().equalsIgnoreCase("BODY")) {
					if (ar.getBody().size() >= Integer.parseInt(qTemp.getNumItemsAllowed1())) {
						
						toAdd1 = true;
					}

				}

				else if (qTemp.getTemp3RulePart1().equalsIgnoreCase("HEAD")) {
					if (ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed1())) {
						
						toAdd1 = true;
					}
				} else if (qTemp.getTemp3RulePart1().equalsIgnoreCase("RULE")) {
					if ((ar.getBody().size() + ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed1()))) {
						
						toAdd1 = true;
					}
				}
				if(!toAdd2)	
				{
				if (qTemp.getNumItemsAllowed2().equalsIgnoreCase("NONE") && count2 == 0) {
					
					toAdd2 = true;
					} else if (qTemp.getNumItemsAllowed2().equals("1") && count2 == 1) {
						
						toAdd2 = true;
					}	
					else if (qTemp.getNumItemsAllowed2().equalsIgnoreCase("ANY") && count2 >= 1) {
						
						toAdd2 = true;
					}

				}	
				
				if(qTemp.getTempRulePart().equalsIgnoreCase("2or1") && (toAdd1 || toAdd2))
				{
						finalRuleList.add(ar);
						
				}
				else if(qTemp.getTempRulePart().equalsIgnoreCase("2and1") && (toAdd1 && toAdd2))
				{
						finalRuleList.add(ar);
						
				}
				
			}

			
		}
		break;
		case 6 : {
			//template 3 2or2 2and2
				boolean toAdd1 = false;
				boolean toAdd2 = false;
			for (AssociationRule ar : ruleList) {
				toAdd1 = false;
				toAdd2 = false;
				if (qTemp.getTemp3RulePart1().equalsIgnoreCase("BODY")) {
					if (ar.getBody().size() >= Integer.parseInt(qTemp.getNumItemsAllowed1())) {
						
						toAdd1 = true;
					}

				}

				else if (qTemp.getTemp3RulePart1().equalsIgnoreCase("HEAD")) {
					if (ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed1())) {
						toAdd1 = true;
					}
				} else if (qTemp.getTemp3RulePart1().equalsIgnoreCase("RULE")) {
					if ((ar.getBody().size() + ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed1()))) {
						toAdd1 = true;
					}
				}
				
				if (qTemp.getTemp3RulePart2().equalsIgnoreCase("BODY")) {
					if (ar.getBody().size() >= Integer.parseInt(qTemp.getNumItemsAllowed2())) {
						
						toAdd2 = true;
					}

				}

				else if (qTemp.getTemp3RulePart2().equalsIgnoreCase("HEAD")) {
					if (ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed2())) {
						
						toAdd2 = true;
					}
				} else if (qTemp.getTemp3RulePart2().equalsIgnoreCase("RULE")) {
					if ((ar.getBody().size() + ar.getHead().size() >= Integer.parseInt(qTemp.getNumItemsAllowed2()))) {
						
						toAdd2 = true;
					}
				}
				
				if(qTemp.getTempRulePart().equalsIgnoreCase("2or2") && (toAdd1 || toAdd2))
				{
						finalRuleList.add(ar);
						
				}
				else if(qTemp.getTempRulePart().equalsIgnoreCase("2and2") && (toAdd1 && toAdd2))
				{
						finalRuleList.add(ar);
						
				}
				
			}

		
			
		}
		break;
		}
		
		
		
	}

	private HashSet<String> fetchItemSetFromString(String stringItemset) {
		stringItemset = stringItemset.substring(1, stringItemset.length() - 1);
		HashSet<String> setFromString = new HashSet<String>(); 
		StringTokenizer st = new StringTokenizer(stringItemset, ",");
		while (st.hasMoreTokens())
			setFromString.add(st.nextToken().replaceAll("'", ""));
		return setFromString;

	}

	private int getTemplateType(String brokenRule) {
		switch (brokenRule.toUpperCase()) {
		case "RULE":
			return 1;
		case "BODY":
			return 1;
		case "HEAD":
			return 1;
		case "SIZEOF":
			return 2;

		default:
			return 3;
		}

	}

	public void generateRuleFromItemSets(Map<Set<String>, Integer> finalFrequentItemSetMap) {
		finalFrequentItemSetMapSmple.putAll(finalFrequentItemSetMap);
		
		for (Entry<Set<String>, Integer> scEntry : finalFrequentItemSetMap.entrySet()) {
			if (scEntry.getKey().size() > 1) {

				checkForSubsets(scEntry.getKey());
				
			}
		}
		
	}

	public void getTotalRulesSize()
	{
		System.out.println("Total Number of rules initially generated = " + ruleList.size());
	}

	private void checkForSubsets(Set<String> key) {
		
		AssociationRule  arule;
		for (Set<String> s : PowerSet.powerSet(key)) {
			 if(!s.isEmpty() && !s.equals(key))
			 {
			itemsetSupportCount = finalFrequentItemSetMapSmple.get(key);
			try{
			bodySupportCount = finalFrequentItemSetMapSmple.get(s);
			}catch(NullPointerException e)
			{
				bodySupportCount = -1;
			}
			
			
			confidenceBody = (1.0 * itemsetSupportCount) / bodySupportCount;
			if (confidenceBody >= minConfidence ) {
				Set<String> hedSet = new HashSet<String>(key);
				hedSet.removeAll(s);
				 arule = new AssociationRule(s, hedSet,confidenceBody);
				
				if (!ruleList.contains(arule)) {
					ruleList.add(arule);
										
				}
				
			}
			}
		

	 }
		
	}

	

	public void displayRules() {
		System.out.println("Query results :");
		for(AssociationRule sd :finalRuleList )
			System.out.println(sd.getBody() + " --> "+ sd.getHead());
		System.out.println("Number of Rules = "+ finalRuleList.size());
		
	}
	

}
