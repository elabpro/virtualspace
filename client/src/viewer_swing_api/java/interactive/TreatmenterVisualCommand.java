/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactive;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


/**
 * TreatmenterVisualCommand
 * Класс для обработки видеопотока
 * 
 * Java version j8
 * 
 * @license IrGUPS
 * @author sleep (Gerschevich A.S.)
 * @link https://github.com/irgups/virtualspace
 */
public class TreatmenterVisualCommand extends Thread
{

    private Image tempImage;
    private CascadeClassifier Detector;
    private MatOfRect rect;
    private ImageIcon imageIcon = null;
    private MainFrame app = null;
    private Mat webcamMatImage;
    private DataInputStream in;
    private DataOutputStream out;
    private String side = "";
    private int state;

    /**
     * TreatmenterVisualCommand 
     * Конструктор обеспечивающий передачу видеопотока через сокет
     * @param in поток входных данных
     * @param out поток выходных данных
     */
    public TreatmenterVisualCommand(DataInputStream in, DataOutputStream out)
    {
        this.webcamMatImage = new Mat();
        this.app = new MainFrame();
        this.in = in;
        this.out = out;
        this.state = 1;
    }
    
    @Override
    /**
     * run
     * метод обеспечивающий отображение видеозахвата
     * @var CAP_PROP_FRAME_WIDTH размер фрейма в ширину
     * @var CAP_PROP_FRAME_HEIGHT размер фрейма в длину
     * 
     */
    public void run()
    {
        try
        {
            VideoCapture capture = new VideoCapture(0);
            capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 400);
            capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 300);
            if (capture.isOpened())
            {
                this.state = 1;
                while (this.state > 0)
                {
                    capture.read(webcamMatImage);
                    if (!webcamMatImage.empty())
                    {
                        //Rect[] var1 = searchImage("haarcascade_frontalface_default.xml");
                        Rect[] var1 = searchImage("hand.xml");
                        //Rect[] var2 = searchImage("haarcascade_eye.xml");
                        //Rect[] var = new Rect[var1.length];
                        //System.arraycopy(var1, 0, var, 0, var1.length);
                        //System.arraycopy(var2, 0, var, var1.length, var2.length);
                        try{
                            manager(var1[0]);
                        } catch(ArrayIndexOutOfBoundsException e){
                            
                        }
                        app.printPhoto(imageIcon, new MatOfRect(var1));
                    } else
                    {
                        System.out.println(" — Frame not captured — Break!");
                        break;
                    }
                    Thread.sleep(50);
                }
            }
        } catch (InterruptedException ex)
        {
            Logger.getLogger(TreatmenterVisualCommand.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(TreatmenterVisualCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * toBufferedImage двумерный массив заданный в RGB
     * отображается попиксельно
     * @param matrix двумерный массив типа Mat
     * @return image двумерный массив
     */
    Image toBufferedImage(Mat matrix)
    {
        int type;
        if (matrix.channels() > 1)
        {
            type = BufferedImage.TYPE_3BYTE_BGR;
        } else
        {
            type = BufferedImage.TYPE_BYTE_GRAY;
        }
        byte[] buffer = new byte[matrix.channels() * matrix.cols() * matrix.rows()];
        matrix.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
    /**
     * searchImagein поиск захваченного изображения
     * @param cascade путь к каскаду в файловой системе 
     * @return Rect[] 
     * @throws InterruptedException 
     */
    private Rect[] searchImage(String cascade) throws InterruptedException
    {
        tempImage = toBufferedImage(webcamMatImage);
        Detector = new CascadeClassifier(cascade);
        rect = new MatOfRect();
        Detector.detectMultiScale(webcamMatImage, rect);
        imageIcon = new ImageIcon(tempImage, "Captured video");
        return rect.toArray();
    }
    /**
     * stopping остановка обработчика видеопотока
     * 
     * @param void
     * @return void
     * 
     */
    public void stopping()
    {
        this.state = 0;
    }
    /**
     * manager 
     * Анализатор изображения и отправляет соответствующие команды
     * 
     * @param Rect r распознанный объект
     * @return void
     * @throws IOException 
     */
    private void manager(Rect r) throws IOException
    {
        if((rect != null) && !rect.empty()){
            if(side.equals("")){
                if(r.x + r.width / 2 < 200){
                    sendMessageToServer("left");
                    side = "left";
                }
                else {
                    sendMessageToServer("right");
                    side = "right";
                }
            } else {
                if(side.equals("right") && (r.x + r.width / 2 + 20 < 200)){
                    sendMessageToServer("left");
                    side = "left";
                } else {
                    if(side.equals("left") && (r.x + r.width / 2 - 20 > 200)) {
                        sendMessageToServer("right");
                        side = "right";
                    }
                }
            }
        }
    }
    /**
     * sendMessageToServer 
     * отправка сообщения на сервер 
     * @param String text сообщение для отправки на текст
     * @throws IOException 
     * @return void
     */
    private void sendMessageToServer(String text) throws IOException{
        text += '\0';
        out.write(text.getBytes()); // отсылаем введенную строку текста серверу.
        out.flush(); // заставляем поток закончить передачу данных.
    }
}

/**
 * ImagePanel
 * Класс для отображения графического интерфейса
 * а также вывод изображения с web-камеры
 * 
 * Java version j8
 * 
 * @license IrGUPS
 * @author ortaz (Reznitskiy M.A.)
 * @link https://github.com/irgups/virtualspace
 */
class ImagePanel extends JPanel
{

    private Image img;
    private MatOfRect rects;
    
    /**
     * 
     * @param img инициализация картинки
     * @param rects выделенная область
     * @return void
     */
    public void Init(Image img, MatOfRect rects)
    {
        this.rects = rects;
        this.img = img;
        Dimension size = new Dimension(img.getHeight(null), img.getHeight(null));
        setSize(size);
    }
    
    /**
     * paintComponent метод перерисовывающий
     * под jPanel
     * @param g формирование отрисовки на изображении
     * @return void
     */
    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(img, 0, 0, null);

        if (rects != null)
        {
            g.setColor(Color.RED);
            for (Rect r : rects.toArray())
            {
                g.drawRect(r.x, r.y, r.width, r.height);
            }
        }
    }
}

/**
 * MainFrame фрейм отбражающий изображение с web-камеры
 * Java version j8
 * 
 * @license IrGUPS
 * 
 * @author sleep (Gerschevich A.S.)
 * 
 * @link https://github.com/irgups/virtualspace
 * 
 */
class MainFrame extends JFrame
{

    private ImagePanel ip;
    MatOfRect rects;

    MainFrame()
    {
        this.setName("Камера");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        ip = new ImagePanel();
        this.add(ip);
        this.pack();
        this.setSize(290, 290);
    }

    public void printPhoto(ImageIcon ico, MatOfRect rect)
    {
        ip.Init(ico.getImage(), rect);
        ip.repaint();
    }
}
