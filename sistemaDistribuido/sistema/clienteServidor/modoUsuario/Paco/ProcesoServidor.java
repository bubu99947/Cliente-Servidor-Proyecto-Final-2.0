package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;
import java.nio.charset.StandardCharsets;
import static java.nio.charset.StandardCharsets.US_ASCII;
import java.util.Arrays;


/**
 * 
 * Francisco Javier Peguero López
 * Paco
 * 209537864
 * 
 */
public class ProcesoServidor extends Proceso{

	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc){
		super(esc);
        nucleo.registraBuzon(dameID());
        nucleo.registrarProcesoLocal(NUMERO_SERVICIO, dameID());
        start();
	}

	/**
	 * 
	 */
        @Override
	public void run(){
		imprimeln("Proceso servidor en ejecucion.");
		byte[] solServidor=new byte[1024];
		byte[] respServidor;
                respServidor=new byte[1024];
                String resp = new String();
		byte dato;
                byte cop;
		while(continuar()){
                        imprimeln("Se está invocando recieve.");
			Nucleo.receive(dameID(),solServidor);
                        cop = solServidor[8];
                        short tam = (short) solServidor[10];
                        String mensaje;
                        mensaje = new String(solServidor,11,solServidor[10]);
                        imprimeln("Desencriptando");
                        switch(cop)
                        {
                            case 0:
                                //Crear
                                imprimeln("El cliente me pidió crear "+mensaje);
                                resp = "Se ha creado";
                                break;
                                
                            case 1:
                                //Leer
                                imprimeln("El cliente me pidió leer "+mensaje);
                                resp = "Se ha leído";
                                break;
                                
                            case 2:
                                //Escribir
                                imprimeln("El cliente me pidió escribir "+mensaje);
                                resp = "Se ha escrito";
                                break;
                                
                            case 3:
                                //Eliminar
                                imprimeln("El cliente me pidió eliminar "+mensaje);
                                resp = "Se ha eliminado";
                                break;
                        }
                        imprimeln("\nGenerando respuesta");
                        int tm = resp.length();
                        respServidor[10] = (byte) tm;
                        byte[] msgB;
                        msgB = resp.getBytes(US_ASCII);
                        System.arraycopy(msgB,0,respServidor,11,tm);
			Pausador.pausa(10000);  //sin esta l�nea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln("Enviando respuesta...");
			Nucleo.send(solServidor[0],respServidor);
		}
        nucleo.deregistraProcesoLocal(NUMERO_SERVICIO, dameID());
	}
}
