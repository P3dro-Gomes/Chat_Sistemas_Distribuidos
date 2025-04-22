package Chat_Sistemas_Distribuidos;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    private Map<String, ChatClientInterface> users = new HashMap<>(); // username -> client



    protected ChatServer() throws RemoteException {
        super();
    }
    // restante do código

    public boolean registerUser(String username, ChatClientInterface client)throws RemoteException {
        if(users.containsKey(username)){
            return false;
        }
        this.users.put(username, client);
        broadcastMessage("", username+" entrou na sala");
        return true;
    }


    public void broadcastMessage(String sender, String message) throws RemoteException{
        for (Map.Entry<String, ChatClientInterface> entry : users.entrySet()) {
            String username = entry.getKey();
            ChatClientInterface client = entry.getValue();


            // {
            //     "batata":10, entry 
            //     "feijao":10,
            //     "picles":10,
            //     "cebola":10
            // }


            if (!username.equals(sender)) {
                client.receiveMessage(sender, message);
            }
        }
    }

    public void sendPrivateMessage(String sender, String receiver, String message)throws RemoteException{
        ChatClientInterface client = users.get(receiver); 
        client.receiveMessage(sender, "MENSAGEM PRIVADA ->"+message);
    }

    public List<String> getOnlineUsers(){
        List<String> a = new ArrayList<String>();
        for(Map.Entry<String, ChatClientInterface> objeto: users.entrySet()){
        a.add(objeto.getKey());
    }
        return a;
    }


    public void disconnectUser(String username)throws RemoteException{
        users.remove(username);
        broadcastMessage("", username+" entrou na sala");
    }
        
    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("ChatServer", server);
            System.out.println("Servidor de chat RMI pronto...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
