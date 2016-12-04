import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Keyboard {
	private static final long serialVersionUID = 6929414347192639739L;
	public HashMap<String,KeySet> keyhash = new HashMap<String,KeySet>();
	
	public Keyboard() {
		keyhash.put("1", new KeySet("1", 1, ""));
		keyhash.put("2", new KeySet("2", 2, ""));
		keyhash.put("3", new KeySet("3", 3, ""));
		keyhash.put("4", new KeySet("4", 4, ""));
		keyhash.put("5", new KeySet("5", 5, ""));
		keyhash.put("6", new KeySet("6", 6, ""));
		keyhash.put("7", new KeySet("7", 7, ""));
		keyhash.put("8", new KeySet("8", 8, ""));
		keyhash.put("9", new KeySet("9", 9, ""));
		keyhash.put("0", new KeySet("0", 0, ""));
		
		keyhash.put("Q", new KeySet("Q", 0, ""));
		keyhash.put("W", new KeySet("W", 1, ""));
		keyhash.put("E", new KeySet("E", 2, ""));
		keyhash.put("R", new KeySet("R", 3, ""));
		keyhash.put("T", new KeySet("T", 4, ""));
		keyhash.put("Y", new KeySet("Y", 5, ""));
		keyhash.put("U", new KeySet("U", 6, ""));
		keyhash.put("I", new KeySet("I", 7, ""));
		keyhash.put("O", new KeySet("O", 8, ""));
		keyhash.put("P", new KeySet("P", 9, ""));
		keyhash.put("A", new KeySet("A", 10, ""));
		keyhash.put("S", new KeySet("S", 11, ""));
		keyhash.put("D", new KeySet("D", 12, ""));
		keyhash.put("F", new KeySet("F", 13, ""));
		keyhash.put("G", new KeySet("G", 14, ""));
		keyhash.put("H", new KeySet("H", 15, ""));
		keyhash.put("J", new KeySet("J", 16, ""));
		keyhash.put("K", new KeySet("K", 17, ""));
		keyhash.put("L", new KeySet("L", 18, ""));
		keyhash.put("Z", new KeySet("Z", 19, ""));
		keyhash.put("X", new KeySet("X", 20, ""));
		keyhash.put("C", new KeySet("C", 21, ""));
		keyhash.put("V", new KeySet("V", 22, ""));
		keyhash.put("B", new KeySet("B", 23, ""));
		keyhash.put("N", new KeySet("N", 24, ""));
		keyhash.put("M", new KeySet("M", 25, ""));
		
		keyhash.put("[", new KeySet("[", 25, ""));
		keyhash.put("]", new KeySet("]", 25, ""));
		keyhash.put("\\", new KeySet("\\", 25, ""));
		keyhash.put(";", new KeySet(";", 25, ""));
		keyhash.put("'", new KeySet("'", 25, ""));
		keyhash.put(",", new KeySet(",", 25, ""));
		keyhash.put(".", new KeySet(".", 25, ""));
		keyhash.put("/", new KeySet("/", 25, ""));
	}

	public String GetSound(String name){
		if(name==null)return "";
		KeySet k = keyhash.get(name);
		if(k==null)return "";
		return k.getSound();}
	public boolean GetStop(String name){
		if(name==null)return false;
		KeySet k = keyhash.get(name);
		if(k==null)return false;
		return k.getStop();}
	public void ApplySound(String name, String sound){
		keyhash.get(name).changeSound(sound);} 
	public void ApplyStop(String name, boolean stop){
		keyhash.get(name).changeStop(stop);}
	public void ClearSound(String name){
		keyhash.get(name).changeSound("");}
	
	
	
	public void saveStatus(String filename){
		   try {
			  File file = new File(filename);
			  if(!file.exists()){
				  file.createNewFile();
			  }
		      FileOutputStream saveFile = new FileOutputStream(file);
		      ObjectOutputStream out = new ObjectOutputStream(saveFile);
		      out.writeObject(keyhash);
		      out.close();
		      saveFile.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		}
	public void loadStatus(String filename){
		   try {
		      FileInputStream saveFile = new FileInputStream(filename);
		      ObjectInputStream in = new ObjectInputStream(saveFile);
		      keyhash = (HashMap<String,KeySet>)in.readObject();
		      in.close();
		      saveFile.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		}
}
