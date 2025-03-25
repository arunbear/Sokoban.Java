import javax.swing.SwingUtilities;
import ihm.HomeWindow;


public class Main implements Runnable  {
	
	    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Main());

    }



	@Override
	public void run() {
        new HomeWindow();

    }

}
