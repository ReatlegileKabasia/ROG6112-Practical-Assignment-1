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



    public boolean deletePatient(String id) {
        Patient p = findPatientById(id);
        if (p != null) {

            releaseBedByPatientId(id);
            patients.remove(p);
            return true;
        }
        return false;

    }

    public List<Patient> getPatients() {
        return patients;


    }

    public void sortPatientsById() {
        patients.sort(Comparator.comparing(Patient::getPatientId));

    }

    public void sortPatientsBySurname() {

        patients.sort(Comparator.comparing(Patient::getLastName, String.CASE_INSENSITIVE_ORDER));

    }



    public boolean allocateBed(String patientId, String bedCode) {
        Patient p = findPatientById(patientId);
        if (p == null || p.getCategory() != PatientCategory.INPATIENT) {
            return false;
        }

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {


                if (bedLayout[r][c].equalsIgnoreCase(bedCode)) {
                    if (bedOccupancy[r][c] != null) {
                        return false;
                    }

                    bedOccupancy[r][c] = patientId;


                    if (!(p instanceof Inpatient)) {
                        patients.remove(p);
                        Inpatient inp = new Inpatient(p.getPatientId(), p.getFirstName(), p.getLastName(), p.getAge(), p.getGender(), p.getMedicalCondition(), "Ward 1", bedCode);
                        patients.add(inp);


                    } else {

                        ((Inpatient) p).setBedNumber(bedCode);
                    }
                    return true;
                }
            }
        }
        return false;
    }



    public boolean releaseBed(String bedCode) {

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {


                if (bedLayout[r][c].equalsIgnoreCase(bedCode)) {

                    if (bedOccupancy[r][c] == null) {
                        return false;
                    }

                    String patientId = bedOccupancy[r][c];
                    bedOccupancy[r][c] = null;
                    Patient p = findPatientById(patientId);


                    if (p instanceof Inpatient) {
                        ((Inpatient) p).setBedNumber("N/A");
                    }
                    return true;
                }


            }

        }
        return false;
    }

    private void releaseBedByPatientId(String patientId) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {


                if (patientId.equalsIgnoreCase(bedOccupancy[r][c])) {
                    bedOccupancy[r][c] = null;
                }

            }
        }


    }

    public String displayWardLayout() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n WARD BED LAYOUT (4x5) \n");
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                String status = (bedOccupancy[r][c] == null) ? "NOT OCCUPIED" : "[" + bedOccupancy[r][c] + "]";
                sb.append(String.format("%-6s %-8s\t", bedLayout[r][c], status));
            }
            sb.append("\n");
        }
        sb.append("=============================\n");
        return sb.toString();
    }

    public List<String> getAvailableBeds() {
        List<String> freeBeds = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (bedOccupancy[r][c] == null) {
                    freeBeds.add(bedLayout[r][c]);
                }
            }
        }
        return freeBeds;
    }

    public List<String> getOccupiedBeds() {
        List<String> occupied = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (bedOccupancy[r][c] != null) {
                    occupied.add(bedLayout[r][c] + " (" + bedOccupancy[r][c] + ")");
                }
            }
        }
        return occupied;
    }

    public int getTotalPatients() {
        return patients.size();
    }

    public int getOccupiedBedCount() {
        return getOccupiedBeds().size();
    }

    public double getOccupancyPercentage() {
        return ((double) getOccupiedBedCount() / (ROWS * COLS)) * 100.0;

    }

}
