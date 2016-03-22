import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class MainServer {
	public static Igra[] igre = new Igra[5];
	public static LinkedList<Igrac> igraci=new LinkedList<Igrac>();
	public static final int BROJ_PORTA=9999;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			ServerSocket soketZaOsluskivanje = new ServerSocket(MainServer.BROJ_PORTA);
			while (true) {
				Socket soket = soketZaOsluskivanje.accept();
				igraci.add(new Igrac(soket));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void posaljiListuSlobodnihIgraca(){
		for (Igrac igrac : igraci) {
			igrac.izlazniTok.println(listaSlobodnihIgraca());
		}
		
	}
	private static String listaSlobodnihIgraca(){
		String lista="";
		for(Igrac igrac : igraci) {
			lista+=igrac+";";
		}
		return lista;
	}
	public static void posaljiPozivnicu(String pozivac, String protivnik){
		nadjiIgraca(protivnik).izlazniTok.println(pozivac);
	}
	
	public static void napraviIgru(String igrac1, String igrac2){
		Igrac igr1=nadjiIgraca(igrac1);
		Igrac igr2=nadjiIgraca(igrac2);
		for (int i = 0; i < igre.length; i++) {
			if(igre[i]==null){
				igre[i]=new Igra(igr1,igr2);
				uvediIgraceUIgru(igr1,igr2);
				posaljiListuSlobodnihIgraca();
				break;
			}
			if(i==igre.length-1){
				//obavesti ih da je pun server
			}
		}
	}
	private static void uvediIgraceUIgru(Igrac igr1,Igrac igr2){
		//igr2.izlazniTok.println(Igrac.PRIHVACENA_IGRA);
		igr1.igraj();
		igr2.igraj();
		igraci.remove(igr1);
		igraci.remove(igr2);
	}
	public static Igrac nadjiIgraca(String ime){
		for (Igrac igrac : igraci) {
			if(igrac.ime.equals(ime)){
				return igrac;
			}
		}
		return null;
	}
}
