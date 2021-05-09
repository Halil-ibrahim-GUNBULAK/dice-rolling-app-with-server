/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import static game.gameTavla.ThisGame2;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zarclient.Client2;

/**
 *
 * @author lenovo
 */
public class gameTavla extends javax.swing.JFrame {

    /**
     * Creates new form gameTavla
     */
    public static gameTavla ThisGame2;
    //ekrandaki resim değişimi için timer yerine thread
    public Thread tmr_slider2;
    public Thread tmr_slider3;
    //karşı tarafın seçimi seçim -1 deyse seçilmemiş
    public int RivalSelection = -1;
    public int myTurn;
    public int RivalTurn;
    public int zar1 = -1;
    public int zar2 = -1;
    public boolean playZar1=true;
    public boolean playZar2=true;
    public int temp;
    public static int rakipZarlar[] = new int[2];
    public static String player1Color="";
    public boolean finish = false;
    //benim seçimim seçim -1 deyse seçilmemiş
    public int myselection = -1;
    //icon dizileri
    ImageIcon icons_right[];
    ImageIcon icons_left[];
    ImageIcon icons[];
    ImageIcon zarlar[];
    public static int panelNum;
    Random rand;
    ImageIcon boardImage;
    ArrayList<JButton> newAr = new ArrayList<JButton>();
    ArrayList<JButton> newAr2 = new ArrayList<JButton>();
    ArrayList<JPanel> PanelArr = new ArrayList<JPanel>();

   

