package com.tanilsoo.tambet_telvis;

import java.util.ArrayList;
import java.util.List;

public class Job {

	private int priority;
	private String description;
	
	List<Job> jobs = new ArrayList<Job>();
	
	public Job(int priority, String description){
		this.priority = priority;
		this.description = description;
		if(doesSamePriorityExists()){
			updateJobsPriorities();
			updateDatabase();
		}
		jobs.add(this);
	}
	
	private void updateDatabase(){
		MysqlConnector.insertNewJob(this.description, this.priority);
	}
	
	private void updateJobsPriorities(){
		for(Job job : jobs){
			if(job.getPriority() >= this.priority){
				job.setPriority(job.getPriority() + 1);
			}
		}		
	}
	
	private boolean doesSamePriorityExists(){
		for(Job job : jobs){
			if(job.getPriority() == this.priority){
				return true;
			}
		}
		return false;
	}
	
	public int getPriority(){
		return priority;
	}

	public void setPriority(int amt){
		this.priority = amt;
	}
	
}
