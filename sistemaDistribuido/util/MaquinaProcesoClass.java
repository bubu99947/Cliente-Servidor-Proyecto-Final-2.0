package sistemaDistribuido.util;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;
public class MaquinaProcesoClass implements ParMaquinaProceso{
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

