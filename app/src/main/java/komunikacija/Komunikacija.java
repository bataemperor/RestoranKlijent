package komunikacija;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;


public class Komunikacija {
	private Socket socket;
//	private static Komunikacija instance;
	private ObjectOutputStream outSocket;

	public Komunikacija() throws IOException {
		socket = new Socket("192.168.0.12", 9998);
//		socket = new Socket("192.168.32.79", 9998);
		System.out.println("Uspesno povezivanje sa serverom.");
	}

//	public static Komunikacija vratiObjekat() throws IOException {
//		if (instance == null) {
//			instance = new Komunikacija();
//		}
//		return instance;
//	}

	public void posaljiZahtev(TransferObjekatZahtev toZahtev)
			throws IOException {
		outSocket = new ObjectOutputStream(socket.getOutputStream());
		outSocket.writeObject(toZahtev);
		outSocket.flush();

	}

	public TransferObjekatOdgovor procitajOdgovor() throws IOException,
			ClassNotFoundException {
		ObjectInputStream inSocket = new ObjectInputStream(
				socket.getInputStream());
		TransferObjekatOdgovor too = (TransferObjekatOdgovor) inSocket
				.readObject();

		return too;
	}

}