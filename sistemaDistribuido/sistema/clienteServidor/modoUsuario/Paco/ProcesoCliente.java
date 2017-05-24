package sistemaDistribuido.sistema.clienteServidor.modoUsuario.Paco;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sun.nio.cs.US_ASCII;
import java.util.*;
import java.nio.charset.StandardCharsets;
import static java.nio.charset.StandardCharsets.US_ASCII;


/**
 * 
 * Francisco Javier Peguero López
 * Paco
 * 209537864
 * 
 */
public class ProcesoCliente extends Proceso{

	/**
	 * 
	 */
    private byte[] paquete = new byte[1024];
    
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */
   
	public void run(){
		imprimeln("Proceso cliente en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		imprimeln("Llenando solicitud.");
		byte[] solCliente=new byte[1024];
		byte[] respCliente=new byte[1024];
		byte dato;
                //Cambiar por mensaje a enviar
		solCliente=paquete;
                imprimeln("Se está invocando send.");
		Nucleo.send(105,solCliente);
                imprimeln("Se está invocando recieve.");
		Nucleo.receive(dameID(),respCliente);
                String respuesta = new String(respCliente,11,respCliente[10]);
		imprimeln("El servidor me envio un: "+respuesta);
	}

    public void mensaje(String msg, String com) {
        short cop;
        switch(com)
        {
            case "Crear":
                cop = 0;
                break;

            case "Leer":
                cop = 1;
                break;

            case "Escribir":
                cop = 2;
                break;

            case "Eliminar":
                cop = 3;
                break;
            default:
                cop = 0;
                break;
        }
        imprimeln("msg:"+msg);
        //Usar el 8 para código de operación, del 0 a 3 quedan para origen y 4 a 7 para destino
        paquete[8] = (byte) cop;
        int tam = msg.length();
        //Dejar el 9 vacío para respuesta
        paquete[10] = (byte) tam;
        byte[] msgB;
        msgB = msg.getBytes(US_ASCII);
        System.arraycopy(msgB,0,paquete,11,tam);
    }
}
