/**
 * Francisco Javier Peguero lópez
 * Paco
 * 209537864
 */

package sistemaDistribuido.visual.clienteServidor.Paco;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Paco.ProcesoServidor;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;

public class ServidorFrame extends ProcesoFrame {
    private static final long serialVersionUID = 1;
    private ProcesoServidor proc1;
    public ServidorFrame(MicroNucleoFrame frameNucleo) {
        super(frameNucleo, "Servidor de Archivos");
        proc1 = new ProcesoServidor(this);
        fijarProceso(proc1);
    }
}
