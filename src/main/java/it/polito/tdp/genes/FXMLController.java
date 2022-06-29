package it.polito.tdp.genes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnStatistiche;

    @FXML
    private Button btnRicerca;

    @FXML
    private ComboBox<String> boxLocalizzazione;

    @FXML
    private TextArea txtResult;

    @FXML
    void doRicerca(ActionEvent event) {
    	txtResult.clear();
    	String localizzazione = boxLocalizzazione.getValue();
    	if (localizzazione == null) {
    		txtResult.setText("Selezionare una localizzazione.\n");
    		return;
    	}
    	List<String> cammino = model.doRicerca(localizzazione);
    	for (String s : cammino) {
    		txtResult.appendText(s+"\n");
    	}
    }

    @FXML
    void doStatistiche(ActionEvent event) {
    	String localizzazione = boxLocalizzazione.getValue();
    	if (localizzazione == null) {
    		txtResult.setText("Selezionare una localizzazione.\n");
    		return;
    	}
    	List<Adiacenza> statistiche = model.getStatistiche(localizzazione);
    	txtResult.appendText("Adiacenti a: "+localizzazione+"\n");
    	for (Adiacenza a : statistiche) {
    		txtResult.appendText(a.getLocalization2()+" "+a.getPeso()+"\n");
    	}
    	txtResult.appendText("\n");
    }

    @FXML
    void initialize() {
        assert btnStatistiche != null : "fx:id=\"btnStatistiche\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicerca != null : "fx:id=\"btnRicerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxLocalizzazione != null : "fx:id=\"boxLocalizzazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		model.creaGrafo();
		txtResult.setText("Grafo creato: "+model.nVertici()+" vertici, "+model.nArchi()+" archi\n\n");
		boxLocalizzazione.getItems().setAll(model.getAllLocalizzazioni());
	}
}
