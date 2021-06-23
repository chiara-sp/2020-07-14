package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Simulatore {

	//coda 
	PriorityQueue<Match> queue;
	
	//mondo 
	int N;
	int X;
	Map<Integer, Integer> reporterPerSquadra;
	PremierLeagueDAO dao;
	Map<Integer,Team> teams;
	Model model;
	
	//output
	double mediaReporter;
	int numPartite;
	int numCriticità;
	
	public void init(int N, int X) {
		this.N=N;
		this.X=X;
		dao= new PremierLeagueDAO();
		model= new Model();
		teams= new HashMap<>();
		for(Team t: dao.listAllTeams()) {
			teams.put(t.teamID, t);
		}
		
	  this.reporterPerSquadra= new HashMap<>();
	  for (Team t: teams.values()) {
		  this.reporterPerSquadra.put(t.getTeamID(), N);
	  }
	  queue= new PriorityQueue<>();
	  for(Match m: dao.listAllMatches()) {
		  queue.add(m);
	  }
	  mediaReporter=0;
	  numPartite=0;
	  numCriticità=0;
	}
	public void run() {
		Match match;
		while((match=queue.poll())!=null) {
			numPartite++;
			mediaReporter+= reporterPerSquadra.get(match.teamHomeID)+ reporterPerSquadra.get(match.teamAwayID);
			Team home= teams.get(match.getTeamHomeID());
			Team away= teams.get(match.teamAwayID);
			
			if((reporterPerSquadra.get(home.getTeamID())+reporterPerSquadra.get(away.getTeamID()))<this.X) {
				this.numCriticità++;
			}
			
			if(match.resultOfTeamHome==1) {
				double num= Math.random();
				if(num<0.5) {
					if(reporterPerSquadra.get(home.getTeamID())>0) {
						
						int index= (int)Math.random()*model.migliori(home).size();
						if(model.migliori(home).size()>0) {
						reporterPerSquadra.put(model.migliori(home).get(index).teamID, reporterPerSquadra.get(model.migliori(home).get(index).teamID)+1);
						reporterPerSquadra.put(match.teamHomeID,reporterPerSquadra.get(match.teamHomeID)-1);
						}
					}
				}
				if(num<0.2) {
					if(this.reporterPerSquadra.get(away.getTeamID())>0) {
					int numReporterBocciati= (int)Math.random()*this.reporterPerSquadra.get(away.getTeamID());
					if(model.peggiori(away).size()>0) {
						int index= (int)Math.random()*model.peggiori(away).size();
					
						reporterPerSquadra.put(model.peggiori(away).get(index).teamID, reporterPerSquadra.get(model.peggiori(away).get(index).teamID)+numReporterBocciati);
						reporterPerSquadra.put(match.teamAwayID,reporterPerSquadra.get(match.teamAwayID)-numReporterBocciati);
						}
					}
				}
				
			}
             if(match.resultOfTeamHome==-1) {
   
            	 double num= Math.random();
 				if(num<0.5) {
 					if(reporterPerSquadra.get(away.getTeamID())>0) {
 						
 						int index= (int)Math.random()*model.migliori(away).size();
 						if(model.migliori(away).size()>0) {
 						reporterPerSquadra.put(model.migliori(away).get(index).teamID, reporterPerSquadra.get(model.migliori(away).get(index).teamID)+1);
 						reporterPerSquadra.put(match.teamAwayID,reporterPerSquadra.get(match.teamAwayID)-1);
 						}
 					}
 				}
 				if(num<0.2) {
 					if(this.reporterPerSquadra.get(home.getTeamID())>0) {
 					int numReporterBocciati= (int)Math.random()*this.reporterPerSquadra.get(home.getTeamID());
 					if(model.peggiori(home).size()>0) {
 						int index= (int)Math.random()*model.peggiori(home).size();
 					
 						reporterPerSquadra.put(model.peggiori(home).get(index).teamID, reporterPerSquadra.get(model.peggiori(home).get(index).teamID)+numReporterBocciati);
 						reporterPerSquadra.put(match.teamHomeID,reporterPerSquadra.get(match.teamHomeID)-numReporterBocciati);
 						}
 					}
 				}
			}
             if(match.resultOfTeamHome==0) {
 				
 			}
		}
	}
	public double mediaReporter() {
		return this.mediaReporter/this.numPartite;
	}
	public int ctiticita() {
		return this.numCriticità;
	}
}
