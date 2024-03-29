package br.com.cripto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.ValidationException;

public class Cripto {
	
	/**
	 * @local Faculdade UPIS
	 * @materia Seguran�a da Informa��o
	 * @professor Olivier Gbegan
	 * @author Faris Saleh Ahmad
	 * @data 25 de setembro de 2019
	 */

	public static void main(String[] args) throws Exception {
		String in = "FPOCLAPPAPFLMCZAUNPNPVPNTONQYVMNJVULYVLNFVAOGHATP";
		Cripto cripto = new Cripto();
		
		System.out.println("Texto Cripotgrafado :");
		System.out.println(in);
		
		System.out.println("\nTexto antes da congru�ncia f(x)= -7x-8 :");
		String texto = cripto.congruenciaReversa(in);
		System.out.println(texto);
		
		System.out.println("\nTexto antes da permuta��o P =(7412356):");
		String key = cripto.findInversa("7412356");
		texto = cripto.permutacao(key, texto);
		System.out.println(texto);
		
		System.out.println("\nTexto antes da transposi��o Ch=7:");
		texto = cripto.decodeTP(texto, 7);
		System.out.println(texto);
		
		System.out.println("\nTexto antes do padr�o de ceasar:");
		texto = cripto.decodeDPC(texto);
		System.out.println(texto);
		
		System.out.println("\nTexto Claro antes da permuta��o P =(7412563):");
		String key2 = cripto.findInversa("7412563");
		String textoClaro = cripto.permutacao(key2, texto);
		System.out.println(textoClaro);
		
		//RESPOSTAS
		
		System.out.println("\nResposta a) :");
		System.out.println("\nTexto Claro:");
		System.out.println(textoClaro);
		System.out.println("\nHASH:");
		System.out.println(cripto.hash(textoClaro));		
		
		
		System.out.println("\nResposta b) TP ch 7:");
		
		System.out.println(cripto.encodeTP(textoClaro, 7));
		
		System.out.println("\nResposta c) P=(1734562):");
		
		System.out.println(cripto.permutacao("1734562", textoClaro));
	}

	//Baseado no m�todo de congru�ncia reversa com a fun��o f(x)= -7x-8 
	private String congruenciaReversa(String texto) {
		int L;
		StringBuilder b = new StringBuilder();
		
		
		for (char s : texto.toCharArray()) {
			int n = 1 ;	
			L =((int) s -64);//Valor da letra
			int mod = 1;//inicializa��o de vari�vel
			int div = 0;//inicializa��o de vari�vel
			while (mod != 0) {
				//L + 26n = -7x -8
				int funcSup = L + 8 + (26*n) ;//Parte superior da fun��o fixa
				int funcInf = -7;//Parte inferior da fun��o fixa
				mod = funcSup % funcInf; //mod
				if (mod ==0) {
					div = funcSup / funcInf;// diviss�o
					
					while(div < 1) {
						div= div + 26;
					}
					while(div > 26) {
						div= div - 26;
					}
//					System.out.println(div);
					b.append((char) (div + 64));
				}
				n++;
			} 
		}
		return b.toString();
	}

	//M�tdo para encontrar P-1 da chave de permuta��o
	private String findInversa(String key) {
		StringBuilder n = new StringBuilder();
		for (int i = 0; i < key.length(); i++) {
			int var = key.indexOf((i +1)+"");
			n.append(var+1);
		}
		return n.toString();
	}

	//M�todo de permuta��o
	private String permutacao(String key, String txt) {
		StringBuilder n = new StringBuilder();
		List<String> lista = new ArrayList<String>();
		
		if (txt.length() > key.length()) {
			lista = Arrays.asList(txt.split("(?<=\\G.{" + key.length() + "})"));			
		}else {
			lista.add(txt);
		}
		for (String s : lista) {
			for (int i = 0; i < s.length(); i++) {
				char c = key.charAt(i);
				n.append(s.charAt(c - '0' -1));
				
			}
			
		}
		return n.toString();
	}

	//M�todo que 'codifca' texto usando m�todo de transaposi��o
	private String encodeTP(String txt, int key) {
		return transposicao(txt, key);
	}

	//M�todo que 'decodifca' texto usando m�todo de transaposi��o
	private String decodeTP(String txt, int key) {
		return transposicao(txt, txt.length()/ key);
	}

	private String transposicao(String txt, int chave) {
		List<String> strings = new ArrayList<String>();
		strings.addAll(Arrays.asList(txt.split("(?<=\\G.{" + chave + "})")));
		StringBuilder tp = new StringBuilder();
		for (int i = 0; i < chave; i++) {
			for (String s : strings) {
				if (i < s.length()) {
					tp.append(s.charAt(i));
				}
			}

		}
		return tp.toString();
	}

	//M�todo que gera o HASH de um texto baseado nas l�gica vista em sala
	public String hash(String texto) throws Exception {
		int tamCasasBin = 8;
		char[] a = texto.toUpperCase().toCharArray();
		List<String> listaBin = new ArrayList<String>();
		for (char c : texto.toUpperCase().toCharArray()) {
			listaBin.add(this.preenhcerZeroEsquerda(tamCasasBin, this.decimalToBinaryString(c)));
		}
		StringBuilder hash = new StringBuilder();
		for (int i = 0; i < tamCasasBin; i++) {
			int sum = 0;
			for (String s : listaBin) {
				sum += s.charAt(i) - '0';
			}
			if (sum % 2 == 0) {
				hash.append(0);
			} else {
				hash.append(1);
			}
		}
		return hash.toString();
	}

	//M�todo que 'codifica' texto baseado no PADR�O DE CEASAR
	private String encodeDPC(String frase) {
		return dpc(frase.toUpperCase(), true);
	}

	//M�todo que 'decodifica' texto baseado no PADR�O DE CEASAR
	private String decodeDPC(String frase) {
		return dpc(frase.toUpperCase(), false);
	}

	// encode => x+3
	// !encode => x-3
	private String dpc(String frase, boolean encode) {
		int varDpc = 3;
		StringBuilder builder = new StringBuilder();
		for (char c : frase.toCharArray()) {
			int num;
			if (encode) {
				num = ((int) c) + varDpc;
			} else {
				num = ((int) c) - varDpc;
			}
			int limSup = 90;
			if (num > limSup) {
				int dif = num - limSup;
				num = 64 + dif;
			} else {
				int limInf = 65;
				if (num < limInf) {
					int dif = limInf - num;
					num = 91 - dif;
				}
			}
			builder.append((char) num);
		}
		return builder.toString();
	}

	//Utils
	private String preenhcerZeroEsquerda(int casas, String tex) {
		long n = Long.parseLong(tex);
		String s = "";
		s = String.format("%0" + casas + "d", n);
		return s;
	}

	//converte valores maiores que zero para bin�rio 
	private String decimalToBinaryString(int num) throws Exception {
		if (num < 1) {
			throw new ValidationException("N�o � permitido zero ou n�meros negativos");
		}
		StringBuilder res = new StringBuilder();
		while (num > 1) {
			res.append(num % 2);
			num = num / 2;
		}
		res.append(num);
		return res.reverse().toString();
	}
}
