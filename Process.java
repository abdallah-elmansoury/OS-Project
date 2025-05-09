class Process {
    int pid;
    int burstTime;
    int remainingTime;
    int arrivalTime;
    int priority;
    int completionTime;
    int waitingTime;
    int turnaroundTime;
    int responseTime = -1;

    public Process(int pid, int burstTime, int arrivalTime, int priority) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "P" + pid + "(BT=" + burstTime + ", AT=" + arrivalTime + ", PR=" + priority + ")";
    }
}

class GanttEntry {
    int pid;
    int startTime;
    int endTime;

    public GanttEntry(int pid, int startTime, int endTime) {
        this.pid = pid;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}