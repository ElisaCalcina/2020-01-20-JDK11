package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenze;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	List<Adiacenze> adiacenze= this.model.getAdiacenze();
    	
    	if(adiacenze==null) {
    		txtResult.appendText("Devi creare prima il grafo");
    		return;
    	}
    	
    	Collections.sort(adiacenze);
    	for(Adiacenze a: adiacenze) {
    		txtResult.appendText(String.format("(%d, %d)= %f\n ", a.getObj1(), a.getObj2(), a.getPeso()));
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Integer id;
    	
    	try {
    		id= Integer.parseInt(txtArtista.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("Inserire un id nel formato corretto");
    		return;
    	}
    	
    	//id deve essere un vertice nel grafo
    	if(!this.model.grafoContiene(id)) {
    		txtResult.appendText("L'artista non è nel grafo");
    		return;
    	}
    	List<Integer> percorso= this.model.trovaPercorso(id);
    	txtResult.appendText("Percorso più lungo: " + percorso.size()+ "\n");
    	for(Integer v: percorso) {
    		txtResult.appendText(v+" ");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String ruolo= boxRuolo.getValue();
    	if(ruolo==null) {
    		txtResult.appendText("Inserire un ruolo");
    		return;
    	}
    	
    	this.model.creaGrafo(ruolo);
    	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi", this.model.nVertici(), this.model.nArchi()));
    	
    	this.btnCalcolaPercorso.setDisable(false);
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.btnCalcolaPercorso.setDisable(true);
    	this.boxRuolo.getItems().addAll(this.model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
