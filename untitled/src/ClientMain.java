import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientMain
{
    public static void main (String [] args)
    {
        try
        {
            GameData gameData = new GameData();
            Socket socket = new Socket("127.0.0.1", 8001);
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());

            CommandFromServer cfs = (CommandFromServer) is.readObject();
            TTTFrame frame;

            if(cfs.getCommand() == CommandFromServer.CONNECTED_AS_X)
            {
                frame = new TTTFrame(gameData, os, 'X');

            }
            else
            {
                frame = new TTTFrame(gameData, os, 'O');
            }

            ClientsListener cl = new ClientsListener(is, os, frame);
            Thread t = new Thread(cl);
            t.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