    public gameTavla() {
        initComponents();
        ThisGame2 = this;
        rakipZarlar[0] = -1;
        rakipZarlar[1] = -1;
        rand = new Random();

        lbl_gamer1.setEnabled(false);
        lbl_gamer2.setEnabled(false);
        zarAt.setEnabled(false);
        devamEt.setEnabled(false);
      //  newAr.add(Tas3);
       // newAr.add(Bt1);
        //newAr2.add(jButton4);
        try {
            boardImage = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/board.png")))
                    .getImage().getScaledInstance(300, 700, Image.SCALE_DEFAULT));
            zarlar = new ImageIcon[6];
            zarlar[0] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/1.png")))
                    .getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            zarlar[1] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/2.png")))
                    .getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            zarlar[2] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/3.png")))
                    .getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            zarlar[3] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/4.png")))
                    .getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            zarlar[4] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/5.png")))
                    .getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            zarlar[5] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/6.png")))
                    .getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));

            icons = new ImageIcon[5];
            icons[4] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/hosgeldin.png"))).getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            icons[0] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/wait.png"))).getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            icons[1] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/lose.png"))).getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            icons[2] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/win.png"))).getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            icons[3] = new ImageIcon(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("game/images/tie.png"))).getImage().getScaledInstance(lbl_gamer2.getWidth(), lbl_gamer2.getHeight(), Image.SCALE_DEFAULT));
            AddArrayToPanel();
            PaintWhiteAndButtons();
            AddPanelToButton();
            
        } catch (IOException ex) {
            Logger.getLogger(gameTavla.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        lbl_gamer1.setIcon(icons[0]);
        
        lbl_gamer2.setIcon(icons[0]);
         lbl_gamer1.setEnabled(true);
          lbl_gamer2.setEnabled(true);
        // resimleri döndürmek için tread aynı zamanda oyun bitiminide takip ediyor
        tmr_slider2 = new Thread(() -> {
            //soket bağlıysa dönsün
            while (Client2.socket.isConnected()) {
                try {
                    //
                    Thread.sleep(100);
                    System.out.println("Rival Selection=" + RivalSelection + " mySelection =" + myselection);
                    //eğer ikisinden biri -1 ise resim dönmeye devam etsin sonucu göstermesin
                    if (RivalSelection == -1 || myselection == -1) {
                        //buraya wait rakip gelicek
                        
                        lbl_gamer2.setIcon(icons[0]);
                    }// eğer iki seçim yapılmışsa sonuç gösterilebilir.  
                    else {
                        Thread.sleep(100);
                        System.out.println("Rival Selection=" + RivalSelection + " mySelection =" + myselection);
                        lbl_gamer2.setIcon(zarlar[RivalSelection - 1]);
                        //sonuç el olarak gösterildikten 4 saniye sonra smiley gelsin
                        Thread.sleep(4000);
                        //smiley sonuç resimleri

                        if (myselection > RivalSelection) {
                            
                            lbl_gamer1.setIcon(icons[2]);
                            lbl_gamer2.setIcon(icons[1]);
                            player1Color="white";
                            PlayerRenk.setBackground(Color.white);
                            PlayerRenk2.setBackground(Color.black);
                            Thread.sleep(4000);
                            zarAt.setEnabled(true);
                            while (!finish) {
                                Thread.sleep(1000);
                                System.out.println("Bu kısma giriyor1");
                                if (rakipZarlar[0] == -1 && zar1 == -1) {
                                    //buraya wait rakip gelicek

                                    lbl_gamer2.setIcon(icons[0]);
                                    lbl_gamer1.setIcon(icons[0]);
                                } else if (rakipZarlar[0] == -1 && zar1 != -1) {
                                    //buraya wait rakip gelicek
                                    lbl_gamer1.setIcon(zarlar[zar1 - 1]);
                                    lbl_gamer2.setIcon(zarlar[zar2 - 1]);

                                }// eğer iki seçim yapılmışsa sonuç gösterilebilir.  
                                else if (rakipZarlar[0] != -1 && zar1 == -1) {
                                    Thread.sleep(100);
                                    System.out.println("RakipZar1=" + rakipZarlar[0] + " RakipZar2 =" + rakipZarlar[1]);
                                    lbl_gamer1.setIcon(zarlar[rakipZarlar[0] - 1]);
                                    lbl_gamer2.setIcon(zarlar[rakipZarlar[1] - 1]);
                                    //sonuç el olarak gösterildikten 4 saniye sonra smiley gelsin

                                    //smiley sonuç resimleri
                                } else {
                                    lbl_gamer1.setIcon(icons[0]);
                                    lbl_gamer2.setIcon(icons[0]);
                                }

                            }

                        } else if (myselection < RivalSelection) {
                            lbl_gamer1.setIcon(icons[1]);
                            lbl_gamer2.setIcon(icons[2]);
                             player1Color="black";
                            PlayerRenk2.setBackground(Color.white);
                            PlayerRenk.setBackground(Color.black);
                            while (!finish) {
                                Thread.sleep(1000);
                                System.out.println("rakipzarlar0= " + rakipZarlar[0] + "zarlar1=" + zar1
                                );
                                if (rakipZarlar[0] == -1 && zar1 == -1) {
                                    //buraya wait rakip gelicek

                                    lbl_gamer2.setIcon(icons[0]);
                                    lbl_gamer1.setIcon(icons[0]);
                                } else if (rakipZarlar[0] == -1 && zar1 != -1) {
                                    //buraya wait rakip gelicek
                                    lbl_gamer1.setIcon(zarlar[zar1 - 1]);
                                    lbl_gamer2.setIcon(zarlar[zar2 - 1]);

                                }// eğer iki seçim yapılmışsa sonuç gösterilebilir.  
                                else if (rakipZarlar[0] != -1 && zar1 == -1) {
                                    Thread.sleep(100);
                                    System.out.println("RakipZar1=" + rakipZarlar[0] + " RakipZar2 =" + rakipZarlar[1]);
                                    lbl_gamer1.setIcon(zarlar[rakipZarlar[0] - 1]);
                                    lbl_gamer2.setIcon(zarlar[rakipZarlar[1] - 1]);
                                    //sonuç el olarak gösterildikten 4 saniye sonra smiley gelsin

                                    //smiley sonuç resimleri
                                } else {
                                    lbl_gamer1.setIcon(icons[0]);
                                    lbl_gamer2.setIcon(icons[0]);
                                   
                                }

                            }
                            Thread.sleep(4000);

                        } else {
                          
                            btn_pick.setEnabled(true);
                            lbl_gamer2.setIcon(icons[3]);
                            lbl_gamer2.setIcon(icons[3]);
                            Thread.sleep(4000);
                            //tekrar pick çalıştır

                        }
                       
                        tmr_slider2.stop();

                        //7 saniye sonra oyun bitsin tekrar bağlansın
                        Thread.sleep(7000);

                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(gameTavla.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Panel13 = new javax.swing.JPanel();
        St1 = new javax.swing.JButton();
        St3 = new javax.swing.JButton();
        St5 = new javax.swing.JButton();
        St2 = new javax.swing.JButton();
        St9 = new javax.swing.JButton();
        Panel14 = new javax.swing.JPanel();
        Panel15 = new javax.swing.JPanel();
        Panel16 = new javax.swing.JPanel();
        Panel18 = new javax.swing.JPanel();
        orta = new javax.swing.JPanel();
        Panel20 = new javax.swing.JPanel();
        Panel21 = new javax.swing.JPanel();
        Panel22 = new javax.swing.JPanel();
        Panel23 = new javax.swing.JPanel();
        Panel24 = new javax.swing.JPanel();
        St6 = new javax.swing.JButton();
        St11 = new javax.swing.JButton();
        Panel2 = new javax.swing.JPanel();
        Panel4 = new javax.swing.JPanel();
        Panel3 = new javax.swing.JPanel();
        Panel5 = new javax.swing.JPanel();
        Panel6 = new javax.swing.JPanel();
        St12 = new javax.swing.JButton();
        St13 = new javax.swing.JButton();
        St14 = new javax.swing.JButton();
        St15 = new javax.swing.JButton();
        St4 = new javax.swing.JButton();
        Panel1 = new javax.swing.JPanel();
        Bt14 = new javax.swing.JButton();
        Bt15 = new javax.swing.JButton();
        Panel7 = new javax.swing.JPanel();
        Panel9 = new javax.swing.JPanel();
        Panel10 = new javax.swing.JPanel();
        Panel11 = new javax.swing.JPanel();
        Panel12 = new javax.swing.JPanel();
        Bt5 = new javax.swing.JButton();
        Bt2 = new javax.swing.JButton();
        Bt1 = new javax.swing.JButton();
        Bt3 = new javax.swing.JButton();
        Bt4 = new javax.swing.JButton();
        Panel17 = new javax.swing.JPanel();
        Bt7 = new javax.swing.JButton();
        Bt8 = new javax.swing.JButton();
        Bt10 = new javax.swing.JButton();
        Panel8 = new javax.swing.JPanel();
        St7 = new javax.swing.JButton();
        St8 = new javax.swing.JButton();
        St10 = new javax.swing.JButton();
        Panel19 = new javax.swing.JPanel();
        Bt6 = new javax.swing.JButton();
        Bt9 = new javax.swing.JButton();
        Bt11 = new javax.swing.JButton();
        Bt12 = new javax.swing.JButton();
        Bt13 = new javax.swing.JButton();
        orta3 = new javax.swing.JPanel();
        orta1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        devamEt = new javax.swing.JButton();
        zarAt = new javax.swing.JButton();
        lbl_gamer1 = new javax.swing.JLabel();
        lbl_gamer2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btn_pick = new javax.swing.JButton();
        txt_rival_name = new javax.swing.JTextField();
        btn_connect = new javax.swing.JButton();
        txt_name = new javax.swing.JTextField();
        PlayerRenk = new javax.swing.JPanel();
        PlayerRenk2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(getIconImage());

        Panel13.setToolTipText("");
        Panel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel13MouseClicked(evt);
            }
        });

        St1.setBackground(new java.awt.Color(255, 153, 153));
        St1.setForeground(new java.awt.Color(255, 255, 255));
        St1.setPreferredSize(new java.awt.Dimension(75, 75));
        St1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St1ActionPerformed(evt);
            }
        });

        St3.setBackground(new java.awt.Color(255, 153, 153));
        St3.setForeground(new java.awt.Color(255, 255, 255));
        St3.setPreferredSize(new java.awt.Dimension(75, 75));
        St3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St3ActionPerformed(evt);
            }
        });

        St5.setBackground(new java.awt.Color(255, 153, 153));
        St5.setForeground(new java.awt.Color(255, 255, 255));
        St5.setPreferredSize(new java.awt.Dimension(75, 75));
        St5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St5ActionPerformed(evt);
            }
        });

        St2.setBackground(new java.awt.Color(255, 153, 153));
        St2.setForeground(new java.awt.Color(255, 255, 255));
        St2.setPreferredSize(new java.awt.Dimension(75, 75));
        St2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St2ActionPerformed(evt);
            }
        });

        St9.setBackground(new java.awt.Color(255, 153, 153));
        St9.setForeground(new java.awt.Color(255, 255, 255));
        St9.setPreferredSize(new java.awt.Dimension(75, 75));
        St9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel13Layout = new javax.swing.GroupLayout(Panel13);
        Panel13.setLayout(Panel13Layout);
        Panel13Layout.setHorizontalGroup(
            Panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel13Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(Panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(St9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        Panel13Layout.setVerticalGroup(
            Panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel13Layout.createSequentialGroup()
                .addComponent(St2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(St1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(St5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(St3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(St9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel14.setName(""); // NOI18N
        Panel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel14MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel14Layout = new javax.swing.GroupLayout(Panel14);
        Panel14.setLayout(Panel14Layout);
        Panel14Layout.setHorizontalGroup(
            Panel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        Panel14Layout.setVerticalGroup(
            Panel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel15.setName(""); // NOI18N
        Panel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel15MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel15Layout = new javax.swing.GroupLayout(Panel15);
        Panel15.setLayout(Panel15Layout);
        Panel15Layout.setHorizontalGroup(
            Panel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        Panel15Layout.setVerticalGroup(
            Panel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel16.setName(""); // NOI18N
        Panel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel16Layout = new javax.swing.GroupLayout(Panel16);
        Panel16.setLayout(Panel16Layout);
        Panel16Layout.setHorizontalGroup(
            Panel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 111, Short.MAX_VALUE)
        );
        Panel16Layout.setVerticalGroup(
            Panel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel18.setName(""); // NOI18N
        Panel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel18MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel18Layout = new javax.swing.GroupLayout(Panel18);
        Panel18.setLayout(Panel18Layout);
        Panel18Layout.setHorizontalGroup(
            Panel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        Panel18Layout.setVerticalGroup(
            Panel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        orta.setBackground(new java.awt.Color(153, 0, 0));
        orta.setName(""); // NOI18N

        javax.swing.GroupLayout ortaLayout = new javax.swing.GroupLayout(orta);
        orta.setLayout(ortaLayout);
        ortaLayout.setHorizontalGroup(
            ortaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 93, Short.MAX_VALUE)
        );
        ortaLayout.setVerticalGroup(
            ortaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel20.setName(""); // NOI18N
        Panel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel20MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel20Layout = new javax.swing.GroupLayout(Panel20);
        Panel20.setLayout(Panel20Layout);
        Panel20Layout.setHorizontalGroup(
            Panel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        Panel20Layout.setVerticalGroup(
            Panel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel21.setName(""); // NOI18N
        Panel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel21MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel21Layout = new javax.swing.GroupLayout(Panel21);
        Panel21.setLayout(Panel21Layout);
        Panel21Layout.setHorizontalGroup(
            Panel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Panel21Layout.setVerticalGroup(
            Panel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel22.setName(""); // NOI18N
        Panel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel22MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel22Layout = new javax.swing.GroupLayout(Panel22);
        Panel22.setLayout(Panel22Layout);
        Panel22Layout.setHorizontalGroup(
            Panel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Panel22Layout.setVerticalGroup(
            Panel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel23.setName(""); // NOI18N
        Panel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel23MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel23Layout = new javax.swing.GroupLayout(Panel23);
        Panel23.setLayout(Panel23Layout);
        Panel23Layout.setHorizontalGroup(
            Panel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        Panel23Layout.setVerticalGroup(
            Panel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel24.setName(""); // NOI18N
        Panel24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel24MouseClicked(evt);
            }
        });

        St6.setBackground(new java.awt.Color(255, 153, 153));
        St6.setForeground(new java.awt.Color(255, 255, 255));
        St6.setPreferredSize(new java.awt.Dimension(75, 75));
        St6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St6ActionPerformed(evt);
            }
        });

        St11.setBackground(new java.awt.Color(255, 153, 153));
        St11.setForeground(new java.awt.Color(255, 255, 255));
        St11.setPreferredSize(new java.awt.Dimension(75, 75));
        St11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel24Layout = new javax.swing.GroupLayout(Panel24);
        Panel24.setLayout(Panel24Layout);
        Panel24Layout.setHorizontalGroup(
            Panel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(St6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel24Layout.setVerticalGroup(
            Panel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(St6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(St11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel2.setToolTipText("");
        Panel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel2Layout = new javax.swing.GroupLayout(Panel2);
        Panel2.setLayout(Panel2Layout);
        Panel2Layout.setHorizontalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Panel2Layout.setVerticalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel4.setToolTipText("");
        Panel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel4Layout = new javax.swing.GroupLayout(Panel4);
        Panel4.setLayout(Panel4Layout);
        Panel4Layout.setHorizontalGroup(
            Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        Panel4Layout.setVerticalGroup(
            Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel3.setToolTipText("");
        Panel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel3Layout = new javax.swing.GroupLayout(Panel3);
        Panel3.setLayout(Panel3Layout);
        Panel3Layout.setHorizontalGroup(
            Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        Panel3Layout.setVerticalGroup(
            Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel5.setToolTipText("");
        Panel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel5Layout = new javax.swing.GroupLayout(Panel5);
        Panel5.setLayout(Panel5Layout);
        Panel5Layout.setHorizontalGroup(
            Panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Panel5Layout.setVerticalGroup(
            Panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel6.setToolTipText("");
        Panel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel6MouseClicked(evt);
            }
        });

        St12.setBackground(new java.awt.Color(255, 153, 153));
        St12.setForeground(new java.awt.Color(255, 255, 255));
        St12.setPreferredSize(new java.awt.Dimension(75, 75));
        St12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St12ActionPerformed(evt);
            }
        });

        St13.setBackground(new java.awt.Color(255, 153, 153));
        St13.setForeground(new java.awt.Color(255, 255, 255));
        St13.setPreferredSize(new java.awt.Dimension(75, 75));
        St13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St13ActionPerformed(evt);
            }
        });

        St14.setBackground(new java.awt.Color(255, 153, 153));
        St14.setForeground(new java.awt.Color(255, 255, 255));
        St14.setPreferredSize(new java.awt.Dimension(75, 75));
        St14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St14ActionPerformed(evt);
            }
        });

        St15.setBackground(new java.awt.Color(255, 153, 153));
        St15.setForeground(new java.awt.Color(255, 255, 255));
        St15.setPreferredSize(new java.awt.Dimension(75, 75));
        St15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St15ActionPerformed(evt);
            }
        });

        St4.setBackground(new java.awt.Color(255, 153, 153));
        St4.setForeground(new java.awt.Color(255, 255, 255));
        St4.setPreferredSize(new java.awt.Dimension(75, 75));
        St4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel6Layout = new javax.swing.GroupLayout(Panel6);
        Panel6.setLayout(Panel6Layout);
        Panel6Layout.setHorizontalGroup(
            Panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(St12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        Panel6Layout.setVerticalGroup(
            Panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel6Layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addComponent(St4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(St15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(St14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(St13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(St12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Panel1.setToolTipText("");
        Panel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel1MouseClicked(evt);
            }
        });

        Bt14.setBackground(new java.awt.Color(255, 153, 153));
        Bt14.setForeground(new java.awt.Color(255, 255, 255));
        Bt14.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt14ActionPerformed(evt);
            }
        });

        Bt15.setBackground(new java.awt.Color(255, 153, 153));
        Bt15.setForeground(new java.awt.Color(255, 255, 255));
        Bt15.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(Bt14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                    .addContainerGap(22, Short.MAX_VALUE)
                    .addComponent(Bt15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(13, 13, 13)))
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Bt14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
            .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                    .addContainerGap(349, Short.MAX_VALUE)
                    .addComponent(Bt15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(23, 23, 23)))
        );

        Panel7.setToolTipText("");
        Panel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel7Layout = new javax.swing.GroupLayout(Panel7);
        Panel7.setLayout(Panel7Layout);
        Panel7Layout.setHorizontalGroup(
            Panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Panel7Layout.setVerticalGroup(
            Panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel9.setToolTipText("");
        Panel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel9Layout = new javax.swing.GroupLayout(Panel9);
        Panel9.setLayout(Panel9Layout);
        Panel9Layout.setHorizontalGroup(
            Panel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 113, Short.MAX_VALUE)
        );
        Panel9Layout.setVerticalGroup(
            Panel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel10.setToolTipText("");
        Panel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel10MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel10Layout = new javax.swing.GroupLayout(Panel10);
        Panel10.setLayout(Panel10Layout);
        Panel10Layout.setHorizontalGroup(
            Panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        Panel10Layout.setVerticalGroup(
            Panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel11.setToolTipText("");
        Panel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel11MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Panel11Layout = new javax.swing.GroupLayout(Panel11);
        Panel11.setLayout(Panel11Layout);
        Panel11Layout.setHorizontalGroup(
            Panel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 111, Short.MAX_VALUE)
        );
        Panel11Layout.setVerticalGroup(
            Panel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Panel12.setToolTipText("");
        Panel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel12MouseClicked(evt);
            }
        });

        Bt5.setBackground(new java.awt.Color(255, 153, 153));
        Bt5.setForeground(new java.awt.Color(255, 255, 255));
        Bt5.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt5ActionPerformed(evt);
            }
        });

        Bt2.setBackground(new java.awt.Color(255, 153, 153));
        Bt2.setForeground(new java.awt.Color(255, 255, 255));
        Bt2.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt2ActionPerformed(evt);
            }
        });

        Bt1.setBackground(new java.awt.Color(255, 153, 153));
        Bt1.setForeground(new java.awt.Color(255, 255, 255));
        Bt1.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt1ActionPerformed(evt);
            }
        });

        Bt3.setBackground(new java.awt.Color(255, 153, 153));
        Bt3.setForeground(new java.awt.Color(255, 255, 255));
        Bt3.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt3ActionPerformed(evt);
            }
        });

        Bt4.setBackground(new java.awt.Color(255, 153, 153));
        Bt4.setForeground(new java.awt.Color(255, 255, 255));
        Bt4.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel12Layout = new javax.swing.GroupLayout(Panel12);
        Panel12.setLayout(Panel12Layout);
        Panel12Layout.setHorizontalGroup(
            Panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel12Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(Panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Bt4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Bt3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Bt1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Bt2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Bt5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        Panel12Layout.setVerticalGroup(
            Panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel12Layout.createSequentialGroup()
                .addContainerGap(93, Short.MAX_VALUE)
                .addComponent(Bt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Bt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Bt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Bt4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Bt5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Panel17.setToolTipText("");
        Panel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel17MouseClicked(evt);
            }
        });

        Bt7.setBackground(new java.awt.Color(255, 153, 153));
        Bt7.setForeground(new java.awt.Color(255, 255, 255));
        Bt7.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt7ActionPerformed(evt);
            }
        });

        Bt8.setBackground(new java.awt.Color(255, 153, 153));
        Bt8.setForeground(new java.awt.Color(255, 255, 255));
        Bt8.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt8ActionPerformed(evt);
            }
        });

        Bt10.setBackground(new java.awt.Color(255, 153, 153));
        Bt10.setForeground(new java.awt.Color(255, 255, 255));
        Bt10.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel17Layout = new javax.swing.GroupLayout(Panel17);
        Panel17.setLayout(Panel17Layout);
        Panel17Layout.setHorizontalGroup(
            Panel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Bt8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Bt7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Bt10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        Panel17Layout.setVerticalGroup(
            Panel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel17Layout.createSequentialGroup()
                .addComponent(Bt8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Bt7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Bt10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel8.setToolTipText("");
        Panel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel8MouseClicked(evt);
            }
        });

        St7.setBackground(new java.awt.Color(255, 153, 153));
        St7.setForeground(new java.awt.Color(255, 255, 255));
        St7.setPreferredSize(new java.awt.Dimension(75, 75));
        St7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St7ActionPerformed(evt);
            }
        });

        St8.setBackground(new java.awt.Color(255, 153, 153));
        St8.setForeground(new java.awt.Color(255, 255, 255));
        St8.setPreferredSize(new java.awt.Dimension(75, 75));
        St8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St8ActionPerformed(evt);
            }
        });

        St10.setBackground(new java.awt.Color(255, 153, 153));
        St10.setForeground(new java.awt.Color(255, 255, 255));
        St10.setPreferredSize(new java.awt.Dimension(75, 75));
        St10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                St10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel8Layout = new javax.swing.GroupLayout(Panel8);
        Panel8.setLayout(Panel8Layout);
        Panel8Layout.setHorizontalGroup(
            Panel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(St7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(St10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel8Layout.setVerticalGroup(
            Panel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(St8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(St7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(St10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Panel19.setToolTipText("");
        Panel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel19MouseClicked(evt);
            }
        });

        Bt6.setBackground(new java.awt.Color(255, 153, 153));
        Bt6.setForeground(new java.awt.Color(255, 255, 255));
        Bt6.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt6ActionPerformed(evt);
            }
        });

        Bt9.setBackground(new java.awt.Color(255, 153, 153));
        Bt9.setForeground(new java.awt.Color(255, 255, 255));
        Bt9.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt9ActionPerformed(evt);
            }
        });

        Bt11.setBackground(new java.awt.Color(255, 153, 153));
        Bt11.setForeground(new java.awt.Color(255, 255, 255));
        Bt11.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt11ActionPerformed(evt);
            }
        });

        Bt12.setBackground(new java.awt.Color(255, 153, 153));
        Bt12.setForeground(new java.awt.Color(255, 255, 255));
        Bt12.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt12ActionPerformed(evt);
            }
        });

        Bt13.setBackground(new java.awt.Color(255, 153, 153));
        Bt13.setForeground(new java.awt.Color(255, 255, 255));
        Bt13.setPreferredSize(new java.awt.Dimension(75, 75));
        Bt13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bt13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel19Layout = new javax.swing.GroupLayout(Panel19);
        Panel19.setLayout(Panel19Layout);
        Panel19Layout.setHorizontalGroup(
            Panel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel19Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(Panel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Bt11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Bt9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Bt12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Bt13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Bt6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel19Layout.setVerticalGroup(
            Panel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel19Layout.createSequentialGroup()
                .addComponent(Bt11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Bt9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Bt12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Bt13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Bt6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        orta3.setBackground(new java.awt.Color(255, 153, 0));
        orta3.setName(""); // NOI18N
        orta3.setPreferredSize(new java.awt.Dimension(31, 500));

        javax.swing.GroupLayout orta3Layout = new javax.swing.GroupLayout(orta3);
        orta3.setLayout(orta3Layout);
        orta3Layout.setHorizontalGroup(
            orta3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );
        orta3Layout.setVerticalGroup(
            orta3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        orta1.setBackground(new java.awt.Color(255, 153, 0));
        orta1.setName(""); // NOI18N

        javax.swing.GroupLayout orta1Layout = new javax.swing.GroupLayout(orta1);
        orta1.setLayout(orta1Layout);
        orta1Layout.setHorizontalGroup(
            orta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        orta1Layout.setVerticalGroup(
            orta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(orta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Panel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Panel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Panel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Panel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orta3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(orta3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 955, Short.MAX_VALUE)
                            .addComponent(orta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(Panel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Panel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(Panel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(79, 79, 79))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(orta1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Panel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(Panel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Panel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(80, 80, 80))))
        );

        Panel14.getAccessibleContext().setAccessibleName("");

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        devamEt.setText("Devam Et");
        devamEt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devamEtActionPerformed(evt);
            }
        });
        jPanel2.add(devamEt, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 232, -1, -1));

        zarAt.setText("Zar At");
        zarAt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zarAtActionPerformed(evt);
            }
        });
        jPanel2.add(zarAt, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 232, -1, -1));

        lbl_gamer1.setBackground(new java.awt.Color(204, 204, 255));
        lbl_gamer1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_gamer1.setMaximumSize(new java.awt.Dimension(35, 25));
        lbl_gamer1.setMinimumSize(new java.awt.Dimension(25, 25));
        lbl_gamer1.setOpaque(true);
        jPanel2.add(lbl_gamer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 121, 112));

        lbl_gamer2.setBackground(new java.awt.Color(204, 204, 204));
        lbl_gamer2.setFocusCycleRoot(true);
        jPanel2.add(lbl_gamer2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 120, 120));

        jPanel3.setBackground(new java.awt.Color(204, 255, 204));

        btn_pick.setText("Oyuma Başla");
        btn_pick.setEnabled(false);
        btn_pick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pickActionPerformed(evt);
            }
        });

        txt_rival_name.setEditable(false);
        txt_rival_name.setText("Rival");
        txt_rival_name.setEnabled(false);
        txt_rival_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_rival_nameActionPerformed(evt);
            }
        });

        btn_connect.setText("Connect");
        btn_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_connectActionPerformed(evt);
            }
        });

        txt_name.setText("Name");

        PlayerRenk.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout PlayerRenkLayout = new javax.swing.GroupLayout(PlayerRenk);
        PlayerRenk.setLayout(PlayerRenkLayout);
        PlayerRenkLayout.setHorizontalGroup(
            PlayerRenkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 62, Short.MAX_VALUE)
        );
        PlayerRenkLayout.setVerticalGroup(
            PlayerRenkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 62, Short.MAX_VALUE)
        );

        PlayerRenk2.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout PlayerRenk2Layout = new javax.swing.GroupLayout(PlayerRenk2);
        PlayerRenk2.setLayout(PlayerRenk2Layout);
        PlayerRenk2Layout.setHorizontalGroup(
            PlayerRenk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );
        PlayerRenk2Layout.setVerticalGroup(
            PlayerRenk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 61, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addComponent(btn_connect, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PlayerRenk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_rival_name, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(PlayerRenk2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_pick, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_connect)
                    .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PlayerRenk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_rival_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PlayerRenk2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_pick, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 958, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_connectActionPerformed

        //bağlanılacak server ve portu veriyoruz
        Client2.Start("127.0.0.1", 2000);
        //başlangıç durumları hoş geldiniz... görüntüsü
        lbl_gamer1.setIcon(icons[4]);
        btn_connect.setEnabled(false);
        txt_name.setEnabled(false);
        btn_pick.setEnabled(false);
     
    }//GEN-LAST:event_btn_connectActionPerformed
    public void Reset() {
        if (Client2.socket != null) {
            if (Client2.socket.isConnected()) {
                Client2.Stop();
            }
        }
        lbl_gamer1.setIcon(icons[0]);
        btn_connect.setEnabled(true);
        txt_name.setEnabled(true);
        btn_pick.setEnabled(false);

    }

    private void AddBoard() {
        //adding the board image to the window.

    }
    private void btn_pickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pickActionPerformed
        //seçim yapıyoruz

        myselection = rand.nextInt(6) + 1;
        System.out.println("my selection= " + myselection);
        lbl_gamer1.setIcon(zarlar[myselection - 1]);
        btn_pick.setEnabled(false);
        lbl_gamer1.setVisible(true);
        lbl_gamer2.setVisible(true);
        //seçildi mesajını gönder
        Message msg = new Message(Message.Message_Type.Selected);
        msg.content = myselection;
        Client2.Send2(msg);
    }//GEN-LAST:event_btn_pickActionPerformed

    private void zarAtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zarAtActionPerformed
        // TODO add your handling code here:
        zar1 = rand.nextInt(6) + 1;
        zar2 = rand.nextInt(6) + 1;
        System.out.println("my selection= " + myselection);
        lbl_gamer1.setIcon(zarlar[zar1 - 1]);
        lbl_gamer2.setIcon(zarlar[zar2 - 1]);
        zarAt.setEnabled(false);
        //seçildi mesajını gönder
        lbl_gamer1.setVisible(true);
        lbl_gamer2.setVisible(true);
        Message msg = new Message(Message.Message_Type.ZarArray);
        int sendZarArray[] = new int[2];
        sendZarArray[0] = zar1;
        sendZarArray[1] = zar2;
        msg.content = sendZarArray;
        Client2.Send2(msg);
    }//GEN-LAST:event_zarAtActionPerformed

    private void devamEtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devamEtActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_devamEtActionPerformed

    private void txt_rival_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_rival_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rival_nameActionPerformed

    private void Panel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel19MouseClicked
       panelNum = 19;
    }//GEN-LAST:event_Panel19MouseClicked

    private void Bt13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt13ActionPerformed
          int buttonNum1=PanelTasSayisi(PanelArr.get(panelNum-1-zar1));
        System.out.println("btn1 tıklandı");
        if(playZar1 && player1Color=="white"){
            
           if(panelNum-zar1>=12)
           {   
               PanelArr.get(panelNum-1-zar1).remove(PanelArr.get(panelNum-1-zar1).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar1).add(Bt13,0);
               PanelArr.get(panelNum-1).remove(Bt13);
               
               if(panelNum-1<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),6-buttonNum1);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "));
               }
              
           }else
           {  System.out.println("Burada");
               PanelArr.get(panelNum-1-zar1).remove(0);
               PanelArr.get(panelNum-1-zar1).add(Bt13,5);
               PanelArr.get(panelNum-1).remove(Bt13);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar1=false;
           
          
       }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1-zar2));
        if(!playZar1 && playZar2 && player1Color=="white"){
           
           if(panelNum-1-zar2>=12)
           {   
               PanelArr.get(panelNum-1-zar2).remove(PanelArr.get(panelNum-1-zar2).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar2).add(Bt13,0);
               PanelArr.get(panelNum).remove(Bt13);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),6-buttonNum2);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1-zar2).remove(0);
               PanelArr.get(panelNum-1-zar2).add(Bt13);
               PanelArr.get(panelNum-1).remove(Bt13);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar2=false;
           
          
       }
    }//GEN-LAST:event_Bt13ActionPerformed

    private void Bt12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Bt12ActionPerformed

    private void Bt11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Bt11ActionPerformed

    private void Bt9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Bt9ActionPerformed

    private void Bt6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Bt6ActionPerformed

    private void Panel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel8MouseClicked
      panelNum = 18;
    }//GEN-LAST:event_Panel8MouseClicked

    private void St10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_St10ActionPerformed

    private void St8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_St8ActionPerformed

    private void St7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_St7ActionPerformed

    private void Panel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel17MouseClicked
       panelNum = 17;
    }//GEN-LAST:event_Panel17MouseClicked

    private void Bt10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Bt10ActionPerformed

    private void Bt8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Bt8ActionPerformed

    private void Bt7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Bt7ActionPerformed

    private void Panel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel12MouseClicked
       panelNum = 12;
    }//GEN-LAST:event_Panel12MouseClicked

    private void Bt4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt4ActionPerformed
          int buttonNum1=PanelTasSayisi(PanelArr.get(panelNum-1-zar1));
        System.out.println("btn1 tıklandı");
        if(playZar1 && player1Color=="white"){
            
           if(panelNum-zar1>=12)
           {   
               PanelArr.get(panelNum-1-zar1).remove(PanelArr.get(panelNum-1-zar1).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar1).add(Bt4,0);
               PanelArr.get(panelNum-1).remove(Bt4);
               
               if(panelNum-1<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),6-buttonNum1);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "));
               }
              
           }else
           {  System.out.println("Burada");
               PanelArr.get(panelNum-1-zar1).remove(0);
               PanelArr.get(panelNum-1-zar1).add(Bt4,5);
               PanelArr.get(panelNum-1).remove(Bt4);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar1=false;
           
          
       }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1-zar2));
        if(!playZar1 && playZar2 && player1Color=="white"){
           
           if(panelNum-1-zar2>=12)
           {   
               PanelArr.get(panelNum-1-zar2).remove(PanelArr.get(panelNum-1-zar2).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar2).add(Bt4,0);
               PanelArr.get(panelNum).remove(Bt4);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),6-buttonNum2);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1-zar2).remove(0);
               PanelArr.get(panelNum-1-zar2).add(Bt4);
               PanelArr.get(panelNum-1).remove(Bt4);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar2=false;
           
          
       }
    }//GEN-LAST:event_Bt4ActionPerformed

    private void Bt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt3ActionPerformed
          int buttonNum1=PanelTasSayisi(PanelArr.get(panelNum-1-zar1));
        System.out.println("btn1 tıklandı");
        if(playZar1 && player1Color=="white"){
            
           if(panelNum-zar1>=12)
           {   
               PanelArr.get(panelNum-1-zar1).remove(PanelArr.get(panelNum-1-zar1).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar1).add(Bt3,0);
               PanelArr.get(panelNum-1).remove(Bt3);
               
               if(panelNum-1<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),6-buttonNum1);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "));
               }
              
           }else
           {  System.out.println("Burada");
               PanelArr.get(panelNum-1-zar1).remove(0);
               PanelArr.get(panelNum-1-zar1).add(Bt3,5);
               PanelArr.get(panelNum-1).remove(Bt3);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar1=false;
           
          
       }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1-zar2));
        if(!playZar1 && playZar2 && player1Color=="white"){
           
           if(panelNum-1-zar2>=12)
           {   
               PanelArr.get(panelNum-1-zar2).remove(PanelArr.get(panelNum-1-zar2).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar2).add(Bt3,0);
               PanelArr.get(panelNum).remove(Bt3);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),6-buttonNum2);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1-zar2).remove(0);
               PanelArr.get(panelNum-1-zar2).add(Bt3);
               PanelArr.get(panelNum-1).remove(Bt3);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar2=false;
           
          
       }
    }//GEN-LAST:event_Bt3ActionPerformed

    private void Bt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt1ActionPerformed
        int buttonNum1=PanelTasSayisi(PanelArr.get(panelNum-1-zar1));
        System.out.println("btn1 tıklandı");
        if(playZar1 && player1Color=="white"){
            
           if(panelNum-zar1>=12)
           {   
               PanelArr.get(panelNum-1-zar1).remove(PanelArr.get(panelNum-1-zar1).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar1).add(Bt1,0);
               PanelArr.get(panelNum-1).remove(Bt1);
               
               if(panelNum-1<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),6-buttonNum1);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "));
               }
              
           }else
           {  System.out.println("Burada");
               PanelArr.get(panelNum-1-zar1).remove(0);
               PanelArr.get(panelNum-1-zar1).add(Bt1,5);
               PanelArr.get(panelNum-1).remove(Bt1);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar1=false;
           
          
       }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1-zar2));
        if(!playZar1 && playZar2 && player1Color=="white"){
           
           if(panelNum-1-zar2>=12)
           {   
               PanelArr.get(panelNum-1-zar2).remove(PanelArr.get(panelNum-1-zar2).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar2).add(Bt1,0);
               PanelArr.get(panelNum).remove(Bt1);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),6-buttonNum2);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1-zar2).remove(0);
               PanelArr.get(panelNum-1-zar2).add(Bt6);
               PanelArr.get(panelNum-1).remove(Bt6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar2=false;
           
          
       }
    }//GEN-LAST:event_Bt1ActionPerformed

    private void Bt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt2ActionPerformed
           int buttonNum1=PanelTasSayisi(PanelArr.get(panelNum-1-zar1));
        System.out.println("btn1 tıklandı");
        if(playZar1 && player1Color=="white"){
            
           if(panelNum-zar1>=12)
           {   
               PanelArr.get(panelNum-1-zar1).remove(PanelArr.get(panelNum-1-zar1).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar1).add(Bt2,0);
               PanelArr.get(panelNum-1).remove(Bt2);
               
               if(panelNum-1<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),6-buttonNum1);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "));
               }
              
           }else
           {  System.out.println("Burada");
               PanelArr.get(panelNum-1-zar1).remove(0);
               PanelArr.get(panelNum-1-zar1).add(Bt2,5);
               PanelArr.get(panelNum-1).remove(Bt2);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar1=false;
           
          
       }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1-zar2));
        if(!playZar1 && playZar2 && player1Color=="white"){
           
           if(panelNum-1-zar2>=12)
           {   
               PanelArr.get(panelNum-1-zar2).remove(PanelArr.get(panelNum-1-zar2).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar2).add(Bt2,0);
               PanelArr.get(panelNum).remove(Bt2);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),6-buttonNum2);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1-zar2).remove(0);
               PanelArr.get(panelNum-1-zar2).add(Bt2);
               PanelArr.get(panelNum-1).remove(Bt2);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar2=false;
           
          
       }
    }//GEN-LAST:event_Bt2ActionPerformed

    private void Bt5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt5ActionPerformed
          int buttonNum1=PanelTasSayisi(PanelArr.get(panelNum-1-zar1));
        System.out.println("btn1 tıklandı");
        if(playZar1 && player1Color=="white"){
            
           if(panelNum-zar1>=12)
           {   
               PanelArr.get(panelNum-1-zar1).remove(PanelArr.get(panelNum-1-zar1).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar1).add(Bt5,0);
               PanelArr.get(panelNum-1).remove(Bt5);
               
               if(panelNum-1<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),6-buttonNum1);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "));
               }
              
           }else
           {  System.out.println("Burada");
               PanelArr.get(panelNum-1-zar1).remove(0);
               PanelArr.get(panelNum-1-zar1).add(Bt5,5);
               PanelArr.get(panelNum-1).remove(Bt5);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar1=false;
           
          
       }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1-zar2));
        if(!playZar1 && playZar2 && player1Color=="white"){
           
           if(panelNum-1-zar2>=12)
           {   
               PanelArr.get(panelNum-1-zar2).remove(PanelArr.get(panelNum-1-zar2).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar2).add(Bt5,0);
               PanelArr.get(panelNum).remove(Bt5);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),6-buttonNum2);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1-zar2).remove(0);
               PanelArr.get(panelNum-1-zar2).add(Bt5);
               PanelArr.get(panelNum-1).remove(Bt5);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar2=false;
           
          
       }
    }//GEN-LAST:event_Bt5ActionPerformed

    private void Panel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel11MouseClicked
       panelNum = 11;
    }//GEN-LAST:event_Panel11MouseClicked

    private void Panel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel10MouseClicked
       panelNum = 10;
    }//GEN-LAST:event_Panel10MouseClicked

    private void Panel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel9MouseClicked
        panelNum = 9;
    }//GEN-LAST:event_Panel9MouseClicked

    private void Panel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel7MouseClicked
       panelNum = 7;
    }//GEN-LAST:event_Panel7MouseClicked

    private void Panel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel1MouseClicked
        panelNum = 1;
    }//GEN-LAST:event_Panel1MouseClicked

    private void Panel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel6MouseClicked
      panelNum = 6;
    }//GEN-LAST:event_Panel6MouseClicked

    private void Panel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel5MouseClicked
       panelNum = 15;
    }//GEN-LAST:event_Panel5MouseClicked

    private void Panel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel4MouseClicked
       panelNum = 14;
    }//GEN-LAST:event_Panel4MouseClicked

    private void Panel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel3MouseClicked
      panelNum = 13;
    }//GEN-LAST:event_Panel3MouseClicked

    private void Panel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel2MouseClicked
        panelNum = 12;
    }//GEN-LAST:event_Panel2MouseClicked

    private void Panel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel13MouseClicked
        panelNum = 13;
    }//GEN-LAST:event_Panel13MouseClicked
    int PanelTasSayisi(JPanel p1){
        int counter=0;
        
        for (int i = 0; i <p1.getComponentCount(); i++) {
            System.out.println(p1.getComponent(i).toString().substring(0, 17).equals("javax.swing.JButt"));
            
              if(p1.getComponent(i).toString().substring(0, 17).equals("javax.swing.JButt")){
                  counter++;
              }
        }
        System.out.println("buton number="+counter);
        return counter;
    };
    private void St6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St6ActionPerformed
       
        
        if(playZar1 && player1Color=="black"){
            int buttonNum1=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
           if(panelNum+zar1>=12)
           {   
               PanelArr.get(panelNum-1+zar1).remove(PanelArr.get(panelNum-1+zar1).getComponentCount()-1);
               PanelArr.get(panelNum-1+zar1).add(St6,0);
               PanelArr.get(panelNum-1).remove(St6);
               
               if(panelNum-1<13){
                    
                    PanelArr.get(panelNum-1+zar1).add(new JLabel("                         "),6-buttonNum1);
                    
               }else{
                   PanelArr.get(panelNum-1+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1+zar1).remove(0);
               PanelArr.get(panelNum-1+zar1).add(St6);
               PanelArr.get(panelNum-1).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1+zar1).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar1=false;
           
          
       }
        
        if(!playZar1 && playZar2 && player1Color=="black"){
            int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
           if(panelNum-1+zar2>=12)
           {   
               PanelArr.get(panelNum-1+zar2).remove(PanelArr.get(panelNum-1+zar2).getComponentCount()-1);
               PanelArr.get(panelNum-1+zar2).add(St6,0);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1+zar2).add(new JLabel("                         "),6-buttonNum2);
                    
               }else{
                   PanelArr.get(panelNum-1+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1+zar2).remove(0);
               PanelArr.get(panelNum-1+zar2).add(St6);
               PanelArr.get(panelNum-1).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1+zar2).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar2=false;
           
          
       }
        
    }//GEN-LAST:event_St6ActionPerformed

    private void St2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St2ActionPerformed
       
      
        int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St2ActionPerformed

        
        
    private void St3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St3ActionPerformed
        int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St3ActionPerformed

    private void St1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St1ActionPerformed
        int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St1ActionPerformed

    private void St9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St9ActionPerformed
       int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St9ActionPerformed

    private void St11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St11ActionPerformed
      int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St11ActionPerformed

    private void St12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St12ActionPerformed
        // TODO add your handling code here:
        int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St12ActionPerformed

    private void St13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St13ActionPerformed
        // TODO add your handling code here:int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St13);
               PanelArr.get(panelNum).remove(St13);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St13);
               PanelArr.get(panelNum).remove(St13);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St13);
               PanelArr.get(panelNum).remove(St13);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St13);
               PanelArr.get(panelNum).remove(St13);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St13ActionPerformed

    private void St14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St14ActionPerformed
       int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St6);
               PanelArr.get(panelNum).remove(St6);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St14);
               PanelArr.get(panelNum).remove(St14);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St14);
               PanelArr.get(panelNum).remove(St14);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St14ActionPerformed

    private void St15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St15ActionPerformed
        int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St15);
               PanelArr.get(panelNum).remove(St15);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St15);
               PanelArr.get(panelNum).remove(St15);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St15);
               PanelArr.get(panelNum).remove(St15);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St15);
               PanelArr.get(panelNum).remove(St15);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St15ActionPerformed

    private void St4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St4ActionPerformed
        int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St4);
               PanelArr.get(panelNum).remove(St4);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St4);
               PanelArr.get(panelNum).remove(St4);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St4);
               PanelArr.get(panelNum).remove(St4);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St4);
               PanelArr.get(panelNum).remove(St4);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St4ActionPerformed

    private void Bt14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt14ActionPerformed
           int buttonNum1=PanelTasSayisi(PanelArr.get(panelNum-1-zar1));
        System.out.println("btn1 tıklandı");
        if(playZar1 && player1Color=="white"){
            
           if(panelNum-zar1>=12)
           {   
               PanelArr.get(panelNum-1-zar1).remove(PanelArr.get(panelNum-1-zar1).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar1).add(Bt6,0);
               PanelArr.get(panelNum-1).remove(Bt6);
               
               if(panelNum-1<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),6-buttonNum1);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "));
               }
              
           }else
           {  System.out.println("Burada");
               PanelArr.get(panelNum-1-zar1).remove(0);
               PanelArr.get(panelNum-1-zar1).add(Bt6,5);
               PanelArr.get(panelNum-1).remove(Bt6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar1=false;
           
          
       }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1-zar2));
        if(!playZar1 && playZar2 && player1Color=="white"){
           
           if(panelNum-1-zar2>=12)
           {   
               PanelArr.get(panelNum-1-zar2).remove(PanelArr.get(panelNum-1-zar2).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar2).add(Bt6,0);
               PanelArr.get(panelNum).remove(Bt6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),6-buttonNum2);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1-zar2).remove(0);
               PanelArr.get(panelNum-1-zar2).add(Bt6);
               PanelArr.get(panelNum-1).remove(Bt6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar2=false;
           
          
       }
    }//GEN-LAST:event_Bt14ActionPerformed

    private void Bt15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bt15ActionPerformed
         int buttonNum1=PanelTasSayisi(PanelArr.get(panelNum-1-zar1));
        System.out.println("btn1 tıklandı");
        if(playZar1 && player1Color=="white"){
            
           if(panelNum-zar1>=12)
           {   
               PanelArr.get(panelNum-1-zar1).remove(PanelArr.get(panelNum-1-zar1).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar1).add(Bt6,0);
               PanelArr.get(panelNum-1).remove(Bt6);
               
               if(panelNum-1<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),6-buttonNum1);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "));
               }
              
           }else
           {  System.out.println("Burada");
               PanelArr.get(panelNum-1-zar1).remove(0);
               PanelArr.get(panelNum-1-zar1).add(Bt6,5);
               PanelArr.get(panelNum-1).remove(Bt6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar1).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar1=false;
           
          
       }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1-zar2));
        if(!playZar1 && playZar2 && player1Color=="white"){
           
           if(panelNum-1-zar2>=12)
           {   
               PanelArr.get(panelNum-1-zar2).remove(PanelArr.get(panelNum-1-zar2).getComponentCount()-1);
               PanelArr.get(panelNum-1-zar2).add(Bt6,0);
               PanelArr.get(panelNum).remove(Bt6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),6-buttonNum2);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum-1-zar2).remove(0);
               PanelArr.get(panelNum-1-zar2).add(Bt6);
               PanelArr.get(panelNum-1).remove(Bt6);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum-1-zar2).add(new JLabel("                         "),getComponentCount()-1);
               }
              
           }
           playZar2=false;
           
          
       }
    }//GEN-LAST:event_Bt15ActionPerformed

    private void Panel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel16MouseClicked
      panelNum = 16;
    }//GEN-LAST:event_Panel16MouseClicked

    private void Panel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel15MouseClicked
      panelNum = 15;
    }//GEN-LAST:event_Panel15MouseClicked

    private void Panel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel14MouseClicked
       panelNum = 14;
    }//GEN-LAST:event_Panel14MouseClicked

    private void Panel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel18MouseClicked
       panelNum = 18;
    }//GEN-LAST:event_Panel18MouseClicked

    private void Panel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel20MouseClicked
       panelNum = 20;
    }//GEN-LAST:event_Panel20MouseClicked

    private void Panel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel21MouseClicked
       panelNum = 21;
    }//GEN-LAST:event_Panel21MouseClicked

    private void Panel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel22MouseClicked
       panelNum = 22;
    }//GEN-LAST:event_Panel22MouseClicked

    private void Panel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel23MouseClicked
        panelNum = 23;
    }//GEN-LAST:event_Panel23MouseClicked

    private void Panel24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel24MouseClicked
       panelNum = 24;
    }//GEN-LAST:event_Panel24MouseClicked

    private void St5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_St5ActionPerformed
       int buttonNum=PanelTasSayisi(PanelArr.get(panelNum-1+zar1));
        if(playZar1){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar1).remove(PanelArr.get(panelNum+zar1).getComponentCount()-1);
               PanelArr.get(panelNum+zar1).add(St5);
               PanelArr.get(panelNum).remove(St5);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar1).remove(0);
               PanelArr.get(panelNum+zar1).add(St5);
               PanelArr.get(panelNum).remove(St5);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar1).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar1).add(new JLabel("                         "));
               }
              
           }
        int buttonNum2=PanelTasSayisi(PanelArr.get(panelNum-1+zar2));
        if(!playZar1 &&playZar2){
            
           if(panelNum+zar1>12)
           {   PanelArr.get(panelNum+zar2).remove(PanelArr.get(panelNum+zar2).getComponentCount()-1);
               PanelArr.get(panelNum+zar2).add(St5);
               PanelArr.get(panelNum).remove(St5);
               
               if(panelNum<13){
                    
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),6-buttonNum);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           }else
           {  
               PanelArr.get(panelNum+zar2).remove(0);
               PanelArr.get(panelNum+zar2).add(St5);
               PanelArr.get(panelNum).remove(St5);
               if(panelNum<13){
                    PanelArr.get(panelNum+zar2).add(new JLabel("                         "),0);
                    
               }else{
                   PanelArr.get(panelNum+zar2).add(new JLabel("                         "));
               }
              
           
           
           }
       }
        }
    }//GEN-LAST:event_St5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(gameTavla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gameTavla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gameTavla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gameTavla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
 
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            new gameTavla().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Bt1;
    private javax.swing.JButton Bt10;
    private javax.swing.JButton Bt11;
    private javax.swing.JButton Bt12;
    private javax.swing.JButton Bt13;
    private javax.swing.JButton Bt14;
    private javax.swing.JButton Bt15;
    private javax.swing.JButton Bt2;
    private javax.swing.JButton Bt3;
    private javax.swing.JButton Bt4;
    private javax.swing.JButton Bt5;
    private javax.swing.JButton Bt6;
    private javax.swing.JButton Bt7;
    private javax.swing.JButton Bt8;
    private javax.swing.JButton Bt9;
    private javax.swing.JPanel Panel1;
    private javax.swing.JPanel Panel10;
    private javax.swing.JPanel Panel11;
    private javax.swing.JPanel Panel12;
    private javax.swing.JPanel Panel13;
    private javax.swing.JPanel Panel14;
    private javax.swing.JPanel Panel15;
    private javax.swing.JPanel Panel16;
    private javax.swing.JPanel Panel17;
    private javax.swing.JPanel Panel18;
    private javax.swing.JPanel Panel19;
    private javax.swing.JPanel Panel2;
    private javax.swing.JPanel Panel20;
    private javax.swing.JPanel Panel21;
    private javax.swing.JPanel Panel22;
    private javax.swing.JPanel Panel23;
    private javax.swing.JPanel Panel24;
    private javax.swing.JPanel Panel3;
    private javax.swing.JPanel Panel4;
    private javax.swing.JPanel Panel5;
    private javax.swing.JPanel Panel6;
    private javax.swing.JPanel Panel7;
    private javax.swing.JPanel Panel8;
    private javax.swing.JPanel Panel9;
    private javax.swing.JPanel PlayerRenk;
    private javax.swing.JPanel PlayerRenk2;
    private javax.swing.JButton St1;
    private javax.swing.JButton St10;
    private javax.swing.JButton St11;
    private javax.swing.JButton St12;
    private javax.swing.JButton St13;
    private javax.swing.JButton St14;
    private javax.swing.JButton St15;
    private javax.swing.JButton St2;
    private javax.swing.JButton St3;
    private javax.swing.JButton St4;
    private javax.swing.JButton St5;
    private javax.swing.JButton St6;
    private javax.swing.JButton St7;
    private javax.swing.JButton St8;
    private javax.swing.JButton St9;
    public javax.swing.JButton btn_connect;
    public javax.swing.JButton btn_pick;
    private javax.swing.JButton devamEt;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JLabel lbl_gamer1;
    public javax.swing.JLabel lbl_gamer2;
    private javax.swing.JPanel orta;
    private javax.swing.JPanel orta1;
    private javax.swing.JPanel orta3;
    public javax.swing.JTextField txt_name;
    public javax.swing.JTextField txt_rival_name;
    private javax.swing.JButton zarAt;
    // End of variables declaration//GEN-END:variables

    private void AddArrayToPanel() {
        Panel1.setLayout(new GridLayout(6,1));
        Panel1.setBackground(Color.red);
        Panel2.setLayout(new GridLayout(6,1));
        Panel2.setBackground(Color.red);
        Panel3.setLayout(new GridLayout(6,1));
        Panel3.setBackground(Color.red);
        Panel4.setLayout(new GridLayout(6,1));
        Panel4.setBackground(Color.red);
        Panel5.setLayout(new GridLayout(6,1));
        Panel5.setBackground(Color.red);
        Panel6.setLayout(new GridLayout(6,1));
        Panel6.setBackground(Color.red);
        Panel7.setLayout(new GridLayout(6,1));
        Panel7.setBackground(Color.red);
        Panel8.setLayout(new GridLayout(6,1));
        Panel8.setBackground(Color.red);
        Panel9.setLayout(new GridLayout(6,1));
        Panel9.setBackground(Color.red);
        Panel10.setLayout(new GridLayout(6,1));
        Panel10.setBackground(Color.red);
        Panel11.setLayout(new GridLayout(6,1));
        Panel11.setBackground(Color.red);
        Panel12.setLayout(new GridLayout(6,1));
        Panel12.setBackground(Color.red);
        Panel13.setLayout(new GridLayout(6,1));
        Panel13.setBackground(Color.red);
        Panel14.setLayout(new GridLayout(6,1));
        Panel14.setBackground(Color.red);
        Panel15.setLayout(new GridLayout(6,1));
        Panel15.setBackground(Color.red);
        Panel16.setLayout(new GridLayout(6,1));
        Panel16.setBackground(Color.red);
        Panel17.setLayout(new GridLayout(6,1));
        Panel17.setBackground(Color.red);
        Panel18.setLayout(new GridLayout(6,1));
        Panel18.setBackground(Color.red);
        Panel19.setLayout(new GridLayout(6,1));
        Panel19.setBackground(Color.red);
        Panel20.setLayout(new GridLayout(6,1));
        Panel20.setBackground(Color.red);
        Panel21.setLayout(new GridLayout(6,1));
        Panel21.setBackground(Color.red);
        Panel22.setLayout(new GridLayout(6,1));
        Panel22.setBackground(Color.red);
        Panel23.setLayout(new GridLayout(6,1));
        Panel23.setBackground(Color.red);
        Panel24.setLayout(new GridLayout(6,1));
        Panel24.setBackground(Color.red);

        PanelArr.add(Panel1);
        PanelArr.add(Panel2);
        PanelArr.add(Panel3);
        PanelArr.add(Panel4);
        PanelArr.add(Panel5);
        PanelArr.add(Panel6); 
        PanelArr.add(Panel7);
        PanelArr.add(Panel8);
        PanelArr.add(Panel9);
        PanelArr.add(Panel10);
        PanelArr.add(Panel11);
        PanelArr.add(Panel12);
        PanelArr.add(Panel13);
        PanelArr.add(Panel14);
        PanelArr.add(Panel15);
        PanelArr.add(Panel16);
        PanelArr.add(Panel17);
        PanelArr.add(Panel18);
        PanelArr.add(Panel19);
        PanelArr.add(Panel20);
        PanelArr.add(Panel21);
        PanelArr.add(Panel22);
        PanelArr.add(Panel23);
        PanelArr.add(Panel24);

    }

    private void AddPanelToButton() {
                 Panel1.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel1.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel1.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                 Panel1.add(new JLabel("                         "),BOTTOM_ALIGNMENT,3);
                 Panel2.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel2.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel2.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                 Panel2.add(new JLabel("                         "),BOTTOM_ALIGNMENT,3);
                 Panel2.add(new JLabel("                         "),BOTTOM_ALIGNMENT,4);
                 Panel2.add(new JLabel("                         "),BOTTOM_ALIGNMENT,5);
                  Panel3.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel3.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel3.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                 Panel3.add(new JLabel("                         "),BOTTOM_ALIGNMENT,3);
                 Panel3.add(new JLabel("                         "),BOTTOM_ALIGNMENT,4);
                 Panel3.add(new JLabel("                         "),BOTTOM_ALIGNMENT,5);
                  Panel4.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel4.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel4.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                 Panel4.add(new JLabel("                         "),BOTTOM_ALIGNMENT,3);
                 Panel4.add(new JLabel("                         "),BOTTOM_ALIGNMENT,4);
                 Panel4.add(new JLabel("                         "),BOTTOM_ALIGNMENT,5);
                 Panel5.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel5.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel5.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                 Panel5.add(new JLabel("                         "),BOTTOM_ALIGNMENT,3);
                 Panel5.add(new JLabel("                         "),BOTTOM_ALIGNMENT,4);
                 Panel5.add(new JLabel("                         "),BOTTOM_ALIGNMENT,5);
                 Panel6.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel7.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel7.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel7.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                 Panel7.add(new JLabel("                         "),BOTTOM_ALIGNMENT,3);
                 Panel7.add(new JLabel("                         "),BOTTOM_ALIGNMENT,4);
                 Panel7.add(new JLabel("                         "),BOTTOM_ALIGNMENT,5);
                 Panel8.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel8.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel8.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                  Panel9.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel9.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel9.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                 Panel9.add(new JLabel("                         "),BOTTOM_ALIGNMENT,3);
                 Panel9.add(new JLabel("                         "),BOTTOM_ALIGNMENT,4);
                 Panel9.add(new JLabel("                         "),BOTTOM_ALIGNMENT,5);
                 Panel10.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel10.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel10.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                 Panel10.add(new JLabel("                         "),BOTTOM_ALIGNMENT,3);
                 Panel10.add(new JLabel("                         "),BOTTOM_ALIGNMENT,4);
                 Panel10.add(new JLabel("                         "),BOTTOM_ALIGNMENT,5);
                 Panel11.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                 Panel11.add(new JLabel("                         "),BOTTOM_ALIGNMENT,1);
                 Panel11.add(new JLabel("                         "),BOTTOM_ALIGNMENT,2);
                 Panel11.add(new JLabel("                         "),BOTTOM_ALIGNMENT,3);
                 Panel11.add(new JLabel("                         "),BOTTOM_ALIGNMENT,4);
                 Panel11.add(new JLabel("                         "),BOTTOM_ALIGNMENT,5);
                Panel12.add(new JLabel("                         "),BOTTOM_ALIGNMENT,0);
                
                Panel13.add(new JLabel("                         "));
                Panel14.add(new JLabel("                         "));
                Panel14.add(new JLabel("                         "));
                Panel14.add(new JLabel("                         "));
                Panel14.add(new JLabel("                         "));
                Panel14.add(new JLabel("                         "));
                Panel14.add(new JLabel("                         "));
                Panel15.add(new JLabel("                         "));
                Panel15.add(new JLabel("                         "));
                Panel15.add(new JLabel("                         "));
                Panel15.add(new JLabel("                         "));
                Panel15.add(new JLabel("                         "));
                Panel15.add(new JLabel("                         "));
                Panel16.add(new JLabel("                         "));
                Panel16.add(new JLabel("                         "));
                Panel16.add(new JLabel("                         "));
                Panel16.add(new JLabel("                         "));
                Panel16.add(new JLabel("                         "));
                Panel16.add(new JLabel("                         "));
                Panel17.add(new JLabel("                         "));
                Panel17.add(new JLabel("                         "));
                Panel17.add(new JLabel("                         "));
                Panel19.add(new JLabel("                         "));
                Panel20.add(new JLabel("                         "));
                Panel20.add(new JLabel("                         "));
                Panel20.add(new JLabel("                         "));
                Panel20.add(new JLabel("                         "));
                Panel20.add(new JLabel("                         "));
                Panel20.add(new JLabel("                         "));
                Panel21.add(new JLabel("                         "));
                Panel21.add(new JLabel("                         "));
                Panel21.add(new JLabel("                         "));
                Panel21.add(new JLabel("                         "));
                Panel21.add(new JLabel("                         "));
                Panel21.add(new JLabel("                         "));
                Panel22.add(new JLabel("                         "));
                Panel22.add(new JLabel("                         "));
                Panel22.add(new JLabel("                         "));
                Panel22.add(new JLabel("                         "));
                Panel22.add(new JLabel("                         "));
                Panel22.add(new JLabel("                         "));
                Panel23.add(new JLabel("                         "));
                Panel23.add(new JLabel("                         "));
                Panel23.add(new JLabel("                         "));
                Panel23.add(new JLabel("                         "));
                Panel23.add(new JLabel("                         "));
                Panel23.add(new JLabel("                         ")); 
                Panel24.add(new JLabel("                         "));
                Panel24.add(new JLabel("                         "));
              
                
             
      
     
    }
    private void PaintWhiteAndButtons()
	{
		Bt1.setBackground(Color.white);
                Bt2.setBackground(Color.white);
                Bt3.setBackground(Color.white);
                Bt4.setBackground(Color.white);
                Bt5.setBackground(Color.white);
                Bt6.setBackground(Color.white);
                Bt7.setBackground(Color.white);
                Bt8.setBackground(Color.white);
                Bt9.setBackground(Color.white);
                Bt10.setBackground(Color.white);
                Bt11.setBackground(Color.white);
                Bt12.setBackground(Color.white);
                Bt13.setBackground(Color.white);
                Bt14.setBackground(Color.white);
                Bt15.setBackground(Color.white);
                St1.setBackground(Color.black);
                St2.setBackground(Color.black);
                St3.setBackground(Color.black);
                St4.setBackground(Color.black);
                St5.setBackground(Color.black);
                St6.setBackground(Color.black);
                St7.setBackground(Color.black);
                St8.setBackground(Color.black);
                St9.setBackground(Color.black);
                St10.setBackground(Color.black);
                St11.setBackground(Color.black);
                St12.setBackground(Color.black);
                St13.setBackground(Color.black);
                St14.setBackground(Color.black);
                St15.setBackground(Color.black);
                
	}
    
}
