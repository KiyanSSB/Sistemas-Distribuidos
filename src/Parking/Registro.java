package Parking;

import java.rmi.*;
import jdk.nashorn.api.scripting.URLReader;
import java.util.*;
public class Registro {
	

	public static int generateRandomIntIntRange(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	 public static void main(String args[]) {
		 	String URL;
		 	int numeroSondasRand=generateRandomIntIntRange(2, 10);
		 	
		 		try {
		 			System.setSecurityManager(new RMISecurityManager());
		 			Integer Party=Naming.list("rmi://localhost:1099").length;
		 			for(Integer i=0 ; i< Party ; i++) {
		 				URL= "/ObjetoR"+(Party-i);
		 			}
		 			for(Integer j=1; j<numeroSondasRand ; j++) { //Creamos sondas aleatoriamente para comprobar que funciona
		 				ObjetoR objR = new ObjetoR(j); //Creamos los objetos remotos
		 				URL="/ObjetoR"+j;
		 				Naming.rebind(URL, objR);
		 				System.out.println("Objeto remoto y su servidor preparados");
		 			}
		 		}catch(Exception e) {
		 			System.out.println(e);
		 		}
	 }
}

