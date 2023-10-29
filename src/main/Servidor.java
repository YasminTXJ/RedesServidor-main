package main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Servidor {
    public static void main(String[] args) throws IOException{
        new Servidor(12345).executa();
    }

    private int porta;
    private List<Socket> clientes;

    public Servidor(int porta){
        this.porta = porta;
        this.setClientes(new ArrayList<Socket>());
    }

    public void executa () throws IOException{
        try (ServerSocket servidor = new ServerSocket(this.porta)) {
			System.out.println("Porta 12345 aberta!");

			while(true){
			    Socket cliente = servidor.accept();
			    System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());
			    clientes.add(cliente);
			    OutputStream outputStream = cliente.getOutputStream();
	            PrintWriter out = new PrintWriter(outputStream, true);
	            out.println("%esperandoJogador%");
			    
			    if(clientes.size() > 1) {
			    	JogoCaraCara jogoCaraCara = new JogoCaraCara( this,clientes.get(0), clientes.get(1));
			    	for(Socket c: clientes) {
					    OutputStream output= c.getOutputStream();
			            PrintWriter o = new PrintWriter(output, true);
			    		o.println("%esperandoPronto%");
			    	}
			    	Thread jogoThread = new Thread(jogoCaraCara);
	                jogoThread.start();
			    	clientes.clear();
			    }
			    
			}
		}
    }

    private Socket clientes(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Socket> getClientes() {
		return clientes;
	}

	public void setClientes(List<Socket> clientes) {
		this.clientes = clientes;
	}
}