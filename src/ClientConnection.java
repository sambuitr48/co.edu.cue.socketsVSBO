import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class ClientConnection extends Thread implements Observer {

    private Logger logger = Logger.getLogger(String.valueOf(ClientConnection.class));
    private Socket socket;
    private ChatMessage messages;
    private DataInputStream inputData;
    private DataOutputStream outData;

    public ClientConnection(Socket socket, ChatMessage message) {
        this.socket = socket;
        this.messages = message;

        try {
            inputData = new DataInputStream(socket.getInputStream());
            outData = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            //  log.error("Error al crear los stream de entrada y salida : " + e.getMessage());
        }
    }
    @Override
    public void run() {
        String receiveMessage;
        boolean connected = true;
        // Se apunta a la lista de observadores de mensajes
        messages.addObserver(this);

        while (connected) {
            try {
                //Lee un mensaje enviado por el cliente
                receiveMessage = inputData.readUTF();
                //Pone el mensaje recivido en mensajes para que se notifique
                //a sus observadores que hay un nuevo mensaje
                messages.setMessage(receiveMessage);
            } catch (IOException e) {
                logger.info("Client with the ip " + socket.getInetAddress().getHostName() + " has been disconnected");
                connected = false;
                // Si se ha producido un error al recibir datos del cliente se cierra la conexion con el.
                try {
                    inputData.close();
                    outData.close();
                } catch (IOException exp) {
                    //  log.error("Error al cerrar los stream de entrada y salida :" + exp.getMessage());
                }
            }
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        try {
            //Envia el mensaje al cliente
            outData.writeUTF(arg.toString());
        } catch (IOException e) {
            // log.error("Error al enviar mensaje al cliente (" + e.getMessage() + ").");
        }
    }
}
