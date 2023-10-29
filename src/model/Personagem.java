package model;

public class Personagem {
	
	String nome;
	
	public Personagem(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String toString() {
		return "%personagem%"
				+ "%nome%" + getNome() + "%/nome%"
											+ "%/personagem%";
	}
	
}
