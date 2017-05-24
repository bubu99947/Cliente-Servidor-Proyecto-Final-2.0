/* ******************************
 * Nestor Ubaldo Gonzalez Alcala*
 * D03                          *
 * Practica 3                   *
 * *****************************/

/* NOTA 1: El separador es *
 * NOTA 2: Tambien incluyo los puntos extras de la practica 1
 * NOTA 3: La extension ".txt" se agrega automaticamente al archivo
 * */

package sistemaDistribuido.visual.rpc;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.rpc.modoUsuario.ProcesoCliente;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;


import java.awt.Panel;
import java.awt.TextField;
import java.awt.Button;
import java.awt.Label;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClienteFrame extends ProcesoFrame{
	private static final long serialVersionUID=1;
	private ProcesoCliente proc;
	private TextField campo1,campo2,campo3,campo4;
	private Button botonSolicitud;
	private CompruebaEstadoCliente habilitaBotonSolicitud;

	public ClienteFrame(RPCFrame frameNucleo){
		super(frameNucleo,"Cliente de Archivos");
		add("South",construirPanelSolicitud());
		validate();
		proc=new ProcesoCliente(this);
		fijarProceso(proc);
		habilitaBotonSolicitud = new CompruebaEstadoCliente();
		habilitaBotonSolicitud.start();
	}

	public Panel construirPanelSolicitud(){
		Panel pSolicitud,pcodop1,pcodop2,pcodop3,pcodop4,pboton;
		pSolicitud=new Panel();
		pcodop1=new Panel();
		pcodop2=new Panel();
		pcodop3=new Panel();
		pcodop4=new Panel();
		pboton=new Panel();
		campo1=new TextField(10);
		campo2=new TextField(10);
		campo3=new TextField(10);
		campo4=new TextField(10);
		pSolicitud.setLayout(new GridLayout(5,1));

		pcodop1.add(new Label("Raiz cuadrada >> "));
		pcodop1.add(new Label("1 parametro"));
		pcodop1.add(campo1);

		pcodop2.add(new Label("Ordenamiento (Burbuja) >> "));
		pcodop2.add(new Label("n parametros"));
		pcodop2.add(campo2);

		pcodop3.add(new Label("Sumatoria"));
		pcodop3.add(new Label("n parametros"));
		pcodop3.add(campo3);

		pcodop4.add(new Label("Media"));
		pcodop4.add(new Label("n parametros"));
		pcodop4.add(campo4);

		botonSolicitud=new Button("Solicitar");
		pboton.add(botonSolicitud);
		botonSolicitud.addActionListener(new ManejadorSolicitud());

		pSolicitud.add(pcodop1);
		pSolicitud.add(pcodop2);
		pSolicitud.add(pcodop3);
		pSolicitud.add(pcodop4);
		pSolicitud.add(pboton);

		return pSolicitud;
	}

	class ManejadorSolicitud implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String com=e.getActionCommand();
			if (com.equals("Solicitar")){
				botonSolicitud.setEnabled(false);
				//proc.guardaSolicitud(,mensaje);
				proc.realizarOperaciones(campo1.getText(),campo2.getText(),campo3.getText(),campo4.getText());
				Nucleo.reanudarProceso(proc);
		
				
			}
		}
	}
	class CompruebaEstadoCliente extends Thread{
		public void run(){		
			while(true){
				if(proc.getState()==Thread.State.WAITING)
					
					{botonSolicitud.setEnabled(true);
					
					}
					
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
