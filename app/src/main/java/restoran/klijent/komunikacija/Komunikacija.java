package restoran.klijent.komunikacija;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;


public class Komunikacija {
	private Socket socket;
	private ObjectOutputStream outSocket;
	public static String ipAddress = "192.168.0.12";
	public Komunikacija() throws IOException {
		socket = new Socket();
		socket.connect(new InetSocketAddress(ipAddress, 9998), 5000);
		System.out.println("Uspesno povezivanje sa serverom.");
	}

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