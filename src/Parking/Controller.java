package Parking;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.rmi.*;
import java.util.*;



import java.nio.file.*;

public class Controller {

	//Para peticiones HTTP , lectura de socket y escritura en socket
    public String 	leeSocket(Socket sk, String p_Datos) {
        try {
            InputStream aux = sk.getInputStream();
            DataInputStream flujo = new DataInputStream(aux);
            p_Datos = new String();
            p_Datos = flujo.readUTF();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return p_Datos;
    }
    public void escribeSocket(Socket sk, String p_Datos) {
        try {
            PrintWriter out= new PrintWriter(sk.getOutputStream());
            out.println(p_Datos);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return;
    }

    
    public String ServerRequest(String host, String port, String request,String RMIserv) {
        Interfaz objetoR = null;
        String RespuestaForServer = "";						//Respuesta
        String display = "";								//Pagina que queremos consultar 
        String objetoRMI = "ObjetoR";						//Nombre del objetoRMI registrado de la Interfaz *IMPORTANTE QUE COINCIDA*
        String check="";
        String inicio="./inicio.txt";
        String NombresObjetos[];
        
        //Filtrado de la petición, dado que no funcionaba el set , simplemente saltamos cogiendo lo que queremos 
        try {
        	StringTokenizer s = new StringTokenizer(request, "?");
            display = s.nextToken();
            check = s.nextToken();
            if(!check.contains("sonda")){
                return "error";
            }
            s = new StringTokenizer(check, "=");		
            s.nextToken();
            objetoRMI = objetoRMI.concat(s.nextToken());
            s.nextToken();					
            s = new StringTokenizer(objetoRMI, "&");
            objetoRMI = s.nextToken();
            String objectRMI = RMIserv.concat("/" + objetoRMI);
        } catch (Exception ex) {
            System.err.println("Error en el tokenizado de la cadena HTTP recibida");
        }

 
        try {
           
           
        	
        	
        	//abrimos lo de seguridad del RMI
            System.setSecurityManager(new SecurityManager());
            
            NombresObjetos = Naming.list(RMIserv);
            
            //Si dentro del controlador SD se nos pide una pagina http 
            if (display.equals("index")) {
                RespuestaForServer = new String (Files.readAllBytes(Paths.get(inicio))); //Leemos del fichero el contenido html 
                for (int i = 0; i < NombresObjetos.length; i++) {
                    i=i+1;
                    RespuestaForServer = RespuestaForServer.concat("<br><a href=\"/controladorSD/all?sonda=" + i + "\" post >sonda " + i + "</a> \n");
                    i=i-1;
                }
                //Cerramos la pagina html y la devolvemos
                RespuestaForServer = RespuestaForServer.concat("</body> \n </html>\n");
                return RespuestaForServer;
            } else {
                objetoR = (Interfaz) Naming.lookup(objetoRMI);
            }
        } catch (Exception ex) {
            System.out.println("Error con los objetos, seguramente, la excepción es: " + ex );
            RespuestaForServer="error";
        }
         
        
        switch(display){
            case "all":
                try{
                    String all="<!DOCTYPE html>\n<html> \n"+ "    <head>\n"     
                            + "        <title>Parking "+objetoR.getIdentifier()+"</title>\n"
                            + "    </head>\n"
                            + "    <body style=\"background-color:LAVENDER\">\n"
                            + "        <h1>Sonda "+objetoR.getIdentifier()+"</h1>\n"
                            + "        <br><a href=\"/controladorSD/date?sonda=Date"				+objetoR.getIdentifier()+		"\">Date: </a>"				+ objetoR.getDate()
                            + "        <br><a href=\"/controladorSD/lastDate?sonda=LastDate"		+objetoR.getIdentifier()+		"\">Last Date: </a>"		+ objetoR.getLastDate()
                            + "        <br><a href=\"/controladorSD/light?sonda=Light"				+objetoR.getIdentifier()+		"\">Light: </a>"			+ objetoR.getlight()
                            + "        <br><a href=\"/controladorSD/volume?sonda=Volume"			+objetoR.getIdentifier()+		"\">Volume: </a>"			+ String.valueOf(objetoR.getVolume())
                            + "        "
                            + "<a href=\"/controladorSD/index\">Sondas</a>\n"                          
                            + "</body> \n </html>\n";
                	RespuestaForServer = all;	
                }
                catch(Exception ex){
                    RespuestaForServer="error";
                    System.err.println("La sonda no se encuentra, la excepción es: "+ex);
                }
                break;
            default:
             
                break;
        }
        return RespuestaForServer;
    }

    public static void main(String[] args) {
        String operacionSolicitada="",RespuestaController="";
        
        
    	//Solo puede arrancarse mediante parametros
        if (args.length != 3) {
        	System.out.println("Llamada a ejecución incorrecta, el formato es: java -Djava.security.policy=registrar.policy Parking.Controller <hostRMI> <puertoRMI> <puertoHTTP>");
            return;
        }

        String RMIserver = "rmi://" + args[0] + ":" + args[1]; //Servidor RMI
        
        System.out.println("El argumento 0 es: " + args[0] + "\n")
        
        while (true) {
            try {
            	Controller controller = new Controller();
                ServerSocket skServidor = new ServerSocket(Integer.parseInt(args[2]));
                System.out.println("Controlador Preparado");
                
                while(true) {
                    Socket skCliente = skServidor.accept();
                    operacionSolicitada = controller.leeSocket(skCliente, operacionSolicitada);
                    RespuestaController = controller.ServerRequest(args[0], args[1], operacionSolicitada,RMIserver);
                    controller.escribeSocket(skCliente, RespuestaController);
                    skCliente.close();
                    
                }
            } catch (Exception e) {
                System.out.println("Algo ha salido mal en el main, el error producido es el siguiente: " + e.toString());
            }
        }
    }
}
