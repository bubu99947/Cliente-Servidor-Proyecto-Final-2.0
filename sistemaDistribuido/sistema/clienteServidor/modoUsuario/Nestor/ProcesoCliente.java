/* ******************************
 * Nestor Ubaldo Gonzalez Alcala*
 * D03                          *
 * Practica 2                   *
 * *****************************/
/* NOTA 1: El separador es *
 * NOTA 2: Tambien incluyo los puntos extras de la practica 1
 * NOTA 3: La extension ".txt" se agrega automaticamente al archivo
 * */
package sistemaDistribuido.sistema.clienteServidor.modoUsuario.Nestor;

import java.awt.Button;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;


public class ProcesoCliente extends Proceso{
	int codigoOperacion;
	String mensajeOperacion;
	
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}
	
	public ProcesoCliente(Escribano esc,Button btnSolicitud){
		super(esc);
		start();
	}

	public void run(){
		imprimeln("Inicio de proceso");
		String mensajeRespuesta;
		PaqueteSolicitud operacionesPaquetes=new PaqueteSolicitud();
		boolean reintenta;
		while(continuar()){
			imprimeln("Esperando datos para continuar");
			Nucleo.suspenderProceso();
			
			if(continuar()){
				byte[] solCliente;
				byte[] respCliente=new byte[1024];
				imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
				solCliente= operacionesPaquetes.generaPaqueteSolicitud(codigoOperacion, mensajeOperacion);
				
				imprimeln("Señalamiento al núcleo para envío de mensaje");
				nucleo.send(100,solCliente);
										
				
				
				imprimeln("Invocando a receive()");
				
				Nucleo.receive(dameID(),respCliente);
				if(respCliente[9]==-10)
					imprimeln("no hay servidores dando el servicio solicitado, intenta mas tarde");
				else{
					imprimeln("Procesando respuesta recibida por el servidor"+new String(respCliente));
					mensajeRespuesta = operacionesPaquetes.desempaquetaRespuesta(respCliente);
					imprimeln("La respuesta del servidor es: "+mensajeRespuesta);	
				}
			}
		}
	}

	public void setSolicitud(int codigoOperacion, String mensaje) {
		this.codigoOperacion=codigoOperacion;
		mensajeOperacion=mensaje;
		
	}
	
	
	
	
}

class PaqueteSolicitud {
	
	public byte[] generaPaqueteSolicitud(int codigoOperacion,String mensaje){
		byte[] paqueteSolicitud = new byte[1024];
		paqueteSolicitud[9]=(byte) codigoOperacion;
		convierteABytes(mensaje, 10, paqueteSolicitud);
		
		return paqueteSolicitud;
		
	}
	
	public String desempaquetaRespuesta(byte[] respuestaCliente){
		return convierteAString(respuestaCliente);
		
	}
	
	private int convierteByteaInt(byte[] enteroByte){
		
		int entero = 0x00000000;
		
		int auxiliar = enteroByte[3];
		auxiliar = auxiliar&0x000000ff;
		entero = entero | auxiliar;
		
		entero = entero << 8;
		auxiliar = enteroByte[2];
		auxiliar = auxiliar&0x000000ff;
		entero = entero | auxiliar;
		
		entero = entero << 8;
		auxiliar = enteroByte[1];
		auxiliar = auxiliar&0x000000ff;
		entero = entero | auxiliar;
		
		entero = entero << 8;
		auxiliar = enteroByte[0];
		auxiliar = auxiliar&0x000000ff;
		entero = entero | auxiliar;
		
		return entero;
	}


	private void convierteABytes(String cadena,int posicion,byte[] paquete){
		byte[] aux;
		int tamanoCadena;
		
		aux=cadena.getBytes();
		tamanoCadena=cadena.length();
		
		for(int i=0;i<tamanoCadena;i++)
			paquete[posicion+i+1]=aux[i];
		paquete[posicion]=(byte)tamanoCadena;

	}
	
	private String convierteAString(byte[] cadenaBytesAscii){
		byte[] tamanoByte=new byte[4];
		System.arraycopy(cadenaBytesAscii, 8,tamanoByte , 0, 4);
		
		int tamano = convierteByteaInt(tamanoByte);
		System.out.println(tamano);
		return new String(cadenaBytesAscii, 12, tamano);
	
	}
}
