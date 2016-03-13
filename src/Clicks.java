
import org.graphstream.ui.view.ViewerListener;


public class Clicks implements ViewerListener {
	MyAffichage f;
	public Clicks(MyAffichage f) {
		this.f=f;
    }
 
    public void viewClosed(String id) {
     //   loop = false;
    }
 
    public void buttonPushed(String id) {
        System.out.println("Button pushed on node "+id);
        f.receivedListener(id);
     }
 
	public void buttonReleased(String id) {
    }
}