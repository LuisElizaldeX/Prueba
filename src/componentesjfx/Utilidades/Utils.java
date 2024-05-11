package componentesjfx.Utilidades;

import javafx.scene.control.Alert;

/**
 *
 * @author king_
 */
public class Utils {
    public static void mostrarAlertaSimple(String titulo, String mensaje,
        Alert.AlertType tipo) {
        Alert alertaSimple = new Alert(tipo);
        alertaSimple.setTitle(titulo);
        alertaSimple.setContentText(mensaje);
        alertaSimple.setHeaderText(null);
        alertaSimple.showAndWait();
    }
}
