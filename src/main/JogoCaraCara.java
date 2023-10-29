package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Personagem;
import model.factory.FactoryPersonagem;

public class JogoCaraCara extends Thread{
    private Socket cliente1;
    private Socket cliente2;
    
    private Boolean clientePronto1;
    private Boolean clientePronto2;
    
    private Boolean clienteRecebeuPersonagens1;
    private Boolean clienteRecebeuPersonagens2;
    
    private Boolean comecaJogo;
    private Servidor servidor;
    
    
    

    public JogoCaraCara( Servidor servidor, Socket cliente1, Socket cliente2){
        this.servidor = servidor;
        this.cliente1 = cliente1;
        this.clientePronto1 = false;
        this.clienteRecebeuPersonagens1 = false;
        
        this.cliente2 = cliente2;
        this.clientePronto2 = false;
        this.clienteRecebeuPersonagens2 = false;
        
        this.comecaJogo = false;
    }
    
    public void run() {
    	System.out.println("Cliente conectado com  thread ("+this.getId()+ ") : "+ cliente1.getInetAddress()+" porta: "+cliente1.getLocalPort() );
    	System.out.println("Cliente conectado com  thread ("+this.getId()+ ") : "+ cliente2.getInetAddress()+" porta: "+cliente2.getLocalPort() );
	
		//InputStreamReader fluxoDados;
		
		try {
			
            InputStream inputStream1 = cliente1.getInputStream();
            InputStream inputStream2 = cliente2.getInputStream();
            BufferedReader in1 = new BufferedReader(new InputStreamReader(inputStream1));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(inputStream2));
		    OutputStream outputStream1 = cliente1.getOutputStream();
		    OutputStream outputStream2 = cliente2.getOutputStream();
            PrintWriter out1 = new PrintWriter(outputStream1, true);
            PrintWriter out2 = new PrintWriter(outputStream2, true);
            
            while (true) {
                // Ler mensagens do primeiro cliente
            	if(in1.ready()) {
            		String mensagemCliente1 = in1.readLine();
            		System.out.println(mensagemCliente1);
                    if (mensagemCliente1 == null) {
                        System.out.println("Cliente 1 desconectado.");
                        break;
                    }
                    System.out.println("Cliente 1 diz: " + mensagemCliente1);
                    if(mensagemCliente1.equals("%pronto%")) {
                    	clientePronto1 = true;
                    }
                    if(mensagemCliente1.equals("%todosPersonagensRecebidos%")) {
                    	clienteRecebeuPersonagens1 = true;
                    	System.out.println("Mensagem repassada");
                    }
                    if(mensagemCliente1.contains("%jogada%")) {
                    	if(mensagemCliente1.contains("%pergunta%")){
                    		int inicioMensagem = mensagemCliente1.indexOf("%pergunta") + 10;
                    		int fimMensagem = mensagemCliente1.indexOf("%/pergunta%");
                    		String pergunta = mensagemCliente1.substring(inicioMensagem, fimMensagem);
                    		String repassaPergunta = "%repassaJogada%%repassaPergunta%" + pergunta + "%/repassaPergunta%%/repassaJogada%";
                    		out2.println(repassaPergunta);
                    	}else
                    	if(mensagemCliente1.contains("%chute%")) {
                    		int inicioMensagem = mensagemCliente1.indexOf("%chute%")+ 7;
                    		int fimMensagem = mensagemCliente1.indexOf("%/chute%");
                    		String chute = mensagemCliente1.substring(inicioMensagem, fimMensagem);
                    		String repassaChute = "%repassaJogada%%repassaChute%" + chute + "%/repassaChute%%/repassaJogada%";
                    		out2.println(repassaChute);
                    	}
                    }
                    if(mensagemCliente1.contains("%respostaChute%")){
                    	if(mensagemCliente1.contains("%chuteCorreto%")) {
                    		out2.println("%fimDeJogo%%voceVenceu%%/fimDeJogo%");
                    		out1.println("%fimDeJogo%%vocePerdeu%%/fimDeJogo%");
                    	}
                    	if(mensagemCliente1.contains("%chuteErrado%")) {
                    		out2.println(mensagemCliente1);
                    	}
                    }if(mensagemCliente1.contains("%respostaPergunta%")) {
            			System.out.println("repassando mensagem para o outro cliente");
            			out2.println(mensagemCliente1);
            		}
            	}

            	
            	/*
            	 * 
            	 * 
            	 * */
            	
            	if(in2.ready()) {
            		String mensagemCliente2 = in2.readLine();
            		System.out.println(mensagemCliente2);
                    if (mensagemCliente2 == null) {
                        System.out.println("Cliente 1 desconectado.");
                        break;
                    }
                    System.out.println("Cliente 2 diz: " + mensagemCliente2);
                    if(mensagemCliente2.equals("%pronto%")) {
                    	clientePronto2 = true;
                    }
                    if(mensagemCliente2.equals("%todosPersonagensRecebidos%")) {
                    	clienteRecebeuPersonagens2 = true;
                    	System.out.println("Mensagem ");
                    }
                    if(mensagemCliente2.contains("%jogada%")) {
                    	if(mensagemCliente2.contains("%pergunta%")){
                    		int inicioMensagem = mensagemCliente2.indexOf("%pergunta") + 10;
                    		int fimMensagem = mensagemCliente2.indexOf("%/pergunta%");
                    		String pergunta = mensagemCliente2.substring(inicioMensagem, fimMensagem);
                    		String repassaPergunta = "%repassaJogada%%repassaPergunta%" + pergunta + "%/repassaPergunta%%/repassaJogada%";
                    		out1.println(repassaPergunta);
                    	}else 
                    	if(mensagemCliente2.contains("%chute%")) {
                    		int inicioMensagem = mensagemCliente2.indexOf("%chute%")+ 7;
                    		int fimMensagem = mensagemCliente2.indexOf("%/chute%");
                    		String chute = mensagemCliente2.substring(inicioMensagem, fimMensagem);
                    		String repassaChute = "%repassaJogada%%repassaChute%" + chute + "%/repassaChute%%/repassaJogada%";
                    		out1.println(repassaChute);
                    	}
                    }
                    if(mensagemCliente2.contains("%respostaChute%")){
                    	if(mensagemCliente2.contains("%chuteCorreto%")) {
                    		out1.println("%fimDeJogo%%voceVenceu%%/fimDeJogo%");
                    		out2.println("%fimDeJogo%%vocePerdeu%%/fimDeJogo%");
                    	}
                    	if(mensagemCliente2.contains("%chuteErrado%")) {
                    		out1.println(mensagemCliente2);
                    	}
                    }
            		if(mensagemCliente2.contains("%respostaPergunta%")) {
            			System.out.println("repassando mensagem para o outro cliente");
            			out1.println(mensagemCliente2);
            		}

            	}
            	if(clientePronto1 && clientePronto2) {
            		String stringCompletaCliente1 = criaStringListaPersonagens();
                	int inicio = stringCompletaCliente1.indexOf("%personagemSorteado%");
                	int inicioPalavra = inicio + 20;
                	int fim = stringCompletaCliente1.indexOf("%/personagemSorteado%");
                	String stringCompletaCliente2 = stringCompletaCliente1.substring(0, inicioPalavra) + personagemSorteado();
                	stringCompletaCliente2 = stringCompletaCliente2 + stringCompletaCliente1.substring(fim);
            		out1.println(stringCompletaCliente1);
            		out2.println(stringCompletaCliente2);
            		clientePronto1 = false;
            		clientePronto2 = false;
            	}
            	if(clienteRecebeuPersonagens1 && clienteRecebeuPersonagens2) {
            		out1.println("%iniciarJogo%");
            		out2.println("%iniciarJogo%");
            		clienteRecebeuPersonagens1 = false;
            		clienteRecebeuPersonagens2 = false;
            		comecaJogo = true;
            	}
            	if(comecaJogo == true) {
            		Integer decisao = decideQuemComeca();
            		if(decisao == 0) {
            			out1.println("%esperandoJogada%");
            		}else if (decisao == 1) {
            			out2.println("%esperandoJogada%");
            		}
            		comecaJogo = false;
            	}
            }
            cliente1.close();
            cliente2.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String criaStringListaPersonagens() {
    	String listaPersonagens = "%listaPersonagens%";
    	listaPersonagens = listaPersonagens + "%personagemSorteado%" + personagemSorteado() + "%/personagemSorteado";
    	
    	List<Personagem> personagens = criaPersonagens();
    	for (Personagem personagem:personagens) {
    		listaPersonagens = listaPersonagens + personagem; 		
    	}
    	listaPersonagens = listaPersonagens + "%/listaPersonagens%";
    	
    	return listaPersonagens;
    }
    
    public Integer personagemSorteado() {
    	 int limiteInferior = 0;
         int limiteSuperior = 15;
         Random random = new Random();
         int numeroAleatorio = random.nextInt(limiteSuperior - limiteInferior + 1) + limiteInferior;
         return numeroAleatorio;
    }
    
    public List<Personagem> criaPersonagens() {
    	List<Personagem> personagens = new ArrayList<Personagem>();
    	personagens.add(FactoryPersonagem.criaPersonagem("Jessie"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Linguine"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Miguel"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Woody"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Buzz"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Boo"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Flecha"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Merida"));  	
    	personagens.add(FactoryPersonagem.criaPersonagem("Violeta"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Vanellope"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Helena"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Carl"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Edna"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Beto"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Ralph"));
    	personagens.add(FactoryPersonagem.criaPersonagem("Russell"));
    	
    	return personagens;
    }
    
    public Integer decideQuemComeca() {
    	int limiteInferior = 0;
        int limiteSuperior = 1;
        Random random = new Random();
        int numeroAleatorio = random.nextInt(limiteSuperior - limiteInferior + 1) + limiteInferior;
        return numeroAleatorio;
    }

    
}