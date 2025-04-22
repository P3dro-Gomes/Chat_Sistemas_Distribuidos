package Chat_Sistemas_Distribuidos;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientInterface extends Remote {
    void receiveMessage(String sender, String message) throws RemoteException;
}
