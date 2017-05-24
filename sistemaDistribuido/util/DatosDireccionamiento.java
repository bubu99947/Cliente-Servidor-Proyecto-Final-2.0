package sistemaDistribuido.util;

public class DatosDireccionamiento {
	public int idProceso,numeroServicio;
	public String ip;
	
	
	public DatosDireccionamiento(int idProc,int numServicio,String ip){
		idProceso=idProc;
		numeroServicio = numServicio;
		this.ip =ip;
		
	}
}
