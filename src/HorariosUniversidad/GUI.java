package HorariosUniversidad;

import javafx.concurrent.Task;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class GUI {
    private JPanel panel1;
    private JButton editarBaseDeDatosButton;
    private JButton iniciarAlgoritmoButton;
    private JTextField InPob;
    private JTextField InMut;
    private JTextField InCruce;
    private JTextField InElit;
    private JTextField InTam;
    private JButton abrirCarpetaButton;
    private JProgressBar progressBar1;
    private JLabel LabelTiempo;
    private JLabel LabelGeneraciones;
    private JLabel LabelEmpalmes;
    private double mutacion, cruce, fitness = 0;
    private int pob, elitismo, tamano;
    private long min, sec;
    private Poblacion poblacion;
    Thread algoritmo, progress;
    Horario horario;

    public GUI() {
        iniciarAlgoritmoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarAlgoritmoButton.setEnabled(false);

                pob = Integer.parseInt(InPob.getText());
                elitismo = Integer.parseInt(InElit.getText());
                tamano = Integer.parseInt(InTam.getText());
                mutacion = Double.parseDouble(InMut.getText());
                cruce = Double.parseDouble(InCruce.getText());

                enableText(false);
                horario = creaHorario();

                progressBar1.setIndeterminate(true);
                progressBar1.setStringPainted(false);

                Task a = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return null;
                    }

                    @Override
                    public void run() {
                        super.run();
                        operaciones(horario);
                        super.done();
                    }
                };
                Thread thread = new Thread(a);
                thread.setDaemon(true);
                thread.start();

            }
        });
        abrirCarpetaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File currDir = new File(".");
                String path = currDir.getAbsolutePath();
                path = path.substring(0, path.length() - 1) + "horario.xlsx";
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + path);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void operaciones(Horario horario) {
        long tInicio, tActual;
        tInicio = System.currentTimeMillis();
        LabelEmpalmes.setText("");
        Ag ag = new Ag(pob, mutacion, cruce, elitismo, tamano);

        poblacion = ag.iniciaPoblacion(horario);
        ag.evalPoblacion(poblacion, horario);

        int generacion = 1;

        while (!ag.isTerminationConditionMet(generacion, 10000) && !ag.isTerminationConditionMet(poblacion)) {
            tActual = System.currentTimeMillis();
            min = ((tActual - tInicio) / 1000) / 60;
            sec = ((tActual - tInicio) / 1000) % 60;
            LabelTiempo.setText(String.format("Tiempo Transcurrido %02d:%02d", min, sec));
            LabelGeneraciones.setText(generacion+" Generaciones");
            fitness = poblacion.getFittest(0).getFitness();
            poblacion = ag.crucePoblacion(poblacion);
            poblacion = ag.mutaPoblacion(poblacion, horario);
            ag.evalPoblacion(poblacion, horario);
            generacion++;

        }

        if (generacion<10000){
            horario.creaClases(poblacion.getFittest(0));
            Clase[] clases = horario.getClases();
            ExcelHelper excelHelper = new ExcelHelper();
            excelHelper.generarHorariosProfesores(clases, horario);
            progressBar1.setIndeterminate(false);
            progressBar1.setValue(100);
            progressBar1.setString("COMPLETADO");
            progressBar1.setStringPainted(true);
            iniciarAlgoritmoButton.setEnabled(true);
            LabelEmpalmes.setText("Empalmes: "+horario.calcClases());
        }else{
            progressBar1.setIndeterminate(false);
            progressBar1.setValue(0);
            progressBar1.setString("NO SE ENCONTRÓ SOLUCIÓN");
            progressBar1.setStringPainted(true);
            iniciarAlgoritmoButton.setEnabled(true);
            LabelEmpalmes.setText("");
        }



        enableText(true);

    }

    private void enableText(boolean b) {
        InCruce.setEnabled(b);
        InElit.setEnabled(b);
        InMut.setEnabled(b);
        InPob.setEnabled(b);
        InTam.setEnabled(b);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private static Horario creaHorario() {
        Horario horario = new Horario();

        horario.addAula(1, "A1", 40);
        horario.addAula(2, "A2", 40);
        horario.addAula(3, "A3", 40);
        horario.addAula(4, "A4", 40);
        horario.addAula(5, "A5", 40);
        horario.addAula(6, "A6", 40);
        horario.addAula(7, "A7", 40);
        horario.addAula(8, "A8", 40);
        horario.addAula(9, "A9", 40);
        horario.addAula(10, "A10", 40);
        horario.addAula(11, "A11", 40);
        horario.addAula(12, "A12", 40);
        horario.addAula(13, "A13", 40);
        horario.addAula(14, "A14", 40);
        horario.addAula(15, "A15", 40);
        horario.addAula(16, "A16", 40);
        horario.addAula(17, "A17", 40);
        horario.addAula(18, "A18", 40);
        horario.addAula(19, "A19", 40);
        horario.addAula(20, "A20", 40);

        horario.addTimpoEspacio(1, "Lun 7:00 - 8:00", 3);
        horario.addTimpoEspacio(2, "Lun 8:00 - 9:00", 2);
        horario.addTimpoEspacio(3, "Lun 9:00 - 10:00", 3);
        horario.addTimpoEspacio(4, "Lun 10:00 - 11:00", 1);
        horario.addTimpoEspacio(5, "Lun 11:00 - 12:00", 2);
        horario.addTimpoEspacio(6, "Lun 12:00 - 13:00", 2);
        horario.addTimpoEspacio(7, "Lun 13:00 - 14:00", 2);
        horario.addTimpoEspacio(8, "Lun 14:00 - 15:00", 1);
        horario.addTimpoEspacio(9, "Lun 15:00 - 16:00", 2);
        horario.addTimpoEspacio(10, "Lun 16:00 - 17:00", 2);
        horario.addTimpoEspacio(11, "Lun 17:00 - 18:00", 2);
        horario.addTimpoEspacio(12, "Lun 18:00 - 19:00", 2);
        horario.addTimpoEspacio(70, "Lun 19:00 - 20:00", 2);

        horario.addTimpoEspacio(13, "Mar 7:00 - 8:00", 2);
        horario.addTimpoEspacio(14, "Mar 8:00 - 9:00", 2);
        horario.addTimpoEspacio(15, "Mar 9:00 - 10:00", 2);
        horario.addTimpoEspacio(16, "Mar 10:00 - 11:00", 2);
        horario.addTimpoEspacio(17, "Mar 11:00 - 12:00", 1);
        horario.addTimpoEspacio(18, "Mar 12:00 - 13:00", 1);
        horario.addTimpoEspacio(19, "Mar 13:00 - 14:00", 1);
        horario.addTimpoEspacio(20, "Mar 14:00 - 15:00", 1);
        horario.addTimpoEspacio(21, "Mar 15:00 - 16:00", 2);
        horario.addTimpoEspacio(22, "Mar 16:00 - 17:00", 2);
        horario.addTimpoEspacio(23, "Mar 17:00 - 18:00", 2);
        horario.addTimpoEspacio(24, "Mar 18:00 - 19:00", 2);
        horario.addTimpoEspacio(71, "Mar 19:00 - 20:00", 2);

        horario.addTimpoEspacio(25, "Mie 7:00 - 8:00", 3);
        horario.addTimpoEspacio(26, "Mie 8:00 - 9:00", 2);
        horario.addTimpoEspacio(27, "Mie 9:00 - 10:00", 3);
        horario.addTimpoEspacio(28, "Mie 10:00 - 11:00", 1);
        horario.addTimpoEspacio(29, "Mie 11:00 - 12:00", 1);
        horario.addTimpoEspacio(30, "Mie 12:00 - 13:00", 1);
        horario.addTimpoEspacio(31, "Mie 13:00 - 14:00", 1);
        horario.addTimpoEspacio(32, "Mie 14:00 - 15:00", 1);
        horario.addTimpoEspacio(33, "Mie 15:00 - 16:00", 2);
        horario.addTimpoEspacio(34, "Mie 16:00 - 17:00", 2);
        horario.addTimpoEspacio(35, "Mie 17:00 - 18:00", 2);
        horario.addTimpoEspacio(36, "Mie 18:00 - 19:00", 2);
        horario.addTimpoEspacio(72, "Mie 19:00 - 20:00", 2);

        horario.addTimpoEspacio(37, "Jue 7:00 - 8:00", 1);
        horario.addTimpoEspacio(38, "Jue 8:00 - 9:00", 1);
        horario.addTimpoEspacio(39, "Jue 9:00 - 10:00", 1);
        horario.addTimpoEspacio(40, "Jue 10:00 - 11:00", 1);
        horario.addTimpoEspacio(41, "Jue 11:00 - 12:00", 1);
        horario.addTimpoEspacio(42, "Jue 12:00 - 13:00", 1);
        horario.addTimpoEspacio(43, "Jue 13:00 - 14:00", 1);
        horario.addTimpoEspacio(44, "Jue 14:00 - 15:00", 1);
        horario.addTimpoEspacio(45, "Jue 15:00 - 16:00", 2);
        horario.addTimpoEspacio(46, "Jue 16:00 - 17:00", 2);
        horario.addTimpoEspacio(47, "Jue 17:00 - 18:00", 2);
        horario.addTimpoEspacio(48, "Jue 18:00 - 19:00", 2);
        horario.addTimpoEspacio(73, "Jue 19:00 - 20:00", 2);

        horario.addTimpoEspacio(49, "Vie 7:00 - 8:00", 1);
        horario.addTimpoEspacio(50, "Vie 8:00 - 9:00", 1);
        horario.addTimpoEspacio(51, "Vie 9:00 - 10:00", 1);
        horario.addTimpoEspacio(52, "Vie 10:00 - 11:00", 1);
        horario.addTimpoEspacio(53, "Vie 11:00 - 12:00", 1);
        horario.addTimpoEspacio(54, "Vie 12:00 - 13:00", 1);
        horario.addTimpoEspacio(55, "Vie 13:00 - 14:00", 1);
        horario.addTimpoEspacio(56, "Vie 14:00 - 15:00", 1);
        horario.addTimpoEspacio(57, "Vie 15:00 - 16:00", 2);
        horario.addTimpoEspacio(58, "Vie 16:00 - 17:00", 2);
        horario.addTimpoEspacio(59, "Vie 17:00 - 18:00", 2);
        horario.addTimpoEspacio(60, "Vie 18:00 - 19:00", 2);
        horario.addTimpoEspacio(74, "Vie 19:00 - 20:00", 2);

        horario.addTimpoEspacio(61, "Sab 7:00 - 8:00", 2);
        horario.addTimpoEspacio(62, "Sab 8:00 - 9:00", 2);
        horario.addTimpoEspacio(63, "Sab 10:00 - 11:00", 2);
        horario.addTimpoEspacio(64, "Sab 11:00 - 12:00", 2);
        horario.addTimpoEspacio(65, "Sab 12:00 - 13:00", 2);
        horario.addTimpoEspacio(66, "Sab 13:00 - 14:00", 2);
        horario.addTimpoEspacio(67, "Sab 14:00 - 15:00", 2);
        horario.addTimpoEspacio(68, "Sab 15:00 - 16:00", 2);
        horario.addTimpoEspacio(69, "Sab 16:00 - 17:00", 2);
        horario.addTimpoEspacio(74, "Sab 19:00 - 20:00", 2);


        //informatica
        horario.addProfessor(1, "P1 Sam", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 19, 20, 21, 25, 26, 27, 28, 29, 30, 31, 32, 33, 37, 38, 39, 40, 41, 42, 43, 44, 45});
        horario.addProfessor(2, "P2 Ivan", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46});
        horario.addProfessor(3, "P3 Tello", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 47, 48, 49, 50, 51, 52, 53, 54});
        horario.addProfessor(4, "P4 Tola", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58});
        horario.addProfessor(5, "P5 Pera", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56});
        horario.addProfessor(6, "P6 Hugo", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44});
        horario.addProfessor(7, "P7 Jessica", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 49, 50, 51, 52, 53, 54, 55, 56, 25, 26, 27, 28, 29, 30, 31, 32, 33});
        horario.addProfessor(8, "P8 Jose M", new int[]{9, 10, 11, 12, 21, 22, 23, 24, 33, 34, 35, 45, 46, 47, 48, 57, 58, 59, 60});

        horario.addProfessor(9, "P9 Eleazar", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 65, 66, 67, 68, 69});
        horario.addProfessor(10, "P10 Brian", new int[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 37, 38, 39, 40, 41, 42, 43, 44, 45, 49, 50, 51, 52, 53, 54, 55, 56, 57});
        horario.addProfessor(11, "P11 jacky", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34});
        horario.addProfessor(12, "P12 alejandra", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58});
        horario.addProfessor(13, "P13 Jennifer", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28});

        //contaduria
        horario.addProfessor(14, "P14 Jazmin", new int[]{37, 38, 39, 40, 41, 42, 43, 44, 45, 49, 50, 51, 52, 53, 54, 55, 56, 57});
        horario.addProfessor(15, "15 Bianka", new int[]{37, 38, 39, 40, 41, 41, 43, 44, 45, 50, 51, 52, 53, 54, 55, 56, 57, 61, 62, 63, 64, 65, 66});
        horario.addProfessor(16, "P16 Jose Juan", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 25, 26, 27, 28, 29, 30, 31, 49, 50, 51, 52, 53, 54, 55, 56, 57});
        horario.addProfessor(17, "P17 Rosa M.", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 25, 26, 27, 28, 29, 30, 31, 32, 33, 13, 14, 15, 16, 17});
        horario.addProfessor(18, "P18 Yaneli", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 18, 19, 20, 25, 26, 27, 28, 29, 30, 31, 32, 33});
        horario.addProfessor(19, "P19 Teresa", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 19, 20, 21, 25, 26, 27, 28, 29, 30, 31, 32, 33, 37, 38, 39, 40, 41, 42, 43, 44, 45});

        horario.addProfessor(20, "P20 Irene", new int[]{3, 4, 19, 20, 21, 22, 28, 29, 30, 31, 32, 33, 37, 38, 39, 40, 41, 42, 43, 44, 45});
        horario.addProfessor(21, "P21 Miroslava", new int[]{22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47});
        horario.addProfessor(22, "P22 Faviola", new int[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 37, 38, 39, 40, 41, 42, 43, 44, 45});
        horario.addProfessor(23, "P23 Gloria", new int[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 49, 50, 51, 52, 53, 54, 55, 56, 57});

        //psicologia
        horario.addProfessor(24, "P24 Moises", new int[]{33, 34, 35, 36, 37, 38, 45, 46, 47, 48, 61, 62, 63, 64, 65, 66, 67});
        horario.addProfessor(25, "P25 Irwin", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 28, 29, 30, 31, 32, 33, 40, 41, 42, 43, 44, 45, 46, 52, 53, 54, 55, 56});
        horario.addProfessor(26, "P26 Monica S. C.", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 19, 25, 26, 27, 28, 29, 30, 31, 32});
        horario.addProfessor(27, "P27 Adriana B", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 19, 25, 26, 27, 28, 29, 30, 31, 32});
        horario.addProfessor(28, "P28 Yuli", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 18, 19, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58});
        horario.addProfessor(29, "P29 Maribel", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 19, 20, 21, 25, 26, 27, 28, 29, 30, 31, 37, 38, 39, 40, 41, 42, 43, 44, 45});
        horario.addProfessor(30, "P30 Ana Lilia", new int[]{37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60});
        horario.addProfessor(31, "P31 Sandra G.", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 19, 20, 21, 25, 26, 27, 28, 29, 30, 31, 32, 33, 37, 38, 39, 40, 41, 42, 43, 44, 45});

        //criminologia
        horario.addProfessor(32, "P32 Sandra F", new int[]{25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 1, 2, 3, 4, 5, 6, 7, 8, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58});
        horario.addProfessor(33, "P33 Jose Antonio", new int[]{49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61});
        horario.addProfessor(34, "P34 Paulina", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 37, 38, 39, 40, 41, 42, 43, 44, 45});
        horario.addProfessor(35, "P35 Joaquin", new int[]{3, 4, 5, 6, 7, 8, 9, 10, 49, 50, 51, 52, 53, 54, 55, 56, 57});
        horario.addProfessor(36, "P36 Jorge Luis", new int[]{25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 61, 62, 63, 64, 65, 66, 67, 68, 69});

        //enfermeria
        horario.addProfessor(37, "P37 Maria Ines", new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58});
        horario.addProfessor(38, "P38 Jose Jiovani", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46});
        horario.addProfessor(39, "P39 Ceciia M", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 19, 20, 49, 50, 51, 52, 53, 54, 55, 556, 57});
        horario.addProfessor(40, "P40 Hilario J", new int[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 37, 38, 39, 40, 41, 42, 43, 44});
        horario.addProfessor(41, "P41 Rosa N", new int[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58});
        horario.addProfessor(42, "P42 Edith H", new int[]{11, 12, 70, 23, 24, 71, 35, 36, 72, 47, 48, 73, 35, 36, 73, 47, 48, 74, 61, 66});
        horario.addProfessor(43, "P43 Alma Luisa", new int[]{4, 5, 6, 7, 8, 9, 10, 11, 16, 17, 18, 19, 20, 21, 22, 28, 29, 30, 31, 32, 33, 34, 35, 36, 40, 41, 42, 43, 44, 45, 46, 47, 52, 53, 54, 56, 57, 58});
        /////////////////////////////////////////////////////////////////////////////////////
        horario.addAsignatura(1, "IFM1", "Programacion I", new int[]{2});
        horario.addAsignatura(2, "IFM2", "Contabilidad", new int[]{7});
        horario.addAsignatura(3, "IFM3", "Fundamentos de Inv", new int[]{9});
        horario.addAsignatura(4, "IFM4", "Matematicas II", new int[]{6});
        horario.addAsignatura(5, "IFM5", "Organizacion de computadoras", new int[]{3});
        horario.addAsignatura(6, "IFM6", "Probabilidad", new int[]{8});
        horario.addAsignatura(7, "IFM7", "Ingles II", new int[]{10});

        horario.addAsignatura(8, "IFM8", "Organizacion de Datos", new int[]{1});
        horario.addAsignatura(9, "IFM9", "Programacion II", new int[]{2});
        horario.addAsignatura(10, "IFM10", "Software de Sistemas", new int[]{3});
        horario.addAsignatura(11, "IFM11", "Investigacion de Operaciones II", new int[]{8});
        horario.addAsignatura(12, "IFM12", "Fundamentos de Redes", new int[]{4});
        horario.addAsignatura(13, "IFM13", "Taller de investigacion I", new int[]{9});
        horario.addAsignatura(14, "IFM14", "Ingles IV", new int[]{11});

        horario.addAsignatura(15, "IFM1", "Taller de BD", new int[]{3});
        horario.addAsignatura(16, "IFM2", "Calidad del SW", new int[]{5});
        horario.addAsignatura(17, "IFM3", "Sist. Op. II", new int[]{4});
        horario.addAsignatura(18, "IFM4", "Sistemas de Inf II", new int[]{1});
        horario.addAsignatura(19, "IFM5", "Iterconectividad de redes", new int[]{2});
        horario.addAsignatura(20, "IFM6", "Taller de Inv. II", new int[]{9});
        horario.addAsignatura(21, "IFM7", "Sistemas Abiertos", new int[]{2});
        horario.addAsignatura(22, "IFM7", "Ingles VI", new int[]{11});

        horario.addAsignatura(23, "IFM1", "Desarrollo de App", new int[]{2});
        horario.addAsignatura(24, "IFM2", "Auditoria Inf.", new int[]{5});
        horario.addAsignatura(25, "IFM3", "Topicos Avanzados de BD", new int[]{4});
        horario.addAsignatura(26, "IFM5", "Evaluacion de proy", new int[]{13});
        horario.addAsignatura(27, "IFM6", "Optativa III", new int[]{3});
        horario.addAsignatura(28, "IFM7", "Optativa IV", new int[]{4});
        horario.addAsignatura(29, "IFM7", "Ingles tecnico I", new int[]{12});

        //contaduria
        horario.addAsignatura(30, "cont2", "Contabilidad Intermedia I", new int[]{19});
        horario.addAsignatura(31, "cont2", "Derecho Constitucional...", new int[]{15});
        horario.addAsignatura(32, "cont2", "Ingles III", new int[]{16});
        horario.addAsignatura(33, "cont2", "Taller de Informatica", new int[]{5});
        horario.addAsignatura(34, "cont2", "Admon de R.H.", new int[]{17});
        horario.addAsignatura(35, "cont2", "Estadistica Adm.", new int[]{8});
        horario.addAsignatura(36, "cont2", "Matematicas Financieras", new int[]{18});
        horario.addAsignatura(37, "cont2", "Fundamentos de Inv", new int[]{9});

        horario.addAsignatura(38, "cont4", "Desarrollo Sustentable", new int[]{20});
        horario.addAsignatura(39, "cont4", "Finanzas I", new int[]{7});
        horario.addAsignatura(40, "cont4", "Ingles 4", new int[]{21});
        horario.addAsignatura(41, "cont4", "Teoria Contable", new int[]{19});
        horario.addAsignatura(42, "cont4", "Derecho Fiscal", new int[]{15});
        horario.addAsignatura(43, "cont4", "Costos Historicos", new int[]{18});
        horario.addAsignatura(44, "cont4", "Entorno Macro Economico", new int[]{13});
        horario.addAsignatura(45, "cont4", "Taller de investigacion I", new int[]{9});

        horario.addAsignatura(46, "cont6", "Contabilidad para Soc", new int[]{7});
        horario.addAsignatura(47, "cont6", "Finanzas III", new int[]{22});
        horario.addAsignatura(48, "cont6", "Auditoria Financ. II", new int[]{23});
        horario.addAsignatura(49, "cont6", "Presupuestos", new int[]{23});
        horario.addAsignatura(50, "cont6", "Talle de Inf III", new int[]{19});
        horario.addAsignatura(51, "cont6", "Impuestos II", new int[]{18});
        horario.addAsignatura(52, "cont6", "ingles 6", new int[]{11});
        horario.addAsignatura(53, "cont6", "Taller de inv II", new int[]{9});

        horario.addAsignatura(54, "cont8", "Contabilidad Bancaria", new int[]{7});
        horario.addAsignatura(55, "cont8", "Seminario de Titulacion", new int[]{22});
        horario.addAsignatura(56, "cont8", "Contabililidad Hotelera", new int[]{23});
        horario.addAsignatura(57, "cont8", "Contabilidad Gubernamental", new int[]{23});
        horario.addAsignatura(58, "cont8", "Talleres de practicas fiscales", new int[]{19});
        horario.addAsignatura(59, "cont8", "ingles tecnico i", new int[]{12});
        horario.addAsignatura(60, "cont8", "Seminario de contaduria", new int[]{18});
        horario.addAsignatura(61, "cont8", "Habilidades directivas", new int[]{13});

        //psicologia
        horario.addAsignatura(62, "psico2", "Base de D. Elect", new int[]{1});
        horario.addAsignatura(63, "psico2", "Fund. de Inv", new int[]{24}); //moises
        horario.addAsignatura(64, "psico2", "Tecnicas de Ent", new int[]{25});//irwin
        horario.addAsignatura(65, "psico2", "Presentaciones elect", new int[]{3});
        horario.addAsignatura(66, "psico2", "Taller de Valores", new int[]{26});//monica s.c.
        horario.addAsignatura(67, "psico2", "Taller de Exp", new int[]{27});//adriana
        horario.addAsignatura(68, "psico2", "Ingles II", new int[]{10});//brian
        horario.addAsignatura(69, "psico2", "Planeacioin Est", new int[]{17});//rosa maria

        horario.addAsignatura(70, "psico4", "Admon de Sueldos u Salarios", new int[]{22});//faviola
        horario.addAsignatura(71, "psico4", "Ingles IV", new int[]{21});
        horario.addAsignatura(72, "psico4", "Estadistica II", new int[]{28});//yuli
        horario.addAsignatura(73, "psico4", "Desarrollo Sustentable", new int[]{28});
        horario.addAsignatura(74, "psico4", "Introduccion al Estudio del Der.", new int[]{30});//analilia
        horario.addAsignatura(75, "psico4", "Investigacion Cualitativa", new int[]{26});
        horario.addAsignatura(76, "psico4", "Evaluacion Psicometrica", new int[]{29});//maribel
        horario.addAsignatura(77, "psico4", "Administracion Estrategica", new int[]{17});//rosa m

        horario.addAsignatura(78, "psico6", "Control de Calidad", new int[]{28});//yuli
        horario.addAsignatura(79, "psico6", "Derecho a la Seg. Social", new int[]{30});
        horario.addAsignatura(80, "psico6", "Capac. a traves de Couch", new int[]{26});
        horario.addAsignatura(81, "psico6", "Problemas Psicosociales en la Ind.", new int[]{29});
        horario.addAsignatura(82, "psico6", "Entrevistas por Comp.", new int[]{29});
        horario.addAsignatura(83, "psico6", "Formacion y Desarrollo de Dir.", new int[]{13}); //jenifer
        horario.addAsignatura(84, "psico6", "Ingles VI", new int[]{11});
        horario.addAsignatura(85, "psico6", "Seguridad e higiene ind.", new int[]{6});//hugo

        horario.addAsignatura(86, "psico8", "Mercado de Talento H", new int[]{24});//moises
        horario.addAsignatura(87, "psico8", "Desarrollo Organizacional", new int[]{29}); //maribel
        horario.addAsignatura(88, "psico8", "Estres y Salud en las organizaciones", new int[]{25});//irwin
        horario.addAsignatura(89, "psico8", "Desarrollo de Emprendedores", new int[]{27});//adriana
        horario.addAsignatura(90, "psico8", "Certificacion de personal por Comp.", new int[]{29});
        horario.addAsignatura(91, "psico8", "Ingles Tecnico 1", new int[]{12});
        horario.addAsignatura(92, "psico8", "Seminario de Relaciones Ind", new int[]{6});
        horario.addAsignatura(93, "psico8", "Seminario de Tesis I", new int[]{31});//sandra

        //criminologia
        horario.addAsignatura(94, "Crimi2", "Derecho Constitucional", new int[]{14});//JAZMIN
        horario.addAsignatura(95, "Crimi2", "Ejecucion de Penas", new int[]{32});//SANDRA
        horario.addAsignatura(96, "Crimi2", "Historia y tendencias", new int[]{32});//SANDRA
        horario.addAsignatura(97, "Crimi2", "Ingles II", new int[]{16});//jOSE JUAN
        horario.addAsignatura(98, "Crimi2", "Metodologia de la Inv II", new int[]{9});//ELEAZAR
        horario.addAsignatura(99, "Crimi2", "Lab de Quimica y Toxicologia", new int[]{33});//JOSE ANTONIO
        horario.addAsignatura(100, "Crimi2", "Probabilidad y Estadistica", new int[]{8});//JOSE MANUEL R

        horario.addAsignatura(102, "Crimi4", "Sistemas de Ident. Hum.", new int[]{34});//PAULINA
        horario.addAsignatura(103, "Crimi4", "Argumentacion Crim.", new int[]{35});// JOACO
        horario.addAsignatura(104, "Crimi4", "Laboratorio Fotografico", new int[]{35});//
        horario.addAsignatura(105, "Crimi4", "Psicopatologia", new int[]{25});//IRWIN
        horario.addAsignatura(106, "Crimi4", "Criminologia II", new int[]{32});//
        horario.addAsignatura(107, "Crimi4", "Derecho Penal I", new int[]{15});//BIANKA
        horario.addAsignatura(108, "Crimi4", "Ingles IV", new int[]{16});// JJ
        horario.addAsignatura(109, "Crimi4", "InformaticaII", new int[]{5});//

        horario.addAsignatura(110, "Crimi6", "Ingles VI", new int[]{21});//MIROSLAVA
        horario.addAsignatura(111, "Crimi6", "Hechos de Transito", new int[]{34});//PAULINA
        horario.addAsignatura(112, "Crimi6", "Peritajes Tecnicos", new int[]{34});//PAULINA
        horario.addAsignatura(113, "Crimi6", "Derecho Procesal", new int[]{14});//JAZMIN
        horario.addAsignatura(114, "Crimi6", "Sistema Integral", new int[]{36});//JORGE LUIS
        horario.addAsignatura(115, "Crimi6", "Metodos de Evaluacion", new int[]{36});//JORGE LUIS
        horario.addAsignatura(116, "Crimi6", "Medicina forense", new int[]{37});//MARIA INES
        horario.addAsignatura(117, "Crimi6", "Psicologia Criminologica", new int[]{30});//SANDRA G

        horario.addAsignatura(118, "Crimi8", "Dactiloscopia", new int[]{34});//PAULINA
        horario.addAsignatura(119, "Crimi8", "Balistica Forense", new int[]{35});//JOAQUIN
        horario.addAsignatura(120, "Crimi8", "Politica Criminologica", new int[]{32});//SANDRA FLORES
        horario.addAsignatura(121, "Crimi8", "Documentoscopia", new int[]{36});//JORGE LUIS
        horario.addAsignatura(122, "Crimi8", "Policiologia", new int[]{36});//JORGE LUIS
        horario.addAsignatura(123, "Crimi8", "Ingles Tecnico I", new int[]{11});//JACQUELINE
        horario.addAsignatura(124, "Crimi8", "Seminario de Tesis", new int[]{9});//ELEAZAR
        horario.addAsignatura(125, "Crimi8", "Genetica Forense", new int[]{33});//JOSE ANTONIO


        //Enfermeria
        horario.addAsignatura(126, "Enfe2", "Metodologia Educativa", new int[]{25});//IRWIN
        horario.addAsignatura(127, "Enfe2", "Fundamentos Teorico M.", new int[]{38});//JIOVANI
        horario.addAsignatura(128, "Enfe2", "Ingles II", new int[]{10});//BRIAN
        horario.addAsignatura(129, "Enfe2", "Autocuidado de la Salud", new int[]{39});//CECILIA
        horario.addAsignatura(130, "Enfe2", "FISIOLOGÍA GENERAL", new int[]{37});//MARIA INES
        horario.addAsignatura(131, "Enfe2", "INFORMÁTICA II", new int[]{5});//DAVID ALEJANDRO
        horario.addAsignatura(132, "Enfe2", "BASES FILOSÓFICA", new int[]{40});//HILARIO

        horario.addAsignatura(134, "Enfe4", "ENFERMERÍA Y MEDICINA", new int[]{38});//JIOVANI
        horario.addAsignatura(135, "Enfe4", "INGLÉS IV", new int[]{10});//BRIAN
        horario.addAsignatura(136, "Enfe4", "MICROBIOLOGÍA", new int[]{37});//MARIA INES
        horario.addAsignatura(137, "Enfe4", "GENÉTICA", new int[]{37});//MARIA INES
        horario.addAsignatura(138, "Enfe4", "MODELOS Y TEORÍAS", new int[]{41});//ROSA NORMA
        horario.addAsignatura(139, "Enfe4", "INFORMÁTICA", new int[]{5});//DAVID ALEJANDRO
        horario.addAsignatura(140, "Enfe4", "ADMINISTRACIÓN", new int[]{42});//EDITH HERNANDEZ

        horario.addAsignatura(142, "Enfe6", "SALUD MENTAL", new int[]{25});//IRWIN
        horario.addAsignatura(143, "Enfe6", "ENFERMERÍA DEL NIÑO", new int[]{43});//ALMA LUISA
        horario.addAsignatura(144, "Enfe6", "ENFERMERÍA GERIÁTRICa", new int[]{38});//JIOVANI
        horario.addAsignatura(145, "Enfe6", "ENFERMERÍA GINECOLOGICA", new int[]{39});//CECILIA
        horario.addAsignatura(146, "Enfe6", "PRÁCTICA CLÍNICA", new int[]{41});//ROSA NORMA
        horario.addAsignatura(147, "Enfe6", "INGLÉS VI", new int[]{12});//ALEJANDRA
        horario.addAsignatura(148, "Enfe6", "METODOLOGÍA", new int[]{30});//SANDRA GONZALEZ
        horario.addAsignatura(149, "Enfe6", "ÉTICA Y LEGISLACIÓN", new int[]{40});//HILARIO

        horario.addAsignatura(150, "Enfe8", "ENFERMERÍA EN CUIDADOS INT", new int[]{43, 38});//ALMA LUISA,JOSE JIOVANI
        horario.addAsignatura(151, "Enfe8", "OPTATIVA 1", new int[]{38, 42});//JOSE JIOVANI, EDITH
        horario.addAsignatura(152, "Enfe8", "ENFERMERÍA EN LA SALUD", new int[]{39, 39});//CECILIA,CECILIA
        horario.addAsignatura(153, "Enfe8", "ENFERMERÍA EN APOYO", new int[]{39, 37});//CECILIA,MARIA INES
        horario.addAsignatura(154, "Enfe8", "OPTATIVA 2", new int[]{41, 42});//ROSA NORMA, EDITH
        horario.addAsignatura(155, "Enfe8", "OPTATIVA 3", new int[]{41, 43});//ROSA NORMA,ALMA LUISA
        horario.addAsignatura(156, "Enfe8", "INGLÉS TÉCNICO I", new int[]{16, 16});//JOSE JUAN,JOSE JUAN


        //(id,cantidad de alumnos, materias y numero de veces, aula)
        //informatica
        horario.addGrupo(1, 10, new int[]{1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 4, 5, 6, 7, 1, 2, 4, 5, 6, 7, 1, 2, 4, 5, 6, 7}, 1);
        horario.addGrupo(2, 10, new int[]{8, 9, 11, 12, 13, 14, 8, 9, 10, 11, 12, 13, 14, 8, 9, 10, 11, 12, 14, 8, 9, 10, 11, 12, 14, 8, 9, 10, 11, 12, 14}, 2);
        horario.addGrupo(3, 10, new int[]{17, 18, 19, 20, 22, 15, 16, 17, 18, 19, 20, 21, 22, 15, 16, 17, 18, 19, 20, 21, 22, 15, 16, 17, 18, 19, 20, 21, 22, 15, 16, 17, 18, 19, 20, 21, 22}, 3);
        horario.addGrupo(4, 10, new int[]{23, 24, 25, 27, 28, 29, 23, 24, 25, 26, 27, 28, 29, 23, 24, 25, 26, 27, 28, 29, 23, 24, 25, 26, 27, 28, 29, 23, 24, 25, 26, 27, 28, 29}, 4);

        //contaduria
        horario.addGrupo(5, 10, new int[]{30, 34, 35, 30, 31, 32, 33, 34, 35, 36, 30, 31, 32, 33, 34, 35, 36, 37, 30, 31, 32, 33, 34, 35, 36, 37, 30, 31, 32, 33, 34, 35, 36, 37}, 5);
        horario.addGrupo(6, 10, new int[]{38, 39, 40, 42, 43, 44, 38, 39, 40, 41, 42, 43, 44, 38, 39, 40, 41, 42, 43, 44, 45, 38, 39, 40, 41, 42, 43, 44, 45, 38, 39, 40, 41, 42, 43, 44, 45}, 6);
        horario.addGrupo(7, 10, new int[]{46, 46, 47, 48, 48, 50, 51, 51, 52, 46, 47, 48, 49, 50, 51, 52, 46, 47, 48, 49, 50, 51, 52, 53, 46, 47, 48, 49, 50, 51, 52, 53}, 7);
        horario.addGrupo(8, 10, new int[]{55, 57, 58, 59, 58, 54, 55, 56, 57, 58, 59, 60, 61, 54, 55, 56, 57, 58, 59, 60, 61, 54, 55, 56, 57, 58, 59, 60, 61, 54, 55, 56, 57, 58, 59, 60, 61}, 8);

        //psicologia
        horario.addGrupo(9, 10, new int[]{62, 64, 65, 67, 68, 69, 62, 64, 65, 66, 67, 68, 69, 62, 63, 64, 65, 66, 67, 68, 69, 62, 63, 64, 65, 66, 67, 68, 69, 62, 63, 64, 65, 66, 67, 68, 69}, 9);
        horario.addGrupo(10, 10, new int[]{70, 71, 72, 75, 76, 77, 70, 71, 72, 73, 74, 75, 76, 77, 70, 71, 72, 73, 74, 75, 76, 77, 70, 71, 72, 73, 74, 75, 76, 77, 70, 71, 72, 73, 74, 75, 76, 77}, 10);
        horario.addGrupo(11, 10, new int[]{78, 79, 80, 82, 83, 84, 85, 78, 79, 80, 81, 82, 83, 84, 85, 78, 79, 80, 81, 82, 83, 84, 85, 78, 79, 80, 81, 82, 83, 84, 85, 78, 79, 80, 81, 82, 83, 84, 85}, 11);
        horario.addGrupo(12, 10, new int[]{86, 87, 88, 89, 90, 91, 92, 86, 87, 88, 89, 90, 91, 92, 93, 86, 87, 88, 89, 90, 91, 92, 93, 86, 87, 88, 89, 90, 91, 92, 93, 86, 87, 88, 89, 90, 91, 92, 93}, 12);

        //criminologia
        //horario.addGrupo(13,10, new int[]{94,97,98,100, 94,95,96,97,98,100, 94,95,96,97,98,99,100, 94,95,96,97,98,99,100, 94,95,96,97,98,99,100  },13);
        //horario.addGrupo(14,10,new int[]{102,105,106,108, 102,103,105,106,108,109, 102,103,104,105,106,107,108,109, 102,103,104,105,106,107,108,109, 102,103,104,105,106,107,108,109 },14);
        //horario.addGrupo(15,10,new int[]{110,111,112,113,114,115,116,117, 110,111,112,113,114,115,116,117, 110,111,112,113,114,115,116,117, 110,111,112,113,114,115,116,117, 110,111,112,113,114,115,116,117 },15);
        //horario.addGrupo(16,10,new int[]{118,119,120,121,122,123,124,125, 118,119,120,121,122,123,124,125, 118,119,120,121,122,123,124,125, 118,119,120,121,122,123,124,125, 118,119,120,121,122,123,124,125 },16);

        //Enfermeria
        //horario.addGrupo(17,10,new int[]{126,127,128,129,130,131,132, 126,127,128,129,130,131,132, 126,127,128,129,130,131,132, 126,127,128,129,130,131,132, 126,127,128,129,130,131,132},17);
        //horario.addGrupo(18,10,new int[]{134,135,136,137,138,139,140, 134,135,136,137,138,139,140, 134,135,136,137,138,139,140, 134,135,136,137,138,139,140, 134,135,136,137,138,139,140},18);
        //horario.addGrupo(19,10,new int[]{142,143,144,145,146,147,148,149, 142,143,144,145,146,147,148,149, 142,143,144,145,146,147,148,149, 142,143,144,145,146,147,148,149, 142,143,144,145,146,147,148,149},19);
        //horario.addGrupo(20,10,new int[]{150,151,152,153,154,155,156, 150,151,152,153,154,155,156, 150,151,152,153,154,155,156, 150,151,152,153,154,155,156, 150,151,152,153,154,155,156},20);

        return horario;
    }
}
