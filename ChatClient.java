package Chat_Sistemas_Distribuidos;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class ChatClient extends UnicastRemoteObject implements ChatClientInterface {
    private String username;
    private ChatServerInterface server;

    protected ChatClient(String username, ChatServerInterface server) throws RemoteException {
        this.username = username;
        this.server = server;
    }
    
    public void receiveMessage(String sender, String message){
        System.out.println("\n"+sender + ": " + message);
        System.out.print(">>> ");
    }
    
    public void start(Scanner scanner) throws RemoteException{
        while (true) {
            System.out.print(">>> "); 
            String message = scanner.nextLine(); 
            if(message.toUpperCase().equals("QUIT")){
                this.server.disconnectUser(this.username); 
                break; 
            } 
            if(message.toUpperCase().equals("PRIVATE")){
                System.out.print("to: "); 
                String username_receiver = scanner.nextLine(); 
                System.out.print(">>> "); 
                message = scanner.nextLine(); 
                this.server.sendPrivateMessage(this.username, username_receiver, message);
            }
            if(message.toUpperCase().equals("LIST")){
                List<String> lista = this.server.getOnlineUsers();
                System.out.println("<<<<<<<<<<<<<<<<<<<< ONLINE USERS >>>>>>>>>>>>>>>>>>>>");
                for(String user: lista){
                    System.out.println(user);
                }
                System.out.println(">>> ");
            }else{
                this.server.broadcastMessage(this.username, message);
            }
        }
    }
    
    
    // restante do código
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ChatServerInterface server = (ChatServerInterface) registry.lookup("ChatServer");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite seu nome de usuário: ");
            String username = scanner.nextLine();

            ChatClient client = new ChatClient(username, server);
            if (server.registerUser(username, client)) {
                System.out.println("Cliente de chat iniciado...");
                System.out.println("comandos: \n'QUIT': sair\n'LIST': listar usuarios online\n'PRIVATE': enviar mensagem privada para algum usuario online");
                System.out.println("Bem-vindo ao chat, " + username + "!");
                client.start(scanner);
                scanner.close();
                System.out.println("Conexão fechada!");
            } else {
                System.out.println("Nome de usuário já em uso.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}