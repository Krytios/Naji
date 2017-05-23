import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
public class Face_EyeDetection {
    // initializing and assigning the detectors
    private CascadeClassifier faceDetector = new CascadeClassifier(getClass().getClassLoader().getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
    private CascadeClassifier eyeDetector = new CascadeClassifier(getClass().getClassLoader().getResource("haarcascade_eye.xml").getPath().substring(1));
    // initializing and assigning the "containers" which will hold information about detected faces/eyes
    private MatOfRect detectedFaces = new MatOfRect();
    private MatOfRect detectedEyes = new MatOfRect();
    public static void main(String[] args) {
        // load OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // initialize and assign the detector
        Face_EyeDetection detector = new Face_EyeDetection();
        // start detecting
        detector.detect();
    }
    private void detect() {
        // initialize and assign the basic image container
        Mat mat = new Mat();
        // initialize and assign which camera to use, on most laptops index - 0 is the main laptop camera
        VideoCapture camera = new VideoCapture(0);
        // get the resolution of the the camera
        final Size frameSize = new Size(camera.get(Highgui.CV_CAP_PROP_FRAME_WIDTH), camera.get(Highgui.CV_CAP_PROP_FRAME_HEIGHT));
        JFrame frame = new JFrame();
        // Make X close the window
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel panel = new JLabel();
        frame.setContentPane(panel);
        frame.setSize((int) frameSize.width, (int) frameSize.height);
        frame.setVisible(true);
        // when the program stops (Pressed X button, closed from the IDE and etc) makes sure that the image container and the video capture are released
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(mat, camera));
        // while the camera is opened and is "reading" video
        while (camera.isOpened() && camera.read(mat)) {
            // detect the faces
            //faceDetector.detectMultiScale(mat, detectedFaces);
            // detect the eyes
            eyeDetector.detectMultiScale(mat, detectedEyes);
            // place a green rectangle over each face
            for (Rect face : detectedFaces.toArray()) {
                if(face.height == 0 && face.width == 0){
                    continue;
                }
                Core.rectangle(mat, new Point(face.x, face.y), new Point(face.x + face.width, face.y + face.height),
                        new Scalar(0, 255,0), 3);
            }
            // place a blue rectangle over each eye
            for (Rect eye : detectedEyes.toArray()) {
                if(eye.height == 0 && eye.width == 0){
                    continue;
                }
                Core.rectangle(mat, new Point(eye.x, eye.y), new Point(eye.x + eye.width, eye.y + eye.height),
                        new Scalar(255, 12, 0), 3);
            }
            // converts the basic image container to image
            ImageIcon image = new ImageIcon(mat2BufferedImage(mat));
            // sets the image
            panel.setIcon(image);
            // refreshes the panel in the frame
            panel.repaint();
        }
    }
    private BufferedImage mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage img = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return img;
    }
    private class ShutdownThread extends Thread {
        private Mat mat;
        private VideoCapture videoCapture;
        public ShutdownThread(Mat mat, VideoCapture videoCapture) {
            this.mat = mat;
            this.videoCapture = videoCapture;
        }
        @Override
        public void run() {
            super.run();
            mat.release();
            videoCapture.release();
        }
    }
}
