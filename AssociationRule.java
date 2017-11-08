package edu.buffalo.dm;

import java.util.Set;
import java.util.TreeSet;

public class AssociationRule {
	Set<String> body;
	Set<String> head;
	double conf;
	public AssociationRule(Set<String> subsetBody, Set<String> ruleHead, double confidence) {
		this.body = new TreeSet<String>(subsetBody);
		this.head = new TreeSet<String>(ruleHead);
		this.conf = confidence;
	}
	public Set<String> getBody() {
		return body;
	}
	public void setBody(Set<String> body) {
		this.body = body;
	}
	public Set<String> getHead() {
		return head;
	}
	public void setHead(TreeSet<String> head) {
		this.head = head;
	}
	public double getConf() {
		return conf;
	}
	public void setConf(double conf) {
		this.conf = conf;
	}
	@Override
	public int hashCode() {
	//System.out.println("In hashcode "+"value is :"+this.body+ " "+this.head);
	int hash = 3;
	hash = 7 * hash + this.body.hashCode() -  this.head.hashCode() ;
	return hash;
	}
	public boolean equals(Object o){
		//System.out.println("In equals " +"value is :" +this.body+ " "+this.head);
		AssociationRule arule = (AssociationRule)o;
		if(arule.getBody().equals(this.body) && arule.getHead().equals(this.head)){
		return true;
		}
		return false;
		}
		 
}
