package sistemaDistribuido.util;

import java.net.DatagramSocket;
import java.util.LinkedList;

import sistemaDistribuido.util.paquetes.EnviarPaquete;

public class BuscarDireccionamientoLocal extends Thread {
	TablaDeDireccionamiento procesosLocales;
	int numeroServicio;
	DatagramSocket ds;
	int puertoRecepcion;
	
	public BuscarDireccionamientoLocal(TablaDeDireccionamiento procesosLocales,int numeroServicio,DatagramSocket ds,int puertoRecepcion){
		this.procesosLocales =procesosLocales;
		this.numeroServicio = numeroServicio;
		this.ds=ds;
		this.puertoRecepcion = puertoRecepcion;
		
		
		
	}
	
	public void run(){
		EnviarPaquete enviador;
		LinkedList<DatosDireccionamiento> encontrados = procesosLocales.buscarTodosProcesos(numeroServicio);
		System.out.println("Estoy aqui!!!"+encontrados.size());
		for (DatosDireccionamiento datosDireccionamiento : encontrados) {
			enviador = new EnviarPaquete(ds, puertoRecepcion);
			enviador.EnviaFSA(datosDireccionamiento.idProceso, datosDireccionamiento.numeroServicio);
			try {
				this.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
