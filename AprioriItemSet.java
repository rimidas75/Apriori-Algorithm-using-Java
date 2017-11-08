package edu.buffalo.dm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class AprioriItemSet {
	public static int totalTransactions;
	public static double sup;
	Set<Set<String>> itemsetList = new HashSet<Set<String>>();
	static Map<Set<String>, Integer> supportCountMap = new HashMap<Set<String>, Integer>();
	Map<Set<String>, Integer> frequentItemSetMap = new HashMap<Set<String>, Integer>();
	static Map<Set<String>, Integer> finalFrequentItemSetMap = new HashMap<Set<String>, Integer>();
	Set<TreeSet<String>> distinctList = new HashSet<TreeSet<String>>();
	public boolean added = false;
	static int maxSetSize=0;

	public static void main(String[] args) {
		AprioriItemSet rf = new AprioriItemSet();
		String filePath = args[0];
		sup = Double.parseDouble(args[1]);
		boolean genRules = Boolean.parseBoolean(args[2]);
		try {
			rf.readFile(filePath);
		} catch (Exception e) {
			System.out.println("Error in fetching or Reading File");
		}
		//sup = 0.3;
		int k = 1;
		while (k <= maxSetSize && rf.added) {
			rf.filterFrequentItemSets();
			k++;
			rf.mergeItemSet(k);
			rf.findsupportCount();
			
		}
		rf.displaySupportCountMap();
		// TEMPLATE 1 sample query --- "RULE ANY ['G82_Down','G59_Up']"
		// TEMPLATE 2 sample query --- "SIZEOF RULE 4"
		// TEMPLATE 3 sample query --- "1AND1 RULE ANY ['G10_Down'] RULE 1 ['G59_Up']"
		// TEMPLATE 3 sample query --- "1AND2 RULE 1 ['G82_Down'] HEAD 1"
		// TEMPLATE 3 sample query --- "2OR1 HEAD 1 RULE 1 ['G82_Down']"
		// TEMPLATE 3 sample query --- "2OR2 HEAD 3 RULE 3"
		if(genRules)
		{
			String ruleString = args[3];
			double minConfidence = Double.parseDouble(args[4]);
			boolean displayRules = Boolean.parseBoolean(args[5]);
		//ruleString = "1AND2 RULE 1 ['G82_Down'] HEAD 2";
		GenerateRule gr = new GenerateRule(ruleString,minConfidence);
		gr.generateRuleFromItemSets(finalFrequentItemSetMap);
		gr.getTotalRulesSize();
		try {
			gr.parseQueryTemplate();
		} catch (Exception e) {
			System.out.println("Invalid query format");
		}
		if(displayRules)
		{
			gr.displayRules();
		}
		}
		//System.out.println("DONE");

	}

	private void displaySupportCountMap() {
		int[] lengthArr = new int[maxSetSize+1];
		int itemSetSize = 0;
		for(Set<String> st :finalFrequentItemSetMap.keySet() )
		{
			itemSetSize = st.size();
			lengthArr[itemSetSize] = lengthArr[itemSetSize]+1;
			
		}
		int k= 1;
		System.out.println("Support is set to be "+ sup*100 +"%");
		try{
		while(lengthArr[k]>0)
		{
		
			System.out.println("number of length-"+k+" frequent itemsets:"+ lengthArr[k]);
			k++;
		}
		}catch(Exception e)
		{
			System.out.println("Error in initialization");
		}
		
		
	}

	private void findsupportCount() {
		
		 supportCountMap.clear();
		
			for (TreeSet<String> newAddedItemSet : distinctList) {
				for (Set<String> itemSet : itemsetList) {
				if (itemSet.containsAll(newAddedItemSet)) {
					if (supportCountMap.containsKey(newAddedItemSet)) {
						supportCountMap.put(newAddedItemSet, supportCountMap.get(newAddedItemSet) + 1);
					} else
						supportCountMap.put(newAddedItemSet, 1);
				}
			}
		}

	}

	private void mergeItemSet(int k) {
		
		added = false;
		distinctList.clear();
		int mapSize = frequentItemSetMap.size();
		List<Set<String>> tempFrequentItemSetList = new ArrayList<Set<String>>();
		TreeSet<String> distinct;
		tempFrequentItemSetList.addAll(frequentItemSetMap.keySet());
		boolean toMerge = true;
		 for (int i = 0; i < mapSize -1; ++i) {
	            for (int j = i + 1; j < mapSize; ++j) {
	            	toMerge = true;
	            	if(k>2)
	            	{
	            		Iterator<String>  firstIter = tempFrequentItemSetList.get(i).iterator();
	            		Iterator<String>  secIter = tempFrequentItemSetList.get(j).iterator();
	            		for(int p = 0;p<k-2;p++)
	            		{
	            			if(!firstIter.next().equals(secIter.next()))
	            			{
	            				toMerge = false;
	            				break;
	            			}
	            				
	            		}
	            		
	            	}
	            	if(toMerge)
	            	{
	            	distinct = mergeTwoItemsets(tempFrequentItemSetList.get(i), tempFrequentItemSetList.get(j));
	                if (!finalFrequentItemSetMap.containsKey(distinct) &&!distinctList.contains(distinct)) {
						added = true;
						distinctList.add(distinct);
					}
	            	}
	            }
	        }
		

	}

	private TreeSet<String> mergeTwoItemsets(Set<String> set1, Set<String> set2) {
		TreeSet<String> tset = new TreeSet<String>(set1);
		tset.addAll(set2);
		return tset;
	}

	public void filterFrequentItemSets() {
		
		 frequentItemSetMap.clear();
		for (Entry<Set<String>, Integer> entry : supportCountMap.entrySet()) {
			
			if (!finalFrequentItemSetMap.containsKey(entry.getKey()) && (1.0 *entry.getValue())/totalTransactions >= sup) {
				frequentItemSetMap.put(entry.getKey(), entry.getValue());
			}

		}
		finalFrequentItemSetMap.putAll(frequentItemSetMap);
	}


	public void readFile(String filename) throws Exception {
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(filename);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			String[] broken_text;
			
			
			// Read File Line By Line
			try {
				while ((strLine = br.readLine()) != null) {
					// Print the content on the console
					// System.out.println (strLine);
					broken_text = strLine.split("\t");
					if (broken_text.length > maxSetSize)
						maxSetSize = broken_text.length;
					for (int i = 0; i < broken_text.length; i++) {
						
						 if(broken_text[i].equals("")||
						  broken_text[i].isEmpty()) {
							 break;
						 }
						 

						if (broken_text[i].equalsIgnoreCase("down")) {
							broken_text[i] = "G" + (i + 1) + "_Down";
						} else if (broken_text[i].equalsIgnoreCase("up")) {
							broken_text[i] = "G" + (i + 1) + "_Up";
						}
						Set<String> brokenTextSet = new HashSet<String>();
						brokenTextSet.add(broken_text[i]);
						if (supportCountMap.containsKey(brokenTextSet)) {
							supportCountMap.put(brokenTextSet, supportCountMap.get(brokenTextSet) + 1);

						} else {
							supportCountMap.put(brokenTextSet, 1);
							added = true;
						}

					}
					Set<String> rowItemSet = new HashSet<String>();
					for (String s : broken_text) {

						rowItemSet.add(s);
					}
					itemsetList.add(rowItemSet);

				}
				totalTransactions = itemsetList.size();
				br.close();
			} catch (IOException e) {
				
				System.out.println("Error in reading & parsing the file");
			}

			// Close the input stream

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

	}

}
