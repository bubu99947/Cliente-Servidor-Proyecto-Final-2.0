/* ******************************
 * Nestor Ubaldo Gonzalez Alcala*
 * D03                          *
 * Practica 5                   *
 * *****************************/
/* NOTA 1: El separador es *
 * NOTA 3: La extension ".txt" se agrega automaticamente al archivo
 * */
package sistemaDistribuido.sistema.clienteServidor.modoUsuario.Nestor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;

import com.sun.corba.se.impl.orbutil.closure.Constant;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;

/**
 * 
 */
public class ProcesoServidor extends Proceso{
	int NUMERO_SERVICIO=100;
	
	Paquete operacionesPaquetes;
	OperacionesArchivos accionArchivo;
	Solicitud valoresSolicitud;
	
	
	
	public ProcesoServidor(Escribano esc){
		super(esc);
		operacionesPaquetes = new Paquete();
		accionArchivo =new OperacionesArchivos();
		nucleo.registraBuzon(dameID());
		nucleo.registrarProcesoLocal(NUMERO_SERVICIO, dameID());
		start();
	}

	/**
	 * 
	 */
	public void run(){
		imprimeln("Proceso servidor en ejecucion.");
		byte[] solServidor=new byte[1204];
		byte[] respServidor;
		String mensajeRespuesta="";
		while(continuar()){
			imprimeln("Invocando a receive()");
			Nucleo.receive(dameID(),solServidor);
			if(continuar()){
				imprimeln("Procesando peticion recibida del cliente");
				valoresSolicitud = operacionesPaquetes.desempaquetaSolicitud(solServidor);
				imprimeln("La operación solicitada es: "+valoresSolicitud.getCodigoOperacionStr()+" archivo");
				switch (valoresSolicitud.getCodigoOperacion()) {
				case 0:
					imprimeln("El nombre para el archivo a crear es: "+valoresSolicitud.getNombreArchivo());
					mensajeRespuesta =accionArchivo.crear(valoresSolicitud.getNombreArchivo());  //"El archivo "+valoresSolicitud.getNombreArchivo()+" se creó correctamente";
					imprimeln(mensajeRespuesta);
					break;
				case 1:
					imprimeln("El archivo a eliminar es: " +valoresSolicitud.getNombreArchivo());
					mensajeRespuesta =accionArchivo.eliminar(valoresSolicitud.getNombreArchivo());  //"El archivo "+valoresSolicitud.getNombreArchivo()+" se eliminó correctamente";
					imprimeln(mensajeRespuesta);
					break;
				case 2:
					imprimeln("El archivo a leer es: " +valoresSolicitud.getNombreArchivo());
					imprimeln("La linea del archivo a leer es: " +valoresSolicitud.getLinea());
					mensajeRespuesta =accionArchivo.leer(valoresSolicitud.getNombreArchivo(),valoresSolicitud.getLinea());  //"El archivo "+valoresSolicitud.getNombreArchivo()+" se leyó correctamente en la línea "+valoresSolicitud.getLinea()+" el mensaje leido es el siguiente: Hola mundo";
					imprimeln(mensajeRespuesta);
					break;
				case 3:
					imprimeln("El archivo a escribir es: " +valoresSolicitud.getNombreArchivo());
					imprimeln("El mensaje a escribir en el archivo es: " +valoresSolicitud.getMensaje());
					mensajeRespuesta =accionArchivo.escribir(valoresSolicitud.getNombreArchivo(),valoresSolicitud.getMensaje());  //"El archivo "+valoresSolicitud.getNombreArchivo()+" se escribió correctamente con el mensaje: "+valoresSolicitud.getMensaje();
					imprimeln(mensajeRespuesta);
					break;
				default:
					break;
				}
				
				imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
				respServidor=operacionesPaquetes.generaPaqueteRespuesta(mensajeRespuesta);
				imprimeln("Señalamiento al núcleo para envío de mensaje");
				Pausador.pausa(10000);  //sin esta línea es posible que Servidor solicite send antes que Cliente solicite receive
				imprimeln("Enviando respuesta");
				Nucleo.send(valoresSolicitud.getIdProceso(),respServidor);
				imprimeln("Respuesta enviada");
			}
		}
		
		nucleo.deregistraProcesoLocal(NUMERO_SERVICIO, dameID());
	}
}

class OperacionesArchivos{
	private File archivo;
	
	public String crear(String nombreArchivo){
		
		archivo = new File(nombreArchivo+".txt");
		
		try {
			
			if(archivo.exists()){
				return "El archivo "+nombreArchivo+" ya existe";
			}
			
			if(archivo.createNewFile())
				return "El archivo "+nombreArchivo+" se creó correctamente";
			else
				return "El archivo "+nombreArchivo+" No se creó";
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "El archivo "+nombreArchivo+" No se creó";
		}
	}
	
	public String eliminar(String nombreArchivo){
		archivo = new File(nombreArchivo+".txt");
		
		if(archivo.exists()){
			if(archivo.delete())
				return "El archivo " + nombreArchivo + " se eliminó correctamente";
			else
				return "El archivo " + nombreArchivo + " no se pudo eliminar";
			
		}
		else
			return "El archivo " + nombreArchivo + " NO existe";
		
		
		
	}
	
