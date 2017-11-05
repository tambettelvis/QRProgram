package ee.tanilsoo.src;

import java.util.ArrayList;
import java.util.List;

public class Job {

	private int priority;
	private String description;
	
	public static List<Job> jobs = new ArrayList<Job>();
	
	public Job(String description, int priority){
		this.priority = priority;
		this.description = description;
		jobs.add(this);
	}
	
	public Job(String description, int priority, boolean saveToDatabase){
		this.priority = priority;
		this.description = description;
		
		if(saveToDatabase){
			updateJobsPriorities();
			addToDatabase();
		}
		jobs.add(this);
	}
	
	public void updatePriority(boolean priorityDecrease){
		int jobPriority; //Job priority that we will compare with.
		if(priorityDecrease){
			jobPriority = this.priority - 1;
		} else {
			jobPriority = this.priority + 1;
		}
		
		for(Job job : jobs){
			if(job.getPriority() == jobPriority){
				if(priorityDecrease){
					job.setPriority(job.getPriority() + 1);
					this.priority -= 1;
				} else {
					job.setPriority(job.getPriority() - 1);
					this.priority += 1;
				}
				MysqlConnector.setJobPriority(job.getDescription(), job.getPriority());
				MysqlConnector.setJobPriority(this.description, this.priority);
				
				break;
			}
		}
	}
	
	private void addToDatabase(){
		MysqlConnector.insertNewJob(this.description, this.priority);
	}
	
	private void updateJobsPriorities(){
		for(Job job : jobs){
			if(job.getPriority() >= this.priority && job != this){
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
	
	public String getDescription(){
		return this.description;
	}
}
