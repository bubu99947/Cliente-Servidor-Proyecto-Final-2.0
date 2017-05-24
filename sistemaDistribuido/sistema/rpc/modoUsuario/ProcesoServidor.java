/* ******************************
 * Nestor Ubaldo Gonzalez Alcala*
 * D03                          *
 * Practica 4                   *
 * *****************************/


package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;   //para práctica 4

import java.net.InetAddress;
import java.net.UnknownHostException;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Paquetes;

/**
 * 
 */
public class ProcesoServidor extends Proceso{
	private LibreriaServidor ls;   //para práctica 3
	private Paquetes manejoPaquete;

	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc){
		super(esc);
		ls=new LibreriaServidor(esc);   //para práctica 3
		manejoPaquete = new Paquetes();
		start();
	}

	/**
	 * Resguardo del servidor
	 */
	public void run(){
		int idUnico;
		imprimeln("Proceso servidor en ejecucion.");
		try {
			idUnico=RPC.exportarInterfaz("Servidor Matematico", "1.0", new MaquinaProcesoClass(dameID(), InetAddress.getLocalHost().getHostAddress()));
			
	
			byte[] solicitudServidor = new byte[1024];
			byte[] respuestaServidor = new byte[1024];
			Paquetes.Solicitud valoresSolicitud;
			int[] resultados= new int[1];
			//int resultado;
			while(continuar()){
				imprimeln("Invocando a receive()");
				Nucleo.receive(dameID(),solicitudServidor);
				
				if(continuar()){
					imprimeln("Procesando peticion recibida del cliente");
					valoresSolicitud = manejoPaquete.desempaquetaSolicitud(solicitudServidor);
					
					switch (valoresSolicitud.getCodigoOperacion()) {
					case 0:
						imprimeln("La operación solicitada es: raiz cuadrada");
						imprimeln("El valor es: "+valoresSolicitud.getOperandos()[0]);
						int resultado = ls.raiz(valoresSolicitud.getOperandos()[0]);
						imprimeln("El resultado es: "+resultado);
						resultados = new int[1];
						resultados[0]=resultado;
						break;
					case 1:
						imprimeln("La operación solicitada es: ordenamiento");
						imprimeln("Los valores son: ");
						imprimeValores(valoresSolicitud.getOperandos());
						resultados = ls.ordena(valoresSolicitud.getOperandos());
						imprimeln("El resultado es: ");
						imprimeValores(resultados);
						break;
					case 2:
						imprimeln("La operación solicitada es: sumatoria");
						imprimeln("Los valores son: ");
						imprimeValores(valoresSolicitud.getOperandos());
						resultado = ls.sumatoria(valoresSolicitud.getOperandos());
						imprimeln("El resultado es: "+resultado);
						resultados = new int[1];
						resultados[0]=resultado;
						break;
					case 3:
						imprimeln("La operación solicitada es: media");
						imprimeln("Los valores son: ");
						imprimeValores(valoresSolicitud.getOperandos());
						resultado = ls.media(valoresSolicitud.getOperandos());
						imprimeln("El resultado es: "+resultado);
						resultados = new int[1];
						resultados[0]=resultado;
						break;
	
					default:
						
						break;
					}
					imprimeln("Generando mensaje a ser enviado");
					respuestaServidor = manejoPaquete.generaPaqueteRespuesta(resultados);
					imprimeln("Enviando mensaje");
					Nucleo.send(solicitudServidor[0], respuestaServidor);
					imprimeln("Mensaje enviado");
				}
			}
			RPC.deregistrarInterfaz("Servidor Matematico", "1.0", idUnico); //para práctica 4
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
	private void imprimeValores(int[] valoresImprimir){
		for(int i : valoresImprimir)
			imprime(i+" ");
		imprimeln("");
		
		
	}
	
	class MaquinaProcesoClass implements ParMaquinaProceso{
		private int id;
		private String ip;
		
		public MaquinaProcesoClass(int id,String ip) {
			this.ip=ip;
			this.id=id;
		}
		@Override
		public String dameIP() {
			// TODO Auto-generated method stub
			return ip;
		}
		@Override
		public int dameID() {
			// TODO Auto-generated method stub
			return id;
		}
		
	}
}
