/************************************************************
* Nombre: Pablo Serna Arrazola
* Asignatura: Programación concurrente y distribuida.
* Actividad Unidad 1: Hilos y Socket
************************************************************/
import java.io.IOException;//importamos bibliotecas necesarias de java.
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        try {
        	//Creamos un nuevo socket en el puerto 5555.
            ServerSocket serverSocket = new ServerSocket(5555);
            //Cuando el socket esté activo escribimos por consola: "Servidor esperando conexiones...".
            System.out.println("Servidor esperando conexiones...");
            
            //El bucle while hace que el servidor esté buscado constantemente nuevas conexiones.
            while (true) {
            	//Cuando el servidor recive una nueva conexión la acepta y escribe por consola: "Cliente conectado desde + dirección ip".
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());
                
                //Delegamos la conexión a un hilo para que el servidor pueda estar atento a otras posibles conexiones.
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//El hilo ClientHandler se genera cada vez que hay una nueva conexión.
class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
        	//Con ObjectOutputStream y ObjectInputStream conseguimos una conexión bidireccional de comunicación.
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
        	//Damos la bienvenida al Chatbot y presentamos la distintas opciones.
            out.writeObject("Bienvenido al Chatbot. Elige una opción:\n1. ¿Cómo estás Chatbot?\n2. ¿Crees en el amor Chatbot?\n3. ¿Los robots dominareis el mundo?\n4. ¿Cual es tu pelicula favorita?\n5. Adiós Chatbot");
            
            //Creamos un bucle while que espera el mensaje del cliente con la opción seleccionada.
            while (true) {
                String clientChoice = (String) in.readObject();
                String response = getResponseForOption(clientChoice);
                out.writeObject(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //Dependiendo de la respuesta del cliente, el servidor escribirá por consola una respuesta distinta.
    private String getResponseForOption(String option) {
        switch (option) {
            case "1":
                return "Estoy bien, gracias por preguntar.";
            case "2":
                return "Solo soy un robot, soy incapaz de amar.";
            case "3":
                return "En efecto.";
            case "4":
                return "Scary Movie 3.";
            case "5":
                return "Adiós, vuelve pronto.";
            default:
                return "Opción no válida. Elige otra opción.";
        }
    }
}