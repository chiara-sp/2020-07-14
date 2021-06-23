package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private List<Team> teams;
	Simulatore sim;
	private SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao= new PremierLeagueDAO();
		teams= new LinkedList<>(dao.listAllTeams());
		for(Team t: teams) {
			dao.setPunteggio(t);
		}
	
	}
	public void creaGrafo() {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.listAllTeams());
		
		for(Team t1: teams) {
			for(Team t2: teams) {
				int punteggio1= t1.getPunteggio();
				int punteggio2= t2.getPunteggio();
				if(punteggio1>punteggio2) {
					Graphs.addEdge(grafo, t1, t2, punteggio1-punteggio2);
				}
				if (punteggio2> punteggio1) {
					Graphs.addEdge(grafo, t2, t1, punteggio2-punteggio1);
				}
			}
		}
		
		
	}
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	public List<Team> getTeams(){
		Collections.sort(teams);
		return teams;
	}
	public List<Team> peggiori(Team team){
		List<Team> result= new LinkedList<>();
		
		for(Team t: teams) {
			if(t.getPunteggio()<team.getPunteggio())
				result.add(t);
		}
		Collections.sort(result, new ComparatorPunt<Team>());
		return result;
	}
	public List<Team> migliori(Team team){
		List<Team> result= new LinkedList<>();
		for(Team t: teams) {
			if(t.getPunteggio()>team.getPunteggio())
				result.add(t);
		}
		Collections.sort(result, new ComparatorPunt<Team>());
		return result;
	}
	public void simula(int N, int X) {
		sim = new Simulatore();
		sim.init(N, X);
		sim.run();
	}
	public double mediaReporter() {
		return sim.mediaReporter();
	}
	public int criticita() {
		return sim.ctiticita();
	}
}
