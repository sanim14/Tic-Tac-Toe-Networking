import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ServersListener implements Runnable
{
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private char player;
    private static char turn = 'X';
    private static GameData gameData = new GameData();
    private static ArrayList <ObjectOutputStream> outs = new ArrayList<>();

    public ServersListener(ObjectInputStream is, ObjectOutputStream os, char player) {
        this.is = is;
        this.os = os;
        this.player = player;
        outs.add(os);
    }

    @Override
    public void run() {
        try
        {
            while(true)
            {
                CommandFromClient cfc = (CommandFromClient) is.readObject();
                if (cfc.getCommand() == CommandFromClient.MOVE && turn == player && !gameData.isWinner('X')
                        && !gameData.isWinner('O') && !gameData.isCat())
                {
                    String data = cfc.getData();
                    int c = data.charAt(0) - '0';
                    int r = data.charAt(1) - '0';

                    if (gameData.getGrid()[r][c] != ' ')
                        continue;

                    gameData.getGrid()[r][c] = player;
                    for (ObjectOutputStream o: outs)
                    {
                        o.writeObject(new CommandFromServer(CommandFromServer.MOVE, data));
                    }
                    changeTurn();
                    checkGameOver();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeTurn()
    {
        if(turn=='X')
            turn =  'O';
        else
            turn = 'X';

        for (ObjectOutputStream o: outs)
        {
            try
            {
                if (turn == 'X')
                    o.writeObject(new CommandFromServer(CommandFromServer.X_TURN, null));
                else
                    o.writeObject(new CommandFromServer(CommandFromServer.O_TURN, null));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void checkGameOver()
    {
        int command = -1;
        if(gameData.isCat())
            command = CommandFromServer.TIE;
        else if(gameData.isWinner('X'))
            command = CommandFromServer.X_WINS;
        else if(gameData.isWinner('O'))
            command = CommandFromServer.O_WINS;


        if (command != -1) {
            for (ObjectOutputStream o : outs) {
                try {
                    o.writeObject(new CommandFromServer(command, null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
