/* ******************************
 * Nestor Ubaldo Gonzalez Alcala*
 * D03                          *
 * Practica 5                   *
 * *****************************/
/* NOTA 1: El separador es *
 * NOTA 3: La extension ".txt" se agrega automaticamente al archivo
 * */
package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.spec.DSAGenParameterSpec;
import java.util.Hashtable;
import java.util.LinkedList;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_HARD_LIGHTPeer;
import com.sun.xml.internal.ws.encoding.policy.EncodingPolicyValidator;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MicroNucleoBase;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.BuscarDireccionamientoLocal;
import sistemaDistribuido.util.Buzon;
import sistemaDistribuido.util.DatosDireccionamiento;
import sistemaDistribuido.util.Pausador;
import sistemaDistribuido.util.TablaDeDireccionamiento;
import sistemaDistribuido.util.MaquinaProcesoClass;
import sistemaDistribuido.util.paquetes.EnviarPaquete;


/**
 * 
 */
public final class MicroNucleo extends MicroNucleoBase{
	private static MicroNucleo nucleo=new MicroNucleo();

	public Hashtable<Integer,ParMaquinaProceso> tablaEmision;
	private Hashtable<Integer, byte[]> tablaRecepcion;
	private Hashtable<Integer, Buzon> buzones; //de buzones
	private TablaDeDireccionamiento tablaDireccionamientoProcesosRemotos;
	private TablaDeDireccionamiento tablaDireccionamientoProcesosLocales;
	private EnviarPaquete enviador;
	
	
	/**
	 * 
	 */
	private MicroNucleo(){
		tablaEmision= new Hashtable<Integer, ParMaquinaProceso>();
		tablaRecepcion = new Hashtable<Integer, byte[]>();
		buzones = new Hashtable<Integer, Buzon>(); //de buzones
		tablaDireccionamientoProcesosLocales = new TablaDeDireccionamiento();
		tablaDireccionamientoProcesosRemotos = new TablaDeDireccionamiento();
		
	}
	
	public void registrarEnTablaEmision(ParMaquinaProceso asa){
		tablaEmision.put(asa.dameID(), asa);
		
		
		
		
	}
	
	
	// funcion de buzones, la llama el proceso servicor en su constructor
	public boolean registraBuzon(int idProceso){
		imprimeln("-------Registrando buzón del servidor "+idProceso);
		buzones.put(idProceso, new Buzon(idProceso));
		return true;
	}
	
