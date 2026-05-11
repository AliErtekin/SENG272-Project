public class AppState {

    //1.adım
    private Profile  profile;
    //2.adım kalite tipi
    private String   qualityType;
    //2.adım mod
    private String   mode;
    //2.adım senaryo
    private Scenario scenario;

    //görüntülenen adım numarası
    private int       currentStep;
    //adım tamamlandı mı tamamlanmadı mı
    private boolean[] completedSteps;

    //yeni oturum baslatma
    public AppState() {
        profile        = new Profile();
        currentStep    = 1;
        completedSteps = new boolean[6];
    }


    //adımı tamamlandı olarak işaretler
    public void    completeStep(int step)  { if (step >= 1 && step <= 5) completedSteps[step] = true; }
    //adımın tamamlanmadıgını göruruz
    public boolean isStepCompleted(int s)  { return s >= 1 && s <= 5 && completedSteps[s]; }


    // get ve set
    public Profile  getProfile()               { return profile; }
    public void     setProfile(Profile p)      { this.profile = p; }

    public String   getQualityType()           { return qualityType; }
    public void     setQualityType(String qt)  { this.qualityType = qt; }

    public String   getMode()                  { return mode; }
    public void     setMode(String m)          { this.mode = m; }

    public Scenario getScenario()              { return scenario; }
    public void     setScenario(Scenario s)    { this.scenario = s; }

    public int      getCurrentStep()           { return currentStep; }
    public void     setCurrentStep(int step)   { this.currentStep = step; }
}
