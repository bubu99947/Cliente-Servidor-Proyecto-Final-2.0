/* ******************************
 * Nestor Ubaldo Gonzalez Alcala*
 * D03                          *
 * Practica 1                   *
 * NOTA: EL SEPARADOR ES UN *   *
 * *****************************/
package sistemaDistribuido.visual.clienteServidor.Nestor;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Nestor.ProcesoCliente;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Choice;
import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionListener;

import jdk.nashorn.internal.ir.WhileNode;

import java.awt.event.ActionEvent;

public class ClienteFrame extends ProcesoFrame{
	private static final long serialVersionUID=1;
	private ProcesoCliente proc;
	private Choice codigosOperacion;
	private TextField campoMensaje;
	private Button botonSolicitud;
	private String codop1,codop2,codop3,codop4;
	private CompruebaEstadoCliente habilitaBotonSolicitud;

	public ClienteFrame(MicroNucleoFrame frameNucleo){
		super(frameNucleo,"Cliente de Archivos Nestor");
		add("South",construirPanelSolicitud());
		validate();
		proc=new ProcesoCliente(this);
		fijarProceso(proc);
		habilitaBotonSolicitud = new CompruebaEstadoCliente(); 
		
	}

	public Panel construirPanelSolicitud(){
		Panel p=new Panel();
		codigosOperacion=new Choice();
		codop1="Crear";
		codop2="Eliminar";
		codop3="Leer";
		codop4="Escribir";
		codigosOperacion.add(codop1);
		codigosOperacion.add(codop2);
		codigosOperacion.add(codop3);
		codigosOperacion.add(codop4);
		campoMensaje=new TextField(10);
		botonSolicitud=new Button("Solicitar");
		botonSolicitud.addActionListener(new ManejadorSolicitud());
		p.add(new Label("Operacion:"));
		p.add(codigosOperacion);
		p.add(new Label("Datos:"));
		p.add(campoMensaje);
		p.add(botonSolicitud);
		return p;
	}

	class ManejadorSolicitud implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String com=e.getActionCommand();
			if (com.equals("Solicitar")){
				botonSolicitud.setEnabled(false);
				com=codigosOperacion.getSelectedItem();
				imprimeln("Solicitud a enviar: "+com);
				imprimeln("Mensaje a enviar: "+campoMensaje.getText());
				proc.setSolicitud(codigosOperacion.getSelectedIndex(),campoMensaje.getText());
				Nucleo.reanudarProceso(proc);
			}
		}
	}
	
	class CompruebaEstadoCliente extends Thread{
		public void run(){		
			while(true){
				if(proc.getState()==Thread.State.WAITING)
					botonSolicitud.setEnabled(true);
				try {
					sleep(3200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
