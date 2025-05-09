import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select scheduling algorithm:");
        System.out.println("1: First Come First Served");
        System.out.println("2: SJF Preemptive");
        System.out.println("3: SJF Non-Preemptive");
        System.out.println("4: Priority Scheduling");
        System.out.println("5: Round Robin");
        System.out.print("Enter choice: ");
        String algo = scanner.nextLine();

        List<Process> processes = new ArrayList<>();
        int n;

        System.out.print("Enter number of processes: ");
        n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1) + ":");
            System.out.print("Burst Time: ");
            int bt = Integer.parseInt(scanner.nextLine());
            System.out.print("Arrival Time: ");
            int at = Integer.parseInt(scanner.nextLine());
            int pr = 0;
            if (algo.equals("4")) {
                System.out.print("Priority: ");
                pr = Integer.parseInt(scanner.nextLine());
            }
            processes.add(new Process(i + 1, bt, at, pr));
        }

        switch (algo) {
            case "1":
                FCFS.schedule(processes);
                break;
            case "2":
                SJFPreemptive.schedule(processes);
                break;
            case "3":
                SJFNonPreemptive.schedule(processes);
                break;
            case "4":
                PriorityScheduling.schedule(processes);
                break;
            case "5":
                System.out.print("Enter Time Quantum: ");
                int quantum = Integer.parseInt(scanner.nextLine());
                RoundRobin.schedule(processes, quantum);
                break;
            default:
                System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}