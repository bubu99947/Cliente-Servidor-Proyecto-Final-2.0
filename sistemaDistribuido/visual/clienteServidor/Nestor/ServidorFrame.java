package sistemaDistribuido.visual.clienteServidor.Nestor;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Nestor.ProcesoServidor;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;

public class ServidorFrame extends ProcesoFrame{
  private static final long serialVersionUID=1;
  private ProcesoServidor proc;

  public ServidorFrame(MicroNucleoFrame frameNucleo){
    super(frameNucleo,"Servidor de Archivos Nestor");
    proc=new ProcesoServidor(this);
    fijarProceso(proc);
  }
}