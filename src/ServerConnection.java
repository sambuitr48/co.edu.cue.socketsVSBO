import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnection implements ActionListener {
    private Logger log = Logger.getLogger(String.valueOf(ServerConnection.class));
    private Socket socket;
    private JTextField tfMessage;
    private String user;
    private DataOutputStream outData;

    public ServerConnection(Socket socket, JTextField tfMessage, String user) {
        this.socket = socket;
        this.tfMessage = tfMessage;
        this.user = user;

        try {
            this.outData = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            // log.error("Error al crear el stream de salida : " + e.getMessage());
        } catch (NullPointerException e) {
            // log.error("El socket no se creo correctamente. ");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            outData.writeUTF(user + ": " + tfMessage.getText());
            tfMessage.setText("");
        } catch (IOException exp) {
            //log.error("Error al intentar enviar un mensaje: " + exp.getMessage());
        }
    }
}