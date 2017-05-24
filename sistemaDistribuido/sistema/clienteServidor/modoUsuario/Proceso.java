package sistemaDistribuido.sistema.clienteServidor.modoUsuario;


import microKernelBasedSystem.system.clientServer.userMode.threadPackage.SystemProcess;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MicroNucleo;
import sistemaDistribuido.util.Escribano;


public abstract class Proceso extends SystemProcess{
	protected MicroNucleo nucleo;


	public Proceso(Escribano esc){
		super(Nucleo.nucleo,esc);
		this.nucleo=Nucleo.nucleo;
	}

	
	public Proceso(MicroNucleo nucleo,Escribano esc){
		this(esc);
		start();
	}

	protected void imprime(String s){
		super.print(s);
	}


	protected void imprimeln(String s){
		super.println(s);
	}


	public final int dameID(){
		return (int)super.getID();
	}


	public final boolean continuar(){
		return super.continueExecuting();
	}


	public void terminar(){
	
	}


	public void run(){
	}


	protected void shutdown(){
		terminar();
	}
}
