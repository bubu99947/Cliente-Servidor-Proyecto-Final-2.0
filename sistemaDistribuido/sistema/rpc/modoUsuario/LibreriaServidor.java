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

import sistemaDistribuido.sistema.rpc.modoUsuario.Libreria;
import sistemaDistribuido.util.Escribano;

public class LibreriaServidor extends Libreria{

	/**
	 * 
	 */
	public LibreriaServidor(Escribano esc){
		super(esc);
	}



	@SuppressWarnings("unchecked")
	@Override
	protected void raiz() {
		int valorRaiz=(Integer)pila.pop();
		
		pila.push((int)Math.round(Math.sqrt(valorRaiz)));
	}



	@SuppressWarnings("unchecked")
	@Override
	protected void ordena() {
		int[] valoresOrdenar=(int[])pila.pop();
		
		for(int i = 0; i < valoresOrdenar.length - 1; i++)
            for(int j = 0; j < valoresOrdenar.length - 1; j++)
                if (valoresOrdenar[j] < valoresOrdenar[j + 1])
                {
                    int tmp = valoresOrdenar[j+1];
                    valoresOrdenar[j+1] = valoresOrdenar[j];
                    valoresOrdenar[j] = tmp;
                }
		
        pila.push(valoresOrdenar);
	}



	@SuppressWarnings("unchecked")
	@Override
	protected void sumatoria() {
		int[] valoresSumar=(int[])pila.pop();
		int resultado=0;
		
		for (int i : valoresSumar)
			resultado+=i;
		
		pila.push(resultado);
		
	}



	@SuppressWarnings("unchecked")
	@Override
	protected void media() {
		int[] valoresMedia=(int[])pila.pop();
		int resultado=0;
		
		for(int i: valoresMedia)
			resultado+=i;
		
		resultado=(int)Math.round(resultado/valoresMedia.length);
		
		pila.push(resultado);
		
	}

}