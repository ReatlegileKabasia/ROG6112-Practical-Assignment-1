import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WardManager {
    private static final int ROWS = 4;
    private static final int COLS = 5;
    private final String[][] bedLayout;
    private final String[][] bedOccupancy;
    private final List<Patient> patients;

    public WardManager() {
        bedLayout = new String[ROWS][COLS];
        bedOccupancy = new String[ROWS][COLS];
        patients = new ArrayList<>();
        initializeBeds();
    }

    private void initializeBeds() {
        int bedNum = 1;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                bedLayout[r][c] = String.format("B%02d", bedNum++);
                bedOccupancy[r][c] = null;
            }

        }


    }



    public boolean registerPatient(Patient patient) {

        if (findPatientById(patient.getPatientId()) != null) {
            return false;
        }

          patients.add(patient);
         return true;

    }



    public Patient findPatientById(String id) {
        for (Patient p : patients) {

            if (p.getPatientId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;

    }


    public boolean updatePatient(String id, String newFirstName, String newLastName, int newAge, String newCondition) {
        Patient p = findPatientById(id);
        if (p != null) {

            p.setFirstName(newFirstName);
            p.setLastName(newLastName);
            p.setAge(newAge);
            p.setMedicalCondition(newCondition);

            return true;

        }
        return false;


    }

    public boolean deletePatient (String id) {
        Patient p = findPatientById(id);
        if (p != null) {


            releaseBedByPatientId(id);
            patients.remove(p);
            return true;
        }
        return false;
    }



    public List <Patient> getPatients() {
        return  patients;
    }



    public void sortPatientsById() {
        patients.sort(Comparator.comparing(Patient::getPatientId));
    }


    public void sortPatientsBySurname() {

         patients.sort(Comparator.comparing(Patient::getLastName, String.CASE_INSENSITIVE_ORDER));

    }

}