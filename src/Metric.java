public class Metric {

    private final String  name;
    private final int     coefficient;
    private final boolean higherIsBetter;
    private final double  rangeMin;//min range
    private final double  rangeMax;//max range
    private final String  unit; //ölçü birimi
    private final double  value;// sabit veri değeri

    public Metric(String name, int coefficient, boolean higherIsBetter,
                  double rangeMin, double rangeMax, String unit, double value) {
        this.name           = name;
        this.coefficient    = coefficient;
        this.higherIsBetter = higherIsBetter;
        this.rangeMin       = rangeMin;
        this.rangeMax       = rangeMax;
        this.unit           = unit;
        this.value          = value;
    }


    //skor hesaplama
    public double calculateScore() {
        double span = rangeMax - rangeMin;
        if (span == 0) return 3.0;

        double raw;
        if (higherIsBetter) {
            raw = 1.0 + ((value - rangeMin) / span) * 4.0;
        } else {
            raw = 5.0 - ((value - rangeMin) / span) * 4.0;
        }
        raw = Math.max(1.0, Math.min(5.0, raw));
        return Math.round(raw * 2.0) / 2.0;
    }

    //yön etiketini türkçeye uygunlaştırır
    public String getDirectionLabel() {
        return higherIsBetter ? "Higher \u2191" : "Lower \u2193";
    }

    // min maks olarak yazdırır
    public String getRangeLabel() {
        return formatNum(rangeMin) + "\u2013" + formatNum(rangeMax);
    }

    //toplanan değeri ekrana yazdırır
    public String getValueLabel() { return formatNum(value); }

    // .0 ları siler
    private String formatNum(double d) {
        return (d == (int) d) ? String.valueOf((int) d) : String.valueOf(d);
    }


    //get ler
    public String  getName()          { return name; }
    public int     getCoefficient()   { return coefficient; }
    public boolean isHigherIsBetter() { return higherIsBetter; }
    public double  getRangeMin()      { return rangeMin; }
    public double  getRangeMax()      { return rangeMax; }
    public String  getUnit()          { return unit; }
    public double  getValue()         { return value; }
}
