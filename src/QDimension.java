import java.util.ArrayList;
import java.util.List;

public class QDimension {

    private final String       name;
    private final int          coefficient;
    private final List<Metric> metrics;


    // adı,ağırlığı verilen yeni kalite boyutu oluşturur
    public QDimension(String name, int coefficient) {
        this.name        = name;
        this.coefficient = coefficient;
        this.metrics     = new ArrayList<>();
    }


    // metric ekler
    public void addMetric(Metric metric) { metrics.add(metric); }


    //skor hesaplama
    public double calculateScore() {
        double weightedSum = 0.0;
        int    totalCoeff  = 0;
        for (Metric m : metrics) {
            weightedSum += m.calculateScore() * m.getCoefficient();
            totalCoeff  += m.getCoefficient();
        }
        return (totalCoeff == 0) ? 0.0 : weightedSum / totalCoeff;
    }


    //get ler
    public String       getName()        { return name; }
    public int          getCoefficient() { return coefficient; }
    public List<Metric> getMetrics()     { return metrics; }

    // string çevirme
    @Override
    public String toString() {
        return name + " (Coefficient: " + coefficient + ")";
    }
}
