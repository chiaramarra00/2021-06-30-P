package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {

	private Graph<String, DefaultWeightedEdge> grafo;
	private GenesDao dao;
	private List<String> listaMigliore;

	public Model() {
		dao = new GenesDao();
	}

	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getAllLocalizzazioni());
		for (Adiacenza a : this.dao.getArchi()) {
			Graphs.addEdgeWithVertices(this.grafo, a.getLocalization1(), a.getLocalization2(), a.getPeso());
		}
		System.out.println("Grafo creato!");
		System.out.println(String.format("# Vertici: %d", this.grafo.vertexSet().size()));
		System.out.println(String.format("# Archi: %d", this.grafo.edgeSet().size()));
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public List<String> getAllLocalizzazioni() {
		return dao.getAllLocalizzazioni();
	}

	public List<Adiacenza> getStatistiche(String l1) {
		List<String> adiacenti = Graphs.neighborListOf(grafo, l1);
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		for (String l2 : adiacenti) {
			result.add(new Adiacenza(l1, l2, (int) grafo.getEdgeWeight(grafo.getEdge(l1, l2))));
		}
		return result;
	}

	public List<String> doRicerca(String localizzazione) {
		List<String> localizzazioniValide = new ArrayList<String>(Graphs.neighborListOf(grafo, localizzazione));
		
		List<String> parziale = new ArrayList<>();
		listaMigliore = new ArrayList<>();
		parziale.add(localizzazione);

		cerca(parziale, localizzazioniValide);

		return listaMigliore;
	}

	private void cerca(List<String> parziale, List<String> localizzazioniValide) {
		if(sommaPesi(parziale) > sommaPesi(listaMigliore)) {
			listaMigliore = new ArrayList<>(parziale);
		}
		
		for(String l2 : localizzazioniValide) {
			if(!parziale.contains(l2)) {
				parziale.add(l2);
				cerca(parziale, Graphs.neighborListOf(grafo, l2));
				parziale.remove(parziale.size()-1);
			}
		}
	}

	private int sommaPesi(List<String> parziale) {
		int somma=0;
		for (int i=1; i<parziale.size(); i++) {
			somma+=grafo.getEdgeWeight(grafo.getEdge(parziale.get(i-1),parziale.get(i)));
		}
		return somma;
	}

}