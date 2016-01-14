package serverviewphoto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverviewphoto.messages.ActivationKey;
import serverviewphoto.messages.ScreenShotMessage;

public class ServerViewPhoto {

    public static final int SERVER_PORT = 1793 ;
    public static final int SERVER_MESSAGE_PORT = 1794 ;
    private static ArrayList<String> users = new ArrayList<>();
    private static ArrayList<ServerThreadViewPhoto> serverThreads = new ArrayList<>();
    private InetAddress ipAddress;
    private FileReader keysFile ;
    private boolean isFill = false ;
    
    public static void main(String[] args) {
        System.out.println("Server running...");
        try {
            ServerSocket serversocket = new ServerSocket(SERVER_PORT);
            ServerSocket serverMessageSocket = new ServerSocket(SERVER_MESSAGE_PORT);
            while (true) {
                Socket sock = serversocket.accept();
                Socket messageSock = serverMessageSocket.accept();
                System.out.println("User here");
                ServerThreadViewPhoto newServerThreadViewPhoto = new ServerThreadViewPhoto(sock, messageSock);
                serverThreads.add(newServerThreadViewPhoto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean checkKey(ActivationKey activationKey) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./src/serverviewphoto/Keys.txt"));
            String line;
            while ((line = br.readLine())!= null) {
                String arrey[] = line.split("#");
                if (arrey[0].equals(activationKey.getKey())) {
                    if(arrey[1].equals("0")) {
                        br.close();
                        return true;
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean saveUser(ActivationKey activationKey) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./src/serverviewphoto/Keys.txt"));
            String all = "";
            String line;
            while ((line = br.readLine())!= null) {
                if (line.split("#")[0].equals(activationKey.getKey())) {
                    all += activationKey.getKey() + "#1#" + activationKey.getUserName();
                } else {
                    all += line;
                }
                all += "\n";
            }
            br.close();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./src/serverviewphoto/Keys.txt")));
            out.write(all);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static String getUserFromKey(ActivationKey activationKey) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./src/serverviewphoto/Keys.txt"));
            String line;
            while ((line = br.readLine())!= null) {
                if (line.split("#")[0].equals(activationKey.getKey())) {
                    br.close();
                    return line.split("#")[2];
                } 
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static String addOnlineUser(ActivationKey activationKey) {
        String name = getUserFromKey(activationKey);
        users.add(name);
        return name;
    }
    
    public static ArrayList<String> getOnlineUser() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
             list.add(users.get(i));
        }
        return list;
    }

    public static void removeOnlineUser(String name) {
        users.remove(name);
        for (int i = 0; i < serverThreads.size(); i++) {
            if(serverThreads.get(i).getUserName() != null && serverThreads.get(i).getUserName().equals(name)) {
                serverThreads.remove(i);
            }
        }
    }
    
    public static void sendMessageToUser(ScreenShotMessage screenShotMessage) {
        for (int i = 0; i < serverThreads.size(); i++) {
            if(serverThreads.get(i).getUserName().equals(screenShotMessage.getReciever())) {
                try {
                    serverThreads.get(i).getMessageOut().writeObject(screenShotMessage);
                } catch (IOException ex) {
                    Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
