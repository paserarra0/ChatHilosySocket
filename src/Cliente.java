import java.io.IOException;//Importamos las bibliotecas de java necesarias
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try {
        	//Abrimos una conexión con el servidor. Para ello introdución la dirección ip y el puerto
            Socket socket = new Socket("localhost", 5551);
            System.out.println("Conectado al servidor.");

            try (
            	//Abrimos un ObjectStream de lectura y escritura para permitir la comunicación
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Scanner scanner = new Scanner(System.in)
            ) {
                while (true) {
                	//Escribimos por consola los mensajes del servidor
                    String serverMessage = (String) in.readObject();
                    System.out.println("Servidor: " + serverMessage);

                    System.out.print("Selecciona una opción (1-5): ");
                    String clientChoice = scanner.nextLine();//Cogemos la respuesta del cliente y la guardamos en clientChoice
                    out.writeObject(clientChoice);

                    //Dependiendo de la respuesta seleccionada se escribe una respuesta distinta.
                    String response = (String) in.readObject();
                    System.out.println("Respuesta: " + response);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}