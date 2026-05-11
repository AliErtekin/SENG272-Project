import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//sabit senaryo verilerini tutar
public class ScenarioRepository {

    private final Map<String, List<Scenario>> scenarioMap = new HashMap<>();
    private static ScenarioRepository instance;

    public static ScenarioRepository getInstance() {
        if (instance == null) instance = new ScenarioRepository();
        return instance;
    }

    private ScenarioRepository() { buildAll(); }

    //sabit senaryoları haritaya ekler
    private void buildAll() {
        add(buildProductEducationAlpha());
        add(buildProductEducationBeta());
        add(buildProductHealthAlpha());
        add(buildProductHealthBeta());
        add(buildProcessEducationX());
        add(buildProcessEducationY());
        add(buildProcessHealthA());
        add(buildProcessHealthB());
    }

    //bir senaryoyu haritaya ekler

    private void add(Scenario s) {
        String key = s.getQualityType() + "|" + s.getMode();
        scenarioMap.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
    }

    //senaryo listesini döndurur
    public List<Scenario> getScenarios(String qualityType, String mode) {
        return scenarioMap.getOrDefault(qualityType + "|" + mode, new ArrayList<>());
    }

    //modları dondurur
    public String[] getModes()        { return new String[]{"Health", "Education"}; }
    //kalite tiplerını dondurur
    public String[] getQualityTypes() { return new String[]{"Product", "Process"}; }

    private Scenario buildProductEducationAlpha() {
        Scenario s = new Scenario("Product", "Education", "Scenario C \u2014 Team Alpha");

        //kullanılabilirlik boyutu
        QDimension usability = new QDimension("Usability", 25);
        usability.addMetric(new Metric("SUS Score",          50, true,  0,  100, "points", 89));
        usability.addMetric(new Metric("Onboarding Time",    50, false, 0,   60, "min",     5));
        s.addDimension(usability);

        QDimension perf = new QDimension("Performance Efficiency", 20);
        perf.addMetric(new Metric("Video Start Time",        50, false, 0,   15, "sec",     2));
        perf.addMetric(new Metric("Concurrent Exams",        50, true,  0,  600, "users", 450));
        s.addDimension(perf);

        //erişilebilirlik boyutu
        QDimension access = new QDimension("Accessibility", 20);
        access.addMetric(new Metric("WCAG Compliance",       50, true,  0,  100, "%",      85));
        access.addMetric(new Metric("Screen Reader Score",   50, true,  0,  100, "%",      78));
        s.addDimension(access);

        //güvenilirlik boyutu

        QDimension reliability = new QDimension("Reliability", 20);
        reliability.addMetric(new Metric("Uptime",           50, true,  95, 100, "%",      99.5));
        reliability.addMetric(new Metric("MTTR",             50, false, 0,  120, "min",    15));
        s.addDimension(reliability);

        //işlevsel uygunluk boyutu

        QDimension func = new QDimension("Functional Suitability", 15);
        func.addMetric(new Metric("Feature Completion",      50, true,  0,  100, "%",      92));
        func.addMetric(new Metric("Assignment Submit Rate",  50, true,  0,  100, "%",      88));
        s.addDimension(func);

        return s;
    }

    private Scenario buildProductEducationBeta() {
        Scenario s = new Scenario("Product", "Education", "Scenario D \u2014 Team Beta");

        QDimension usability = new QDimension("Usability", 25);
        usability.addMetric(new Metric("SUS Score",          50, true,  0,  100, "points", 65));
        usability.addMetric(new Metric("Onboarding Time",    50, false, 0,   60, "min",    28));
        s.addDimension(usability);

        QDimension perf = new QDimension("Performance Efficiency", 20);
        perf.addMetric(new Metric("Video Start Time",        50, false, 0,   15, "sec",     8));
        perf.addMetric(new Metric("Concurrent Exams",        50, true,  0,  600, "users", 210));
        s.addDimension(perf);

        QDimension access = new QDimension("Accessibility", 20);
        access.addMetric(new Metric("WCAG Compliance",       50, true,  0,  100, "%",      60));
        access.addMetric(new Metric("Screen Reader Score",   50, true,  0,  100, "%",      55));
        s.addDimension(access);

        QDimension reliability = new QDimension("Reliability", 20);
        reliability.addMetric(new Metric("Uptime",           50, true,  95, 100, "%",      96.8));
        reliability.addMetric(new Metric("MTTR",             50, false, 0,  120, "min",    60));
        s.addDimension(reliability);

        QDimension func = new QDimension("Functional Suitability", 15);
        func.addMetric(new Metric("Feature Completion",      50, true,  0,  100, "%",      72));
        func.addMetric(new Metric("Assignment Submit Rate",  50, true,  0,  100, "%",      68));
        s.addDimension(func);

        return s;
    }

