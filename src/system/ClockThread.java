package system;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClockThread extends JFrame {

    JLabel labelTime;

    public ClockThread() {//constructor
        iniciaRelogio();//start time
    }

    public void iniciaRelogio() {

        new Thread() {//initiate a new thread

            @Override
            public void run() {

                while (0 == 0) {

                    GregorianCalendar gc = new GregorianCalendar();

                    int hora = gc.get(Calendar.HOUR_OF_DAY);

                    int minuto = gc.get(Calendar.MINUTE);

                    int segundo = gc.get(Calendar.SECOND);

                    String horaString;

                    String minString;

                    String segundoString;

                    if (hora < 10) {

                        horaString = "0" + hora;

                    } else {

                        horaString = "" + hora;

                    }

                    if (minuto < 10) {

                        minString = "0" + minuto;

                    } else {

                        minString = "" + minuto;

                    }

                    if (segundo < 10) {

                        segundoString = "0" + segundo;

                    } else {

                        segundoString = "" + segundo;

                    }

                    labelTime.setText(horaString + ":" + minString + ":" + segundoString);

                    try {

                        sleep(1000);

                    } catch (Exception e) {
                    }

                }

            }
        }.start();//initiate a thread.

    }
}
