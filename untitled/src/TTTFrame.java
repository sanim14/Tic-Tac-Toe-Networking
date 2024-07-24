import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;

public class TTTFrame extends JFrame implements KeyListener {
    // Display message
    private String text = "";
    // the letter you are playing as
    private char player;
    // store the letter of the active player
    private char turn = 'X';
    // stores all the game data
    private GameData gameData = null;
    // output stream to the server
    ObjectOutputStream os;

    public TTTFrame(GameData gameData, ObjectOutputStream os, char player)
    {
        super("TTT Game");
        // sets the attributes
        this.gameData = gameData;
        this.os = os;
        this.player = player;

        // adds a KeyListener to the Frame
        addKeyListener(this);

        // makes closing the frame close the program
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (player == 'X')
        {
            text = "Waiting for O to Connect";
        }
        setSize(400,460);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    public void paint(Graphics g)
    {
        // draws the background
        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());

        // draws the display text to the screen
        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman",Font.BOLD,30));
        g.drawString(text,20,55);

        // draws the tic-tac-toe grid lines to the screen
        g.setColor(Color.RED);
        for(int y =0;y<=1; y++)
            g.drawLine(0,(y+1)*133+60,getWidth(),(y+1)*133+60);
        for(int x =0;x<=1; x++)
            g.drawLine((x+1)*133,60,(x+1)*133,getHeight());

        g.setFont(new Font("Times New Roman",Font.BOLD,70));
        for(int r=0; r<gameData.getGrid().length; r++)
            for(int c=0; c<gameData.getGrid().length; c++)
                g.drawString(""+gameData.getGrid()[r][c],c*133+42,r*133+150);
    }


    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public char getPlayer() {
        return player;
    }

    public void setPlayer(char player) {
        this.player = player;
    }

    public char getTurn() {
        return turn;
    }

    public void setTurn(char turn) {
        this.turn = turn;
        if (turn == player)
        {
            text = "Your turn";
        }
        else
        {
            text = turn + " 's turn.";
        }
        repaint();
    }

    public void makeMove(int c, int r, char letter)
    {
        gameData.getGrid()[r][c] = letter;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent event) {
        char key = event.getKeyChar();
        int r;
        int c;
        switch(key)
        {
            case '1':
                r = 0;
                c = 0;
                break;
            case '2':
                r = 0;
                c = 1;
                break;
            case '3':
                r = 0;
                c = 2;
                break;
            case '4':
                r = 1;
                c = 0;
                break;
            case '5':
                r = 1;
                c = 1;
                break;
            case '6':
                r = 1;
                c = 2;
                break;
            case '7':
                r = 2;
                c = 0;
                break;
            case '8':
                r = 2;
                c = 1;
                break;
            case '9':
                r = 2;
                c = 2;
                break;
            default:
                c = r = 1;
        }
        if (c != -1)
        {
            try {
                System.out.println("writing to server " + "" + c + r + player);
                os.writeObject(new CommandFromClient(CommandFromClient.MOVE, "" + c + r + player));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void reset()
    {
        gameData.reset();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public char other()
    {
        if(player=='x')
            return 'o';
        else
            return 'x';
    }

    public GameData getGameData()
    {
        return gameData;
    }
}
