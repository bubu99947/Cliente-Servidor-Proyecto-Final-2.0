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



import java.util.Stack;

import sistemaDistribuido.util.*;

public abstract class Libreria{
	private Escribano esc;

	protected Stack pila;
	protected Paquetes manejoPaquete;
	/**
	 * 
	 */
	public Libreria(Escribano esc){
		this.esc=esc;
		pila= new Stack();
		manejoPaquete= new Paquetes();
	}

	/**
	 * 
	 */
	protected void imprime(String s){
		esc.imprime(s);
	}

	/**
	 * 
	 */
	protected void imprimeln(String s){
		esc.imprimeln(s);
	}

	/**
	 * Ejemplo para el paso intermedio de parametros en pila.
	 * Esto es lo que esta disponible como interfaz al usuario programador
	 */


	/**
	 * Servidor suma verdadera generable por un compilador estandar
	 * o resguardo de la misma por un compilador de resguardos.
	 */
	@SuppressWarnings("unchecked")
	public int raiz(int valorRaiz) {
		// TODO Auto-generated method stub
		
		pila.push(valorRaiz);
		raiz();
		
		return (Integer)pila.pop();
	}

	@SuppressWarnings("unchecked")
	public int[] ordena(int[] valoresOrdenar){
		
		pila.push(valoresOrdenar);
		ordena();
		
		return (int[])pila.pop();
	}
	
	@SuppressWarnings("unchecked")
	public int sumatoria(int[] valoresSumar){
		
		pila.push(valoresSumar);
		sumatoria();
		
		return (Integer)pila.pop();
	}
	
	@SuppressWarnings("unchecked")
	public int media(int[] valores){
		pila.push(valores);
		media();
		
		return (Integer)pila.pop();
	}
	
	protected abstract void ordena();
	
	protected abstract void sumatoria();
	
	protected abstract void media();
	
	protected abstract void raiz(); 
		// TODO Auto-generated method st
}