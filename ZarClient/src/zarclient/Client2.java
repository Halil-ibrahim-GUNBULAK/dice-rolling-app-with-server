/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zarclient;

import game.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static zarclient.Client2.sInput;
import game.gameTavla;
import java.util.ArrayList;

class Listen extends Thread {

    public void run() {
        //soket bağlı olduğu sürece dön
        while (Client2.socket.isConnected()) {
            try {
                //mesaj gelmesini bloking olarak dinyelen komut
                Message received = (Message) (sInput.readObject());
                //mesaj gelirse bu satıra geçer
                //mesaj tipine göre yapılacak işlemi ayır.
                switch (received.type) {
                    case Name:
                        break;
                    case RivalConnected:
                        String name = received.content.toString();
                        gameTavla.ThisGame2.txt_rival_name.setText(name);
                        gameTavla.ThisGame2.btn_pick.setEnabled(true);
              //          gameTavla.ThisGame.btn_send_message.setEnabled(true);
                        gameTavla.ThisGame2.tmr_slider2.start();
                        break;
                    case Disconnect:
                        break;
                    case Text:
              //        gameTavla.ThisGame2.txt_receive.setText(received.content.toString());
                        System.out.println("Case Text");
                        break;
                    case Selected:
                        gameTavla.ThisGame2.RivalSelection = (int) received.content;
                        break;
                    case ZarArray:
                        gameTavla.ThisGame2.rakipZarlar[0]= ((int[]) received.content)[0];
                       gameTavla.ThisGame2.rakipZarlar[1]= ((int[]) received.content)[1];
                        break;
                    case Bitis:
                        break;

                }

            } catch (IOException ex) {

                Logger.getLogger(Client2.class.getName()).log(Level.SEVERE, null, ex);
                Client2.Stop();
                break;
            } catch (ClassNotFoundException ex) {
                System.out.println("hata burada");
                Logger.getLogger(Client2.class.getName()).log(Level.SEVERE, null, ex);
                Client2.Stop();
                break;
            }
        }

    }
}

public class Client2{

    //her clientın bir soketi olmalı
    public static Socket socket;

    //verileri almak için gerekli nesne
    public static ObjectInputStream sInput;
    //verileri göndermek için gerekli nesne
    public static ObjectOutputStream sOutput;
    //serverı dinleme thredi 
    public static Listen listenMe;

    public static void Start(String ip, int port) {
        try {
            // Client Soket nesnesi
            Client2.socket = new Socket(ip, port);
            Client2.Display2("Servera bağlandı");
            // input stream
            Client2.sInput = new ObjectInputStream(Client2.socket.getInputStream());
            // output stream
            Client2.sOutput = new ObjectOutputStream(Client2.socket.getOutputStream());
            Client2.listenMe = new Listen();
            Client2.listenMe.start();
            
            //ilk mesaj olarak isim gönderiyorum
            Message msg = new Message(Message.Message_Type.Name);
            msg.content = gameTavla.ThisGame2.txt_name.getText();
            Client2.Send2(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //client durdurma fonksiyonu
    public static void Stop() {
        try {
            if (Client2.socket != null) {
                Client2.listenMe.stop();
                Client2.socket.close();
                Client2.sOutput.flush();
                Client2.sOutput.close();

                Client2.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void Display2(String msg) {

        System.out.println(msg);

    }

    //mesaj gönderme fonksiyonu
    public static void Send2(Message msg) {
        try {
            Client2.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
