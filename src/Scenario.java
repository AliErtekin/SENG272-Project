import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private final String          qualityType;
    private final String          mode;
    private final String          name;
    private final List<QDimension> dimensions;

    // yeni senaryo oluştur
    public Scenario(String qualityType, String mode, String name) {
        this.qualityType = qualityType;
        this.mode        = mode;
        this.name        = name;
        this.dimensions  = new ArrayList<>();
    }

    //senaryoya kalite boyutu ekleme
    public void addDimension(QDimension d) { dimensions.add(d); }

    //get ler
    public String           getQualityType() { return qualityType; }
    public String           getMode()        { return mode; }
    public String           getName()        { return name; }
    public List<QDimension> getDimensions()  { return dimensions; }

    @Override
    public String toString() { return name; }
}
