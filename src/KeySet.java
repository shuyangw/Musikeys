import java.io.Serializable;

public class KeySet implements Serializable {
	private static final long serialVersionUID = -4976128714218674674L;
	private String name;
	private int key;
	private String sound;
	private boolean stop = false;
	
	public KeySet(String name, int key, String sound){
		this.name = name;
		this.key = key;
		
		if(sound==null || !sound.equals("")){this.sound = "";}
		else{this.sound = sound;}
		
	}
	
	public String getName(){return this.name;}
	public int getKey(){return this.key;}
	public String getSound(){return this.sound;}
	public void changeSound(String newSound){this.sound = newSound;}
	public boolean getStop(){return this.stop;}
	public void changeStop(boolean stop){this.stop = stop;}
}
