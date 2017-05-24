package sistemaDistribuido.util.paquetes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.scene.chart.PieChart.Data;
import sistemaDistribuido.util.MaquinaProcesoClass;



public class EnviarPaquete {
	DatagramSocket ds;
	int puertoRecepcion;
	
	public EnviarPaquete(DatagramSocket ds,int puertoRecepcion) {
		// TODO Auto-generated constructor stub
		
		this.ds=ds;
		this.puertoRecepcion=puertoRecepcion;
	}
	
	public void EnviaLSA(int numeroServicio){
		
		byte[] msj = new byte[1024];
		
		msj[9] = (byte)-3;
		msj[8] = (byte) numeroServicio;
		
		System.out.println(numeroServicio);
		
		
		try {
			
			DatagramPacket dp = new DatagramPacket(msj,msj.length,InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()),puertoRecepcion);
			
			ds.send(dp);
		} 
		catch(SocketException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}catch(UnknownHostException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}catch(IOException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}
		
	}
	
	
	public void EnviaAU(MaquinaProcesoClass pmp,byte[] mensaje){
		

		byte[] msj = new byte[1024];
		System.arraycopy(mensaje, 0, msj, 0, 1024);
		
		
		msj[9] = (byte)-1;
		
		try {
			
			DatagramPacket dp = new DatagramPacket(msj,msj.length,InetAddress.getByName(pmp.dameIP()),puertoRecepcion);
			
			ds.send(dp);
		} 
		catch(SocketException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}catch(UnknownHostException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}catch(IOException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}
		
	}

	public void EnviaTA(MaquinaProcesoClass pmp,byte[] mensaje){
	
		byte[] msj = new byte[1024];
		System.arraycopy(mensaje, 0, msj, 0, 1024);
		
		msj[9] = (byte)-2;
			
		try {
			
			DatagramPacket dp = new DatagramPacket(msj,msj.length,InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()),puertoRecepcion);
			
			ds.send(dp);
		} 
		catch(SocketException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}catch(UnknownHostException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}catch(IOException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}

	}
	
	
	public void EnviaFSA(int idProceso, int numeroServicio){
		byte[] msj = new byte[1024];
		
		msj[9] = (byte)-4;
		msj[10] = (byte)numeroServicio;
		msj[11] = (byte)idProceso;
			
		try {
			
			DatagramPacket dp = new DatagramPacket(msj,msj.length,InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()),puertoRecepcion);
			
			ds.send(dp);
		} 
		catch(SocketException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}catch(UnknownHostException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}catch(IOException ex){
			System.out.println("Error iniciando socket: "+ex.getMessage());
		}
		
	}
}
