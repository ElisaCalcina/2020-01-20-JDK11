package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

//USIAMO DEGLI ID E NON DEGLI AUTORI PER SEMPLICITA' PER QUANTO RIGUARDA I VERTICI (NO IDMAP)

public class Model {
	
	private ArtsmiaDAO dao;
	private Graph<Integer,DefaultWeightedEdge> grafo;
	private List<Adiacenze> adiacenze;
	private List<Integer> best;
	
	public Model() {
		dao= new ArtsmiaDAO();
	}
	
	public List<String> getRuoli(){
		return this.dao.getRuoli();
	}
	
	public void creaGrafo(String ruolo) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		adiacenze = this.dao.getAdiacenze(ruolo);
		
		//2 opzioni per l'introduzione dei vertici nel grafo:
		//1) mi faccio dare tutti i vertici che devo inserire nel grafo dal db e successivamente aggiungere gli archi tra questi vertici (con le adiacenze)
		//2) avendo nelle adiacenze gli id, li usiamo per controllare se tali vertici esistono già nel grafo o no e se non ci sono ancora aggiungerli direttamente
		
		//la differenza sta nel fatto che nelle adiacenze potrebbero mancare dei vertici (ad esempio quelli isolati che non hanno adiacenze con nessuno)
		
		//la soluzione migliore dipende dalle richieste dell'esercizio: quando si parla di componenti connesse (ultima richiesta= stampa numero di componenti connesse) --> aggiungo tutti i vertici a priori prima delle adiacenze
		// altrimenti, se le richieste sono relative a vertici che hanno una adiacenza, allora va bene inserire solo i vertici che recuperiamo dalle adiacenze
		
		
		//in questo caso aggiungo i vertici partendo dalle adiacenze
		/*for(Adiacenze a: adiacenze) {
			if(!grafo.containsVertex(a.getObj1()))
				this.grafo.addVertex(a.getObj1());
			if(!grafo.containsVertex(a.getObj2()))
				this.grafo.addVertex(a.getObj2());
			
			if(this.grafo.getEdge(a.getObj1(), a.getObj2())==null)
				Graphs.addEdgeWithVertices(grafo, a.getObj1(),  a.getObj2(), a.getPeso());
		}*/

		//se voglio aggiungere tutti gli artisti con un determinato ruolo a prescindere dalle adiacenze
		//se volessi inserire tutti i vertici, controllo solo più gli archi
		
		//SE SCELGO QUESTA VIA SONO SICURO DI POTER RISPONDERE A TUTTE LE RICHIESTE
		Graphs.addAllVertices(grafo, this.dao.getArtisti(ruolo));
		for(Adiacenze a: adiacenze) {
		if(this.grafo.getEdge(a.getObj1(), a.getObj2())==null)
			Graphs.addEdgeWithVertices(grafo, a.getObj1(),  a.getObj2(), a.getPeso());
		}
		
		//per entrambi
		System.out.println(String.format("Grafo creato con %d vertici e %d archi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenze> getAdiacenze(){
		return this.adiacenze;
	}
	
	public List<Integer> trovaPercorso(Integer sorgente){
		best= new ArrayList<>();
		List<Integer> parziale= new ArrayList<Integer>();
		parziale.add(sorgente);
		//lancio la ricorsione
		ricorsione(parziale, -1); //peso che non c'è nel grafo -> sono al primo giro nella ricorsione
		
		return best;
	}
	
	//dobbiamo esplorare percorsi in cui tutti gli archi abbiano lo stesso peso
	private void ricorsione(List<Integer> parziale, int peso) {
	
		Integer ultimo= parziale.get(parziale.size()-1);
		//ottengo i vicini
		List<Integer> vicini = Graphs.neighborListOf(grafo,ultimo);
		for(Integer vicino: vicini) {
			if(!parziale.contains(vicino) && peso==-1) {
				parziale.add(vicino);
				ricorsione(parziale, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)));
				parziale.remove(vicino);
			} else {
				//aggiungo solo i vicini collegati con archi di quel peso
				if(!parziale.contains(vicino) && this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino))==peso) {
					parziale.add(vicino);
					ricorsione(parziale, peso);
					parziale.remove(vicino);
				}
			}
		}
		
		
		if(parziale.size() > best.size()) {
			this.best= new ArrayList<>(parziale);
		}
		
	}
	
	public boolean grafoContiene(Integer id) {
		if(this.grafo.containsVertex(id))
			return true;
		else
			return false;
	}
	
	//oppure nuova classe --> artist che modella il db e poi idMap 
	//questo se vogliamo mettere gli artisti come vertici e non gli id interi
	
}