	public String escribir(String nombreArchivo,String mensaje){
		archivo = new File(nombreArchivo+".txt");
		
		try {
			
			if(archivo.exists()){
				BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo,true));
				escritor.write(mensaje);
				escritor.newLine();
				//escritor.append(mensaje);
				escritor.flush();
				escritor.close();
				return "El archivo "+nombreArchivo+" se escribió correctamente con el mensaje: "+mensaje;
				
			
			}else
				return "El archivo "+nombreArchivo+" NO existe";
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "El archivo "+nombreArchivo+" NO se escribió ";
		}
		
	}
	
	public String leer(String nombreArchivo,int linea){
		archivo = new File(nombreArchivo+".txt");
		String mensaje="";
		
		try {
			
			if(archivo.exists()){
				
				BufferedReader lector = new BufferedReader(new FileReader(archivo));
				
				for(int i=0;i<linea;i++){
					mensaje = lector.readLine();
					if(mensaje==null){
						lector.close();
						return "La linea "+linea+" NO existe";
					}
						
				} 
				lector.close();
				mensaje ="El archivo "+nombreArchivo+" se leyó correctamente en la linea "+linea+" con el mensaje: "+mensaje; 
				System.out.println(mensaje.length());
				return mensaje;
				
			}else
				return "El archivo "+nombreArchivo+" NO existe";
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "El archivo "+nombreArchivo+" NO pudo leer ";
		}
		
	}
	
	
}



class Paquete{
		public byte[] generaPaqueteRespuesta(String mensaje){
			byte[] paqueteRespuesta = new byte[1024];
			
			convierteABytes(mensaje, 8, paqueteRespuesta);
			
			return paqueteRespuesta;
			
		}
		
		public Solicitud desempaquetaSolicitud(byte[] paqueteSolicitud){
			Solicitud valoresSolicitud = new Solicitud();
			String[] valoresOperacionArr;
			String valoresOperacionStr;
			
			
			valoresSolicitud.setCodigoOperacion((int)paqueteSolicitud[9]);
			valoresOperacionStr = convierteAString(paqueteSolicitud);
			valoresOperacionArr = valoresOperacionStr.split("\\*+");
			valoresSolicitud.setIdProceso((int)paqueteSolicitud[0]);
			switch (valoresSolicitud.getCodigoOperacion()) {
				case 0:
					valoresSolicitud.setNombreArchivo(valoresOperacionArr[0]);
					valoresSolicitud.setCodigoOperacionStr("crear");
					break;
				case 1:
					valoresSolicitud.setNombreArchivo(valoresOperacionArr[0]);
					valoresSolicitud.setCodigoOperacionStr("eliminar");
					break;
				case 2:
					valoresSolicitud.setNombreArchivo(valoresOperacionArr[0]);
					valoresSolicitud.setLinea(Integer.parseInt(valoresOperacionArr[1]));
					valoresSolicitud.setCodigoOperacionStr("leer");
					break;
				case 3:
					valoresSolicitud.setNombreArchivo(valoresOperacionArr[0]);
					valoresSolicitud.setMensaje(valoresOperacionArr[1]);
					valoresSolicitud.setCodigoOperacionStr("escribir");
					break;
	
				default:
					valoresSolicitud=null;
					break;
			}
			
			
			
			return valoresSolicitud;
		}
		
		private byte[] convierteIntaByte(int entero){
			
			byte[] enteroByte = new byte[4];
			//for(int i=0;i<4;i++)
			enteroByte[0] = (byte)entero;
			enteroByte[1] = (byte)(entero >> 8);
			enteroByte[2] = (byte)(entero >> 16);
			enteroByte[3] = (byte)(entero >> 24);
			System.out.println(enteroByte[0]+" "+enteroByte[1]+" "+enteroByte[2]+" "+enteroByte[3]);
			return enteroByte;
		}
		
		private void convierteABytes(String cadena,int posicion,byte[] paquete){
			byte[] aux;
			int tamanoCadena;
			
			aux=cadena.getBytes();
			tamanoCadena=cadena.length();
			
			byte[] tamanoCadenaByte = convierteIntaByte(tamanoCadena);
			
			for(int i=0;i<tamanoCadena;i++)
				paquete[posicion+4+i]=aux[i];
			for(int i=0;i<4;i++)
				paquete[posicion+i]=tamanoCadenaByte[i];
		}
		
		private String convierteAString(byte[] cadenaBytesAscii){
			return new String(cadenaBytesAscii, 11, (int)cadenaBytesAscii[10]);
		}
}


class Solicitud{
	private int codigoOperacion;
	private String nombreArchivo;
	private String mensaje;
	private int linea;
	private String codigoOperacionStr; 
	private int idProceso;
	

	
	public int getCodigoOperacion(){
		return codigoOperacion;
	}
	
	public String  getCodigoOperacionStr(){
		return codigoOperacionStr;
		
	}
	public String getNombreArchivo(){
		return nombreArchivo;
	}
	public String getMensaje(){
		return mensaje;
	}
	public int getLinea(){
		return linea;
	}
	
	public int getIdProceso(){
		return idProceso;
	}
	
	public void setCodigoOperacion(int codigoOperacion){
		this.codigoOperacion = codigoOperacion;
	}
	
	public void setNombreArchivo(String nombreArchivo){
		this.nombreArchivo=nombreArchivo;
	}
	
	public void setMensaje(String mensaje){
		this.mensaje=mensaje;
	}
	public void setLinea(int linea){
		this.linea=linea;
	}
	
	public void setCodigoOperacionStr(String codigoOeracionStr){
		this.codigoOperacionStr=codigoOeracionStr;
	}
	
	public void setIdProceso(int idProceso){
		this.idProceso=idProceso;
	}
	
}
