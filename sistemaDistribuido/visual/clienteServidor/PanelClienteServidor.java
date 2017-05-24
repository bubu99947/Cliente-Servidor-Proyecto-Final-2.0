package sistemaDistribuido.visual.clienteServidor;

import java.awt.Panel;
import java.awt.Button;
import java.awt.event.ActionListener;

public class PanelClienteServidor extends Panel{
  private static final long serialVersionUID=1;
  private Button botonClienteNestor,botonServidorNestor,botonClientePaco,botonServidorPaco;
  private Panel panelNestor,panelPaco;

  public PanelClienteServidor(){
	 panelNestor = new Panel();
	 panelPaco = new Panel();
     botonClienteNestor=new Button("Cliente Nestor");
     botonServidorNestor=new Button("Servidor Nestor");
     botonClientePaco = new Button("Cliente Paco");
     botonServidorPaco = new Button("Servidor Paco");
     
     
     panelNestor.add(botonClienteNestor);
     panelNestor.add(botonServidorNestor);
     panelPaco.add(botonClientePaco);
     panelPaco.add(botonServidorPaco);
     add(panelNestor);
     add(panelPaco);
     
  }
  
 
  public void agregarActionListener(ActionListener al){
    botonClienteNestor.addActionListener(al);
    botonServidorNestor.addActionListener(al);
    botonClientePaco.addActionListener(al);
    botonServidorPaco.addActionListener(al);
  }
}