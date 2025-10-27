package model.transport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Feedback {
    private int idFeedback;
    private int idCursa;
    private int idSender;
    private int idReceiver;
    private int scor;
    private String comentariu;
    private String tip;
    private LocalDateTime dataCreare;

    public Feedback() { }

    //constructor pt insert
    public Feedback(int idCursa, int idSender, int idReceiver,
                    int scor, String comentariu, String tip) {
        this.idCursa = idCursa;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.scor = scor;
        this.comentariu = comentariu;
        this.tip = tip;
        this.dataCreare = LocalDateTime.now();
    }

    //constructor pt read
    public Feedback(int idFeedback, int idCursa, int idSender, int idReceiver,
                    int scor, String comentariu, String tip, LocalDateTime dataCreare) {
        this.idFeedback = idFeedback;
        this.idCursa = idCursa;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.scor = scor;
        this.comentariu = comentariu;
        this.tip = tip;
        this.dataCreare = dataCreare;
    }

    public int getIdFeedback() { return idFeedback; }
    public void setIdFeedback(int idFeedback) { this.idFeedback = idFeedback; }

    public int getIdCursa() { return idCursa; }
    public void setIdCursa(int idCursa) { this.idCursa = idCursa; }

    public int getIdSender() { return idSender; }
    public void setIdSender(int idSender) { this.idSender = idSender; }

    public int getIdReceiver() { return idReceiver; }
    public void setIdReceiver(int idReceiver) { this.idReceiver = idReceiver; }

    public int getScor() { return scor; }
    public void setScor(int scor) { this.scor = scor; }

    public String getComentariu() { return comentariu; }
    public void setComentariu(String comentariu) { this.comentariu = comentariu; }

    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }

    public LocalDateTime getDataCreare() { return dataCreare; }
    public void setDataCreare(LocalDateTime dataCreare) { this.dataCreare = dataCreare; }

    @Override
    public String toString() {
        return "Feedback#" + idFeedback +
                " | Cursa ID: " + idCursa +
                " | Sender ID: " + idSender +
                " | Receiver ID: " + idReceiver +
                " | Scor: " + scor +
                " | Comentariu: " + comentariu +
                " | Tip: " + tip;
    }
}