	public boolean registrarProcesoLocal(int numeroServicio,int idProceso){
		imprimeln("-------Registrando servidor en la tabla de direccionamiento de procesos Locales "+idProceso);
		try {
			tablaDireccionamientoProcesosLocales.agregarProceso(idProceso, numeroServicio, InetAddress.getLocalHost().getHostAddress());
			System.out.println("Agregando Proceso a la tabla de emision");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean deregistraProcesoLocal(int numeroServicio,int idProceso){
		imprimeln("-------Registrando servidor en la tabla de direccionamiento de procesos Locales "+idProceso);
		try {
			tablaDireccionamientoProcesosLocales.quitarProceso(idProceso, InetAddress.getLocalHost().getHostAddress());
			System.out.println("Quitando Proceso a la tabla de emision");
		} catch (UnknownHostException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 
	 */
	public final static MicroNucleo obtenerMicroNucleo(){
		return nucleo;
	}

	/*---Metodos para probar el paso de mensajes entre los procesos cliente y servidor en ausencia de datagramas.
    Esta es una forma incorrecta de programacion "por uso de variables globales" (en este caso atributos de clase)
    ya que, para empezar, no se usan ambos parametros en los metodos y fallaria si dos procesos invocaran
    simultaneamente a receiveFalso() al reescriir el atributo mensaje---*/
	byte[] mensaje;

	public void sendFalso(int dest,byte[] message){
		System.arraycopy(message,0,mensaje,0,message.length);
		notificarHilos();  //Reanuda la ejecucion del proceso que haya invocado a receiveFalso()
	}

	public void receiveFalso(int addr,byte[] message){
		mensaje=message;
		suspenderProceso();
	}
	/*---------------------------------------------------------*/

	/**
	 * 
	 */
	protected boolean iniciarModulos(){
		return true;
	}

	/**
	 * 
	 */
	public void sendVerdadero(int dest,byte[] message){
		//sendFalso(dest,message);
		imprimeln("El proceso invocante de SEND es el "+super.dameIdProceso());
		ParMaquinaProceso pmp=null;
		DatosDireccionamiento datosDireccionamiento;
		int reintentos=1;
		
		
		
		//lo siguiente aplica para la práctica #2
		do{
		//AQUI HACEMOS EL LSA
			if((pmp=tablaEmision.get(dest))==null){
				
				if((datosDireccionamiento=tablaDireccionamientoProcesosRemotos.buscarProceso(dest))==null){
					imprimeln("Numero de servicio no encontrado, enviando LSA, reintento "+reintentos );
					enviador = new EnviarPaquete(dameSocketEmision(), damePuertoRecepcion());
					enviador.EnviaLSA(dest);
					try {
						reintentos++;
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//return false;
				} else{
					pmp = new MaquinaProcesoClass(datosDireccionamiento.idProceso, datosDireccionamiento.ip);
					break;
					
				}	
				
			}
		}while(reintentos<4 &&pmp==null);
		
		if(pmp==null){
			reanudarProcesoNumeroServicioNoEncontrado(dameIdProceso(),dest);	
		}else{
		
			imprimeln("El proceso destinatario es: "+pmp.dameID()+" y la ip es: "+pmp.dameIP());
			
			message[0]=(byte)dameIdProceso();
			message[4]=(byte)pmp.dameID();
			
			try {
				DatagramSocket ds = dameSocketEmision();
				DatagramPacket dp = new DatagramPacket(message,message.length,InetAddress.getByName(pmp.dameIP()),damePuertoRecepcion());
				imprimeln("Enviando paquete");
				ds.send(dp);
			} 
			catch(SocketException ex){
				imprimeln("Error iniciando socket: "+ex.getMessage());
			}catch(UnknownHostException ex){
				imprimeln("UnknownHostException: "+ex.getMessage());
			}catch(IOException ex){
				imprimeln("IOException: "+ex.getMessage());
			} 
			
		}
		
		//suspenderProceso();   //esta invocacion depende de si se requiere bloquear al hilo de control invocador
		 
	}

	/**
	 * de buzones
	 * 1 if verifica si el proceso tiene buzon, si no lo tiene quiere decir que es un proceso cliente
	 * 2 if verifica si el buzon esta vacio, en caso de que lo este se agrega el servidor a la tabla de recepcion
	 * 3 el buzon no esta vacio y el servor toma la siguiente solicitud de la cola
	 */
	protected void receiveVerdadero(int addr,byte[] message){
		//receiveFalso(addr,message);
		//el siguiente aplica para la práctica #2
		imprimeln("El proceso invocante de RECEIVE es el "+super.dameIdProceso());
		Buzon buzonServidor;
		if((buzonServidor=buzones.get(addr))==null){
			tablaRecepcion.put(addr, message);
			imprimeln("---------------Agregando proceso cliente "+addr+" a la tabla de recepción");
			suspenderProceso();
			
		}else if(buzonServidor.vacio()){
			tablaRecepcion.put(addr, message);
			imprimeln("-----------El buzon del servidor esta vacio. Agregando proceso servidor "+addr+"  a la tabla de recepción");
			System.out.println(buzones.get(addr).hayEspacio());
			suspenderProceso();
		} else {
			System.arraycopy(buzonServidor.retira(), 0, message, 0, 1024);
			imprimeln("-----------El servidor  "+addr+"  obtuvo un elemento del cliente "+message[0]+" del buzón");
		}
	}

	/**
	 * Para el(la) encargad@ de direccionamiento por servidor de nombres en prï¿½ctica 5  
	 */
	protected void sendVerdadero(String dest,byte[] message){
	}

	/**
	 * Para el(la) encargad@ de primitivas sin bloqueo en prï¿½ctica 5
	 */
	protected void sendNBVerdadero(int dest,byte[] message){
	}

	/**
	 * Para el(la) encargad@ de primitivas sin bloqueo en prï¿½ctica 5
	 */
	protected void receiveNBVerdadero(int addr,byte[] message){
	}

	/**
	 * 
	 */
	public void run(){
		DatagramSocket socketRecepcion;
		DatagramPacket dp;
		byte[] buffer=new byte[1024];
		byte[] mensaje;
		Proceso procesoDestino;
		ReenviaPaquete reenviador; //hilo que reenvia los paquetes
		
		try{
			socketRecepcion=dameSocketRecepcion();
			dp=new DatagramPacket(buffer,buffer.length);
			while(seguirEsperandoDatagramas()){
				imprimeln("Recibiendo paquete");
				System.out.println("Recibiendo paquete");
				socketRecepcion.receive(dp);
				
				if(seguirEsperandoDatagramas()){
					imprimeln("Paquete recibido, procesando paquete");
					
					
					if(buffer[9]==(int)-1){// si es AU
						imprimeln("*********El proceso destino no fue encontrado");
						imprimeln("*********Elminando proceso destino de la tabla de procesos remotos");
						System.out.println(dp.getAddress().getHostAddress());
						
						int numeroServicio=tablaDireccionamientoProcesosRemotos.quitarProceso(buffer[4], dp.getAddress().getHostAddress());
						DatosDireccionamiento siguienteOpcion=tablaDireccionamientoProcesosRemotos.buscarProceso(numeroServicio);
						if(siguienteOpcion!=null){
							buffer[4]=(byte)siguienteOpcion.idProceso;
							imprimeln("*********Se reenviará el paquete del cliente "+buffer[0]+" en 5 segundos");
							imprimeln("*********El nuevo destino es el proceso "+ siguienteOpcion.idProceso+" con la ip "+siguienteOpcion.ip);
							
							mensaje = new byte[1024];
							System.arraycopy(buffer, 0, mensaje, 0, 1024);
							reenviador = new ReenviaPaquete(siguienteOpcion.ip,mensaje);
							reenviador.start();
						} else {//LSA
							EnviadorLSAYVerificador enviadorYVerficador = new EnviadorLSAYVerificador(numeroServicio,buffer);
							enviadorYVerficador.start();
						}
						
					
					}else if(buffer[9]==(int)-2){//Si es TA
						imprimeln("---------El proceso destino no puede aceptar solicitudes");
						imprimeln("---------Se reenviará el paquete del cliente "+buffer[0]+" en 5 segundos");
						mensaje = new byte[1024];
						System.arraycopy(buffer, 0, mensaje, 0, 1024);
						reenviador = new ReenviaPaquete(dp.getAddress().getHostAddress(),mensaje);
						reenviador.start();
						//si el paquete es AU si despierta al proceso cliente, si es TA no lo despierta
					
					} else if(buffer[9]==-3){//LSA
						imprimeln("***********Se recibió un paquete LSA");
						imprimeln("***********Buscando en la tabla de procesos locales el numero de servicio "+buffer[8]);
						BuscarDireccionamientoLocal bdl= new BuscarDireccionamientoLocal(tablaDireccionamientoProcesosLocales, buffer[8], dameSocketEmision(), damePuertoRecepcion());
						bdl.start();
						
						
					
					} else if(buffer[9]==-4){//FSA
						imprimeln("***********Se recibió un paquete FSA");
						tablaDireccionamientoProcesosRemotos.agregarProceso(buffer[11],buffer[10],dp.getAddress().getHostAddress());
					
					} else if((procesoDestino=dameProcesoLocal((int)buffer[4]))!=null){
						imprimeln("El proceso local "+(int)buffer[4]+" fue encontrado");
						
						if((mensaje=tablaRecepcion.get((int)buffer[4]))!=null){
							imprimeln("El proceso "+(int)buffer[4]+" está esperando paquetes");
							tablaRecepcion.remove((int)buffer[4]);
							tablaEmision.put((int)buffer[0],new MaquinaProcesoClass((int)buffer[0],dp.getAddress().getHostAddress()));
							System.arraycopy(buffer, 0, mensaje, 0, dp.getLength());
							imprimeln("Despertando al proceso");
							reanudarProceso(procesoDestino);
						}else{
							//de buzones
							Buzon buzonProceso = buzones.get((int)buffer[4]);
							System.out.println("parte agregar a buzon "+buzonProceso.idProceso);
							
							if(buzonProceso.hayEspacio()){
								System.out.println("Si hay espacio");
								imprimeln("------------Agregando la solicitud del proceso " + buffer[4]+" al buzon ");
								mensaje = new byte[1024]; 
								tablaEmision.put((int)buffer[0],new MaquinaProcesoClass((int)buffer[0],dp.getAddress().getHostAddress()));
								System.arraycopy(buffer, 0, mensaje, 0, 1024);
								buzonProceso.agrega(mensaje);
							} else {
								System.out.println("NO hay espacio");
								imprimeln("-------------Se recibió un mensaje del cliente "+buffer[0]+" pero el buzón está lleno, enviando un paquete TA");
								enviador = new EnviarPaquete(dameSocketEmision(), damePuertoRecepcion());
								enviador.EnviaTA(new MaquinaProcesoClass((int)buffer[0],dp.getAddress().getHostAddress()),buffer);
							}
							System.out.println("Tamaño buzon "+buzonProceso.tamano());
						}
					
					}else{
						imprimeln("El proceso local "+(int)buffer[4]+"NO fue encontrado, enviando paquete AU");
						enviador = new EnviarPaquete(dameSocketEmision(), damePuertoRecepcion());
						enviador.EnviaAU(new MaquinaProcesoClass((int)buffer[0],dp.getAddress().getHostAddress()),buffer);
					}
							
					sleep(500);
			}
		}
			
		}catch(SocketException e){
			System.out.println("Error iniciando socket: "+e.getMessage());
		}catch(IOException e){
			System.out.println("IOException: "+e.getMessage());
		}catch(InterruptedException e){
			System.out.println("InterruptedException");
		}
		
		
		
		
		
	}
	
	
	private void reanudarProcesoNumeroServicioNoEncontrado(int idProceso,int numeroServicio){
		byte[] mensajeError = new byte[1024];
		byte[] mensaje = new byte[1024];
		
		mensajeError[9]=-10;
		imprimeln("*********No se encontro ningun proceso dando el servicio "+numeroServicio+", despertando al proceso origen "+idProceso);
		if((mensaje=tablaRecepcion.get(idProceso))!=null){
			tablaRecepcion.remove(idProceso);
			
			System.arraycopy(mensajeError, 0, mensaje, 0, 1024);
			imprimeln("Despertando al proceso");
			reanudarProceso(dameProcesoLocal(idProceso));
		}
		
		
	}
	
	class EnviadorLSAYVerificador extends Thread{
		int contadorIntentos=1,numeroServicio;
		byte[] mensaje;
		
		DatosDireccionamiento datosDireccionamiento;
		
		public EnviadorLSAYVerificador(int numeroServicio,byte[] mensaje){
			this.mensaje = new byte[1024];
			System.arraycopy(mensaje, 0, this.mensaje, 0, 1024);
			this.numeroServicio=numeroServicio;
			datosDireccionamiento=null;
			
		}
		
		public void run(){
			ReenviaPaquete reenviador;
			do{
				if((datosDireccionamiento=tablaDireccionamientoProcesosRemotos.buscarProceso(numeroServicio))==null){
					imprimeln("*********No se encontro ningun proceso dando el servicio "+numeroServicio+" enviando LSA intento "+contadorIntentos);
					enviador = new EnviarPaquete(dameSocketEmision(), damePuertoRecepcion());
					enviador.EnviaLSA(numeroServicio);
					try {
						contadorIntentos++;
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
				} else{
					mensaje[4]=(byte)datosDireccionamiento.idProceso;
					imprimeln("*********Se reenviará el paquete del cliente "+mensaje[0]+" en 5 segundos");
					imprimeln("*********El nuevo destino es el proceso "+ datosDireccionamiento.idProceso+" con la ip "+datosDireccionamiento.ip);
					
					mensaje = new byte[1024];
					
					reenviador = new ReenviaPaquete(datosDireccionamiento.ip,mensaje);
					reenviador.start();
				
					
				}	
				
				
			}while(contadorIntentos !=4 && datosDireccionamiento==null);
				
			if(datosDireccionamiento==null) {
				reanudarProcesoNumeroServicioNoEncontrado(mensaje[0],numeroServicio);
			}
			
			
			
		}
		
	}
	

	
	
	
	class ReenviaPaquete extends Thread{
		
		String ip;
		byte[] mensaje;
		
		public ReenviaPaquete(String ip,byte[] mensaje){
			this.mensaje = mensaje;
			this.ip=ip;
		}
		
		public void run(){
			Pausador.pausa(5000);
			
			mensaje[9]=0;
			try {
				DatagramSocket ds = dameSocketEmision();
				DatagramPacket dpReintenta = new DatagramPacket(mensaje,mensaje.length,InetAddress.getByName(ip),damePuertoRecepcion());
				System.out.println("Reenviando paquete de proceso " + mensaje[0]);
				ds.send(dpReintenta);
			} 
			catch(SocketException ex){
				imprimeln("Error iniciando socket: "+ex.getMessage());
			}catch(UnknownHostException ex){
				imprimeln("UnknownHostException: "+ex.getMessage());
			}catch(IOException ex){
				imprimeln("IOException: "+ex.getMessage());
			}
			
		}
		
	}
}
