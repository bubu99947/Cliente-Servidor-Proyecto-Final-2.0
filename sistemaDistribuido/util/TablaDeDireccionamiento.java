package sistemaDistribuido.util;

import java.util.LinkedList;

public class TablaDeDireccionamiento {
	LinkedList<DatosDireccionamiento> tablaProcesos;
	
	public TablaDeDireccionamiento(){
		tablaProcesos = new LinkedList<DatosDireccionamiento>();
		
		
	}
	
	
	public void agregarProceso(int idProceso,int numeroServicio,String ip){
		DatosDireccionamiento proceso = new DatosDireccionamiento(idProceso, numeroServicio, ip);
		System.out.println("Agregando "+idProceso+" "+numeroServicio+" "+ip);
		tablaProcesos.add(proceso);
		System.out.println(tablaProcesos.size());
	}
	
	
	public int quitarProceso(int idProceso, String ip){
		DatosDireccionamiento procesoQuitar = new DatosDireccionamiento(0, 0, "");
		for (DatosDireccionamiento proceso : tablaProcesos) {
			if(proceso.idProceso==idProceso && proceso.ip.equals(ip))
				procesoQuitar = proceso;
		}
		System.out.println(tablaProcesos.size());
		tablaProcesos.remove(procesoQuitar);
		System.out.println(tablaProcesos.size());
		return procesoQuitar.numeroServicio;
	}
	
	public DatosDireccionamiento buscarProceso(int numeroServicio){
		System.out.println("Buscando Proceso");
		for (DatosDireccionamiento proceso : tablaProcesos) {
			if(proceso.numeroServicio==numeroServicio){
				
				System.out.println("Encontrado "+proceso.idProceso+" "+proceso.numeroServicio+" "+proceso.ip);
				return proceso;
			}
				
		}
		
		return null;
	}
	
	public LinkedList<DatosDireccionamiento> buscarTodosProcesos(int numeroServicio) {
		LinkedList<DatosDireccionamiento> temporal = new LinkedList<DatosDireccionamiento>();
		System.out.println("Buscando Procesos");
		for (DatosDireccionamiento proceso : tablaProcesos) {
			System.out.println(proceso.idProceso+proceso.numeroServicio+numeroServicio);
			if(proceso.numeroServicio==numeroServicio){
				System.out.println("encontrado "+proceso.idProceso+" "+proceso.numeroServicio+" "+proceso.ip);
				temporal.add(proceso);
				
			}
				
		}
		
		return temporal;
	}
	
	
}
