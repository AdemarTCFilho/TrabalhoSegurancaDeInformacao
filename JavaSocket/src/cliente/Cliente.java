/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
public class Cliente {

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
            
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        String pass = criptografarSenha();
        //Pega o endereço do localhost.
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
            //estabelece conexão com o servidor socket
            socket = new Socket(host.getHostName(), 9876);
            //escreve para o socket usando o ObjectOutputStream passando como parâmetro o token criptografado
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            oos.writeObject(pass);
            //Lê a resposta do servidor.
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message: " + message);
            //Fecha os recursos.
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
    }

