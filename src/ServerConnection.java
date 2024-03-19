import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnection implements ActionListener {
    private Logger logger = Logger.getLogger(String.valueOf(ServerConnection.class));
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
            logger.info("Error. Out Stream was not created.: " + e.getMessage());
        } catch (NullPointerException e) {
             logger.info("Socket was not created correctly. ");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            outData.writeUTF(user + ": " + tfMessage.getText());
            System.out.println("********************************+"+tfMessage.getText());
            tfMessage.setText("");
        } catch (IOException exp) {
            logger.info("Error sending message: " + exp.getMessage());
        }
    }
}
