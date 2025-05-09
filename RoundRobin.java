import java.util.*;

public class RoundRobin {
    public static void schedule(List<Process> originalProcesses, int quantum) {
        List<Process> processes = new ArrayList<>();
        for (Process p : originalProcesses) {
            processes.add(new Process(p.pid, p.burstTime, p.arrivalTime, p.priority));
        }
        int timeNow = 0;
        int contextSwitches = -1;
        Queue<Process> queue = new LinkedList<>();
        List<Process> readyList = new ArrayList<>(processes);
        List<Process> done = new ArrayList<>();
        Set<Integer> finishedPids = new HashSet<>();
        List<GanttEntry> ganttChart = new ArrayList<>();

        System.out.println("\n--- Round Robin Scheduling ---");

        while (!readyList.isEmpty() || !queue.isEmpty()) {
            Iterator<Process> iter = readyList.iterator();
            while (iter.hasNext()) {
                Process p = iter.next();
                if (p.arrivalTime <= timeNow) {
                    queue.add(p);
                    iter.remove();
                }
            }

            if (queue.isEmpty()) {
                timeNow++;
                continue;
            }

            Process process = queue.poll();
            if (process.responseTime == -1) {
                process.responseTime = timeNow - process.arrivalTime;
            }

            int execTime = Math.min(quantum, process.remainingTime);
            System.out.println("Running " + process + " for " + execTime + " units");
            ganttChart.add(new GanttEntry(process.pid, timeNow, timeNow + execTime));
            contextSwitches++;

            timeNow += execTime;
            process.remainingTime -= execTime;

            Iterator<Process> iter2 = readyList.iterator();
            while (iter2.hasNext()) {
                Process p = iter2.next();
                if (p.arrivalTime <= timeNow) {
                    queue.add(p);
                    iter2.remove();
                }
            }

            if (process.remainingTime == 0 && !finishedPids.contains(process.pid)) {
                process.completionTime = timeNow;
                process.turnaroundTime = process.completionTime - process.arrivalTime;
                process.waitingTime = process.turnaroundTime - process.burstTime;
                done.add(process);
                finishedPids.add(process.pid);
            } else {
                queue.add(process);
            }
        }

        printResults(done, true);
        printGanttChart(ganttChart);
        System.out.println("Number of Context Switches = " + contextSwitches);
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