    private Scenario buildProductHealthAlpha() {
        Scenario s = new Scenario("Product", "Health", "Scenario A \u2014 Clinic Alpha");

        QDimension usability = new QDimension("Usability", 30);
        usability.addMetric(new Metric("Task Success Rate",  50, true,  0,  100, "%",      88));
        usability.addMetric(new Metric("Error Rate",         50, false, 0,   20, "%",       3));
        s.addDimension(usability);

        //güvenlik: kimlik doğrulama hataları ve şifreleme kapsamı

        QDimension security = new QDimension("Security", 25);
        security.addMetric(new Metric("Auth Failures",       50, false, 0,  100, "events",  4));
        security.addMetric(new Metric("Encryption Coverage", 50, true,  0,  100, "%",      98));
        s.addDimension(security);

        //güvenilirlik: sistem çalışma süresi ve kurtarma süresi
        QDimension reliability = new QDimension("Reliability", 25);
        reliability.addMetric(new Metric("System Uptime",   50, true,  95, 100, "%",      99.2));
        reliability.addMetric(new Metric("Recovery Time",   50, false, 0,   60, "min",     8));
        s.addDimension(reliability);

        //performans: API yanıt süresi ve eşzamanlı kullanıcı sayısı
        QDimension perf = new QDimension("Performance", 20);
        perf.addMetric(new Metric("API Response Time",      50, false, 0, 2000, "ms",    150));
        perf.addMetric(new Metric("Concurrent Users",       50, true,  0,  500, "users", 420));
        s.addDimension(perf);

        return s;
    }

    private Scenario buildProductHealthBeta() {
        Scenario s = new Scenario("Product", "Health", "Scenario B \u2014 Hospital Beta");

        QDimension usability = new QDimension("Usability", 30);
        usability.addMetric(new Metric("Task Success Rate",  50, true,  0,  100, "%",      72));
        usability.addMetric(new Metric("Error Rate",         50, false, 0,   20, "%",       9));
        s.addDimension(usability);

        QDimension security = new QDimension("Security", 25);
        security.addMetric(new Metric("Auth Failures",       50, false, 0,  100, "events", 22));
        security.addMetric(new Metric("Encryption Coverage", 50, true,  0,  100, "%",      85));
        s.addDimension(security);

        QDimension reliability = new QDimension("Reliability", 25);
        reliability.addMetric(new Metric("System Uptime",   50, true,  95, 100, "%",      97.1));
        reliability.addMetric(new Metric("Recovery Time",   50, false, 0,   60, "min",    35));
        s.addDimension(reliability);

        QDimension perf = new QDimension("Performance", 20);
        perf.addMetric(new Metric("API Response Time",      50, false, 0, 2000, "ms",    680));
        perf.addMetric(new Metric("Concurrent Users",       50, true,  0,  500, "users", 180));
        s.addDimension(perf);

        return s;
    }

    private Scenario buildProcessEducationX() {
        Scenario s = new Scenario("Process", "Education", "Scenario E \u2014 Dev Team X");

        //sprint verimliliği: hız ve sprint tamamlama oranı
        QDimension sprint = new QDimension("Sprint Efficiency", 30);
        sprint.addMetric(new Metric("Velocity",              50, true,  0,  100, "pts",    82));
        sprint.addMetric(new Metric("Sprint Burn Rate",      50, true,  0,  100, "%",      91));
        s.addDimension(sprint);

        //kod kalitesi: test kapsamı ve kod inceleme oranı
        QDimension code = new QDimension("Code Quality", 25);
        code.addMetric(new Metric("Code Coverage",           50, true,  0,  100, "%",      78));
        code.addMetric(new Metric("Code Review Rate",        50, true,  0,  100, "%",      95));
        s.addDimension(code);

        //ekip işbirliği: pr inceleme süresi ve toplantı etkinliği
        QDimension collab = new QDimension("Team Collaboration", 25);
        collab.addMetric(new Metric("PR Review Time",        50, false, 0,   72, "hours",   4));
        collab.addMetric(new Metric("Meeting Efficiency",    50, true,  0,  100, "%",      88));
        s.addDimension(collab);

        //teslimat: dağıtım sıklığı ve teslim süresi
        QDimension delivery = new QDimension("Delivery", 20);
        delivery.addMetric(new Metric("Deploy Frequency",   50, true,  0,   30, "/month",  12));
        delivery.addMetric(new Metric("Lead Time",          50, false, 0,   30, "days",     5));
        s.addDimension(delivery);

        return s;
    }

