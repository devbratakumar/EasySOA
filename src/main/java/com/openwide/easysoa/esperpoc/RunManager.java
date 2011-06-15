package com.openwide.easysoa.esperpoc;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

public class RunManager {
	
	/**
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(RunManager.class.getName());	

	/**
	 * 
	 */
	private static RunManager runManager = null;

	/**
	 * The current run
	 */
	private Run currentRun;
	
	/**
	 * Run list
	 */
	private ArrayDeque<Run> runList;
	
	// Contains a collection of Run's & Methods to manipulate a Run	
	// runs, start() (if not autostart), stop(), listRuns() / getLastRun()..., rerun(Run) 
	
	private RunManager(){
		runList = new ArrayDeque<Run>();
	}
	
	/**
	 * Return an instance of RunManager
	 * @return An instance of <code>RunManager</code>
	 */
	public static RunManager getInstance(){
		if(runManager == null){
			runManager = new RunManager();
		}
		return runManager;
	}
	
	/**
	 * Returns the current run. null is returned if there is no current run ! 
	 * @return The current <code>Run</code>
	 */
	public Run getCurrentRun(){
		return this.currentRun;
	}
	
	/**
	 * Starts a new run. A new <code>Run</code> is started only if the current run was stopped before with a call to the stop() method. 
	 */
	public void start(){
		if(currentRun == null){
			currentRun = new Run();
			currentRun.setStartDate(new Date());
		}
	}
	
	/**
	 * Stop the current run
	 */
	public void stop(){
		if(this.currentRun != null){
			this.currentRun.setStopDate(new Date());
			this.runList.add(currentRun);
			this.currentRun = null;
		}
	}
	
	/**
	 * Returns the run list
	 * @return A <code>List</code> of <code>Run</code>
	 */
	public List<Run> listRuns(){
		return this.listRuns();
	}
	
	/**
	 * Returns the last run
	 * @return The last <code>Run</code>
	 */
	public Run getLastRun(){
		return this.runList.getLast();
	}
	
	/**
	 * 
	 * @param run
	 */
	public void reRun(Run run){
		
	}
	
}
