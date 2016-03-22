import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Igrac implements Runnable {
	Socket soket;
	BufferedReader ulazniTok;
	PrintStream izlazniTok;
	String ime;
	Thread t;
	boolean zauzet;
	public final static String POZIVAC="1";
	public final static String PRIHVACENA_IGRA="D";
	
	
	
	public Igrac(Socket soket) {

		try {
			this.soket = soket;
			ulazniTok = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			izlazniTok = new PrintStream(soket.getOutputStream());
			ime = ulazniTok.readLine();
			MainServer.posaljiListuSlobodnihIgraca();
			zauzet=false;
			t = new Thread(this);
			t.setDaemon(true);
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	// ostalo je par nezgoodnih situacija
	public void run() {
		try {
			while (true) {
				String[] podaci = ulazniTok.readLine().split(";");
				if(zauzet){
					if(hoceDaPovucePoziv(podaci)){
						zauzet=false;
					}
					continue;
				}
				if(hoceDaSeDiskonektuje(podaci)){
					MainServer.igraci.remove(this);
					break;
				}
				String protivnik = podaci[0];
				String tipIgraca=podaci[1];
				String prihvacenaIgra=podaci[2];
				if (tipIgraca.equals(Igrac.POZIVAC)) {
					MainServer.posaljiPozivnicu(ime, protivnik);
					zauzet=true;
				} else {
					if (prihvacenaIgra.equals(Igrac.PRIHVACENA_IGRA)) {
						MainServer.napraviIgru(ime, protivnik);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return ime;
	}
	void zavrsiIgru(){
		zauzet=false;
	}
	
	void igraj() {
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	boolean hoceDaSeDiskonektuje(String[] niz){
		return niz.length==1;
	}
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Igrac))return false;
		Igrac i=(Igrac) o;
		return ime.equals(i.ime);
	}
	boolean hoceDaPovucePoziv(String[] niz){
		return (niz.length==1);
	}
	
}
