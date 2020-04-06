package Parking;

import java.rmi.*;

public interface Interfaz extends Remote{
  
    //Getters
    public int generateRandomIntIntRange(int min, int max) throws java.rmi.RemoteException;
    public Integer getVolume() 		 throws java.rmi.RemoteException;
    public Integer getIdentifier()	 throws java.rmi.RemoteException;
    public String getDate() 		 throws java.rmi.RemoteException;
    public String getLastDate() 	 throws java.rmi.RemoteException;
    public String getlight() 		 throws java.rmi.RemoteException;
   
    //Funciones
    public void Default() 			 throws java.rmi.RemoteException;
}
