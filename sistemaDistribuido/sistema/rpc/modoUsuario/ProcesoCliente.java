/* ******************************
 * Nestor Ubaldo Gonzalez Alcala*
 * D03                          *
 * Practica 3                   *
 * *****************************/

/* NOTA 1: El separador es *
 * NOTA 2: Tambien incluyo los puntos extras de la practica 1
 * NOTA 3: La extension ".txt" se agrega automaticamente al archivo
 * */
package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso{
	private Libreria lib;

	int valorRaiz;
	int[] valoresOrdenar,valoresSumar,valoresMedia;
	public ProcesoCliente(Escribano esc){
		super(esc);
		//lib=new LibreriaServidor(esc);  //primero debe funcionar con esta para subrutina servidor local
		lib=new LibreriaCliente(esc);  //luego con esta comentando la anterior, para subrutina servidor remota
		start();
	}

	/**
	 * Programa Cliente
 
	 */
	public void run() {

		imprimeln("Proceso cliente en ejecucion.");
		while(continuar()){
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		imprimeln("Salio de suspenderProceso");

		int resultado;
		@SuppressWarnings("unused")
		int[] resultados;
		
		if(continuar()){
			resultado=lib.raiz(valorRaiz);
			imprimeln("Raiz = "+resultado);
			try {
				sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultados=lib.ordena(valoresOrdenar);
			imprime("Ordenamiento: ");
			imprimeValores(resultados);
			try {
				sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultado=lib.sumatoria(valoresSumar);
			imprimeln("Sumatoria = "+resultado);
			try {
				sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultado=lib.media(valoresMedia);
			imprimeln("Media = "+resultado);
		
			imprimeln("Fin del cliente.");
		}
		}
	}

	private void imprimeValores(int[] valoresImprimir){
		for(int i : valoresImprimir)
			imprime(i+" ");
		imprimeln("");
		
		
	}
	
	public void realizarOperaciones(String raizCuadrada, String ordena, String sumatoria, String media) {
		
		valorRaiz=Integer.valueOf(raizCuadrada);
		
		valoresOrdenar = separaCadenaValores(ordena);
		
		valoresSumar = separaCadenaValores(sumatoria);
		
		valoresMedia = separaCadenaValores(media);
		
	}
	
	private int[] separaCadenaValores(String valores){
		int[] auxiliar;
		String[] valoresString;
		valores=valores.trim();
		valoresString = valores.split(" +");
		
		auxiliar = new int[valoresString.length];
		
		for (int i = 0;i<valoresString.length;i++) {
			auxiliar[i]=Integer.parseInt(valoresString[i]);
		
		}
		
		return auxiliar;	
	}
}
