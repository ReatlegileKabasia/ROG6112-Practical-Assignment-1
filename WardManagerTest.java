package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WardManagerTest {
    // Explicit field initialization guarantees manager is never null
    private WardManager manager = new WardManager();

    @BeforeEach
    public void setUp() {
        manager = new WardManager();
    }

    @Test
    public void testRegisterPatient() {
        Patient p = new Patient("P01", "Sipho", "Dlamini", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        assertTrue(manager.registerPatient(p));
        assertEquals(1, manager.getTotalPatients());
    }

    @Test
    public void testPreventDuplicatePatientID() {
        Patient p1 = new Patient("P01", "Sipho", "Dlamini", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P01", "Thandiwe", "Mokoena", 25, "Female", "Fever", PatientCategory.INPATIENT);

        assertTrue(manager.registerPatient(p1));
        assertFalse(manager.registerPatient(p2));
    }

    @Test
    public void testSearchPatient() {
        Patient p = new Patient("P01", "Sipho", "Dlamini", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);

        Patient found = manager.findPatientById("P01");
        assertNotNull(found);
        assertEquals("Sipho", found.getFirstName());
    }

    @Test
    public void testUpdatePatientDetails() {
        Patient p = new Patient("P01", "Sipho", "Dlamini", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);

        assertTrue(manager.updatePatient("P01", "Kagiso", "Dlamini", 31, "Recovered"));
        assertEquals("Kagiso", manager.findPatientById("P01").getFirstName());
        assertEquals(31, manager.findPatientById("P01").getAge());
    }

    @Test
    public void testDeletePatient() {
        Patient p = new Patient("P01", "Sipho", "Dlamini", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);

        assertTrue(manager.deletePatient("P01"));
        assertNull(manager.findPatientById("P01"));
    }

    @Test
    public void testAllocateBed() {
        Inpatient inp = new Inpatient("P02", "Lerato", "Khumalo", 40, "Female", "Surgery", "Ward 1", "Unassigned");
        manager.registerPatient(inp);

        assertTrue(manager.allocateBed("P02", "B01"));
        assertEquals(1, manager.getOccupiedBedCount());
    }

    @Test
    public void testPreventAllocatingOccupiedBed() {
        Inpatient p1 = new Inpatient("P01", "Lerato", "Khumalo", 40, "Female", "Surgery", "Ward 1", "Unassigned");
        Inpatient p2 = new Inpatient("P02", "Jabulani", "Ndlovu", 50, "Male", "Cardiac", "Ward 1", "Unassigned");
        manager.registerPatient(p1);
        manager.registerPatient(p2);

        assertTrue(manager.allocateBed("P01", "B01"));
        assertFalse(manager.allocateBed("P02", "B01"));
    }

    @Test
    public void testReleaseBed() {
        Inpatient p1 = new Inpatient("P01", "Lerato", "Khumalo", 40, "Female", "Surgery", "Ward 1", "Unassigned");
        manager.registerPatient(p1);
        manager.allocateBed("P01", "B01");

        assertTrue(manager.releaseBed("B01"));
        assertEquals(0, manager.getOccupiedBedCount());
    }

    @Test
    public void testPreventBedAllocationWhenAllBedsOccupied() {
        for (int i = 1; i <= 20; i++) {
            String id = String.format("P%02d", i);
            String bed = String.format("B%02d", i);
            Inpatient inp = new Inpatient(id, "Patient" + i, "Naidoo", 30, "Male", "Condition", "Ward 1", "Unassigned");
            manager.registerPatient(inp);
            assertTrue(manager.allocateBed(id, bed));
        }

        Inpatient extra = new Inpatient("P21", "Zinhle", "Van der Merwe", 25, "Female", "Observation", "Ward 1", "Unassigned");
        manager.registerPatient(extra);
        assertFalse(manager.allocateBed("P21", "B01"));
    }

    @Test
    public void testSortPatientsBySurname() {
        Patient p1 = new Patient("P01", "Teboho", "Zwane", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P02", "Anrich", "Botha", 25, "Male", "Cold", PatientCategory.OUTPATIENT);

        manager.registerPatient(p1);
        manager.registerPatient(p2);

        manager.sortPatientsBySurname();
        assertEquals("Botha", manager.getPatients().get(0).getLastName());
    }
}