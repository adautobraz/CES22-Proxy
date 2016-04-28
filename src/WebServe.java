import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class WebServe implements Runnable {
	Socket soc;
	OutputStream os;
	DataInputStream is;
	String resource, request;
	
	public static void main (String args[]){
		try{
			ServerSocket s = new ServerSocket(8080);
			for(;;){
				WebServe w = new WebServe(s.accept());
				Thread t = new Thread(w);
				t.start();
			}
		
		}catch (IOException i){
			System.err.println("IOException in Server");
		}
	}
	
	WebServe(Socket s) throws IOException{
		soc = s;
		os = soc.getOutputStream();
		is = new DataInputStream(soc.getInputStream());
	}
	
	void getRequest(){
		try{
			request="";
			boolean header = true;
			String message;
			while((message = is.readLine()) != null){
				if (message.equals(""))
					break;
				System.err.println(message);
				StringTokenizer t = new StringTokenizer (message);
				String token = t.nextToken();
				if(token.equals("GET")){
					resource = t.nextToken();
					header = false;
				}
				if(header==false)
					request += message + "\n";
			}
		} catch (IOException e){
				System.err.println("Error receiving Web request");
		}
	}
	
	public void run(){
		getRequest();
		returnResponse();
		close();
	}
	
	void returnResponse(){
		String endereco="", requisicao="/";
		boolean emptyRequest = true;
		int c;
		try{
			 char[] res = resource.toCharArray();
			 for(int i=1;i<resource.length();i++){
				 if(emptyRequest){
					 if(res[i]=='/'){
						 emptyRequest=false;
					 }
					 else
						 endereco += res[i];
				 }
				 else{
					 requisicao += res[i];
				 }
			 }
			 System.out.println("Address: " + endereco);
			 System.out.println("Request: " + requisicao);
			 WebRetriever w = new WebRetriever(endereco, 80);
			 w.request(requisicao,endereco);
			 byte[] write_b = w.getResponse().getBytes();
			 System.out.print(w.getResponse());
			 os.write(write_b);
			 w.close();
		}catch (IOException e){
			System.err.println("IOException in reading in Web server");
		}
	}
	
	public void close(){
		try{
			is.close();
			os.close();
			soc.close();
		}catch(IOException e){
			System.err.println("IOException in closing connection");
		}
	}
	
	

}
