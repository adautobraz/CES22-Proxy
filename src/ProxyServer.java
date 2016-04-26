import java.io.*;
import java.net.*;

public class ProxyServer {
	public static void main(String[] args) throws IOException {
	    try {
	      String host = "seu Servidor Proxy ";
	      int remoteport = 100;
	      int localport = 111;
	      // Imprimir mensagem inicial
	      System.out.println("Iniciando proxy para " + host + ":" + remoteport
	          + " on port " + localport);
	      // Rodar o servidor
	      runServer(host, remoteport, localport); 
	    } catch (Exception e) {
	      System.err.println(e);
	    }
	  }

	
}
