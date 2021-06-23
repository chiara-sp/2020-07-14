package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

public class ComparatorPunt<T> implements Comparator<Team> {

	@Override
	public int compare(Team o1, Team o2) {
		// TODO Auto-generated method stub
		return -(o1.getPunteggio()-o2.getPunteggio());
	}

}
