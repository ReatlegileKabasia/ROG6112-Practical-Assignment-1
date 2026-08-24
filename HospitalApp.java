package org.example;

import java.util.Scanner;

public class HospitalApp {
    private static final WardManager manager = new WardManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    registerPatientUI();
                    break;
                }
                case "2" -> {
                    searchPatientUI();
                    break;
                }

                case "3" -> {
                    updatePatientUI();
                    break;
                }

                case "4" -> {
                    deletePatientUI();
                    break;
                }

                case "5" -> {
                    allocateBedUI();
                    break;
                }

                case "6" -> {
                    releaseBedUI();
                    break;
                }

                case "7" -> {
                    displayWardLayoutUI();
                    break;
                }

                case "8" -> {
                    generateReportsUI();
                    break;
                }

                case "9" -> {
                    sortPatientsUI();
                    break;
                }

                case "0" -> {
                    System.out.println("Exiting Medicare Patient Admission System. Goodbye!");
                    exit = true;
                }
                default -> System.out.println("Invalid option! Please try again.");

            }


        }

    }

    private static void printMainMenu() {

        System.out.println("MEDICARE HOSPITAL PATIENT ADMISSION SYSTEM");
        System.out.println("=============================================");

        System.out.println("1. Register New Patient");
        System.out.println("2. Search Patient");
        System.out.println("3. Update Patient Details");
        System.out.println("4. Delete Patient");
        System.out.println("5. Allocate Bed to Inpatient");
        System.out.println("6. Release Bed");
        System.out.println("7. View Ward Layout");
        System.out.println("8. Generate Ward Reports");
        System.out.println("9. Sort Patient List");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
    }



    private static void registerPatientUI() {
        System.out.println(" Register Patient ");
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();

        if (manager.findPatientById(id) != null) {
            System.out.println("Error: A patient with ID '" + id + "' already exists");
            return;
        }

        System.out.print("Enter First Name: ");
        String fname = scanner.nextLine().trim();


        System.out.print("Enter Last Name: ");
        String lname = scanner.nextLine().trim();


        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());


        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine().trim();

        System.out.print("Enter Medical Condition: ");
        String condition = scanner.nextLine().trim();

        System.out.println("Select Patient Category:");
        System.out.println("1. Inpatient");
        System.out.println("2. Outpatient");
        System.out.println("3. Emergency");
        System.out.print("Choice: ");
        int catChoice = Integer.parseInt(scanner.nextLine().trim());

        PatientCategory category = switch (catChoice) {
            case 1 -> PatientCategory.INPATIENT;
            case 2 -> PatientCategory.OUTPATIENT;
            default -> PatientCategory.EMERGENCY;

        };

        Patient p;
        if (category == PatientCategory.INPATIENT) {
            p = new Inpatient(id, fname, lname, age, gender, condition, "Ward 1", "Unassigned");
        } else {
            p = new Patient(id, fname, lname, age, gender, condition, category);
        }

        if (manager.registerPatient(p)) {
            System.out.println("Patient registered successfully!");
        } else {
            System.out.println("Failed to register patient.");
        }
    }

    private static void searchPatientUI() {
        System.out.print("Enter Patient ID to search: ");
        String id = scanner.nextLine().trim();
        Patient p = manager.findPatientById(id);


        if (p != null) {
            System.out.println("Patient Found:");
            System.out.println(p.displayDetails());
        } else {

            System.out.println("Patient not found.");

        }
    }

    private static void updatePatientUI() {
        System.out.print("Enter Patient ID to update: ");
        String id = scanner.nextLine().trim();


        if (manager.findPatientById(id) == null) {
            System.out.println("Patient not found!");
            return;
        }
        System.out.print("Enter New First Name: ");
        String fname = scanner.nextLine().trim();

        System.out.print("Enter New Last Name: ");
        String lname = scanner.nextLine().trim();


        System.out.print("Enter New Age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());


        System.out.print("Enter New Condition: ");
        String condition = scanner.nextLine().trim();

        if (manager.updatePatient(id, fname, lname, age, condition)) {
            System.out.println("Patient updated successfully!");

        }
    }

    private static void deletePatientUI() {
        System.out.print("Enter Patient ID to delete: ");
        String id = scanner.nextLine().trim();


        if (manager.deletePatient(id)) {
            System.out.println("Patient record deleted.");
        } else {

            System.out.println("Patient ID not found.");
        }
    }

    private static void allocateBedUI() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();


        System.out.print("Enter Bed Code (e.g. B01): ");
        String bedCode = scanner.nextLine().trim();

        if (manager.allocateBed(id, bedCode)) {
            System.out.println("Bed successfully allocated!");
        } else {


            System.out.println("Allocation failed! Verify Patient ID (Must be Inpatient) and Bed availability.");
        }
    }

    private static void releaseBedUI() {
        System.out.print("Enter Bed Code to release (e.g. B01): ");
        String bedCode = scanner.nextLine().trim();


        if (manager.releaseBed(bedCode)) {
            System.out.println("Bed successfully released!");
        } else {


            System.out.println("Release failed! Bed may already be empty or invalid code.");
        }

    }

    private static void displayWardLayoutUI() {
        System.out.println(manager.displayWardLayout());
    }

    private static void generateReportsUI() {
        System.out.println(" WARD REPORTS ");
        System.out.println("Total Registered Patients : " + manager.getTotalPatients());

        System.out.println("Total Occupied Beds      : " + manager.getOccupiedBedCount());

        System.out.println("Total Available Beds     : " + manager.getAvailableBeds().size());

        System.out.printf("Ward Occupancy Percentage : %.2f%%\n", manager.getOccupancyPercentage());

        System.out.println("Available Bed List: " + manager.getAvailableBeds());

        System.out.println("Occupied Bed List : " + manager.getOccupiedBeds());

        System.out.println("Registered Patient Roster:");

        for (Patient p : manager.getPatients()) {
            System.out.println(p.displayDetails());
        }
    }

    private static void sortPatientsUI() {
        System.out.println("Select Sorting Option:");

        System.out.println("1. Sort by Patient ID");
        System.out.println("2. Sort by Surname");

        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();


        if ("1".equals(choice)) {
            manager.sortPatientsById();

            System.out.println("Patients sorted by ID.");


        } else if ("2".equals(choice)) {
            manager.sortPatientsBySurname();
            System.out.println("Patients sorted by Surname.");


        }

    }


}