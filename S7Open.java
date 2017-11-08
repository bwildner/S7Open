import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class S7Open {

	public static void main(String[] args) throws IOException, AWTException{
	//String pfadmnumbers ="\\\\n0204\\dateien"; //Hier liegen die MNumber Verzeichnisse
	String pfadmnumbers ="\\\\Heller.biz\\hnt\\Steuerungstechnik\\Projects-Machines\\M-Numbers"; //Hier liegen die MNumber Verzeichnisse
	//Eingabe
	Eingabe eingabe = new Eingabe();
	String nummer = eingabe.eingabe();

	//Verarbeitung
	String teilnummer = nummer.substring(0, 2); //nur die vorderen 2 Chars für den Zwischenpfad nehmen
	String pfad=pfadmnumbers+"\\M"+teilnummer+"xxx"; // ZwischenPfad zusammenbauen (M52xxx)
	System.out.println(pfad);

	//Suchen nach dem Endpfad
	Suchen suchen = new Suchen();
	String endpfad = suchen.verz(pfad, nummer);
	System.out.println("Pfad für Ausgabe: "+endpfad);

	File Datei = new File(endpfad); // Pfad für Suche zusammenbauen
	ArrayList<File> treffer = new ArrayList<File> ();
	treffer = suchen.searchFile(Datei, ".s7p");

	for (File Datei2 : treffer){
		 System.out.println("Name der S7p Datei: "+ Datei2.getAbsolutePath()); // Name der Datei ausgeben
	}


	//Ausgabe
	Ausgabe aus = new Ausgabe();
	aus.ausgabe(treffer);
	}
}



class Suchen{

	String fullpath = null;
	String rueckgabe = null;
	String verz(String pfad, String MNummer){
		File temp= new File(pfad); // Pfad für Suche zusammenbauen

		File[] tempInhalt = temp.listFiles(); //Files im Pfad auslesen
//		System.out.println("Inhalt: "+ temp.getAbsolutePath());
		if (tempInhalt==null)
			System.exit(0);



		for(int i=0;i<tempInhalt.length;i++){
			//System.out.println( tempInhalt[i].toString());
			if (tempInhalt[i].isDirectory()){
				//System.out.println("ist Verzeichniss");
				String verzname=tempInhalt[i].toString();
				if (verzname.contains(MNummer)){
					System.out.print("hier ist das gewünschte Vezeichniss: ");
					String fullpath = tempInhalt[i].getPath();
					System.out.println(fullpath);
					rueckgabe = fullpath;

				}
			}
		}

		return rueckgabe;
		}


	public ArrayList<File> searchFile(File dir, String find) {

		File[] files = dir.listFiles();
		ArrayList<File> matches = new ArrayList<File> ();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(find)) { // überprüft ob der Dateiname mit dem Suchstring
										 // übereinstimmt. Groß-/Kleinschreibung wird
										 // ignoriert.
					matches.add(files[i]);
					//System.out.println("Treffer: " + files[i].getName());
				}
				if (files[i].isDirectory()) {
				//	System.out.println("Verzeichnis: " + files[i].getName());

					matches.addAll(searchFile(files[i], find)); // fügt der ArrayList die ArrayList mit den
										    // Treffern aus dem Unterordner hinzu
				}
			}
		}
		return matches;
}

}


class Ausgabe{
	void ausgabe(ArrayList<File> liste) throws IOException, AWTException{

		for (File Datei3: liste){
			Runtime.getRuntime().exec("S7tgtopx.exe  /keep  /e "+ Datei3.getAbsolutePath());

		}
	//	String pfad2= 	"explorer.exe \"" + pfad+"\"";
		//Runtime.getRuntime().exec(pfad2);

	// Ab hier werden die Fenster im Step7 angeordnet

		Robot robot = new Robot();
		robot.delay(1000);                  //warte 1000 ms
		robot.keyPress(KeyEvent.VK_ALT);//drücke alt (hält gedrückt)
		robot.keyPress(KeyEvent.VK_A);  //drücke a (mit Shift a)
		robot.keyRelease(KeyEvent.VK_ALT);//lasse Alt los
        robot.keyPress(KeyEvent.VK_E);    //drücke e
        robot.keyPress(KeyEvent.VK_B);    //drücke b
        robot.keyPress(KeyEvent.VK_ALT);//drücke alt (hält gedrückt)
		robot.keyPress(KeyEvent.VK_A);  //drücke a (mit Shift a)
		robot.keyRelease(KeyEvent.VK_ALT);//lasse Alt los
        robot.keyPress(KeyEvent.VK_D);    //drücke d




	}
}

class Eingabe {
	//Hier nur Eingabe , Rückgabewert ist ein String "45123"
	String eingabe(){
		String eingabe1 = null;
		int fehler;
		do{
			try{
		//	  addWindowListener(new WindowClosingAdapter());
			  eingabe1 = JOptionPane.showInputDialog("Bitte MNummer ohne M eingeben: ");
			  if (eingabe1 == null){
				  System.exit(0);
			  }

//			  int eing1 = new Integer(eingabe1).intValue();
			  fehler=0;
		  }
		  catch(NumberFormatException e1) {
			  String ausgabe = "Nur Zahlen bitte!!";
			  JOptionPane.showMessageDialog(null, ausgabe , "Antwort", JOptionPane.INFORMATION_MESSAGE);
			  fehler=1;
		  }

	  }		  while (fehler==1);


	return eingabe1;
	}
}


