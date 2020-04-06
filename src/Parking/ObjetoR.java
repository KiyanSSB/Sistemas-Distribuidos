package Parking;

import java.io.*;
import java.rmi.*;
import java.util.*;

import javax.annotation.Generated;

import java.rmi.server.*;
import java.rmi.*;
import jdk.nashorn.api.scripting.URLReader;
import java.util.*;
import java.rmi.*;
import jdk.nashorn.api.scripting.URLReader;
import java.util.*;
public class ObjetoR extends UnicastRemoteObject implements Interfaz, Serializable{

	private static final long serialVersionUID = 1L;
	private Integer volume,identifier;
	private String date,lastDate,light;
	
	
	public ObjetoR (Integer id) throws RemoteException{
		super(); //Necesario para evitar el paso de RMIC
		this.identifier=id;
		try{
			this.Default();
			
			this.exportFichero(); //LLamamos a exportFichero para crear un fichero que contenga los datos de la sonda 
		}catch(Exception e) {
		}
		
	}
	//Random
	@Override
	public  int generateRandomIntIntRange(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	//Getters
	@Override
	public Integer 	getVolume		(){
		return this.volume;
	}
	@Override
	public Integer 	getIdentifier	(){
		return this.identifier;
	}
	@Override
	public String 	getDate			(){
		return this.date;
	}
	@Override
	public String 	getLastDate		(){
		return this.lastDate;
	}	
	@Override
	public String 	getlight		(){
		return this.light;
	}
	
		
	//Establece los valores por defecto de la sonda
	public void Default() {	
		if(light==null) {
			int luz = generateRandomIntIntRange(0,2);
			if(luz==1) {
				this.light="red";
			}else {
				this.light="green";
			}
		}
		
		if(this.date==null) {
			int day    = generateRandomIntIntRange(0, 30);
			int month  = generateRandomIntIntRange(0, 12);
			int year   = generateRandomIntIntRange(2000,2050);
			date =  day + "/" + month + "/"+ year ;
		}
		
		if(this.volume==null) {
			this.volume=generateRandomIntIntRange(0, 100);
		}
		
		
		if(this.lastDate==null) {
			this.lastDate=date;
		}
	}
	

	//Crea un fichero que contiene los datos del de la sonda
	//Valores introducidos son de prueba se podrían generar aleatoriamente para darle mas seriedad
	public void exportFichero() {
		try{
			File fichero= new File("./sonda" + this.identifier + ".txt");
			BufferedWriter escribir = new BufferedWriter(new FileWriter(fichero));
			String content= "volume=" 		+ this.volume +
						    "\ndate=" 		+ "00/00/00 "+ 
					        "\nlight=" 		+ this.light +
							"\nlastDate=" 	+ this.lastDate +
							"\nidentifier=" + this.identifier;
			escribir.write(content);
			escribir.close();
		}catch(Exception e) {
			System.err.println("El fichero no se ha creado ");
		}
	}
		
		
	//Lee un fichero y se crea una plaza del parking con sus datos etc
	public void importFichero(){
		 try{
	            File fichero = new File("./sonda" + this.getIdentifier() + ".txt");
	            BufferedReader buffer = new BufferedReader(new FileReader(fichero));
	            String linea = buffer.readLine();
	            while(linea!=null){
	                StringTokenizer variable = new StringTokenizer(linea, "=");
	                variable.nextToken();
	                if(variable.countTokens()!=0){
	                    if(linea.contains("volume")){
	                        this.volume = Integer.parseInt(variable.nextToken());
	                    }
	                    else if(linea.contains("date")){
	                        this.lastDate = variable.nextToken();
	                    }
	                    else if(linea.contains("light")){
	                        this.light = variable.nextToken();
	                    }
	                }
	                linea = buffer.readLine();
	            }
	            buffer.close();
	        } catch(FileNotFoundException nf){
	        	System.out.println("No se ha podido completar la creación de fichero: sonda" + this.identifier + ".txt");
	        } catch(Exception e){
	            System.out.println("Error en objetoRemoto: " + e.toString());
	        }
		 	//Colocamos los valores por si acaso
	        this.Default();
	}
		
}
		