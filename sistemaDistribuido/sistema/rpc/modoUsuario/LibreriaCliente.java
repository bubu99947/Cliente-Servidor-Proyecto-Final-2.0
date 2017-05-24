/* ******************************
 * Nestor Ubaldo Gonzalez Alcala*
 * D03                          *
 * Practica 4                   *
 * *****************************/

package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;  //para práctica 4
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.rpc.modoUsuario.Libreria;
import sistemaDistribuido.util.Escribano;

public class LibreriaCliente extends Libreria{

	/**
	 * 
	 */
	public LibreriaCliente(Escribano esc){
		super(esc);
	}

	/**
	 * Ejemplo de resguardo del cliente suma
	 */


	@SuppressWarnings("unchecked")
	@Override
	protected void raiz() {
		// TODO Auto-generated method stub
		int[] operandos= new int[1]; 
		operandos[0]=(Integer)pila.pop();
		System.out.println(operandos[0]);
		 
		byte[] solicitudCliente= manejoPaquete.generaPaqueteSolicitud(0, operandos);
		byte[] respuestaCliente= new byte[1024];
		
		int idDestino=RPC.importarInterfaz("Servidor Matematico", "1.0");  //para práctica 4
		
		if(idDestino!=-1){
			Nucleo.send(idDestino, solicitudCliente);
			Nucleo.receive(Nucleo.dameIdProceso(), respuestaCliente);
			
			pila.push(manejoPaquete.desempaquetaRespuesta(respuestaCliente)[0]);
		}else{
			pila.push(-255);
		}
		
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void ordena() {
		int[] operandos	= (int[])pila.pop();
		 
		byte[] solicitudCliente = manejoPaquete.generaPaqueteSolicitud(1, operandos);
		byte[] respuestaCliente = new byte[1024];
		
		int idDestino=RPC.importarInterfaz("Servidor Matematico", "1.0");  //para práctica 4
		
		if(idDestino!=-1){
			Nucleo.send(idDestino, solicitudCliente);
			Nucleo.receive(Nucleo.dameIdProceso(), respuestaCliente);
			
			pila.push(manejoPaquete.desempaquetaRespuesta(respuestaCliente));
		}else{
			pila.push(new int[]{-255});
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void sumatoria() {
		int[] operandos	= (int[])pila.pop();
		 
		byte[] solicitudCliente = manejoPaquete.generaPaqueteSolicitud(2, operandos);
		byte[] respuestaCliente = new byte[1024];
		
		
		int idDestino=RPC.importarInterfaz("Servidor Matematico", "1.0");  //para práctica 4
		
		if(idDestino!=-1){
			Nucleo.send(idDestino, solicitudCliente);
			Nucleo.receive(Nucleo.dameIdProceso(), respuestaCliente);
			
			pila.push(manejoPaquete.desempaquetaRespuesta(respuestaCliente)[0]);
		}else{
			pila.push(-255);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void media() {
		int[] operandos	= (int[])pila.pop();
		 
		byte[] solicitudCliente = manejoPaquete.generaPaqueteSolicitud(3, operandos);
		byte[] respuestaCliente = new byte[1024];
		
		int idDestino=RPC.importarInterfaz("Servidor Matematico", "1.0");  //para práctica 4
		
		if(idDestino!=-1){
			Nucleo.send(idDestino, solicitudCliente);
			Nucleo.receive(Nucleo.dameIdProceso(), respuestaCliente);
			
			pila.push(manejoPaquete.desempaquetaRespuesta(respuestaCliente)[0]);
		}else{
			pila.push(-255);
		}
	}



}