/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactive;

import java.awt.BorderLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

/**
 * TreatmenterVisualCommand Класс для обработки видеопотока
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
    private static int state;
    private static int palmX;
    private static int palmY;

    /**
     * TreatmenterVisualCommand Конструктор обеспечивающий передачу видеопотока
     * через сокет
     *
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
     * run метод обеспечивающий отображение видеозахвата
     *
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
                state = 3;

                while (state > 0)
                {
                    Rect[] var = null;
                    capture.read(webcamMatImage);
                    if (!webcamMatImage.empty())
                    {
                        var = searchImage("haarcascade/palm.xml");
                        if (state == 1)
                        {
                            var = searchImage("haarcascade/palm.xml");
                            if (var.length == 0)
                            {
                                var = searchImage("haarcascade/fist.xml");
                                if (var.length != 0)
                                {
                                    ExchangeMessageWithServer.sendMessage("клик", out, in);
                                }
                            } else
                            {
                                manager(var[0]);
                            }
                        }
                        app.printPhoto(imageIcon, new MatOfRect(var));
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
     * toBufferedImage двумерный массив заданный в RGB отображается попиксельно
     *
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
     *
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
     * manager Анализатор изображения и отправляет соответствующие команды
     *
     * @param Rect r распознанный объект
     * @return void
     * @throws IOException
     */
    private void manager(Rect r) throws IOException
    {
        if ((rect != null) && !rect.empty())
        {
            if (palmX == -1 || palmY == -1)
            {
                palmX = r.x;
                palmY = r.y;
                return;
            } else
            {
                if(r.x > palmX) ExchangeMessageWithServer.sendMessage("вправо", out, in);
                else ExchangeMessageWithServer.sendMessage("влево", out, in);
                if(r.y > palmY) ExchangeMessageWithServer.sendMessage("вниз", out, in);
                else ExchangeMessageWithServer.sendMessage("вверх", out, in);
                palmX = r.x;
                palmY = r.y;
            }
        }
    }

    public static void onHand()
    {
        state = 1;
        palmX = -1;
        palmX = -1;
    }
    
    public static void onDefault()
    {
        state = 3;
        palmX = -1;
        palmX = -1;
    }


}

/**
 * ImagePanel Класс для отображения графического интерфейса а также вывод
 * изображения с web-камеры
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
     * paintComponent метод перерисовывающий под jPanel
     *
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
 * MainFrame фрейм отбражающий изображение с web-камеры Java version j8
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
    public static JTextArea output;

    MainFrame()
    {
        this.setName("Камера");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        ip = new ImagePanel();
        String TEXT = "Вас приветствует помощник\n"
                + "виртуального пространства!";
        JTextArea output = new JTextArea(10, 20);
        output.setText(TEXT);
        output.setCaretPosition(0);
        final JScrollPane scrollPane = new JScrollPane(output);
        this.add(ip, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.SOUTH);
        this.pack();
        this.setSize(290, 480);
    }

    public void printPhoto(ImageIcon ico, MatOfRect rect)
    {
        ip.Init(ico.getImage(), rect);
        ip.repaint();
    }
}
