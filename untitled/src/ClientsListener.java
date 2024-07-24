import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientsListener implements Runnable
{
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private TTTFrame frame = null;

    public ClientsListener(ObjectInputStream is, ObjectOutputStream os, TTTFrame frame)
    {
        this.is = is;
        this.os = os;
        this.frame = frame;
    }

    @Override
    public void run() {
        try
        {
            while(true)
            {
                CommandFromServer cfs = (CommandFromServer) is.readObject();
                if(cfs.getCommand() == CommandFromServer.X_TURN)
                {
                    frame.setTurn('X');
                }
                else if(cfs.getCommand() == CommandFromServer.O_TURN)
                {
                    frame.setTurn('O');
                }
                else if(cfs.getCommand() == cfs.MOVE)
                {
                    String data = cfs.getData();
                    int c = data.charAt(0) - '0';
                    int r = data.charAt(1) - '0';
                    frame.getGameData().getGrid()[r][c] = data.charAt(2);
                    frame.repaint();
                    System.out.println("got a move");
                }
                else if(cfs.getCommand() == CommandFromServer.TIE)
                {
                    frame.setText("Tie game (R to reset)");
                }
                else if(cfs.getCommand() == CommandFromServer.X_WINS)
                {
                    frame.setText("X wins! (R to reset)");
                }
                else if(cfs.getCommand() == CommandFromServer.O_WINS)
                {
                    frame.setText("O wins! (R to reset)");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
