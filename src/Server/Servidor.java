package Server;

import java.io.*;
import java.net.*;


public class Servidor {
	//Propiedades del servidor
	static int  port 		= 0;
	static int controller 	= 0;
	static int max			= 0;
	static String hostController="";
    
    public void Dfault(int puerto, int puertoController, int maxConexiones, String hostC) {
        if (puerto == 0) {
            port = 8080;
        }
        if (puertoController == 0) {
            controller = 8081;
        }
        if (maxConexiones == 0) {
            max = 5;
        }
        if(hostC.isEmpty()) {
        	hostController="localhost";
        }
    }

    public void InicializacionServidor(int port, String hostController,int controller, int max) {
    	
    	Dfault(port,controller,max,hostController);
    	    	
    	System.out.println("Parametros:\nPuerto: " + this.port + "\nController: " + this.controller + "\nHost Controler: " + this.hostController + "\nMaxConexiones:" + this.max);
    
        try {
            ServerSocket servidor = new ServerSocket(this.port);
            
            int OpenSockets=0;//Número de sockets abiertos    
            
             while(true){
                if(OpenSockets<this.max){
                    OpenSockets++;
                    Socket cliente= servidor.accept(); 
                    System.out.println("Sirviendo a cliente número: " + OpenSockets);
                    Thread t = new ServidorThread(cliente,this.hostController, this.controller);
                    t.start(); //Iniciamos el Hilo para que pase a controlar como funciona la el servidor 
                    OpenSockets--;
                }
            }

        } catch (Exception ex) {
            System.err.println("Se ha producido un error y no se ha podido abrir el servidor: " + ex);
        }

    }
    
    public static void main(String[] args) {
        Servidor Server = new Servidor();        
        if(args.length == 4) {
        	port=Integer.parseInt(args[0]);
        	controller=Integer.parseInt(args[2]);
        	max=Integer.parseInt(args[3]);
        	hostController=args[1];
        	Server.InicializacionServidor(port,hostController,controller,max);
        }
        
        try {
        	System.out.println("Si querías abrir por parametros, la has liado , la llamada es:\nServer.Servidor <puerto> <hostDelControlador> <puertoControlador> <maximoConexiones> \n \n");
			//Imput del puerto
            System.out.println("Si no es un error, introduce el Puerto (8080 por defecto):");
            BufferedReader dock = new BufferedReader(new InputStreamReader(System.in));
            String readPort = dock.readLine();
            if (!readPort.equals("")) {
                port = Integer.parseInt(readPort);
            }
			else{
				port = 0;
        	}
			
			
			//Imput del host del Controller
            System.out.println("Host del Controller(localhost por defecto)");
            BufferedReader hostControlle = new BufferedReader(new InputStreamReader(System.in));
            String readHost = hostControlle.readLine();
            if (!readHost.equals("")) {
                hostController=readHost;
            }else {
            	hostController="localhost";
			}
            
            //Imput del nº max de peticiones
            System.out.println("Número de conexiones que acepta el servidor (5 por defecto) ");
            BufferedReader maxCon = new BufferedReader(new InputStreamReader(System.in));
            String readMax = maxCon.readLine();
            if (!readMax.equals("")) {
            	max=Integer.parseInt(readMax);
            }else {
            	max=0;
            }
            
            
            //Imput del puerto del controlador
            System.out.println("Número de puerto del controlador (8081 por defecto)");
            BufferedReader ControllerPort = new BufferedReader(new InputStreamReader(System.in));
            String readPort2 = ControllerPort.readLine();
            if (!readPort2.equals("")) {
            	controller = Integer.parseInt(readPort2);
            }else {
            	controller =0;
            }
		}catch(Exception e) {
			 System.err.println("oops , algo ha salido mal: " + e);
		}
        Server.InicializacionServidor(port,hostController, controller, max);
    }

}