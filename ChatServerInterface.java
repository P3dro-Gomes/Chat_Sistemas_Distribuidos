package Chat_Sistemas_Distribuidos;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServerInterface extends Remote {
    boolean registerUser(String username, ChatClientInterface client) throws RemoteException;
    void broadcastMessage(String sender, String message) throws RemoteException;
    void sendPrivateMessage(String sender, String receiver, String message) throws RemoteException;
    List<String> getOnlineUsers() throws RemoteException;
    void disconnectUser(String username) throws RemoteException;
}