    private Scenario buildProcessEducationY() {
        Scenario s = new Scenario("Process", "Education", "Scenario F \u2014 Dev Team Y");

        QDimension sprint = new QDimension("Sprint Efficiency", 30);
        sprint.addMetric(new Metric("Velocity",              50, true,  0,  100, "pts",    55));
        sprint.addMetric(new Metric("Sprint Burn Rate",      50, true,  0,  100, "%",      68));
        s.addDimension(sprint);

        QDimension code = new QDimension("Code Quality", 25);
        code.addMetric(new Metric("Code Coverage",           50, true,  0,  100, "%",      52));
        code.addMetric(new Metric("Code Review Rate",        50, true,  0,  100, "%",      74));
        s.addDimension(code);

        QDimension collab = new QDimension("Team Collaboration", 25);
        collab.addMetric(new Metric("PR Review Time",        50, false, 0,   72, "hours",  18));
        collab.addMetric(new Metric("Meeting Efficiency",    50, true,  0,  100, "%",      62));
        s.addDimension(collab);

        QDimension delivery = new QDimension("Delivery", 20);
        delivery.addMetric(new Metric("Deploy Frequency",   50, true,  0,   30, "/month",   5));
        delivery.addMetric(new Metric("Lead Time",          50, false, 0,   30, "days",    18));
        s.addDimension(delivery);

        return s;
    }

    private Scenario buildProcessHealthA() {
        Scenario s = new Scenario("Process", "Health", "Scenario G \u2014 IT Team A");

        QDimension sprint = new QDimension("Sprint Efficiency", 30);
        sprint.addMetric(new Metric("Velocity",              50, true,  0,  100, "pts",    75));
        sprint.addMetric(new Metric("Sprint Burn Rate",      50, true,  0,  100, "%",      85));
        s.addDimension(sprint);

        QDimension code = new QDimension("Code Quality", 25);
        code.addMetric(new Metric("Code Coverage",           50, true,  0,  100, "%",      82));
        code.addMetric(new Metric("Code Review Rate",        50, true,  0,  100, "%",      90));
        s.addDimension(code);

        QDimension collab = new QDimension("Team Collaboration", 25);
        collab.addMetric(new Metric("PR Review Time",        50, false, 0,   72, "hours",   6));
        collab.addMetric(new Metric("Meeting Efficiency",    50, true,  0,  100, "%",      80));
        s.addDimension(collab);

        QDimension delivery = new QDimension("Delivery", 20);
        delivery.addMetric(new Metric("Deploy Frequency",   50, true,  0,   30, "/month",   8));
        delivery.addMetric(new Metric("Lead Time",          50, false, 0,   30, "days",     8));
        s.addDimension(delivery);

        return s;
    }

    private Scenario buildProcessHealthB() {
        Scenario s = new Scenario("Process", "Health", "Scenario H \u2014 IT Team B");

        QDimension sprint = new QDimension("Sprint Efficiency", 30);
        sprint.addMetric(new Metric("Velocity",              50, true,  0,  100, "pts",    45));
        sprint.addMetric(new Metric("Sprint Burn Rate",      50, true,  0,  100, "%",      58));
        s.addDimension(sprint);

        QDimension code = new QDimension("Code Quality", 25);
        code.addMetric(new Metric("Code Coverage",           50, true,  0,  100, "%",      40));
        code.addMetric(new Metric("Code Review Rate",        50, true,  0,  100, "%",      65));
        s.addDimension(code);

        QDimension collab = new QDimension("Team Collaboration", 25);
        collab.addMetric(new Metric("PR Review Time",        50, false, 0,   72, "hours",  28));
        collab.addMetric(new Metric("Meeting Efficiency",    50, true,  0,  100, "%",      50));
        s.addDimension(collab);

        QDimension delivery = new QDimension("Delivery", 20);
        delivery.addMetric(new Metric("Deploy Frequency",   50, true,  0,   30, "/month",   3));
        delivery.addMetric(new Metric("Lead Time",          50, false, 0,   30, "days",    22));
        s.addDimension(delivery);

        return s;
    }
}
