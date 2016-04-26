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

	 public static void runServer(String host, int remoteport, int localport)
		      throws IOException {
		    
		 // Criar um ServerSocket para procurar conexões
		    ServerSocket ss = new ServerSocket(localport);

		    final byte[] request = new byte[1024];
		    byte[] reply = new byte[4096];
		    
		    while (true) {
		        Socket client = null, server = null;
		        try {
		          // Esperar por conexão na porta local
		          client = ss.accept();

		          final InputStream streamFromClient = client.getInputStream();
		          final OutputStream streamToClient = client.getOutputStream();

		          // Fazer uma conexão com o servidor real
		          // Se não conseguirmos conectar com o servidor, enviar mensagem de erro
		          // para o cliente, desconectar e continuar esperando por conexões
		          try {
		            server = new Socket(host, remoteport);
		          } catch (IOException e) {
		            PrintWriter out = new PrintWriter(streamToClient);
		            out.print("Proxy server cannot connect to " + host + ":"
		                + remoteport + ":\n" + e + "\n");
		            out.flush();
		            client.close();
		            continue;
		          }
		          
		          // Get server streams
		          final InputStream streamFromServer = server.getInputStream();
		          final OutputStream streamToServer = server.getOutputStream();

		          // Uma thread para ler a requisição do cliente e passá-las para o servidor
		          Thread t = new Thread(){
		        	  public void run() {
		                  int bytesRead;
		                  try {
		                    while ((bytesRead = streamFromClient.read(request)) != -1) {
		                      streamToServer.write(request, 0, bytesRead);
		                      streamToServer.flush();
		                    }
		                  } catch (IOException e) {
		                  }

		                  // O cliente fechou sua conexão conosco,vamos fechar nossa conexão com o servidor
 		                  try {
		                    streamToServer.close();
		                  } catch (IOException e) {
		                  }
		                }
		              };

		              // Rodar a thread de requisição cliente-servidor
		              t.start();
		        	  
		              //Ler a resposta do servidor e passá-la de volta para o cliente
		              int bytesRead;
		              try {
		                while ((bytesRead = streamFromServer.read(reply)) != -1) {
		                  streamToClient.write(reply, 0, bytesRead);
		                  streamToClient.flush();
		                }
		              } catch (IOException e) {
		              }
		        	  
		        	  
		        	  
		        	  
		        	  
		        	  
		        	  
		        	  
		        	  
		          }

}
