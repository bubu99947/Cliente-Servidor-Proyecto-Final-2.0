/* ******************************
 * Nestor Ubaldo Gonzalez Alcala*
 * D03                          *
 * Practica 4                   *
 * *****************************/

package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;
import sistemaDistribuido.visual.clienteServidor.Nestor.ServidorFrame;
import sistemaDistribuido.visual.rpc.DespleganteConexiones;
import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedList;

public class ProgramaConector{
	private DespleganteConexiones desplegante;
	private Hashtable<Integer,Servidor> conexiones;   //las llaves que provee DespleganteConexiones
	
	/**
	 * 
	 */
	public ProgramaConector(DespleganteConexiones desplegante){
		this.desplegante=desplegante;
	}

	/**
	 * Inicializar tablas en programa conector
	 */
	public void inicializar(){
		conexiones=new Hashtable<Integer,Servidor>();
	}

	/**
	 * Remueve tuplas visualizadas en la interfaz gr�fica registradas en tabla conexiones
	 */
	private void removerConexiones(){
		Set<Integer> s=conexiones.keySet();
		Iterator<Integer> i=s.iterator();
		while(i.hasNext()){
			desplegante.removerServidor(((Integer)i.next()).intValue());
			i.remove();
		}
	}

	/**
	 * Al solicitar que se termine el proceso, por si se implementa como tal
	 */
	public void terminar() {
		removerConexiones();
		desplegante.finalizar();
	}

	public int registro(String nombreServidor, String version, ParMaquinaProceso asa) {
		// TODO Auto-generated method stub
		Servidor datosServidor = new Servidor(nombreServidor, version, asa);
		
		int idUnico = desplegante.agregarServidor(nombreServidor, version, asa.dameIP(), String.valueOf(asa.dameID()));
		
		conexiones.put(idUnico, datosServidor);
		
		return idUnico;
	}
	
	class Servidor{
		
		public String nombre,version;
		public ParMaquinaProceso asa;
		
		public Servidor(String nombre, String version, ParMaquinaProceso asa){
			this.nombre=nombre;
			this.version=version;
			this.asa=asa;
			
		}
		
	}

	public boolean deregistro(String nombreServidor, String version, int identificacionUnica) {
		// TODO Auto-generated method stub
		Servidor datosServidor=(Servidor)conexiones.get(identificacionUnica);
		
		if(datosServidor.nombre.equalsIgnoreCase(nombreServidor) && datosServidor.version.equalsIgnoreCase(version)){
			desplegante.removerServidor(identificacionUnica);
			conexiones.remove(identificacionUnica, datosServidor);
			return true;
		}
		
		return false;
			
	}

	public ParMaquinaProceso busqueda(String nombreServidor,String version) {
		// TODO Auto-generated method stub
		Servidor datosServidor;
		
		for(Integer id:conexiones.keySet()){
			datosServidor=conexiones.get(id);
			
			if(nombreServidor.equalsIgnoreCase(datosServidor.nombre) && version.equalsIgnoreCase(datosServidor.version) ){
				
				return datosServidor.asa;
			}
		}
		
		return null;
	}
	
	
}
