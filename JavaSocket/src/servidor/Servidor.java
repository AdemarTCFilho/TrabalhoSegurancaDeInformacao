/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

/**
 *
 * @author Manoel
 */
public class Servidor {
    private static String criptografarSenha()
{
        //Obtem a data e hora do computador no formato definido.
            DateFormat data = new SimpleDateFormat("MM-yyyy-dd mm:HH");
            Date hoje = Calendar.getInstance().getTime();
            String password = data.format(hoje);
    // Cria a variável sha1 como string e atribui o valor vazio nela. 
    String sha1 = "";
    try
    {
        //Try para criptografar a data como SHA-1.
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes());
        sha1 = byteToHex(crypt.digest());
    }
    catch(NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
    // Retorna a variável sha1 populada com a data e criptografada.
    return sha1;
}

private static String byteToHex(final byte[] hash)
{
    Formatter formatter = new Formatter();
    for (byte b : hash)
    {
        formatter.format("%02x", b);
    }
    String result = formatter.toString();
    formatter.close();
    return result;
}   
    //Variável static ServerSocket
    private static ServerSocket server;
    //A porta do servidor do socket
    private static int port = 9876;
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        String token = criptografarSenha();
        //Cria o objeto ServerSocket
        server = new ServerSocket(port);
        //Continua escutando até o cliente digitar 'exit'
        while(true){
            System.out.println("Esperando comunicação com o Cliente...");
            //Criando socket e esperando a conexão do cliente
            Socket socket = server.accept();
            //Lê do socket através do ObjectInputStream
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //Convert o ObjectInputStream em uma String
            String message = (String) ois.readObject();
            //Compara a mensagem ao token recebido do cliente
            if(message.equals(token)){
            System.out.println("Message Received: " + message);
            //Cria um objeto ObjectOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //Escreve o objeto para o socket
            oos.writeObject("Hi Client "+message);
            //fecha recursos.
            ois.close();
            oos.close();
            socket.close();
            //Se a mensagem não for igual ao token ele fecha a conexão
            } else {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject("Não Autorizado");
                        break;
            }
            //Fecha o server se o cliente digitar 'exit'
            if(message.equalsIgnoreCase("exit")) break;
        
        }
        System.out.println("Shutting down Socket server!!");
        //Fecha o Socket do servidor
        server.close();
    }
     
}