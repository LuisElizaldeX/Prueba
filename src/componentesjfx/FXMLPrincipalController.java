package componentesjfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


/**
 *
 * @author king_
 */
public class FXMLPrincipalController implements Initializable {
    
    @FXML
    private Label lbResultado;
    @FXML
    private TextField tfMonto;
   
    private static final double IVA=0.16;
    private ObservableList<String> carrerasUV;
    private File imagenSeleccionada;
    
    @FXML
    private ComboBox<String> cbCarreras;
    @FXML
    private Label lbCarrera;
    @FXML
    private TextField tfNuevaCarrera;
    @FXML
    private Label lbError;
    @FXML
    private RadioButton rbAzul;
    @FXML
    private ToggleGroup tgColores;
    @FXML
    private RadioButton rbVerde;
    @FXML
    private RadioButton rbAmarillo;
    @FXML
    private Pane paneFondo;
    @FXML
    private Label lbColor;
    @FXML
    private CheckBox cbJamon;
    @FXML
    private CheckBox cbPepperoni;
    @FXML
    private CheckBox cbSalami;
    @FXML
    private CheckBox cbPinia;
    @FXML
    private CheckBox cbOtro;
    @FXML
    private TextField tfOtroIngrediente;
    @FXML
    private Label lbErrorIngrediente;
    @FXML
    private ImageView ivFoto;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TabPane tabPaneOpciones;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InicializarDatosCarreras();
        cbCarreras.valueProperty().addListener(new ChangeListener<String>(){
            
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                lbCarrera.setText("Carrera seleccionada: "+ newValue);
                System.out.println("Old value: "+ oldValue);
            }
            
        });
        implementarEventoRadioGroup();
        configurarEventoCheckBoxOtro();
    }
    
    @FXML
    private void btnClicCalcularIVA(ActionEvent event){
        String cantidad = tfMonto.getText();
        if(!cantidad.isEmpty()){
            try{
                int montoSinIVA = Integer.parseInt(cantidad);
                double montoIVA = montoSinIVA * IVA;
                lbResultado.setText("El IVA de $"+cantidad+" es: $"+convertirFormatoMoneda(montoIVA));   
            }catch(NumberFormatException e){
                 lbResultado.setText("El monto ingresado debe ser un valor numérico");
            }
        }else{
            lbResultado.setText("Debes ingresar un monto para calcular su IVA");
        }
    }
    
    private String convertirFormatoMoneda(double monto){
        DecimalFormat formatoMoneda = new DecimalFormat("#,###.00");
        return formatoMoneda.format(monto);
    }

    @FXML
    private void btnClicAgregarCarrera(ActionEvent event) {
        lbError.setText("");
        String nuevaCarrera = tfNuevaCarrera.getText().trim();
        if(!nuevaCarrera.isEmpty()){
            if(!verificarExistenciaCarrera(nuevaCarrera)){
                carrerasUV.add(nuevaCarrera);
                tfNuevaCarrera.setText("");
            }else{
                lbError.setText("No se puede agregar la carrera porque ya existe en la lista");
            }
        }else{
            lbError.setText("El campo carrera  no debe estar vacío");
        }
    }
    
    private void InicializarDatosCarreras(){
        carrerasUV = FXCollections.observableArrayList();
        carrerasUV.add("Ingenieria de software");
        carrerasUV.add("Danza contemporanea");
        carrerasUV.add("Arquitectura");
        carrerasUV.add("Medicina");
        cbCarreras.setItems(carrerasUV);
    }
    
    private boolean verificarExistenciaCarrera(String carrera){
        for(String carreraLista : carrerasUV){
            if(carrera.toLowerCase().equals(carreraLista.toLowerCase()))
                return true;
        }
        return false;
    }
    
    private void  implementarEventoRadioGroup(){
        tgColores.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(rbAzul.isSelected()){
                    paneFondo.setStyle("-fx-background-color: #0000FF");
                    lbColor.setText(rbAzul.getText());
                }
                if(rbVerde.isSelected()){
                    paneFondo.setStyle("-fx-background-color: #00FF00");
                    lbColor.setText(rbVerde.getText());
                }
                if(rbAmarillo.isSelected()){
                    paneFondo.setStyle("-fx-background-color: #E3F500");
                    lbColor.setText(rbAmarillo.getText());
                }
            }
        });
    }
    
    @FXML
    private void btnClicRestaurarFondo(ActionEvent event) {
        paneFondo.setStyle(null);
        lbColor.setText(null);
        try{
            tgColores.getSelectedToggle().setSelected(false);
        }catch(NullPointerException e){
            
        } 
    }

    @FXML
    private void btnClicVerOrden(ActionEvent event) {
        String ordenCreada = "";
        lbErrorIngrediente.setText(null);
        if(cbOtro.isSelected()){
            if(!tfOtroIngrediente.getText().trim().isEmpty()){
                String otroIngrediente = tfOtroIngrediente.getText().trim();
                ordenCreada += "\n - "+ otroIngrediente;
            }else{
                lbErrorIngrediente.setText("Ingrese un ingrediente");
            }
        }
        if(cbJamon.isSelected())
            ordenCreada += "\n - "+cbJamon.getText();
        if(cbPepperoni.isSelected())
            ordenCreada += "\n - "+cbPepperoni.getText();
        if(cbSalami.isSelected())
            ordenCreada += "\n - "+cbSalami.getText();
        if(cbPinia.isSelected())
            ordenCreada += "\n - "+cbPinia.getText();
        if(!ordenCreada.isEmpty() && lbErrorIngrediente.getText()==null)
            mostrarOrdenCreada(ordenCreada);   
    }
    
    private void mostrarOrdenCreada(String orden){
        Alert dialogoOrden = new Alert(Alert.AlertType.INFORMATION);
        dialogoOrden.setTitle("Orden seleccionada");
        dialogoOrden.setHeaderText(null);
        dialogoOrden.setContentText(orden);
        dialogoOrden.showAndWait();
    }
    
    private void configurarEventoCheckBoxOtro(){
        cbOtro.selectedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                if(newValue == true)
                    tfOtroIngrediente.setVisible(true);
                else
                    tfOtroIngrediente.setVisible(false);
            }
        });
    }

    @FXML
    private void btnClicSeleccionarFoto(ActionEvent event) {
        FileChooser dialogoSeleccionImg = new FileChooser();
        dialogoSeleccionImg.setTitle("Selecciona tu foto:");
        String etiquetaExtension = "Archivos PNG (*.png)";
        String extensionArchivo = "*.png";
        FileChooser.ExtensionFilter filtroSeleccion = new FileChooser.ExtensionFilter(etiquetaExtension, extensionArchivo);
        dialogoSeleccionImg.getExtensionFilters().add(filtroSeleccion);
        //Stage escenario = (Stage) ivFoto.getScene().getWindow(); //Sirve para seleccionar la stage de la scene 
        imagenSeleccionada = dialogoSeleccionImg.showOpenDialog(ivFoto.getScene().getWindow());
        if(imagenSeleccionada !=null){
            mostrarImagenPerfil(imagenSeleccionada);
        }
    }
    
    private void mostrarImagenPerfil(File imagen){
        try{
             BufferedImage bufferImg = ImageIO.read(imagen);
             Image imagenPreview = SwingFXUtils.toFXImage(bufferImg, null);
             ivFoto.setImage(imagenPreview);
        }catch(IOException e){
            System.out.println("Error al mostrar la imagen...");
        }
    }

    @FXML
    private void btnClicRegistrarAlumno(ActionEvent event) {
        HashMap<String,String> datos = new HashMap<>();
        if(!tfMatricula.getText().isEmpty()){
            datos.put("matricula", tfMatricula.getText());
        }
        if(!tfNombre.getText().isEmpty()){
            datos.put("nombre", tfNombre.getText());
        }
        if(!tfApellidoPaterno.getText().isEmpty()){
            datos.put("apellidoPaterno", tfApellidoPaterno.getText());
        }
        if(!tfApellidoMaterno.getText().isEmpty()){
            datos.put("apellidoMaterno", tfApellidoMaterno.getText());
        }
        if(imagenSeleccionada != null){
            datos.put("imagen", imagenSeleccionada.getPath());
        }
        if(datos.size() == 5){
             mostrarDialogo("Información de perfil", 
                     convierteInformacionHas(datos), 
                    AlertType.WARNING);
        }else{
            mostrarDialogo("Campos requeridos", "Debes ingresar toda la información "
                    + "de los campos y seleccionar una imagen para continuar", 
                    AlertType.INFORMATION);
        }
    }
    
    private String convierteInformacionHas(HashMap<String, String> datos){
        return String.format("Matricula: %s\nNombre: %s\nApellido paterno: %s"
                + "\nApellido materno: %s\nRuta foto perfil: %s", datos.get("matricula"), 
                datos.get("nombre"), datos.get("apellidoPaterno"), 
                datos.get("apellidoMaterno"),datos.get("imagen"));
    }
    
    private void mostrarDialogo(String titulo, String contenido, AlertType tipo){
        Alert dialogoMensaje = new Alert(tipo);
        dialogoMensaje.setTitle(titulo);
        dialogoMensaje.setHeaderText(null);
        dialogoMensaje.setContentText(contenido);
        dialogoMensaje.showAndWait();
    }

    @FXML
    private void menuClickAcercaDe(ActionEvent event) {
        mostrarDialogo("Acerca de", "Primer Poroyecto de JavaFX de la materia "
                + "de Principios de Construcción de Software periodo FEB-JUL 2024. \nCreado por Luis Angel", AlertType.INFORMATION);
    }

    @FXML
    private void menuClickSalir(ActionEvent event) {
        Stage escenarioActual = (Stage) tabPaneOpciones.getScene().getWindow();
        escenarioActual.close();
    }

    @FXML
    private void menuClickIrPrimerElemento(ActionEvent event) {
        tabPaneOpciones.getSelectionModel().selectFirst();
    }

    @FXML
    private void menuClickIrUltimoElemento(ActionEvent event) {
        tabPaneOpciones.getSelectionModel().selectLast();
    }
    
}
