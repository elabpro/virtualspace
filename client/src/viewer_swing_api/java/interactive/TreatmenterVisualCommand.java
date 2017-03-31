/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactive;

import java.awt.AWTException;
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
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
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

public class TreatmenterVisualCommand extends Thread
{

    private Image tempImage;
    private CascadeClassifier Detector;
    private MatOfRect rect;
    private ImageIcon imageIcon = null;
    private MainFrame app = null;
    private Mat webcamMatImage;
    private String side = "";
    private Robot robot;
    private static int state;

    Dimension ScreenSize; //Размеры окна компьютера
    Dimension WindowSize;

    public TreatmenterVisualCommand()
    {
        this.webcamMatImage = new Mat();
        this.app = new MainFrame();
        this.state = 1;
        this.ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.WindowSize = new Dimension(300, 400);
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
            this.robot = new Robot();
            VideoCapture capture = new VideoCapture(0);
            capture.set(Videoio.CAP_PROP_FRAME_WIDTH, WindowSize.width);
            capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, WindowSize.height);
            if (capture.isOpened())
            {
                state = 1;

                while (state > 0)
                {
                    Rect[] var = null;
                    capture.read(webcamMatImage);
                    if (!webcamMatImage.empty())
                    {
                        var = searchImage("haarcascade/palm.xml");
                        if (var.length == 0 && state > 2)
                        {
                            var = searchImage("haarcascade/fist.xml");
                            if (var.length != 0)
                            {
                                robot.mousePress(InputEvent.BUTTON1_MASK);
                                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                                System.out.println("клик");
                            }
                        } else
                        {
                            manager(var[0]);
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
        } catch (AWTException ex)
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
            double x = r.x + ((double) r.width) / 2;
            double y = r.y + ((double) r.height) / 2;

            x = (x / this.WindowSize.width) * this.ScreenSize.width;
            y = (y / this.WindowSize.height) * this.ScreenSize.height;

            robot.mouseMove((int) x, (int) y);
        }
    }

    public static void onHand()
    {
        state = 2;
    }

    public static void onHandAndFist()
    {
        state = 3;
    }

    public static void onDefault()
    {
        state = 1;
    }

}

/**
 * ImagePanel Класс для отображения графического интерфейса а также вывод
 * изображения с web-камеры
 *
 * @author glebmillenium
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
 * @author glebmillenium
 */
class MainFrame extends JFrame
{

    private ImagePanel ip;
    MatOfRect rects;
    public static JTextArea output;
    public static String TEXT = "";
    public static boolean updateText = false;

    MainFrame()
    {
        this.setName("Камера");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        ip = new ImagePanel();
        TEXT = "Вас приветствует помощник\n"
                + "виртуального пространства!";
        output = new JTextArea(10, 20);
        output.setText(TEXT);
        output.setCaretPosition(0);
        final JScrollPane scrollPane = new JScrollPane(output);
        this.add(ip, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.SOUTH);
        this.pack();
        this.setSize(290, 480);
    }

    public void printPhoto(ImageIcon ico, MatOfRect rect) throws InterruptedException
    {
        if (updateText)
        {
            output.setText(TEXT);
            updateText = false;
            Thread.sleep(100);
        }
        ip.Init(ico.getImage(), rect);
        ip.repaint();
    }

    public static void updateText(String text)
    {
        TEXT = "Доступные команды: \n" + text;
        updateText = true;
    }
}
