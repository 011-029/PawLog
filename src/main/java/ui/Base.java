package ui;

import content.PetTipMgr;
import content.UnsafePetFoodMgr;
import core.*;

import javax.swing.*;

public class Base extends JPanel {

    protected final MainFrame mainFrame;
    protected final UserMgr userMgr;
    protected final PetMgr petMgr;
    protected final HealthMgr healthMgr;
    protected final MedicalMgr medicalMgr;
    protected final MedicineRecordMgr medicineRecordMgr;
    protected final MedicineRoutineMgr medicineRoutineMgr;
    protected final PlayMgr playMgr;
    protected final VaccineMgr vaccineMgr;
    protected final WalkMgr walkMgr;
    protected final UnsafePetFoodMgr unsafePetFoodMgr;
    protected final PetTipMgr petTipMgr;

    protected Base(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userMgr = UserMgr.getInstance();
        this.petMgr = PetMgr.getInstance();
        this.healthMgr = HealthMgr.getInstance();
        this.medicalMgr = MedicalMgr.getInstance();
        this.medicineRecordMgr = MedicineRecordMgr.getInstance();
        this.medicineRoutineMgr = MedicineRoutineMgr.getInstance();
        this.playMgr = PlayMgr.getInstance();
        this.vaccineMgr = VaccineMgr.getInstance();
        this.walkMgr = WalkMgr.getInstance();
        this.unsafePetFoodMgr = UnsafePetFoodMgr.getInstance();
        this.petTipMgr = PetTipMgr.getInstance();
    }
}
