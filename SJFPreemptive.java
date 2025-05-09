import java.util.*;

public class SJFPreemptive {
    public static void schedule(List<Process> originalProcesses) {
        List<Process> processes = new ArrayList<>();
        for (Process p : originalProcesses) {
            processes.add(new Process(p.pid, p.burstTime, p.arrivalTime, p.priority));
        }
        int timeNow = 0;
        List<Process> completed = new ArrayList<>();
        List<Process> readyQueue = new ArrayList<>();
        List<GanttEntry> ganttChart = new ArrayList<>();
        int lastPid = -1;
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        System.out.println("\n--- SJF Preemptive Scheduling ---");

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= timeNow) {
                readyQueue.add(processes.remove(0));
            }
            if (readyQueue.isEmpty()) {
                timeNow++;
                continue;
            }

            Process process = Collections.min(readyQueue, Comparator.comparingInt(p -> p.remainingTime));
            if (process.responseTime == -1) {
                process.responseTime = timeNow - process.arrivalTime;
            }
            System.out.println("Running " + process + " for 1 unit");

            if (lastPid != process.pid) {
                ganttChart.add(new GanttEntry(process.pid, timeNow, timeNow + 1));
            } else {
                ganttChart.get(ganttChart.size() - 1).endTime++;
            }
            lastPid = process.pid;

            process.remainingTime--;
            timeNow++;

            if (process.remainingTime == 0) {
                readyQueue.remove(process);
                process.completionTime = timeNow;
                process.turnaroundTime = process.completionTime - process.arrivalTime;
                process.waitingTime = process.turnaroundTime - process.burstTime;
                completed.add(process);
            }
        }

        printResults(completed, true);
        printGanttChart(ganttChart);
    }

    private static void printResults(List<Process> processes, boolean showResponse) {
        int totalWaiting = 0;
        int totalTurnaround = 0;
        int totalResponse = 0;

        System.out.println("\n--- Process Results ---");
        for (Process p : processes) {
            System.out.print("P" + p.pid + ": Waiting Time = " + p.waitingTime + ", Turnaround Time = " + p.turnaroundTime);
            totalWaiting += p.waitingTime;
            totalTurnaround += p.turnaroundTime;
            if (showResponse) {
                System.out.print(", Response Time = " + p.responseTime);
                totalResponse += p.responseTime;
            }
            System.out.println();
        }

        double avgWaiting = (double) totalWaiting / processes.size();
        double avgTurnaround = (double) totalTurnaround / processes.size();
        System.out.printf("\nAverage Waiting Time = %.2f\n", avgWaiting);
        System.out.printf("Average Turnaround Time = %.2f\n", avgTurnaround);
        if (showResponse) {
            double avgResponse = (double) totalResponse / processes.size();
            System.out.printf("Average Response Time = %.2f\n", avgResponse);
        }
        }

private static void printGanttChart(List<GanttEntry> chart) {
    System.out.println("\n--- Gantt Chart ---");

    for (GanttEntry e : chart) {
        System.out.printf("|  P%-2d  ", e.pid);
    }
    System.out.println("|");

    for (GanttEntry e : chart) {
        System.out.printf("%-8d", e.startTime); 
    }

    System.out.println(chart.get(chart.size() - 1).endTime);
}
}