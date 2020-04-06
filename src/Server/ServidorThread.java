package Server;

import java.nio.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorThread extends Thread {

    private Socket 		cliente;					//Socket con el cliente , en este caso es para el browser 
    private String 		hostController;
    private int 		puertoHilo;
    private Socket 		controlador;


    
    //Constructor del servidor
    public ServidorThread(Socket sk, String hostController, int puertoHilo) {
        this.cliente 		= sk;
        this.hostController = hostController;
        this.puertoHilo 	= puertoHilo;
    }

    //lee la petición del cliente del browser basicamente 
    public String readSocket(Socket sk, String datos) {
        try {
            InputStream aux = sk.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(aux));;
            datos = new String();
            while (in.ready()) {
                datos = datos.concat(in.readLine() + "\n");
                System.out.println(datos);
            }
        } catch (Exception ex) {}
        return datos;
    }
    
    //Escribe en el browser lo que se pida
    public void writeSocket(Socket sk, String datos) {
        try {
        	
            PrintWriter html = new PrintWriter(sk.getOutputStream()); //Convierte en bytes el contenido que se le pasa
            BufferedReader br1;
            String data = "";
            String abrir = "";
            
            //Si el browser pide el error , almacenamos en Abrir el archivo que pide
            if (datos.contains("error")) {
                abrir = "./error.html";
            }
            
            //Si el browser pide el index o no pide nada , le pasamos el index 
            if (datos.equals("/") || datos.equals("index.html")) {
                abrir = "./index.html";
            }
                        
            //Si no coincide con error o index , da error
            if (abrir.equals("./index.html") | abrir.equals("./error.html")) {
            	
                br1 = new BufferedReader(new FileReader(abrir)); //Guardamos el archivo que se abre en br1
                
                if (abrir.equals("./index.html")) {
                	html.println("HTTP/1.1 200 OK");
                    html.println("Content-Type: text/html; charset=utf-8");
                    html.println("Server:");
                } else {
                	html.println("HTTP/1.1 404 Not Found");
                    html.println("Content-Type: text/html; charset=utf-8");
                    html.println("Server: server");
                }
                
                html.println("");
                data = br1.readLine();
                while (data != null) {
                	html.println(data);
                    data = br1.readLine();
                }
            } else {
            	html.println("HTTP/1.1 200 OK");
                html.println("Content-Type: text/html; charset=utf-8");
                html.println("Server: MyHTTPServer");
                html.println("");
                html.println(datos);
                //Si no se han pasado bien los datos, no hace una mierda, por lo tanto ...
            }

            html.flush();
            html.close();

        } catch (Exception ex) {
            System.err.println("No se ha podido escribir en el socket: " + ex);
        }

    }

    //Función para escribir en el controlador una petición, crea un output en UTF que se le envía al controlador
    public void escribeSocket(Socket sk, String datos) {
        try {
            OutputStream aux = sk.getOutputStream();
            DataOutputStream flujo = new DataOutputStream(aux);
            flujo.writeUTF(datos);
        } catch (Exception e) {
            System.out.println("No se ha podido conectar con el controlador: " + e.toString());
        }
    }

    @Override
    public void run() {
    	 String ContenidoSocket="";
         String Response = "";
         try {
             do {
            	
                 ContenidoSocket = readSocket(cliente, ContenidoSocket);
                 StringTokenizer TokenizerContenido = new StringTokenizer(ContenidoSocket);
                 ContenidoSocket = TokenizerContenido.nextToken();
                 if (ContenidoSocket.equals("GET")) {
                	 ContenidoSocket = TokenizerContenido.nextToken();
                     String[] aux = ContenidoSocket.split("/");
                      if (aux.length > 0) {
                    	  ContenidoSocket = aux[1];
                      }
                      if (ContenidoSocket.equals("controladorSD")) {
                    	  try {
                    		  controlador = new Socket(hostController, puertoHilo);
                    	  } catch (Exception ex) {
                    		  System.err.println("No se ha podido conectar con el controlador: " + ex);
                    	  }   
                    	  Response = aux[2];
                    	  escribeSocket(controlador, Response);
                    	  Response = ""; //Vaciamos cadena para rellenarla con el valor 
                    	  String Lectura="";
                    	  while (Lectura.isEmpty()) {
                    		  Lectura = readSocket(controlador, Lectura);
                    	  }
                          ContenidoSocket = Lectura;
                      } 
                      writeSocket(cliente, ContenidoSocket);
                 }
             } while (true);
         } catch (Exception ex) {
             System.err.println("Se ha producido un error y no se ha podido abrir el servidor: " + ex);
         }
         
         try {
             cliente.close();
         } catch (IOException ex1) {
         }
     }
}
