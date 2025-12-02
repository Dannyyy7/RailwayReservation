import java.util.*;

class Train {
    private int trainNo;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private int bookedSeats = 0;

    Queue<Passenger> waitingList = new LinkedList<>();
    private final int WAITING_LIMIT = 3;

    ArrayList<Passenger> confirmedList = new ArrayList<>();

    Train(int no, String name, String src, String dest, int seats) {
        this.trainNo = no;
        this.trainName = name;
        this.source = src;
        this.destination = dest;
        this.totalSeats = seats;
    }

    public int getTrainNo() { return trainNo; }
    public String getTrainName() { return trainName; }

    public boolean hasSeats() {
        return bookedSeats < totalSeats;
    }

    public boolean hasWaitingListSpace() {
        return waitingList.size() < WAITING_LIMIT;
    }

    public void bookSeat(Passenger p) {
        if (hasSeats()) {
            confirmedList.add(p);
            bookedSeats++;
            System.out.println(" Seat Confirmed for " + p.getName());
        } else if (hasWaitingListSpace()) {
            waitingList.add(p);
            System.out.println(" Added to Waiting List: " + p.getName());
        } else {
            System.out.println(" No seats or waiting list available!");
        }
    }

    public void cancelSeat(String pnr) {
        Passenger remove = null;

        for (Passenger p : confirmedList) {
            if (p.getPnr().equals(pnr)) {
                remove = p;
            }
        }

        if (remove != null) {
            confirmedList.remove(remove);
            bookedSeats--;

            System.out.println("✔ Ticket Cancelled: " + remove.getName());

            if (!waitingList.isEmpty()) {
                Passenger fromWait = waitingList.poll();
                confirmedList.add(fromWait);
                bookedSeats++;
                System.out.println(" Moved from Waiting to Confirmed: " + fromWait.getName());
            }
        } else {
            System.out.println(" No ticket found for PNR: " + pnr);
        }
    }

    public void showPassengers() {
        System.out.println("\n--- Confirmed Passengers ---");
        for (Passenger p : confirmedList) {
            System.out.println(p.getName() + " | PNR: " + p.getPnr());
        }

        System.out.println("\n--- Waiting List ---");
        for (Passenger p : waitingList) {
            System.out.println(p.getName() + " | PNR: " + p.getPnr());
        }
    }

    public void showDetails() {
        System.out.println("Train No: " + trainNo + " | " + trainName +
                " | " + source + " → " + destination +
                " | Seats Left: " + (totalSeats - bookedSeats));
    }
}

class Passenger {
    private String name;
    private int age;
    private String pnr;

    Passenger(String name, int age, String pnr) {
        this.name = name;
        this.age = age;
        this.pnr = pnr;
    }

    public String getName() { return name; }
    public String getPnr() { return pnr; }
}

class ReservationSystem {
    ArrayList<Train> trains = new ArrayList<>();

    public void addTrain(int no, String name, String src, String dest, int seats) {
        trains.add(new Train(no, name, src, dest, seats));
    }

    public Train getTrain(int no) {
        for (Train t : trains) {
            if (t.getTrainNo() == no) return t;
        }
        return null;
    }

    public String generatePNR() {
        return "PNR" + (int)(Math.random() * 100000);
    }

    public void showAllTrains() {
        for (Train t : trains) {
            t.showDetails();
        }
    }
}

public class RailwayReservation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ReservationSystem rs = new ReservationSystem();

        // Default trains
        rs.addTrain(101, "Shatabdi Express", "Delhi", "Kanpur", 3);
        rs.addTrain(202, "Duronto Express", "Mumbai", "Pune", 2);

        while (true) {
            System.out.println("\n===== RAILWAY RESERVATION SYSTEM =====");
            System.out.println("1. Show Trains");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. Show Passenger List");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    rs.showAllTrains();
                    break;

                case 2:
                    System.out.print("Enter Train No: ");
                    int tno = sc.nextInt();
                    Train selected = rs.getTrain(tno);

                    if (selected == null) {
                        System.out.println(" Train Not Found!");
                        break;
                    }

                    sc.nextLine();
                    System.out.print("Enter Passenger Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();

                    String pnr = rs.generatePNR();
                    Passenger p = new Passenger(name, age, pnr);

                    selected.bookSeat(p);

                    System.out.println("Your PNR: " + pnr);
                    break;

                case 3:
                    System.out.print("Enter Train No: ");
                    int tnc = sc.nextInt();

                    Train t = rs.getTrain(tnc);

                    if (t == null) {
                        System.out.println(" Train Not Found!");
                        break;
                    }

                    System.out.print("Enter PNR: ");
                    String pnrCancel = sc.next();

                    t.cancelSeat(pnrCancel);
                    break;

                case 4:
                    System.out.print("Enter Train No: ");
                    int tshow = sc.nextInt();
                    Train tt = rs.getTrain(tshow);

                    if (tt == null) {
                        System.out.println(" Train Not Found!");
                        break;
                    }

                    tt.showPassengers();
                    break;

                case 5:
                    System.out.println("✔ Thank you for using the system!");
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}